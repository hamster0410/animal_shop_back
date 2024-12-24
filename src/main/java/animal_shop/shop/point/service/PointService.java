package animal_shop.shop.point.service;

import animal_shop.community.member.Role;
import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.shop.point.PointStatus;
import animal_shop.shop.point.dto.WithdrawDTO;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.order_item.dto.MyItemDTO;
import animal_shop.shop.order_item.repository.OrderItemRepository;
import animal_shop.shop.point.dto.*;
import animal_shop.shop.point.entity.Point;
import animal_shop.shop.point.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
        // end 날짜에 시간을 추가 (23:59:59)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(end!=null){
            LocalDateTime endDateTime = LocalDate.parse(end, formatter).atStartOfDay().plusDays(1).minusSeconds(1);
            end = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if(start!=null){
            LocalDateTime startDateTime = LocalDate.parse(start, formatter).atStartOfDay();
            start = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

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

    public List<PointEntireSellerDTO> getSellerSumEntire(String token, String time, String start, String end,String state) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String userId = tokenProvider.extractIdByAccessToken(token);

        Member admin = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));
        if(admin.getRole().equals(Role.USER)){
            throw new IllegalArgumentException("user is not seller or admin");
        }

        if(end!=null){
            LocalDateTime endDateTime = LocalDate.parse(end, formatter).atStartOfDay().plusDays(1).minusSeconds(1);
            end = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        if(start!=null){
            LocalDateTime startDateTime = LocalDate.parse(start, formatter).atStartOfDay();
            start = startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        List<Object[]> objects = pointRepository.findEntireTotalPointsBySellerForTime(time,start,end,state);
        List<PointEntireSellerDTO> pointEntireSellerDTOList= new ArrayList<>();
        for(Object[] obj : objects){
            Member member = memberRepository.findById((Long) obj[0])
                    .orElseThrow(() -> new IllegalArgumentException("member is not found") );
            PointEntireSellerDTO pointYearSellerDTO = new PointEntireSellerDTO((String) obj[1], (BigDecimal) obj[2], member.getNickname());
            pointEntireSellerDTOList.add(pointYearSellerDTO);
        }

        return pointEntireSellerDTOList;
    }

    public long withdrawPoint(String token, WithdrawDTO withdrawDTO) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member admin = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("Member does not exist with ID: "));
        if(!admin.getRole().equals(Role.ADMIN)){
            throw new IllegalArgumentException("user is not admin");
        }

        Member seller = memberRepository.findByNickname(withdrawDTO.getSellerNickname());

        StringTokenizer st = new StringTokenizer(withdrawDTO.getDate(),"-");
        ArrayList<String> date = new ArrayList<>();
        while(st.hasMoreTokens()){
            date.add(st.nextToken());
        }


        Integer year = null;
        Integer month = null;
        Integer day = null;
        if(date.size() > 2){
            day = Integer.valueOf(date.get(2));
        }
        if(date.size() > 1){
            month = Integer.valueOf(date.get(1));
        }
        if (!date.isEmpty()){
            year = Integer.valueOf(date.get(0));
        }else{
            throw new IllegalArgumentException("date error");
        }


        Specification<Point> specification = Specification.where(null);
        // Pageable 설정 (페이지 당 10개로 제한)
        specification = specification.and(PointSpecification.hasYearAndMonthAndDay(year,month,day));

        specification = specification.and(PointSpecification.findBySellerId(seller.getId()));

        List<Point> points = pointRepository.findAll(specification);
        long totalPoint= 0L;
        for(Point point : points){
            System.out.println(point.getGetDate());
            point.setStatus(PointStatus.valueOf("WITHDRAWN"));
            totalPoint += point.getPoint();
            pointRepository.save(point);
        }
        return totalPoint;

    }
}
