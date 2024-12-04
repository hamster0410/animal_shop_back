package animal_shop.shop.point.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.point.dto.PointTotalDTOResponse;
import animal_shop.shop.point.dto.PointYearSellerDTO;
import animal_shop.shop.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point")
public class PointController {

    @Autowired
    private PointService pointService;

    @GetMapping("/month-sum-total")
    public ResponseEntity<?> MonthSumPoint(@RequestHeader(value = "Authorization") String token,
                                           @RequestParam(value = "year") int year){
        ResponseDTO responseDTO;
        try{
            PointTotalDTOResponse pointDTOList = pointService.getMonthSum(token,year);
            return ResponseEntity.ok().body(pointDTOList);
        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/day-sum-total")
    public ResponseEntity<?> DaySumPoint(@RequestHeader(value = "Authorization") String token,
                                           @RequestParam(value = "year") int year,
                                           @RequestParam(value = "month") int month){
        ResponseDTO responseDTO;
        try{
            PointTotalDTOResponse pointDTOList = pointService.getDaySum(token,year,month);
            return ResponseEntity.ok().body(pointDTOList);
        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/year-sum-seller")
    public ResponseEntity<?> YearSumSeller(@RequestHeader(value = "Authorization") String token,
                                            @RequestParam(value = "year") int year){
        ResponseDTO responseDTO;
        try{
            List<PointYearSellerDTO> pointYearSellerDTOList = pointService.getSellerSumYear(token,year);

            return ResponseEntity.ok().body(pointYearSellerDTOList);
        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/month-sum-seller")
    public ResponseEntity<?> MonthSumSeller(@RequestHeader(value = "Authorization") String token,
                                           @RequestParam(value = "year") int year,
                                           @RequestParam(value = "month") int month){
        ResponseDTO responseDTO;
        try{
            List<PointYearSellerDTO> pointDTOList = pointService.getSellerSumMonth(token,year,month);

            return ResponseEntity.ok().body(pointDTOList);
        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/day-sum-seller")
    public ResponseEntity<?> DaySumSeller(@RequestHeader(value = "Authorization") String token,
                                          @RequestParam(value = "year") int year,
                                          @RequestParam(value = "month") int month,
                                          @RequestParam(value = "day") int day){
        ResponseDTO responseDTO;
        try{
            List<PointYearSellerDTO> pointDTOList = pointService.getSellerSumDay(token,year,month, day);

            return ResponseEntity.ok().body(pointDTOList);
        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
