package animal_shop.shop.pet.entity;

import animal_shop.community.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

public class PetEntity {
    @Entity
    @Data
    public static class Pet {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id; // 동물 ID

        @ManyToOne
        @JoinColumn(name="owner_id",nullable = false)
        private Member member;

        private String name; // 동물 이름

        @Enumerated(EnumType.STRING)
        private PetSpecies species; // 종 (개, 고양이, 소동물)

        private String breed; // 품종

        private Boolean isNeutered; // 중성화 유무 (중성화 여부)

        private int age; // 나이

        @Enumerated(EnumType.STRING)
        private Gender gender; // 성별

        private double weight; // 체중

        private String hasRegistrationCertificate; // 동물 등록증 유무

        private String description; // 간단한 자기소개

        private String profileImageUrl; // 동물 프로필 이미지 (URL 형식)

        // Enum: 종 (개, 고양이, 소동물 등)
        public enum PetSpecies {
            DOG, CAT, SMALL_ANIMAL
        }

        // Enum: 성별
        public enum Gender {
            MALE, FEMALE
        }
    }
}
