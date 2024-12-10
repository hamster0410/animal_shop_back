package animal_shop.tools.abandoned_animal.dto;

import animal_shop.tools.abandoned_animal.entity.AbandonedAnimal;
import lombok.Getter;

@Getter
public class AnimalDetailDTO {
    private long id;

    private String filename;  // 썸네일 이미지 파일명

    private String kindCd;  // 품종 코드 (예: 개, 고양이)

    private long age;  // 나이 (예: 2019년생)

    private String weight;  // 체중 (예: 5kg)

    private String noticeNo;  // 공고번호

    private String noticeSdt;  // 공고 시작일 (YYYYMMDD)

    private String noticeEdt;  // 공고 종료일 (YYYYMMDD)

    private String popfile;  // 공고 이미지 URL

    private String processState;  // 상태 (예: 보호중, 공고중)

    private String sexCd;  // 성별 (M: 수컷, F: 암컷, Q: 미상)

    private String neuterYn;  // 중성화 여부 (Y: 예, N: 아니오, U: 미상)

    // 추가된 필드들
    private String specialMark;  // 특징 (예: 파란색 목줄 착용, 겁이 많음)

    private String careNm;  // 보호소 이름

    private String careTel;  // 보호소 전화번호

    private String careAddr;  // 보호소 주소

    private String orgNm;  // 관할 기관 (예: 시군구명)

    private String chargeNm;  // 담당자 이름

    private String officetel;  // 담당자 연락처

    private String noticeComment;  // 특이사항

    private String desertionNo;

    public AnimalDetailDTO(AbandonedAnimal animal){
        this.id = animal.getId();
        this.age = animal.getAge();
        this.careAddr = animal.getCareAddr();
        this.careNm = animal.getCareNm();
        this.careTel = animal.getCareTel();
        this.chargeNm = animal.getChargeNm();
        this.filename = animal.getFilename();
        this.kindCd = animal.getKindCd();
        this.weight = animal.getWeight();
        this.orgNm = animal.getOrgNm();
        this.officetel = animal.getOfficetel();
        this.noticeComment = animal.getNoticeComment();
        this.noticeEdt = animal.getNoticeEdt();
        this.neuterYn = animal.getNeuterYn();
        this.noticeSdt = animal.getNoticeSdt();
        this.noticeNo = animal.getNoticeNo();
        this.popfile = animal.getPopfile();
        this.processState = animal.getProcessState();
        this.sexCd = animal.getSexCd();
        this.specialMark = animal.getSpecialMark();
        this.desertionNo = animal.getDesertionNo();

    }
}
