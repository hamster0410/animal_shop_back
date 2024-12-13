package animal_shop.tools.map_service.dto;

import animal_shop.tools.map_service.entity.MapEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapDetailDTO {
    String facility_name;
    double latitude;
    double longitude;
    String additional_pet_fee;
    String admission_fee;
    String allowed_pet_size;
    String category;
    String indoor_available;
    String outdoor_available;
    String operating_hours;
    String parking_available;
    String pet_restrictions;
    String phone_number;
    String place_description;
    String road_address;
    String homepage;
    String closed_days;
    long rating;
    long comment_count;
    boolean like;

    public MapDetailDTO(MapEntity mapEntity) {
        this.facility_name = mapEntity.getFacilityName();
        this.latitude = mapEntity.getLatitude();
        this.longitude = mapEntity.getLongitude();
        this.additional_pet_fee = mapEntity.getAdditionalPetFee();
        this.admission_fee = mapEntity.getAdmissionFee();
        this.allowed_pet_size = mapEntity.getAllowedPetSize();
        this.category = mapEntity.getCategory3();
        this.indoor_available = mapEntity.getIndoorAvailable();
        this.outdoor_available = mapEntity.getOutdoorAvailable();
        this.operating_hours = mapEntity.getOperatingHours();
        this.parking_available = mapEntity.getParkingAvailable();
        this.pet_restrictions = mapEntity.getPetRestrictions();
        this.phone_number = mapEntity.getPhoneNumber();
        this.place_description = mapEntity.getPlaceDescription();
        this.road_address = mapEntity.getRoadAddress();
        this.homepage = mapEntity.getHomepage();
        this.closed_days = mapEntity.getClosedDays();
        // 나중에 수정 요망
        this.rating = 0;
        this.comment_count = 0;
    }
}
