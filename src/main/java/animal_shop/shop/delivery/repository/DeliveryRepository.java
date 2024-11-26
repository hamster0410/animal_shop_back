package animal_shop.shop.delivery.repository;

import animal_shop.community.member.entity.Member;
import animal_shop.shop.delivery.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Page<Delivery> findByMember(Member member, Pageable pageable);

    Delivery findByOrderCode(String orderCode);

}
