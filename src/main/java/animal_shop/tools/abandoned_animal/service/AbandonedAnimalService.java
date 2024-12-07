package animal_shop.tools.abandoned_animal.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.tools.abandoned_animal.dto.*;
import animal_shop.tools.abandoned_animal.entity.AbandonedAnimal;
import animal_shop.tools.abandoned_animal.entity.InterestAnimal;
import animal_shop.tools.abandoned_animal.repository.AbandonedAnimalRepository;
import animal_shop.tools.abandoned_animal.repository.InterestAnimalRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class AbandonedAnimalService {

    @Autowired
    private AbandonedAnimalRepository animalRepository;

    @Autowired
    private InterestAnimalRepository interestAnimalRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Value("${file.upload-dir-interest}")
    private String uploadDir;


    // @Value 어노테이션을 사용하여 프로퍼티 주입
    @Value("${openApi.serviceKey}")
    private String serviceKey;

    @Value("${openApi.callBackUrl}")
    private String callBackUrl;

    @Value("${openApi.dataType}")
    private String dataType;

    public ResponseEntity<?> storeAPIInfo() {
        int pageNo = 1;  // 첫 페이지 번호

        animalRepository.deleteAll();;

        while (true) {
            // API 호출 URL 생성
            String API_URL = callBackUrl + "?serviceKey=" + serviceKey + "&dataType="
                    + dataType + "&numOfRows=1000&pageNo=" + pageNo + "&_type=json";
            System.out.println("API 호출 URL: " + API_URL);

            // RestTemplate을 사용하여 API 호출
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(API_URL, String.class);

            // JSON 응답을 Java 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

                // item 리스트로 변환
                if (itemsNode.isArray() && itemsNode.size() > 0) {
                    for (JsonNode itemNode : itemsNode) {
                        AnimalDTO item = objectMapper.treeToValue(itemNode, AnimalDTO.class);
                        AbandonedAnimal animalEntity = AbandonedAnimal.fromDTO(item);
                        animalRepository.save(animalEntity); // DB에 저장
                    }
                    // 다음 페이지로 넘어가기 위해 pageNo 증가
                    log.info("now Page" + pageNo);
                    pageNo++;
                } else {
                    // 더 이상 아이템이 없다면 종료
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        // 결과 반환
        return ResponseEntity.ok().body("동물 정보 저장 완료");
    }

    public AnimalListDTOResponse searchAPIInfo(AnimalSearchDTO animalSearchDTO, int page) {
        Specification<AbandonedAnimal> specification = Specification.where(null);
        if(animalSearchDTO.getAge()!=null){
            specification = specification.and(AbandonedAnimalSpecification.ageRanges(animalSearchDTO.getAge()));
        }
        if(animalSearchDTO.getSex()!=null){
            specification = specification.and(AbandonedAnimalSpecification.hasSex(animalSearchDTO.getSex()));
        }
        if(animalSearchDTO.getNeuter()!=null){
            specification = specification.and(AbandonedAnimalSpecification.isNeutered(animalSearchDTO.getNeuter()));
        }
        if(animalSearchDTO.getSpecies()!=null){
            specification = specification.and(AbandonedAnimalSpecification.kindCdFilter(animalSearchDTO.getSpecies(), animalSearchDTO.getBreed()));
        }

            specification = specification.and(AbandonedAnimalSpecification.noticeDateBasedOnStatus());

        if(animalSearchDTO.getLocation()!=null){
            specification = specification.and(AbandonedAnimalSpecification.locationFilter(animalSearchDTO.getLocation()));
        }
        Pageable pageable = PageRequest.of(page, 20);
        Page<AbandonedAnimal> abandonedAnimals = animalRepository.findAll(specification, pageable);

        List<AnimalListDTO> animals = abandonedAnimals.map(AnimalListDTO::new).getContent();

        // 검색 결과 반환
        return AnimalListDTOResponse.builder()
                .animalListDTOList(animals)
                .total_count(abandonedAnimals.getTotalElements())
                .build();

    }

    public AnimalDetailDTO searchDetailAPI(Long animalId) {
        AbandonedAnimal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("animal is not found"));
        return new AnimalDetailDTO(animal);
    }







    @Transactional
    public void interestAnimal(String token, InterestAnimalDTO interestAnimalDTO, int page, MultipartFile file) {
        // 인증
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        //USER 확인
        if(!member.getRole().toString().equals("USER"))
            throw new IllegalStateException("User is not USER");


        // 유기동물 정보 확인 abandonedAnimal 이 animalRespository에 있나 확인

        // DTO -> 엔티티 변환 (InterestAnimalDTO -> InterestAnimal 엔티티로 변환)
        InterestAnimal interestAnimal = InterestAnimalDTO.toEntity(interestAnimalDTO, member);
        // 페이징
        Pageable pageable = PageRequest.of(page, 12, Sort.by("createdDate").descending());
        InterestAnimal animal = new InterestAnimal();
        animal.setName(interestAnimalDTO.getName());
        animal.setCareNm(interestAnimalDTO.getCareNm());

        if (file != null && !file.isEmpty()) {
            try {
                // 디렉토리가 없으면 생성
                Files.createDirectories(Paths.get(uploadDir));
                // 파일명 중복 방지 (UUID 사용)
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                interestAnimal.setAttachmentUrl(filePath.toString()); // 파일 경로 저장
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
            }
        } else {
            interestAnimal.setAttachmentUrl(""); // 파일이 없을 경우 빈 값 설정
        }
        // 등록
        interestAnimalRepository.save(interestAnimal);
    }


    //관심동물 삭제
    @Transactional
    public void indifferentAnimal(String token, Long id) {

        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        //USER 확인
        if(!member.getRole().toString().equals("USER"))
            throw new IllegalStateException("User is not USER");

        InterestAnimal interestAnimal = interestAnimalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("InterestAnimal not found"));

        if (!interestAnimal.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("Unauthorized to delete this InterestAnimal");
        }
        interestAnimalRepository.delete(interestAnimal);

    }
}

