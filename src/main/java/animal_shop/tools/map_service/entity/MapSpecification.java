package animal_shop.tools.map_service.entity;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MapSpecification {

    public static Specification<MapEntity> searchByKeyword(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("facilityName")), "%" + searchTerm.toLowerCase() + "%");
        };
    }

    public static Specification<MapEntity> searchByCategory(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) return null;
            return criteriaBuilder.equal(root.get("category3"), searchTerm);
        };
    }

    public static Specification<MapEntity> searchByParking(Boolean searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null) return null;
            if(searchTerm){
                return criteriaBuilder.equal(root.get("parkingAvailable"), "Y");
            }else{
                return null;
            }
        };
    }

    public static Specification<MapEntity> searchByIndoor(Boolean searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null) return null;
            if(searchTerm){
                return criteriaBuilder.equal(root.get("indoorAvailable"), "Y");
            }else{
                return null;
            }
        };
    }

    public static Specification<MapEntity> searchByOutdoor(Boolean searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null) return null;
            if(searchTerm){
                return criteriaBuilder.equal(root.get("outdoorAvailable"), "Y");
            }else{
                return null;
            }
        };
    }

    public static Specification<MapEntity> searchByRange(String sw_x, String sw_y, String ne_x, String ne_y) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (sw_x == null || sw_y == null || ne_x == null || ne_y == null) return null;

            predicates.add(criteriaBuilder.between(root.get("longitude"),sw_x,ne_x));
            predicates.add(criteriaBuilder.between(root.get("latitude"),sw_y,ne_y));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<MapEntity> orderByDistance(double baseLatitude, double baseLongitude) {
        return (root, query, criteriaBuilder) -> {
            // 위도 차이의 절대값
            Expression<Double> latitudeDifference = criteriaBuilder.abs(
                    criteriaBuilder.diff(root.get("latitude"), baseLatitude)
            );

            // 경도 차이의 절대값
            Expression<Double> longitudeDifference = criteriaBuilder.abs(
                    criteriaBuilder.diff(root.get("longitude"), baseLongitude)
            );

            // 절대값 차이의 합
            Expression<Double> totalDifference = criteriaBuilder.sum(latitudeDifference, longitudeDifference);

            // 정렬 추가
            query.orderBy(criteriaBuilder.asc(totalDifference));

            return null; // 정렬만 적용하므로 조건식은 없음
        };
    }

}
