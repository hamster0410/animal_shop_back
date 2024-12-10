package animal_shop.tools.map_service.dto;

import lombok.Data;

@Data
public class SearchRequestDTO {
    private String keyword;           // 검색 키워드
    private String category;
    private Boolean parking;          // true인 경우 'Y', false 또는 null인 경우 전체
    private Boolean indoor;           // 실내 여부
    private Boolean outdoor;          // 실외 여부
    private LatLng swLatlng;          // 남서 좌표
    private LatLng neLatlng;          // 북동 좌표

    @Data
    public static class LatLng {
        private String longitude;             // 경도(Longitude)
        private String latitude;             // 위도(Latitude)
    }
}
