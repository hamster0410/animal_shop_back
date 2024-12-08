package animal_shop.shop.point.service;

import animal_shop.shop.cart_item.entity.CartItem;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.point.entity.Point;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PointSpecification {

    public static Specification<Point> hasYearAndMonthAndDay(Integer year, Integer month, Integer day) {
        return (root, query, criteriaBuilder) -> {
            Expression<LocalDateTime> createdDate = root.get("getDate");
            List<Predicate> predicates = new ArrayList<>();

            // 연도 필터링
            if (year != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, createdDate), year));
            }

            // 월 필터링
            if (month != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, createdDate), month));
            }

            // 일 필터링
            if (day != null) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.function("DAY", Integer.class, createdDate), day));
            }

            // Predicate 리스트를 기준으로 AND 조건 생성
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
