package animal_shop.shop.point.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.order_item.dto.MyItemDTO;
import animal_shop.shop.order_item.repository.OrderItemRepository;
import animal_shop.shop.point.dto.*;
import animal_shop.shop.point.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private OrderItemRepository orderItemRepository;

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

    public List<MyPointDTO> pointByTime(String token, String time, int page) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        List<Object[]> objects;
        Pageable pageable = (Pageable) PageRequest.of(page,20);
        if (time == null) {
            objects =  pointRepository.myPoint(Long.valueOf(userId), pageable);
            objects.add(objects.get(0));
            objects.set(0,null);
        } else {
            objects =  pointRepository.myPointTime(Long.valueOf(userId),time, pageable);
        }
        List<MyPointDTO> myPointDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            myPointDTOList.add(new MyPointDTO(obj));
        }
        return myPointDTOList;
    }

    public List<MyItemDTO> totalItem(String token, String time, String start, String end ) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        List<Object[]> objects;
        if (time == null) {
            objects =  orderItemRepository.totalItem(Long.valueOf(userId), start, end);
        } else {
            objects =  orderItemRepository.totalItemTime(Long.valueOf(userId), time, start, end);
        }
        List<MyItemDTO> myItemDTOList = new ArrayList<>();
        for(Object[] obj : objects){
            myItemDTOList.add(new MyItemDTO(obj));
        }
        return myItemDTOList;
    }

    public List<PointEntireSellerDTO> getSellerSumEntire(String token, String time, String start, String end) {
        List<Object[]> objects = pointRepository.findEntireTotalPointsBySellerForTime(time,start,end);
        List<PointEntireSellerDTO> pointEntireSellerDTOList= new ArrayList<>();
        for(Object[] obj : objects){
            Member member = memberRepository.findById((Long) obj[0])
                    .orElseThrow(() -> new IllegalArgumentException("member is not found") );
            PointEntireSellerDTO pointYearSellerDTO = new PointEntireSellerDTO((String) obj[1], (BigDecimal) obj[2], member.getNickname());
            pointEntireSellerDTOList.add(pointYearSellerDTO);
        }

        return pointEntireSellerDTOList;
    }
}
