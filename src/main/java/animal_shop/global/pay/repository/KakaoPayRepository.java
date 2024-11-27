package animal_shop.global.pay.repository;

import animal_shop.global.pay.entity.KakaoPay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoPayRepository extends JpaRepository<KakaoPay,Long> {
}
