package animal_shop.tools.map_service.dto;

import animal_shop.tools.map_service.entity.MapEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapDTO {
    @JsonProperty("시설명")
    private String facilityName;            // 시설명

    @JsonProperty("카테고리1")
    private String category1;               // 카테고리1

    @JsonProperty("카테고리2")
    private String category2;               // 카테고리2

    @JsonProperty("카테고리3")
    private String category3;               // 카테고리3

    @JsonProperty("시도 명칭")
    private String provinceName;            // 시도 명칭

    @JsonProperty("시군구 명칭")
    private String cityName;                // 시군구 명칭

    @JsonProperty("법정읍면동명칭")
    private String townName;                // 법정읍면동명칭

    @JsonProperty("리 명칭")
    private String villageName;             // 리 명칭

    @JsonProperty("번지")
    private String lotNumber;               // 번지

    @JsonProperty("도로명 이름")
    private String roadName;                // 도로명 이름


    @JsonProperty("건물 번호")
    private String buildingNumber;          // 건물 번호
    @JsonProperty("위도")
    private Double latitude;                 // 위도

    @JsonProperty("경도")
    private Double longitude;               // 경도

    @JsonProperty("우편번호")
    private Integer zipCode;                // 우편번호

    @JsonProperty("도로명주소")
    private String roadAddress;             // 도로명주소

    @JsonProperty("지번주소")
    private String lotAddress;              // 지번주소

    @JsonProperty("전화번호")
    private String phoneNumber;             // 전화번호
    @JsonProperty("홈페이지")
    private String homepage;                // 홈페이지

    @JsonProperty("휴무일")
    private String closedDays;              // 휴무일

    @JsonProperty("운영시간")
    private String operatingHours;          // 운영시간

    @JsonProperty("주차 가능여부")
    private String parkingAvailable;        // 주차 가능여부

    @JsonProperty("입장(이용료)가격 정보")
    private String admissionFee;            // 입장(이용료)가격 정보

    @JsonProperty("반려동물 동반 가능정보")
    private String petFriendlyInfo;         // 반려동물 동반 가능정보

    @JsonProperty("반려동물 전용 정보")
    private String petExclusiveInfo;        // 반려동물 전용 정보

    @JsonProperty("입장 가능 동물 크기")
    private String allowedPetSize;          // 입장 가능 동물 크기

    @JsonProperty("반려동물 제한사항")
    private String petRestrictions;         // 반려동물 제한사항

    @JsonProperty("장소(실내) 여부")
    private String indoorAvailable;         // 장소(실내) 여부

    @JsonProperty("장소(실외)여부")
    private String outdoorAvailable;        // 장소(실외)여부

    @JsonProperty("기본 정보_장소설명")
    private String placeDescription;        // 기본 정보_장소설명

    @JsonProperty("애견 동반 추가 요금")
    private String additionalPetFee;        // 애견 동반 추가 요금

    @JsonProperty("최종작성일")
    private String lastUpdated;             // 최종작성일

    private long commentCount;
    private long totalRating;
    // Entity를 DTO로 변환하는 생성자
    public MapDTO(MapEntity entity) {
        this.facilityName = entity.getFacilityName();
        this.category1 = entity.getCategory1();
        this.category2 = entity.getCategory2();
        this.category3 = entity.getCategory3();
        this.provinceName = entity.getProvinceName();
        this.cityName = entity.getCityName();
        this.townName = entity.getTownName();
        this.villageName = entity.getVillageName();
        this.lotNumber = entity.getLotNumber();
        this.roadName = entity.getRoadName();
        this.buildingNumber = entity.getBuildingNumber();
        this.latitude = entity.getLatitude();
        this.longitude = entity.getLongitude();
        this.zipCode = entity.getZipCode();
        this.roadAddress = entity.getRoadAddress();
        this.lotAddress = entity.getLotAddress();
        this.phoneNumber = entity.getPhoneNumber();
        this.homepage = entity.getHomepage();
        this.closedDays = entity.getClosedDays();
        this.operatingHours = entity.getOperatingHours();
        this.parkingAvailable = entity.getParkingAvailable();
        this.admissionFee = entity.getAdmissionFee();
        this.petFriendlyInfo = entity.getPetFriendlyInfo();
        this.petExclusiveInfo = entity.getPetExclusiveInfo();
        this.allowedPetSize = entity.getAllowedPetSize();
        this.petRestrictions = entity.getPetRestrictions();
        this.indoorAvailable = entity.getIndoorAvailable();
        this.outdoorAvailable = entity.getOutdoorAvailable();
        this.placeDescription = entity.getPlaceDescription();
        this.additionalPetFee = entity.getAdditionalPetFee();
        this.lastUpdated = entity.getLastUpdated();
        this.totalRating = entity.getTotalRating();
        this.commentCount = entity.getCommentCount();
    }

    // Entity -> DTO 변환
    public static MapDTO fromEntity(MapEntity entity) {
        return new MapDTO(entity);
    }

    public MapEntity toEntity() {
        MapEntity mapEntity = new MapEntity();
        mapEntity.setFacilityName(this.getFacilityName());
        mapEntity.setCategory1(this.getCategory1());
        mapEntity.setCategory2(this.getCategory2());
        mapEntity.setCategory3(this.getCategory3());
        mapEntity.setProvinceName(this.getProvinceName());
        mapEntity.setCityName(this.getCityName());
        mapEntity.setTownName(this.getTownName());
        mapEntity.setVillageName(this.getVillageName());
        mapEntity.setLotNumber(this.getLotNumber());
        mapEntity.setRoadName(this.getRoadName());
        mapEntity.setLatitude(this.getLatitude());
        mapEntity.setLongitude(this.getLongitude());
        mapEntity.setZipCode(this.getZipCode());
        mapEntity.setRoadAddress(this.getRoadAddress());
        mapEntity.setLotAddress(this.getLotAddress());
        mapEntity.setPhoneNumber(this.getPhoneNumber());
        mapEntity.setHomepage(this.getHomepage());
        mapEntity.setClosedDays(this.getClosedDays());
        mapEntity.setOperatingHours(this.getOperatingHours());
        mapEntity.setParkingAvailable(this.getParkingAvailable());
        mapEntity.setAdmissionFee(this.getAdmissionFee());
        mapEntity.setPetFriendlyInfo(this.getPetFriendlyInfo());
        mapEntity.setPetExclusiveInfo(this.getPetExclusiveInfo());
        mapEntity.setAllowedPetSize(this.getAllowedPetSize());
        mapEntity.setPetRestrictions(this.getPetRestrictions());
        mapEntity.setIndoorAvailable(this.getIndoorAvailable());
        mapEntity.setOutdoorAvailable(this.getOutdoorAvailable());
        mapEntity.setPlaceDescription(this.getPlaceDescription());
        mapEntity.setAdditionalPetFee(this.getAdditionalPetFee());
        mapEntity.setLastUpdated(this.getLastUpdated());
        mapEntity.setCommentCount(this.getCommentCount());
        mapEntity.setTotalRating(this.getTotalRating());
        return mapEntity;}
}