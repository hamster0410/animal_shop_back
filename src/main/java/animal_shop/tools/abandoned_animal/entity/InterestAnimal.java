package animal_shop.tools.abandoned_animal.entity;

import animal_shop.community.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "interest_animal")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class InterestAnimal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attachmentUrl") // DB 컬럼명과 동일
    private String attachment_url;   // 첨부파일 URL

    private String kind_cd;

    private String neuter_yn;

    private String special_mark;

    private String sex_cd;

    private long age;

    @Column(name = "care_nm") // DB 컬럼명이 필드명과 다를 경우
    private String care_nm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // FK 설정
    private Member member;

    private String desertionNo;


    public InterestAnimal(Member member, AbandonedAnimal abandonedAnimal) {
        this.attachment_url = abandonedAnimal.getPopfile();
        this.care_nm = abandonedAnimal.getCareNm();
        this.kind_cd = abandonedAnimal.getKindCd();
        this.neuter_yn = abandonedAnimal.getNeuterYn();
        this.special_mark = abandonedAnimal.getSpecialMark();
        this.sex_cd = abandonedAnimal.getSexCd();
        this.age = LocalDate.now().getYear() - abandonedAnimal.getAge();
        this.member = member;
        this.desertionNo = abandonedAnimal.getDesertionNo();
    }

}
