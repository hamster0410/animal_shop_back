package animal_shop.shop.delivery.service;

import animal_shop.shop.delivery.DeliveryStatus;
import animal_shop.shop.delivery.entity.DeliveryProgress;
import animal_shop.shop.delivery.repository.DeliveryProgressRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeliveryScheduler {

    private final DeliveryService deliveryService;
    private final DeliveryProgressRepository deliveryProgressRepository;

    public DeliveryScheduler(DeliveryService deliveryService, DeliveryProgressRepository deliveryProgressRepository) {
        this.deliveryService = deliveryService;
        this.deliveryProgressRepository = deliveryProgressRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행
    public void checkCompletedDeliveries() {
        List<DeliveryProgress> progresses = deliveryProgressRepository.findAllByDeliveryStatusAndDeliveredDateBefore(
                DeliveryStatus.COMPLETED, LocalDateTime.now().minusDays(7));

        for (DeliveryProgress progress : progresses) {
            deliveryService.moveToCompleted(progress);
        }
    }
}
