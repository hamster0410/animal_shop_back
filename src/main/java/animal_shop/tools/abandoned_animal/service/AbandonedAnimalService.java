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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@Slf4j
public class AbandonedAnimalService {

    @Autowired
    private AbandonedAnimalRepository abandonedAnimalRepository;

    @Autowired
    private InterestAnimalRepository interestAnimalRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private EmailService emailService;

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

        abandonedAnimalRepository.deleteAll();;

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
                        abandonedAnimalRepository.save(animalEntity); // DB에 저장
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

    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Seoul") // 매일 16시 32분 (KST)
    public ResponseEntity<?> updateAPIInfo() {
        int pageNo = 1;  // 첫 페이지 번호

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
                        AbandonedAnimal Entity = abandonedAnimalRepository.findByDesertionNo(item.getDesertionNo());
                        if(Entity != null){
                            log.info("updated Item {}",item.getDesertionNo());
                            Entity.update(item);
                            abandonedAnimalRepository.save(Entity); // DB에 저장
                        }else{
                            AbandonedAnimal animalEntity = AbandonedAnimal.fromDTO(item);
                            abandonedAnimalRepository.save(animalEntity); // DB에 저장
                        }
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

        Pageable pageable = PageRequest.of(page, 21);
        Page<AbandonedAnimal> abandonedAnimals = abandonedAnimalRepository.findAll(specification, pageable);

        List<AnimalListDTO> animals = abandonedAnimals.map(AnimalListDTO::new).getContent();

        // 검색 결과 반환
        return AnimalListDTOResponse.builder()
                .animalListDTOList(animals)
                .total_count(abandonedAnimals.getTotalElements())
                .build();

    }

    public AnimalDetailDTO searchDetailAPI(Long animalId) {
        AbandonedAnimal animal = abandonedAnimalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("animal is not found"));
        return new AnimalDetailDTO(animal);
    }


    @Transactional
    public void interestAnimal(String token,String desertion_no) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found")  );


        AbandonedAnimal abandonedAnimal = abandonedAnimalRepository.findByDesertionNo(desertion_no);
        System.out.println(abandonedAnimal.getAge());
        InterestAnimal interestAnimal = new InterestAnimal(member,abandonedAnimal);
        // 이미 관심 동물로 등록된 경우 예외 처리
        boolean alreadyExists = interestAnimalRepository.existsByMemberAndDesertionNo(member, desertion_no);
        if (alreadyExists) {
            throw new IllegalArgumentException("Animal is already in the interest list");
        }

        interestAnimalRepository.save(interestAnimal);
    }


    //관심동물 삭제
    @Transactional
    public void indifferentAnimal(String token, Long id) {

        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));


        InterestAnimal interestAnimal = interestAnimalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("InterestAnimal not found"));

        //USER 확인
        if(!Long.valueOf(userId).equals(interestAnimal.getMember().getId()))
            throw new IllegalStateException("User is not USER");

        interestAnimalRepository.delete(interestAnimal);



    }
    @Transactional
    public InterestDTOResponse listInterestAnimal(String token, int page) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found")  );

        Pageable pageable = (Pageable) PageRequest.of(page,12);
        Page<InterestAnimal> interestAnimals = interestAnimalRepository.findByMember(member,pageable);
        return InterestDTOResponse.builder()
                .interestAnimalDTOList(interestAnimals.stream().map(InterestAnimalDTO::new).toList())
                .total_count(interestAnimals.getTotalElements())
                .build();

    }

    @Transactional
    public void modifyStatus(String token, ByeAnimalDTO byeAnimalDTO) {
        // 인증
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("Member is not found"));

        // ADMIN 권한 확인
        if (!"ADMIN".equals(member.getRole().toString())) {
            throw new IllegalArgumentException("Member is not an ADMIN");
        }

        // ByeAnimalDTO에서 값 추출
        String desertionNo = byeAnimalDTO.getDesertionNo();
        String newState = byeAnimalDTO.getNewState();

        // 유기 동물 조회
        AbandonedAnimal abandonedAnimal = abandonedAnimalRepository.findByDesertionNo(desertionNo);

        String careNm = abandonedAnimal.getCareNm();
        String kindCd = abandonedAnimal.getKindCd();

        if (abandonedAnimal == null) {
            throw new IllegalArgumentException("Abandoned animal not found with desertion number: " + desertionNo);
        }

        String currentState = abandonedAnimal.getProcessState(); // 현재 상태 확인

        if (newState == null || newState.isBlank()) {
            throw new IllegalArgumentException("New state is null or empty");
        }

        // 상태 변경 조건 확인 및 처리
        if ("보호중".equals(currentState)) {
            abandonedAnimal.setProcessState(newState); // 상태 변경

            // 관심 동물을 등록한 회원들에게 이메일 알림
            List<InterestAnimal> interestedAnimals = interestAnimalRepository.findByDesertionNo(desertionNo);
            for (InterestAnimal interestedAnimal : interestedAnimals) {
                Member interestedMember = interestedAnimal.getMember();
                if (interestedMember != null && interestedMember.getMail() != null) {
                    // 메일 내용 구성
                    String subject = "유기 동물 상태 변경 알림";
                    String body = generateEmailBody(desertionNo, newState, careNm, kindCd,abandonedAnimal.getPopfile());

                    // 이메일 전송
                    emailService.sendEmail(interestedMember.getMail(), subject, body);
                    log.info("sending email");
                }
            }

            // 변경된 동물 상태 저장
            abandonedAnimalRepository.save(abandonedAnimal);
        } else {
            throw new IllegalArgumentException("Animal is not in 보호중 state, cannot be updated");
        }
    }
  private String generateEmailBody(String desertionNo, String newState, String careNm, String kindCd, String popfile) {
        return String.format("""
        <h1 style="color: #f39c12; font-size: 32px; text-align: center; margin-bottom: 20px; font-family: 'Arial', sans-serif;">🎉 유기 동물 입양 성공 알림 🎉</h1>
        <p style="font-size: 20px; font-weight: bold; color: #27ae60; text-align: center;">안녕하세요! 😊</p>
        <p style="font-size: 18px; color: #2c3e50; margin-bottom: 20px; text-align: center;">기쁜 소식을 전해드려요! 관심을 표현해 주셨던 동물이 새 가족을 찾았습니다. 💖</p>
        <ul style="list-style-type: none; padding: 0; font-size: 16px; color: #34495e; text-align: center;">
            <li style="margin-bottom: 10px;"><b style="color: #2980b9;">보호소 이름:</b> %s</li>
            <li style="margin-bottom: 10px;"><b style="color: #2980b9;">유기 번호:</b> %s</li>
            <li style="margin-bottom: 10px;"><b style="color: #2980b9;">품종:</b> %s</li>
            <li style="margin-bottom: 10px;"><b style="color: #2980b9;">상태:</b> %s</li>
        </ul>
        <img src='%s' alt='Animal Image' style="width: 300px; height: auto; display: block; margin: 20px auto; border-radius: 10px; border: 5px solid #f39c12; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
        <p style="font-size: 18px; color: #e74c3c; text-align: center; margin-top: 20px; font-weight: bold;">이제 새로운 삶을 시작하는 이 친구에게 많은 응원과 사랑을 부탁드려요! 💖</p>
        <p style="font-size: 16px; color: #7f8c8d; text-align: center; margin-top: 20px;">입양을 기다리던 동물들에게도 계속적인 관심 부탁드립니다. 감사합니다!</p>
        <div style="background-color: #f39c12; padding: 10px; margin-top: 30px; color: white; font-size: 16px; font-weight: bold; border-radius: 5px; text-align: center;">
            <p>🐾 <a href="http://localhost:3000/" style="color: white; text-decoration: none; font-weight: bold;">애니멀핑에 와서 더 많은 입양 가능한 동물을 확인하고, 관심 동물로 등록해 주세요! 🐾</a></p>
        </div>
    """, careNm, desertionNo, kindCd, newState, popfile);
    }


}




