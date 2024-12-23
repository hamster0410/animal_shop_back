package animal_shop.tools.map_service.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.item_comment.repository.ItemCommentRepository;
import animal_shop.shop.item_comment_like.repository.ItemCommentLikeRepository;
import animal_shop.tools.map_service.dto.*;
import animal_shop.tools.map_service.entity.MapComment;
import animal_shop.tools.map_service.entity.MapEntity;
import animal_shop.tools.map_service.entity.MapSpecification;
import animal_shop.tools.map_service.repository.MapCommentRespository;
import animal_shop.tools.map_service.repository.MapLikeRepository;
import animal_shop.tools.map_service.repository.MapRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class MapService {

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MapCommentRespository mapCommentRespository;

    @Autowired
    private MapLikeRepository mapLikeRepository;

    @Autowired
    ItemCommentRepository itemCommentRepository;

    @Autowired
    ItemCommentLikeRepository itemCommentLikeRepository;


    @Value("${openApi.serviceKey}")
    private String serviceKey;

    @Value("${openApi.endPoint}")
    private String endPoint;

    @Value("${openApi.dataType}")
    private String dataType;

    private long totalPages;

    private long totalCount;
    public ResponseEntity<?> mapFind() {

        mapRepository.deleteAll();
        int pageNo = 1; // 페이지 번호 초기화
        List<MapDTO> resultList = new ArrayList<>(); // 결과를 담을 리스트
        totalCount = 0; // 총 항목 수
        totalPages = Long.MAX_VALUE; // 최대 페이지를 추후 계산

        RestTemplate restTemplate = new RestTemplate();

        while (pageNo <= totalPages) {
            String API_URL = endPoint + "?serviceKey=" + serviceKey + "&dataType=" + dataType +
                    "&perPage=1000&page=" + pageNo;

            log.info("API 호출 URL: {}", API_URL);
            log.info("전체 페이지 : {}", totalPages);

            try {
                // API 호출 및 응답 처리
                String response = restTemplate.getForObject(API_URL, String.class);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response);

                // total_count가 존재하면 최대 페이지 계산
                JsonNode totalCountNode = rootNode.path("totalCount");
                if (totalCountNode.isNumber()) {
                    totalCount = totalCountNode.asLong();
                    totalPages = (totalCount + 1000 - 1) / 1000; // 총 페이지 계산
                }

                // 데이터 처리
                JsonNode dataNode = rootNode.path("data");
                if (dataNode.isArray() && dataNode.size() > 0) {
                    for (JsonNode itemNode : dataNode) {
                        MapDTO item = objectMapper.treeToValue(itemNode, MapDTO.class);
                        resultList.add(item);

                        // 엔티티로 변환하여 DB 저장
                        mapRepository.save(item.toEntity());
                    }
                    log.info("현재 페이지 처리 완료: {}", pageNo);
                    pageNo++; // 다음 페이지로 이동
                } else {
                    log.info("더 이상 데이터가 없습니다. 반복 종료.");
                    break;
                }

            } catch (Exception e) {
                log.error("API 응답 처리 중 오류 발생: ", e);
                break; // 오류 발생 시 반복 종료
            }
        }

        // 결과 로그 확인
        log.info("총 항목 수: {}", totalCount);
        log.info("수집된 데이터 수: {}", resultList.size());

        MapDTOResponse response = MapDTOResponse.builder()
                .MapDTOList(resultList)
                .total_count(totalCount)
                .build();

        return ResponseEntity.ok().body(response);
    }


    @Scheduled(cron = "0 0 1 * * *", zone = "Asia/Seoul") // 매일 16시 32분 (KST)
    public ResponseEntity<?> mapUpdate() {

        int pageNo = 1; // 페이지 번호 초기화
        List<MapDTO> resultList = new ArrayList<>(); // 결과를 담을 리스트
        totalCount = 0; // 총 항목 수
        totalPages = Long.MAX_VALUE; // 최대 페이지를 추후 계산

        RestTemplate restTemplate = new RestTemplate();

        while (pageNo <= totalPages) {
            String API_URL = endPoint + "?serviceKey=" + serviceKey + "&dataType=" + dataType +
                    "&perPage=1000&page=" + pageNo;

            log.info("API 호출 URL: {}", API_URL);
            log.info("전체 페이지 : {}", totalPages);

            try {
                // API 호출 및 응답 처리
                String response = restTemplate.getForObject(API_URL, String.class);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response);

                // total_count가 존재하면 최대 페이지 계산
                JsonNode totalCountNode = rootNode.path("totalCount");
                if (totalCountNode.isNumber()) {
                    totalCount = totalCountNode.asLong();
                    totalPages = (totalCount + 1000 - 1) / 1000; // 총 페이지 계산
                }

                // 데이터 처리
                JsonNode dataNode = rootNode.path("data");
                if (dataNode.isArray() && dataNode.size() > 0) {
                    for (JsonNode itemNode : dataNode) {
                        MapDTO item = objectMapper.treeToValue(itemNode, MapDTO.class);

                        // 존재 여부 확인 및 업데이트/삽입
                        Optional<MapEntity> existingEntityOpt = mapRepository.findUniqueByCategory3AndFacilityNameAndLotAddress(item.getCategory3(),item.getFacilityName(), item.getLotAddress());
                        if (existingEntityOpt.isPresent()) {
                            MapEntity existingEntity = existingEntityOpt.get();

                            // 변경된 데이터만 업데이트
                            existingEntity.update(item.toEntity());
                            log.info("변경된 데이터 업데이트: {}", item.getFacilityName());

                        } else {
//                            // 새로운 데이터 삽입
                            mapRepository.save(item.toEntity());
                            log.info("새로운 데이터 삽입: {}", item.getFacilityName());
                            System.out.println("here new Map ");
                        }

                        resultList.add(item);
                    }
                    log.info("현재 페이지 처리 완료: {}", pageNo);
                    pageNo++; // 다음 페이지로 이동
                } else {
                    log.info("더 이상 데이터가 없습니다. 반복 종료.");
                    break;
                }

            } catch (Exception e) {
                log.error("API 응답 처리 중 오류 발생: ", e);
                break; // 오류 발생 시 반복 종료
            }
        }

        // 결과 로그 확인
        log.info("총 항목 수: {}", totalCount);
        log.info("수집된 데이터 수: {}", resultList.size());

        MapDTOResponse response = MapDTOResponse.builder()
                .MapDTOList(resultList)
                .total_count(totalCount)
                .build();

        return ResponseEntity.ok().body(response);
    }
    public MapPositionDTOResponse search(String token, SearchRequestDTO searchRequestDTO, int page) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        if(userId == null){
            throw new IllegalArgumentException("user is not found");
        }

        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "countLike"));
        Specification<MapEntity> specification = Specification.where(null);

        if(searchRequestDTO.getKeyword()!=null){
            specification = specification.and(MapSpecification.searchByKeyword(searchRequestDTO.getKeyword()));
        }

        if(searchRequestDTO.getCategory()!=null){
            specification = specification.and(MapSpecification.searchByCategory(searchRequestDTO.getCategory()));
        }

        if(searchRequestDTO.getParking()!=null){
            specification = specification.and(MapSpecification.searchByParking(searchRequestDTO.getParking()));
        }

        if(searchRequestDTO.getOutdoor()!=null){
            specification = specification.and(MapSpecification.searchByOutdoor(searchRequestDTO.getOutdoor()));
        }

        if(searchRequestDTO.getIndoor()!=null){
            specification = specification.and(MapSpecification.searchByIndoor(searchRequestDTO.getIndoor()));
        }
        if (searchRequestDTO.getSwLatlng() != null && searchRequestDTO.getNeLatlng() != null) {
            specification = specification.and(MapSpecification.searchByRange(
                    searchRequestDTO.getSwLatlng().getLongitude(),
                    searchRequestDTO.getSwLatlng().getLatitude(),
                    searchRequestDTO.getNeLatlng().getLongitude(),
                    searchRequestDTO.getNeLatlng().getLatitude()));
        }

        // 거리 정렬
        specification = specification.and(MapSpecification.orderByDistance(
                (Double.parseDouble(searchRequestDTO.getSwLatlng().getLatitude()) + Double.parseDouble(searchRequestDTO.getNeLatlng().getLatitude())) / 2,
                (Double.parseDouble(searchRequestDTO.getSwLatlng().getLongitude()) + Double.parseDouble(searchRequestDTO.getNeLatlng().getLongitude())) / 2
        ));

        Page<MapEntity> maps = mapRepository.findAll(specification,pageable);
        List<MapPositionDTO> mapPositionDTOList = new ArrayList<>();
        for(MapEntity mapEntity : maps){
            MapPositionDTO mapPositionDTO = new MapPositionDTO(mapEntity);
            if(mapLikeRepository.findByMemberIdAndMapId(mapEntity.getId(), Long.valueOf(userId)) != null){
                mapPositionDTO.setLike(true);
            }else{
                mapPositionDTO.setLike(false);
            }
            mapPositionDTOList.add(mapPositionDTO);
        }

        return MapPositionDTOResponse.builder()
                .mapPositionDTOList(mapPositionDTOList)
                .total_count(maps.getTotalElements())
                .build();
    }
    public MapDetailDTO detail(String token, long mapId) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        MapEntity mapEntity = mapRepository.findById(mapId)
                .orElseThrow(()->new IllegalArgumentException("facility is not found"));
        MapDetailDTO mapDetailDTO = new MapDetailDTO(mapEntity);
        if(mapLikeRepository.findByMemberIdAndMapId(mapEntity.getId(), Long.valueOf(userId)) != null){
            mapDetailDTO.setLike(true);
        }else{
            mapDetailDTO.setLike(false);
        }
        return mapDetailDTO;
    }
      
    //반려동물 동반 시설에 댓글 달기
    @Transactional
    public void createMapComment(String token,  MapCommentDTO mapCommentDTO) {
        //인증
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        // 지도 찾기 (mapId로 지도 존재 여부 확인)
        MapEntity mapEntity = mapRepository.findById(mapCommentDTO.getMap_id())
                .orElseThrow(() -> new IllegalArgumentException("Map not found with ID: " + mapCommentDTO.getMap_id()));
        //별점 1~5로 제한
        if(mapCommentDTO.getRating() < 1 || mapCommentDTO.getRating() >5){
            throw new IllegalStateException("rating error");
        }
        //댓글 등록시 item의 별점 증가
        mapEntity.setTotalRating(mapEntity.getTotalRating() + mapCommentDTO.getRating());
        // 댓글 등록
        MapComment mapComment = new MapComment();
        mapComment.setContents(mapCommentDTO.getContents());
        mapComment.setMapId(mapCommentDTO.getMap_id());
        mapComment.setMap_comment_thumbnail_url(mapCommentDTO.getMap_comment_thumbnail_url());
        mapComment.setRating(mapCommentDTO.getRating());
        mapComment.setMember(member);


        mapEntity.setCommentCount(mapEntity.getCommentCount()+1);
        // 댓글을 DB에 저장
        mapRepository.save(mapEntity);
        mapCommentRespository.save(mapComment);
    }
    //댓글 수정
//     @Transactional
//     public void updateMapComment(String token, MapCommentDTO mapCommentDTO) {
      

  
      
    @Transactional
    public MapCommentDTOResponse selectMapComment(String token, long map_id, int page) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        if(userId == null){
            throw new IllegalArgumentException("member is not found");
        }
        // 댓글 리스트 가져오기
        Pageable pageable = PageRequest.of(page, 7, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<MapComment> comments = mapCommentRespository.findByMapId(map_id, pageable);

        // DTO 변환
        List<MapCommentDTO> commentDTOs = new ArrayList<>();
        for (MapComment comment : comments) {
            MapCommentDTO dto = new MapCommentDTO(comment);
            commentDTOs.add(dto);
        }

        // 총 댓글 수 가져오기
        long totalCount = comments.getTotalElements();

        // 응답 DTO 생성
        MapCommentDTOResponse response = new MapCommentDTOResponse();
        response.setComments(commentDTOs);
        response.setTotal_count(totalCount);

        return response;
    }

    @Transactional
    public void updateMapComment(String token, MapCommentDTO mapCommentDTO) {
        // 인증
        String userId = tokenProvider.extractIdByAccessToken(token);

        // USER인지 확인
        if (userId == null) {
            throw new IllegalStateException("User is not USER");
        }

        // 댓글 찾기
        MapComment comment = mapCommentRespository.findById(mapCommentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 총 평점 수정
        MapEntity mapEntity = mapRepository.findById(mapCommentDTO.getMap_id())
                .orElseThrow(() -> new IllegalArgumentException("Map not found with ID: " + mapCommentDTO.getMap_id()));
        mapEntity.setTotalRating(mapEntity.getTotalRating() - comment.getRating() + mapCommentDTO.getRating());

        // 댓글 수정
        comment.setContents(mapCommentDTO.getContents());
        comment.setRating(mapCommentDTO.getRating());
        comment.setMap_comment_thumbnail_url(mapCommentDTO.getMap_comment_thumbnail_url());

        mapCommentRespository.save(comment);
    }

    @Transactional
    public void deleteMapComment(String token, long comment_id) {
        String userId = tokenProvider.extractIdByAccessToken(token);

        MapComment comment = mapCommentRespository.findById(comment_id)
                .orElseThrow(()->new IllegalArgumentException("comment is not found"));

        MapEntity mapEntity = mapRepository.findById(comment.getMapId())
                .orElseThrow(() -> new IllegalArgumentException("Map not found"));
        mapEntity.setTotalRating(mapEntity.getTotalRating() - comment.getRating());
        mapEntity.setCommentCount(mapEntity.getCommentCount()-1);
        mapCommentRespository.delete(comment);
    }

    public boolean checkMapComment(String token, long commentId) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member is not found"));

        MapComment comment = mapCommentRespository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("comment is not found"));

        if (!comment.getMember().equals(member)){
            return false;
        }
        return true;

    }


}
