package animal_shop.tools.abandoned_animal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimalDTO {

//    AnimalDTO 클래스는 개별 유기 동물에 대한 상세 정보를 저장하는 DTO(Data Transfer Object) 클래스입니다.
//    각 필드는 유기 동물에 대한 특성(예: 유기 번호, 썸네일 이미지, 유기 발생 일자 등)을 포함하고 있습니다.
//    이 클래스는 동물 한 마리의 정보를 표현하기 위해 사용됩니다.

    @JsonProperty("desertionNo")
    private String desertionNo;  // 유기번호

    @JsonProperty("filename")
    private String filename;  // 썸네일 이미지 파일명

    @JsonProperty("happenDt")
    private String happenDt;  // 유기 발생일 (YYYYMMDD)

    @JsonProperty("happenPlace")
    private String happenPlace;  // 유기 발생 장소

    @JsonProperty("kindCd")
    private String kindCd;  // 품종 코드 (예: 개, 고양이)

    @JsonProperty("colorCd")
    private String colorCd;  // 색상 코드

    @JsonProperty("age")
    private String age;  // 나이 (예: 2019년생)

    @JsonProperty("weight")
    private String weight;  // 체중 (예: 5kg)

    @JsonProperty("noticeNo")
    private String noticeNo;  // 공고번호

    @JsonProperty("noticeSdt")
    private String noticeSdt;  // 공고 시작일 (YYYYMMDD)

    @JsonProperty("noticeEdt")
    private String noticeEdt;  // 공고 종료일 (YYYYMMDD)

    @JsonProperty("popfile")
    private String popfile;  // 공고 이미지 URL

    @JsonProperty("processState")
    private String processState;  // 상태 (예: 보호중, 공고중)

    @JsonProperty("sexCd")
    private String sexCd;  // 성별 (M: 수컷, F: 암컷, Q: 미상)

    @JsonProperty("neuterYn")
    private String neuterYn;  // 중성화 여부 (Y: 예, N: 아니오, U: 미상)

    // 추가된 필드들
    @JsonProperty("specialMark")
    private String specialMark;  // 특징 (예: 파란색 목줄 착용, 겁이 많음)

    @JsonProperty("careNm")
    private String careNm;  // 보호소 이름

    @JsonProperty("careTel")
    private String careTel;  // 보호소 전화번호

    @JsonProperty("careAddr")
    private String careAddr;  // 보호소 주소

    @JsonProperty("orgNm")
    private String orgNm;  // 관할 기관 (예: 시군구명)

    @JsonProperty("chargeNm")
    private String chargeNm;  // 담당자 이름

    @JsonProperty("officetel")
    private String officetel;  // 담당자 연락처

    @JsonProperty("noticeComment")
    private String noticeComment;  // 특이사항

    @JsonProperty("numOfRows")
    private int numOfRows;

    @JsonProperty("pageNo")
    private int pageNo;

    @JsonProperty("items")
    private List<AnimalDTO> items;

    private String reqNo;  // 필드 추가

    // getters and setters
    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }
}

