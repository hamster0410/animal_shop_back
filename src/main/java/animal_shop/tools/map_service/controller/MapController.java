package animal_shop.tools.map_service.controller;


import animal_shop.global.dto.ResponseDTO;
import animal_shop.tools.map_service.dto.MapDTO;
import animal_shop.tools.map_service.dto.MapDTOResponse;
import animal_shop.tools.map_service.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;

    @GetMapping("/find")
    public ResponseEntity<?> find_home() {
        ResponseDTO responseDTO = null;
        try {
           mapService.mapFind();
            responseDTO = ResponseDTO.builder()
                    .message("success find")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
