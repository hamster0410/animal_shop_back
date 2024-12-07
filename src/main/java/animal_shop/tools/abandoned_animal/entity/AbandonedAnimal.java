package animal_shop.tools.abandoned_animal.entity;

import animal_shop.tools.abandoned_animal.dto.AnimalDTO;
import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
@Table(name = "abandoned_animals")
public class AbandonedAnimal{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String desertionNo;
    private String filename;
    private String happenDt;  // 유기 발생일 (YYYYMMDD)
    private String happenPlace;  // 유기 발생 장소
    private String kindCd;
    private String colorCd;
    private Long age;
    private String weight;
    private String noticeNo;
    private String noticeSdt;
    private String noticeEdt;
    private String popfile;
    private String processState; // 상태
    private String sexCd;        // 성별 (M: 수컷, F: 암컷, Q: 미상)
    private String neuterYn;     // 중성화 여부

    // 추가된 필드들
    private String specialMark;  // 특징
    private String careNm;       // 보호소 이름
    private String careTel;      // 보호소 전화번호
    private String careAddr;     // 보호소 주소
    private String orgNm;        // 관할 기관
    private String chargeNm;     // 담당자 이름
    private String officetel;    // 담당자 연락처
    private String noticeComment;// 특이사항
    private int numOfRows;       // 한 페이지에서 반환되는 동물 개수
    private int pageNo;          // 현재 페이지 번호
    private String reqNo;


    // DTO에서 Entity로 변환하는 메서드
    public static AbandonedAnimal fromDTO(AnimalDTO dto) {
        AbandonedAnimal entity = new AbandonedAnimal();

        entity.setDesertionNo(dto.getDesertionNo());
        entity.setFilename(dto.getFilename());
        entity.setHappenDt(dto.getHappenDt());
        entity.setHappenPlace(dto.getHappenPlace());
        entity.setKindCd(dto.getKindCd());
        entity.setColorCd(dto.getColorCd());

        // 나이와 무게는 숫자로 변환
        entity.setAge(parseAge(dto.getAge()));
        entity.setWeight(dto.getWeight());

        entity.setNoticeNo(dto.getNoticeNo());
        entity.setNoticeSdt(dto.getNoticeSdt());
        entity.setNoticeEdt(dto.getNoticeEdt());
        entity.setPopfile(dto.getPopfile());
        entity.setProcessState(dto.getProcessState());
        entity.setSexCd(dto.getSexCd());
        entity.setNeuterYn(dto.getNeuterYn());
        entity.setSpecialMark(dto.getSpecialMark());
        entity.setCareNm(dto.getCareNm());
        entity.setCareTel(dto.getCareTel());
        entity.setCareAddr(dto.getCareAddr());
        entity.setOrgNm(dto.getOrgNm());
        entity.setChargeNm(dto.getChargeNm());
        entity.setOfficetel(dto.getOfficetel());
        entity.setNoticeComment(dto.getNoticeComment());
        entity.setNumOfRows(dto.getNumOfRows());
        entity.setPageNo(dto.getPageNo());

        // reqNo 설정 (setReqNo 메서드 사용)
        entity.setReqNo(dto.getReqNo());

        return entity;
    }

    // reqNo 설정하는 메서드 (필드에 값을 설정)
    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    // 나이 문자열을 Long으로 변환하는 메서드
    public static Long parseAge(String ageString) {
        // 숫자만 남기고 추출
        String age = ageString.replaceAll("[^0-9]", ""); // 숫자만 남김
        try{
            if (age.length() > 4) {
                age = age.substring(0, 4); // 4자리만 추출
            }
            return Long.valueOf(age); // 4자리로 잘라낸 값을 Long으로 변환
        }catch(Exception e){
            return Long.valueOf(0L); // 4자리로 잘라낸 값을 Long으로 변환
        }
    }


}


