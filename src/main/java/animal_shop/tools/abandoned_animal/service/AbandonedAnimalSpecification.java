package animal_shop.tools.abandoned_animal.service;

import animal_shop.tools.abandoned_animal.entity.AbandonedAnimal;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class AbandonedAnimalSpecification {
    public static Specification<AbandonedAnimal> ageRanges(List<Integer> ranges) {
        return (Root<AbandonedAnimal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (ranges == null || ranges.isEmpty()) {
                return criteriaBuilder.conjunction(); // 빈 조건
            }

            Predicate predicate = criteriaBuilder.disjunction(); // OR 조건 사용
            int currentYear = LocalDate.now().getYear(); // 현재 연도 계산
            for (Integer range : ranges) {
                switch (range) {
                    case 1:
                        predicate = criteriaBuilder.or(
                                predicate,
                                criteriaBuilder.lt(criteriaBuilder.diff(currentYear, root.get("age")), 1) // 1살 미만
                        );
                        break;
                    case 5:
                        predicate = criteriaBuilder.or(
                                predicate,
                                criteriaBuilder.between(criteriaBuilder.diff(currentYear, root.get("age")), 1, 5) // 1~5살
                        );
                        break;
                    case 9:
                        predicate = criteriaBuilder.or(
                                predicate,
                                criteriaBuilder.between(criteriaBuilder.diff(currentYear, root.get("age")), 6, 9) // 6~9살
                        );
                        break;
                    case 10:
                        predicate = criteriaBuilder.or(
                                predicate,
                                criteriaBuilder.ge(criteriaBuilder.diff(currentYear, root.get("age")), 10) // 10살 이상
                        );
                        break;
                }
            }

            return predicate;
        };
    }



    public static Specification<AbandonedAnimal> hasSex(String sex) {
        return (root, query, criteriaBuilder) -> {
            if (sex == null || sex.isEmpty()) {
                return criteriaBuilder.conjunction(); // 조건 없음
            }
            return criteriaBuilder.equal(root.get("sexCd"), sex);
        };
    }

    public static Specification<AbandonedAnimal> isNeutered(String neuter) {
        return (root, query, criteriaBuilder) -> {
            if (neuter == null || neuter.isEmpty()) {
                return criteriaBuilder.conjunction(); // 조건 없음
            }
            return criteriaBuilder.equal(root.get("neuterYn"), neuter);
        };
    }

    public static Specification<AbandonedAnimal> kindCdFilter(String species, List<String> breeds) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction(); // 기본 조건

            // species 조건 추가
            if (species != null && !species.isEmpty()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(root.get("kindCd"), "%" + species + "%") // kindCd에 species 포함 여부
                );
            }

            // inBreed 조건 추가
            if (breeds != null && !breeds.isEmpty()) {
                Predicate breedPredicate = criteriaBuilder.disjunction(); // OR 조건

                for (String breed : breeds) {
                    breedPredicate = criteriaBuilder.or(
                            breedPredicate,
                            criteriaBuilder.like(root.get("kindCd"), "%" + breed + "%") // kindCd에 breed 포함 여부
                    );

                }
                predicate = criteriaBuilder.and(predicate, breedPredicate); // species와 breed 조건 결합
            }

            return predicate;
        };
    }

    public static Specification<AbandonedAnimal> noticeDateBasedOnStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            Predicate predicate;

            if ("T".equalsIgnoreCase(status)) {
                // notice_edt가 오늘 이후 또는 오늘인 데이터
                predicate = criteriaBuilder.like(root.get("processState"), "%보호%");
            } else if ("F".equalsIgnoreCase(status)) {
                // notice_edt가 오늘 이전인 데이터
                predicate = criteriaBuilder.like(root.get("processState"), "%종료%");
            } else {
                // status가 유효하지 않으면 조건 없음
                predicate = criteriaBuilder.conjunction();
            }

            return predicate;
        };
    }

    public static Specification<AbandonedAnimal> locationFilter(List<String> locations) {
        return (root, query, criteriaBuilder) -> {
            if (locations == null || locations.isEmpty()) {
                return criteriaBuilder.conjunction(); // 조건 없음
            }

            // 길이가 1인 경우: 첫 번째 값(상위 지역)만 조회
            if (locations.size() == 1) {
                String location = locations.get(0);
                if (location != null && !location.isEmpty()) {
                    return criteriaBuilder.like(root.get("careAddr"), "%" + location + "%");
                }
                return criteriaBuilder.conjunction();
            }

            // 길이가 2인 경우: 두 번째 값(하위 지역)만 조회
            if (locations.size() == 2) {
                String subLocation = locations.get(1); // 하위 지역
                if (subLocation != null && !subLocation.isEmpty()) {
                    return criteriaBuilder.like(root.get("careAddr"), "%" + subLocation + "%");
                }
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.conjunction();
        };
    }

}
