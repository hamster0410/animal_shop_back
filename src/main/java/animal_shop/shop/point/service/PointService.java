package animal_shop.shop.point.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.shop.point.dto.PointYearSellerDTO;
import animal_shop.shop.point.dto.PointTotalDTO;
import animal_shop.shop.point.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<PointTotalDTO> getMonthSum(String token, int year) {
        List<Object[]> objects = pointRepository.findMonthlyPointSumsByYear(year);
        List<PointTotalDTO> pointTotalDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            pointTotalDTOList.add(new PointTotalDTO(obj));
        }
        return pointTotalDTOList;
    }


    public List<PointTotalDTO> getDaySum(String token, int year, int month) {
        List<Object[]> objects = pointRepository.findDailyPointSumsByYearAndMonth(year, month);
        List<PointTotalDTO> pointTotalDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            pointTotalDTOList.add(new PointTotalDTO(obj));
        }
        return pointTotalDTOList;
    }

    public List<PointYearSellerDTO> getSellerSumYear(String token,int year) {
        List<Object[]> objects = pointRepository.findTotalPointsBySellerAndYear(year);
        List<PointYearSellerDTO> pointYearSellerDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            Member member = memberRepository.findById((Long) obj[0])
                    .orElseThrow(() -> new IllegalArgumentException("member is not found"));
            PointYearSellerDTO pointYearSellerDTO = new PointYearSellerDTO(String.valueOf(year) , (Long) obj[1], member.getNickname());
            pointYearSellerDTOList.add(pointYearSellerDTO);
        }
        return pointYearSellerDTOList;
    }

    public List<PointYearSellerDTO> getSellerSumMonth(String token, int year, int month) {
        List<Object[]> objects = pointRepository.findTotalPointsBySellerAndMonth(year, month);
        List<PointYearSellerDTO> pointTotalDTOList = new ArrayList<>();

        for(Object[] obj : objects){
            Member member = memberRepository.findById((Long) obj[0])
                    .orElseThrow(() -> new IllegalArgumentException("member is not found") );
            PointYearSellerDTO pointYearSellerDTO = new PointYearSellerDTO(year + "-" + month, (Long) obj[1], member.getNickname());
            pointTotalDTOList.add(pointYearSellerDTO);
        }
        return pointTotalDTOList;
    }

    public List<PointYearSellerDTO> getSellerSumDay(String token, int year, int month) {
        List<Object[]> objects = pointRepository.findDailyTotalPointsBySellerForMonth(year, month);
        List<PointYearSellerDTO> pointTotalDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            Member member = memberRepository.findById((Long) obj[1])
                    .orElseThrow(() -> new IllegalArgumentException("member is not found") );
            PointYearSellerDTO pointYearSellerDTO = new PointYearSellerDTO((String) obj[0], (Long) obj[2], member.getNickname());
            pointTotalDTOList.add(pointYearSellerDTO);
        }
        return pointTotalDTOList;
    }
}
