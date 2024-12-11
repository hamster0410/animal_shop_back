package animal_shop.tools.map_service.dto;

import animal_shop.tools.map_service.entity.MapEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapPositionDTO {
    private String facility_name;
    private  Double longitude;
    private  Double latitude;
    private long map_id;
    private String place_description;
    public MapPositionDTO(MapEntity mapEntity){
        this.facility_name = mapEntity.getFacilityName();
        this.latitude = mapEntity.getLatitude();
        this.longitude = mapEntity.getLongitude();
        this.map_id = mapEntity.getId();
        this.place_description = mapEntity.getPlaceDescription();
    }
}
