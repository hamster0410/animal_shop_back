package animal_shop.shop.point.service;

import animal_shop.shop.point.entity.Point;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class PointSpecification {
    public static Specification<Point> monthlyPointSumByYear(int year) {
        return (root, query, criteriaBuilder) -> {
            Expression<String> monthExpression = criteriaBuilder.function("DATE_FORMAT", String.class, root.get("getDate"), criteriaBuilder.literal("%Y-%m"));
            Expression<Integer> yearExpression = criteriaBuilder.function("YEAR", Integer.class, root.get("getDate"));

            Predicate yearPredicate = criteriaBuilder.equal(yearExpression, year);

            query.groupBy(monthExpression);
            query.orderBy(criteriaBuilder.asc(monthExpression));

            return criteriaBuilder.and(yearPredicate);
        };
    }

    public static Specification<Point> dailyPointSumByYearAndMonth(int year, int month) {
        return (root, query, criteriaBuilder) -> {
            Expression<String> dayExpression = criteriaBuilder.function("DATE_FORMAT", String.class, root.get("getDate"), criteriaBuilder.literal("%Y-%m-%d"));
            Expression<Integer> yearExpression = criteriaBuilder.function("YEAR", Integer.class, root.get("getDate"));
            Expression<Integer> monthExpression = criteriaBuilder.function("MONTH", Integer.class, root.get("getDate"));

            Predicate yearPredicate = criteriaBuilder.equal(yearExpression, year);
            Predicate monthPredicate = criteriaBuilder.equal(monthExpression, month);

            query.groupBy(dayExpression);
            query.orderBy(criteriaBuilder.asc(dayExpression));

            return criteriaBuilder.and(yearPredicate, monthPredicate);
        };
    }

    public static Specification<Point> totalPointsBySellerAndYear(int year) {
        return (root, query, criteriaBuilder) -> {
            Expression<Integer> yearExpression = criteriaBuilder.function("YEAR", Integer.class, root.get("getDate"));

            Predicate yearPredicate = criteriaBuilder.equal(yearExpression, year);

            query.groupBy(root.get("sellerId"));
            query.orderBy(criteriaBuilder.asc(root.get("sellerId")));

            return criteriaBuilder.and(yearPredicate);
        };
    }

    public static Specification<Point> totalPointsBySellerAndMonth(int year, int month) {
        return (root, query, criteriaBuilder) -> {
            Expression<Integer> yearExpression = criteriaBuilder.function("YEAR", Integer.class, root.get("getDate"));
            Expression<Integer> monthExpression = criteriaBuilder.function("MONTH", Integer.class, root.get("getDate"));

            Predicate yearPredicate = criteriaBuilder.equal(yearExpression, year);
            Predicate monthPredicate = criteriaBuilder.equal(monthExpression, month);

            query.groupBy(root.get("sellerId"));
            query.orderBy(criteriaBuilder.asc(root.get("sellerId")));

            return criteriaBuilder.and(yearPredicate, monthPredicate);
        };
    }
}
