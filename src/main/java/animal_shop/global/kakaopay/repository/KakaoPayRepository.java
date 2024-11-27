package animal_shop.global.kakaopay.repository;

import animal_shop.global.kakaopay.entity.KakaoPay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoPayRepository extends JpaRepository<KakaoPay,Long> {
}
