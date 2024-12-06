package animal_shop.tools.map_service.service;

import animal_shop.tools.map_service.dto.MapDTO;
import animal_shop.tools.map_service.dto.MapDTOResponse;
import animal_shop.tools.map_service.entity.MapEntity;
import animal_shop.tools.map_service.repository.MapRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class MapService {

    @Autowired
    private MapRepository mapRepository;

    @Value("${openApi.serviceKey}")
    private String serviceKey;

    @Value("${openApi.endPoint}")
    private String endPoint;

    @Value("${openApi.dataType}")
    private String dataType;


    public ResponseEntity<?> mapFind() {

        mapRepository.deleteAll();
        int pageNo = 1; // 페이지 번호 초기화
        List<MapDTO> resultList = new ArrayList<>(); // 결과를 담을 리스트
        long totalCount = 0; // 총 항목 수

        // RestTemplate 초기화
        RestTemplate restTemplate = new RestTemplate();

        // API 호출 반복
        while (true) {
            // API 호출 URL 구성
            String API_URL = endPoint + "?serviceKey=" + serviceKey + "&dataType=" + dataType +
                    "&perPage=1000&pageNo=" + pageNo;

            log.info("API 호출 URL: {}", API_URL);

            try {
                // API 호출 및 응답 처리
                String response = restTemplate.getForObject(API_URL, String.class);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode dataNode = rootNode.path("data");
                JsonNode totalCountNode = rootNode.path("total_count");

                // "total_count"가 숫자라면 저장
                if (totalCountNode.isNumber()) {
                    totalCount = totalCountNode.asLong();
                }

                // "data"가 배열이라면 각 항목을 처리
                if (dataNode.isArray() && dataNode.size() > 0) {
                    for (JsonNode itemNode : dataNode) {
                        // JSON 데이터를 MapDTO로 변환
                        MapDTO item = objectMapper.treeToValue(itemNode, MapDTO.class);
                        resultList.add(item); // 결과 리스트에 추가

                        MapEntity mapEntity = item.toEntity(); // 변환 메서드 호출
                        mapRepository.save(mapEntity); // DB에 저장
                    }
                    log.info("현재 페이지: {}", pageNo);
                    pageNo++; // 다음 페이지로 이동
                } else {
                    // 더 이상 데이터가 없는 경우 반복 종료
                    log.info("데이터 없음. 종료.");
                    break;
                }

            } catch (Exception e) {
                log.error("API 응답 처리 중 오류 발생: ", e);
                break; // 오류 발생 시 반복 종료
            }
        }

        // 로그로 결과 확인
        log.info("총 항목 수: {}", totalCount);
        log.info("결과 항목 수: {}", resultList.size());
        resultList.forEach(item -> log.info("시설명: {}", item.getFacilityName())); // 각 시설명을 출력

        // 결과를 MapDTOResponse로 감싸서 반환
        MapDTOResponse response = MapDTOResponse.builder()
                .MapDTOList(resultList)
                .total_count(totalCount)
                .build();

        return ResponseEntity.ok().body(response);
    }
}