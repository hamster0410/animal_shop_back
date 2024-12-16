package animal_shop.tools.calculate.contrtoller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.point.service.PointService;
import animal_shop.tools.calculate.dto.AgeCalcDTO;
import animal_shop.tools.calculate.dto.CalorieCalcDTO;
import animal_shop.tools.calculate.dto.FoodCalcDTO;
import animal_shop.tools.calculate.service.CalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calc")
public class CalculatorController {

    @Autowired
    private CalcService calcService;

    @GetMapping("/age")
    public ResponseEntity<?> ageCalc(@RequestHeader(value = "Authorization") String token){

        ResponseDTO responseDTO;
        try{
            AgeCalcDTO ageCalcDTO = calcService.ageCalc(token);
            return ResponseEntity.ok().body(ageCalcDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/calorie")
    public ResponseEntity<?> calorieCalc(@RequestHeader(value = "Authorization") String token){

        ResponseDTO responseDTO;
        try{
            CalorieCalcDTO calorieCalc = calcService.calorieCalc(token);
            return ResponseEntity.ok().body(calorieCalc);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/food-info")
    public ResponseEntity<?> foodCalc(@RequestHeader(value = "Authorization") String token){

        ResponseDTO responseDTO;
        try{
            FoodCalcDTO foodCalcDTO = calcService.foodCalc(token);
            return ResponseEntity.ok().body(foodCalcDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
