package animal_shop.shop.cart_item.entity;

import animal_shop.community.member.entity.Member;
import animal_shop.shop.item.entity.Item;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class CartItemSpecification {


    public static Specification<CartItem> hasYearAndMonth(Integer year, Integer month) {
        return (root, query, criteriaBuilder) -> {
            Expression<LocalDateTime> createdDate = root.get("createdDate");
            Predicate yearPredicate = null;
            Predicate monthPredicate = null;
            if (year != null) {
                yearPredicate = criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, createdDate), year);
            }

            // 월이 제공되면, 해당 월에 해당하는 데이터 필터링
            if (month != null) {
                monthPredicate = criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, createdDate), month);
            }

            return criteriaBuilder.and(yearPredicate, monthPredicate);
        };
    }

    public static Specification<CartItem> hasMemberAndItem(Member member, List<Item> items){
        return (root, query, criteriaBuilder) -> {
            // 'member'와 일치하는 Predicate 생성
            Predicate memberPredicate = criteriaBuilder.equal(root.get("cart").get("member"), member);

            // 'items' 리스트 내의 'item'이 포함된 경우를 찾는 Predicate 생성
            Predicate itemPredicate = root.get("item").in(items);

            // 'memberPredicate'와 'itemPredicate'를 AND 조건으로 결합
            return criteriaBuilder.and(memberPredicate, itemPredicate);
        };
    }

//    public static Specification<CartItem> countCartItemsByItem() {
//        return (root, query, criteriaBuilder) -> {
//            // CartItem의 item을 기준으로 Group By를 설정합니다.
//            query.groupBy(root.get("item"));
//
//            // COUNT(CartItem) 설정
//            Expression<Long> countExpression = criteriaBuilder.count(root);
//
//            // COUNT 결과를 SELECT 절로 추가합니다.
//            query.select(root.get("item")); // COUNT를 SELECT로 설정
//            query.
//            // 결과 반환: Item별로 CartItem의 개수를 셀 수 있습니다.
//            return query.getRestriction();
//        };
//    }

}
