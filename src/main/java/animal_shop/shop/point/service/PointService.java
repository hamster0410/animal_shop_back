package animal_shop.shop.point.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.point.dto.PointTotalDTOResponse;
import animal_shop.shop.point.dto.PointYearSellerDTO;
import animal_shop.shop.point.dto.PointTotalDTO;
import animal_shop.shop.point.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    public PointTotalDTOResponse getMonthSum(String token, int year) {
        List<Object[]> objects = pointRepository.findMonthlyPointSumsByYear(year);
        List<PointTotalDTO> pointTotalDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            pointTotalDTOList.add(new PointTotalDTO(obj));
        }
        return PointTotalDTOResponse
                .builder()
                .pointTotalDTOList(pointTotalDTOList)
                .first_date(getEarliestPointDate())
                .build();
    }


    public PointTotalDTOResponse getDaySum(String token, int year, int month) {
        List<Object[]> objects = pointRepository.findDailyPointSumsByYearAndMonth(year, month);
        List<PointTotalDTO> pointTotalDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            pointTotalDTOList.add(new PointTotalDTO(obj));
        }
        return PointTotalDTOResponse
                .builder()
                .pointTotalDTOList(pointTotalDTOList)
                .first_date(getEarliestPointDate())
                .build();
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

    public List<PointYearSellerDTO> getSellerSumDay(String token, int year, int month, int day) {
        List<Object[]> objects = pointRepository.findDailyTotalPointsBySellerForDay(year, month, day);
        List<PointYearSellerDTO> pointTotalDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            Member member = memberRepository.findById((Long) obj[1])
                    .orElseThrow(() -> new IllegalArgumentException("member is not found") );
            PointYearSellerDTO pointYearSellerDTO = new PointYearSellerDTO((String) obj[0], (Long) obj[2], member.getNickname());
            pointTotalDTOList.add(pointYearSellerDTO);
        }

        return pointTotalDTOList;
    }

    public LocalDateTime getEarliestPointDate() {
        return pointRepository.findEarliestPointDate();
    }

    public void withdrawAll(String token) {

    }

    public void pointByTime(String token, String time) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        List<Object[]> objects;
        if (time == null) {
            objects =  pointRepository.myPoint(userId);
        } else {
            objects =  pointRepository.myPointTime(userId,time);
        }
        for(Object[] obj : objects){
            System.out.println(obj[0] + " " + obj[1]);
        }
    }
}
