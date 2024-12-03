package animal_shop.tools.abandoned_animal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AbandonedAnimalScheduler {

    @Autowired
    AbandonedAnimalService animalService;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void newInfo(){
        animalService.storeAPIInfo();
    }
}
