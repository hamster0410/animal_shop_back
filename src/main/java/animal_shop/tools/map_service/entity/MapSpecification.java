package animal_shop.tools.map_service.entity;

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
                return criteriaBuilder.equal(root.get("parkingAvailable"), "N");
            }
        };
    }

    public static Specification<MapEntity> searchByIndoor(Boolean searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null) return null;
            if(searchTerm){
                return criteriaBuilder.equal(root.get("indoorAvailable"), "Y");
            }else{
                return criteriaBuilder.equal(root.get("indoorAvailable"), "N");
            }
        };
    }

    public static Specification<MapEntity> searchByOutdoor(Boolean searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null) return null;
            if(searchTerm){
                return criteriaBuilder.equal(root.get("outdoorAvailable"), "Y");
            }else{
                return criteriaBuilder.equal(root.get("outdoorAvailable"), "N");
            }
        };
    }

    public static Specification<MapEntity> searchByRange(String sw_x, String sw_y, String ne_x, String ne_y) {
        System.out.println("here 2");
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (sw_x == null || sw_y == null || ne_x == null || ne_y == null) return null;

            predicates.add(criteriaBuilder.between(root.get("longitude"),sw_x,ne_x));
            predicates.add(criteriaBuilder.between(root.get("latitude"),sw_y,ne_y));
            System.out.println("here 3");
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
