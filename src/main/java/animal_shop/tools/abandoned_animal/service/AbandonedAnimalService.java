package animal_shop.tools.abandoned_animal.service;


import animal_shop.tools.abandoned_animal.dto.AnimalDTO;
import animal_shop.tools.abandoned_animal.entity.AbandonedAnimal;
import animal_shop.tools.abandoned_animal.repository.AbandonedAnimalRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
public class AbandonedAnimalService {

    @Autowired
    private AbandonedAnimalRepository animalRepository;


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
            String API_URL = callBackUrl + "?serviceKey=" + serviceKey + "&dataType=" + dataType + "&numOfRows=12&pageNo=" + pageNo + "&_type=json";
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
}

