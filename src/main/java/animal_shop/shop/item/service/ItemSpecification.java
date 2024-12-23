package animal_shop.shop.item.service;

import animal_shop.community.member.entity.Member;
import animal_shop.shop.item.entity.Item;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {

    public static Specification<Item> searchByItemName(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchTerm.toLowerCase() + "%");
        };
    }

    public static Specification<Item> searchByMemberNickname(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("member").get("nickname")), "%" + searchTerm.toLowerCase() + "%");
        };
    }

    public static Specification<Item> searchByMemberId(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) return null;
            return criteriaBuilder.equal(root.get("member").get("id"), searchTerm);
        };
    }

    // 'itemName' 또는 'memberNickname'을 검색하는 조건
    public static Specification<Item> searchByItemNameOrMemberNickname(String searchTerm) {
        return (root, query, criteriaBuilder) -> {
            if (searchTerm == null || searchTerm.isEmpty()) return null;
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + searchTerm.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("member").get("nickname")), "%" + searchTerm.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Item> searchBySpecies(String species) {
        return (root, query, criteriaBuilder) -> {
            if (species == null) return null;
            return criteriaBuilder.equal(root.get("species"), species);
        };
    }

    public static Specification<Item> searchByCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) return null;
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Item> searchByDetailedCategory(String detailedCategory) {
        return (root, query, criteriaBuilder) -> {
            if (detailedCategory == null) return null;
            return criteriaBuilder.equal(root.get("detailed_category"), detailedCategory);
        };
    }

    public static Specification<Item> searchByStatus(String status) {
        return (root, query, criteriaBuilder) ->{
            if (status == null) return null;
            if(status.equals("stop")){
                return criteriaBuilder.equal(root.get("itemSellStatus"),2);
            }else if(status.equals("sold_out")){
                return criteriaBuilder.equal(root.get("itemSellStatus"),1);
            }else if(status.equals("sell")){
                return criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("itemSellStatus"), 0),
                        criteriaBuilder.equal(root.get("itemSellStatus"), 3)
                );
            }else{
                return null;
            }
        };
    }

    public static Specification<Item> searchByItemStatusStop() {
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.equal(root.get("itemSellStatus"),2);
        };
    }

    public static Specification<Item> searchByItemStatusNotStop() {
        return (root, query, criteriaBuilder) ->{
            return criteriaBuilder.notEqual(root.get("itemSellStatus"),2);
        };
    }


    public static Specification<Item> searchByUserId(Member member) {
        return(root, query, criteriaBuilder) -> {
            if(member == null) return null;
            return criteriaBuilder.equal(root.get("member"), member);
        };
    }
    public static Specification<Item> searchByDiscount(Boolean discount) {
        return (root, query, criteriaBuilder) -> {
            if (discount == null) return null;
            if (discount) {
                return criteriaBuilder.equal(root.get("itemSellStatus"), 3);
            } else {
                return criteriaBuilder.notEqual(root.get("itemSellStatus"), 3);  // 상태가 2인 Item만 가져오기
            }
        };
    }
}

