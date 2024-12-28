package animal_shop.shop.delivery.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.admin.entity.StopItem;
import animal_shop.global.pay.dto.KakaoCancelRequest;
import animal_shop.global.pay.service.KakaoPayService;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.delivery.DeliveryStatus;
import animal_shop.shop.delivery.dto.*;
import animal_shop.shop.delivery.entity.Delivery;
import animal_shop.shop.delivery.entity.DeliveryCompleted;
import animal_shop.shop.delivery.entity.DeliveryItem;
import animal_shop.shop.delivery.entity.DeliveryProgress;
import animal_shop.shop.delivery.repository.DeliveryCompletedRepository;
import animal_shop.shop.delivery.repository.DeliveryItemRepository;
import animal_shop.shop.delivery.repository.DeliveryProgressRepository;
import animal_shop.shop.delivery.repository.DeliveryRepository;
import animal_shop.shop.order.OrderStatus;
import animal_shop.shop.order.entity.Order;
import animal_shop.shop.order.repository.OrderRepository;
import animal_shop.shop.order_item.dto.OrderHistDTO;
import animal_shop.shop.order_item.dto.OrderHistDTOResponse;
import animal_shop.shop.order_item.dto.OrderItemDTO;
import animal_shop.shop.order_item.entity.OrderItem;
import animal_shop.shop.order_item.repository.OrderItemRepository;
import animal_shop.shop.point.PointStatus;
import animal_shop.shop.point.entity.Point;
import animal_shop.shop.point.repository.PointRepository;
import animal_shop.tools.abandoned_animal.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class DeliveryService {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    KakaoPayService kakaoPayService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    DeliveryItemRepository deliveryItemRepository;

    @Autowired
    DeliveryProgressRepository deliveryProgressRepository;

    @Autowired
    DeliveryCompletedRepository deliveryCompletedRepository;

    @Autowired
    PointRepository pointRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    private  JavaMailSender javaMailSender;

    @Transactional(readOnly = true)
    public DeliveryDTOResponse get_list(String token, int page) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
        Pageable pageable = (Pageable) PageRequest.of(page,10, Sort.by("orderDate").descending());

        Page<Delivery> deliveries = deliveryRepository.findByMember(member, pageable);
        List<DeliveryDTO> deliveryDTOList = new ArrayList<>();
        //이 사람 에게 온 배달리스트 검색
        for(Delivery delivery : deliveries){
            DeliveryDTO dto = new DeliveryDTO();
            Member customer = memberRepository.findById(delivery.getDeliveryItems().get(0).getBuyerId())
                            .orElseThrow(() -> new IllegalArgumentException("member is not found"));
            dto.setId(delivery.getId());
            dto.setDeliveryId(delivery.getId());
            dto.setCustomer(customer.getNickname());
            dto.setOrderId(delivery.getOrderId());
            dto.setTid(delivery.getTid());
            dto.setOrderDate(delivery.getOrderDate());
            dto.setTotalPrice(delivery.getTotalPrice());

            List<DeliveryItemDTO> deliveryItemDTOList = new ArrayList<>();

            //잃어버린 썸네일을 찾아서...
            for(DeliveryItem deliveryItem : delivery.getDeliveryItems()){
                DeliveryItemDTO deliveryItemDTO = new DeliveryItemDTO(deliveryItem);
                OrderItem orderItem = orderItemRepository.findById(deliveryItem.getOrderItemId())
                                .orElseThrow(() -> new IllegalArgumentException("order item is not found"));
                deliveryItemDTO.setThumbnailUrl(orderItem.getItem().getThumbnail_url().get(0));
                deliveryItemDTOList.add(deliveryItemDTO);
            }
            dto.setDeliveryItemDTOList(deliveryItemDTOList);
            deliveryDTOList.add(dto);
        }

        return DeliveryDTOResponse.builder()
                .deliveryDTOList(deliveryDTOList)
                .total_count(deliveries.getTotalElements())
                .build();

    }

    @Transactional(readOnly = true)
    public DeliveryDetailDTO get_detail(Long orderItemId, String token) {

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("order item is not found"));
        if(!tokenProvider.extractIdByAccessToken(token).equals(String.valueOf(orderItem.getItem().getMember().getId()))){
            throw new IllegalArgumentException("you are not this seller");

        }
        Order order = orderItem.getOrder();
        DeliveryDetailDTO deliveryDetailDTO = DeliveryDetailDTO.builder()
                .recipient(orderItem.getOrder().getRecipient())
                .thumbnailUrl(orderItem.getItem().getThumbnail_url().get(0))
                .itemName(orderItem.getItem().getName())
                .optionName(orderItem.getOrder_name())
                .total_price(orderItem.getTotalPrice())
                .address(order.getAddress())
                .order_code(order.getOrderCode())
                .order_date(order.getOrderDate())
                .order_status(orderItem.getPaymentStatus())
                .phone_number(order.getPhoneNumber())
                .quantity(orderItem.getCount())
                .build();

        return deliveryDetailDTO;

    }

    @Transactional
    public void approve(DeliveryRequestDTO deliveryRequestDTO, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        Delivery delivery = deliveryRepository.findById(deliveryRequestDTO.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("delivery is not found"));

        for (DeliveryItem d : delivery.getDeliveryItems()) {
            if (d.isDelivery_revoke()) {
                throw new IllegalStateException("some delivery item is revoke");
            }

            //배송 승인처리
            d.setDelivery_approval(true);

            //배송 진행 테이블 생성
            OrderItem orderItem = orderItemRepository.findById(d.getOrderItemId())
                    .orElseThrow(() -> new IllegalArgumentException("orderItem is not found"));
            DeliveryProgress deliveryProgress = new DeliveryProgress(d,orderItem);
            deliveryProgressRepository.save(deliveryProgress);
        }

        Order order = orderRepository.findById(delivery.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
        order.setOrderStatus(OrderStatus.valueOf("PROGRESS"));

        for(OrderItem orderItem : order.getOrderItems()){
            if(orderItem.getItem().getMember().equals(member) ){
                if(orderItem.isDelivery_revoke()){
                    throw new IllegalStateException("some order item is revoke");

                }else orderItem.setDelivery_approval(true);
            }
        }

    }

    @Transactional
    public void approve_detail(DeliveryApproveDetailDTO deliveryApproveDetailDTO, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));
        OrderItem orderItem = orderItemRepository.findById(deliveryApproveDetailDTO.getOrderItemId())
                .orElseThrow(() -> new IllegalArgumentException("order item not found"));
        if(orderItem.isDelivery_revoke()){
            throw new IllegalArgumentException("orderItem is revoke");
        }
        orderItem.setDelivery_approval(true);
        Order order = orderItem.getOrder();
        order.setOrderStatus(OrderStatus.valueOf("PROGRESS"));

        orderItemRepository.save(orderItem);

        DeliveryItem deliveryItem = deliveryItemRepository.findByOrderItemId(deliveryApproveDetailDTO.getOrderItemId());

        //배송 진행 테이블 생성
        DeliveryProgress deliveryProgress = new DeliveryProgress(deliveryItem,orderItem);
        deliveryProgressRepository.save(deliveryProgress);

        if(!member.getId().equals(deliveryItem.getSellerId())){
            throw new IllegalArgumentException("seller is not matching");
        }

        deliveryItem.setDelivery_approval(true);
        deliveryItemRepository.save(deliveryItem);

    }

    @Transactional
    public DeliveryRevokeResponse revoke(DeliveryRequestDTO deliveryRequestDTO, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        Delivery delivery = deliveryRepository.findById(deliveryRequestDTO.getDeliveryId())
                .orElseThrow(() -> new IllegalArgumentException("delivery is not found"));

        if(!member.getId().equals(delivery.getDeliveryItems().get(0).getSellerId())){
            throw new IllegalArgumentException("seller is not matching");
        }

        Order order = orderRepository.findById(delivery.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("order not found"));

        for (DeliveryItem d : delivery.getDeliveryItems()) {
            d.setDelivery_revoke(true);
        }

        for(OrderItem orderItem : order.getOrderItems()){
            if(orderItem.getItem().getMember().equals(member) ){

                orderItem.setDelivery_revoke(true);
            }
        }

        // itemQuantity 계산: 배송 아이템들의 전체 수량 합산
        int itemQuantity = delivery.getDeliveryItems().stream()
                .mapToInt(DeliveryItem::getQuantity)
                .sum();


        //결제 취소 로직
        KakaoCancelRequest kakaoCancelRequest = new KakaoCancelRequest();
        kakaoCancelRequest.setTid(order.getTid());
        kakaoCancelRequest.setItemName(order.getOrderItems().get(0).getOrder_name() + " 외 " + order.getOrderItems().size() +"건");
        kakaoCancelRequest.setItemQuantity(String.valueOf(order.getOrderItems().size()));
        kakaoCancelRequest.setCancelAmount(order.getTotalPrice());
        kakaoCancelRequest.setCancelTaxFreeAmount(0);
        kakaoCancelRequest.setCancelVatAmount(0);

        kakaoPayService.kakaoCancel(kakaoCancelRequest);


        // 이메일 정보 준비
        String itemName = delivery.getDeliveryItems().get(0).getItemName() + "외 " + (delivery.getDeliveryItems().size() - 1) + "건";
        String buyerName = order.getMember().getUsername();
        String sellerName = delivery.getMember().getNickname();
//        int itemQuantity = delivery.getDeliveryItems().size();
        long cancelAmount = delivery.getDeliveryItems().stream()
                .mapToLong(deliveryItem -> deliveryItem.getQuantity() * deliveryItem.getOptionPrice())
                .sum();

        sendCancellationEmail(order.getMember().getMail(), itemName, itemQuantity, (int) cancelAmount, sellerName, buyerName);

        return DeliveryRevokeResponse.builder()
                .tid(order.getTid())
                .itemName(itemName)
                .itemQuantity(String.valueOf(itemQuantity))
                .cancelAmount(cancelAmount)
                .build();
    }

    @Transactional
    public DeliveryRevokeResponse revoke_detail(DeliveryRevokeDTO deliveryRevokeDTO, String token) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Member member = memberRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        OrderItem orderItem = null;

        int itemQuantity = 0;
        int cancelAmount = 0;
        int totalItemCount = 0;
        for(Long orderItemId : deliveryRevokeDTO.getOrderItemIds()){
            orderItem = orderItemRepository.findById(orderItemId)
                    .orElseThrow(() -> new IllegalArgumentException("order item not found"));
            itemQuantity++;
            totalItemCount = orderItem.getCount();
            cancelAmount += orderItem.getOrder_price() * orderItem.getCount();

            orderItem.setDelivery_revoke(true);

            DeliveryItem deliveryItem = deliveryItemRepository.findByOrderItemId(orderItemId);

            if(!member.getId().equals(deliveryItem.getSellerId())){
                throw new IllegalArgumentException("seller is not matching");
            }

            deliveryItem.setDelivery_revoke(true);
        }

        String item_name = orderItem.getItem().getName();
        String sellerName = orderItem.getItem().getMember().getNickname();
//        String buyer_name = orderItem.getUsername();
        if(itemQuantity != 1){
            item_name += "외 " + (itemQuantity - 1) + "건";
        }

        // 결제 취소 로직
        KakaoCancelRequest kakaoCancelRequest = new KakaoCancelRequest();
        kakaoCancelRequest.setTid(orderItem.getOrder().getTid());
        kakaoCancelRequest.setItemName(item_name);
        kakaoCancelRequest.setItemQuantity(String.valueOf(itemQuantity));
        kakaoCancelRequest.setCancelAmount(cancelAmount);
        kakaoCancelRequest.setCancelTaxFreeAmount(0);
        kakaoCancelRequest.setCancelVatAmount(0);

        kakaoPayService.kakaoCancel(kakaoCancelRequest);

        // 구매자에게 주문 취소 알림 메일 전송
        sendCancellationEmail(orderItem.getOrder().getMember().getMail(), item_name, totalItemCount, cancelAmount, sellerName, orderItem.getOrder().getMember().getUsername());

        return DeliveryRevokeResponse.builder()
                .tid(orderItem.getOrder().getTid())
                .itemQuantity(String.valueOf(itemQuantity))
                .cancelAmount((long) cancelAmount)
                .itemName(item_name)
                .build();
    }

    private void sendCancellationEmail(String email, String itemName, int itemCount, int cancelAmount, String sellerName, String userName) {
        String subject = "주문 취소 알림";
        String message = String.format(
                "<h1>안녕하세요, %s님</h1>" +
                        "<p>구매하신 상품이 판매자(%s)에 의해 취소되었습니다.</p>" +
                        "<ul>" +
                        "<li><b>상품명:</b> %s</li>" +
                        "<li><b>취소 수량:</b> %d</li>" + // itemQuantity 수정
                        "<li><b>취소 금액:</b> %d원</li>" +
                        "</ul>" +
                        "<p>추가 문의사항은 고객센터로 연락 부탁드립니다.</p>",
                userName, sellerName, itemName, itemCount, cancelAmount // 여기에서 itemQuantity를 사용
        );

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true);  // true = HTML 형식

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


    public void createDelivery(Member m, List<OrderItem> orderItems, Order order) {
        Delivery delivery = new Delivery(m, orderItems, order);
        deliveryRepository.save(delivery);
    }

    // `DeliveryProgress`를 `DeliveryCompleted`로 이동
    public void moveToCompleted(DeliveryProgress progress) {
        DeliveryCompleted completed = new DeliveryCompleted();
        completed.setDeliveryItemId(progress.getDeliveryItemId());
        completed.setSellerId(progress.getSellerId());
        completed.setBuyerId(progress.getBuyerId());
        completed.setAddress(progress.getAddress());
        completed.setTrackingNumber(progress.getTrackingNumber());
        completed.setCourier(progress.getCourier());
        completed.setOrderId(progress.getOrderId());
        //현재시간이 배송 완료시간
        completed.setDeliveredDate(LocalDateTime.now());

        // 포인트 추가
        Point point = addPoints(completed);
        completed.setPointId(point.getId());

        deliveryCompletedRepository.save(completed);
        deliveryProgressRepository.delete(progress); // 기존 데이터를 삭제
    }

    // 포인트 추가
    private Point addPoints(DeliveryCompleted completed) {
        Point point = new Point();
        DeliveryItem deliveryItem = deliveryItemRepository.findById(completed.getDeliveryItemId())
                .orElseThrow(() -> new IllegalArgumentException("delivery item is not found"));
        OrderItem orderItem = orderItemRepository.findById(deliveryItem.getOrderItemId())
                        .orElseThrow(() -> new IllegalArgumentException("not found orderItem id"));
        point.setSellerId(completed.getSellerId());
        point.setBuyerId(completed.getBuyerId());
        point.setItemId(orderItem.getItem().getId());
        point.setItemName(deliveryItem.getItemName());
        point.setOptionName(deliveryItem.getOptionName());
        point.setQuantity(deliveryItem.getQuantity());
        point.setPrice(deliveryItem.getOptionPrice());
        point.setStatus(PointStatus.valueOf("AVAILABLE"));
        point.setPoint(deliveryItem.getOptionPrice() * deliveryItem.getQuantity()); // 기본 포인트 (비즈니스 로직에 따라 조정)
        point.setGetDate(LocalDateTime.now());
        point.setDeliveryCompletedId(completed.getId());

        pointRepository.save(point);

        return point;
    }

    public OrderHistDTOResponse get_deliveryList(String token, int page) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Pageable pageable = (Pageable) PageRequest.of(page,10, Sort.by("deliveredDate").descending());

        Page<DeliveryProgress> deliveryProgresses = deliveryProgressRepository.findByBuyerId(Long.valueOf(userId),pageable);

        List<OrderHistDTO> orderHistDTOList = new ArrayList<>();

        HashMap<Long,ArrayList<OrderItemDTO>> hashMap = new HashMap<>();
        HashMap<Long,DeliveryProgress> hashMap2 = new HashMap<>();
        for(DeliveryProgress deliveryProgress : deliveryProgresses) {

            DeliveryItem deliveryItem = deliveryItemRepository.findById(deliveryProgress.getDeliveryItemId())
                    .orElseThrow(() -> new IllegalArgumentException("deliveryItem is not found"));

            OrderItem orderItem = orderItemRepository.findById(deliveryItem.getOrderItemId())
                    .orElseThrow(() -> new IllegalArgumentException("order item not found"));

            if (!hashMap.containsKey(deliveryProgress.getOrderId())) {
                hashMap.put(deliveryProgress.getOrderId(), new ArrayList<OrderItemDTO>());
                hashMap.get(deliveryProgress.getOrderId()).add(new OrderItemDTO(deliveryProgress, deliveryItem, orderItem));
            } else {
                hashMap.get(deliveryProgress.getOrderId()).add(new OrderItemDTO(deliveryProgress,deliveryItem, orderItem));
            }

            if (!hashMap2.containsKey(deliveryProgress.getOrderId())) {
                hashMap2.put(deliveryProgress.getOrderId(), deliveryProgress);
            }
        }

        for(Long orderId :hashMap2.keySet()){
            OrderHistDTO orderHistDTO = new OrderHistDTO();
            orderHistDTO.setOrderId(orderId);
            orderHistDTO.setOrderStatus(OrderStatus.valueOf("PROGRESS"));
            orderHistDTO.setOrderDate(String.valueOf(hashMap2.get(orderId).getDeliveredDate()));
            orderHistDTO.setOrderItemDTOList(hashMap.get(orderId));
            orderHistDTOList.add(orderHistDTO);
        }


        return OrderHistDTOResponse.builder()
                .orderHistDTOList(orderHistDTOList)
                .total_count((long) orderHistDTOList.size())
                .build();
    }

    public OrderHistDTOResponse get_deliveryCompltedList(String token, int page) {
        String userId = tokenProvider.extractIdByAccessToken(token);
        Pageable pageable = (Pageable) PageRequest.of(page,10, Sort.by("deliveredDate").descending());

        Page<DeliveryCompleted> deliveryCompleteds = deliveryCompletedRepository.findByBuyerId(Long.valueOf(userId),pageable);
        List<DeliveryCustomerDTO> deliveryCustomerDTOList = new ArrayList<>();

        List<OrderHistDTO> orderHistDTOList = new ArrayList<>();

        HashMap<Long,ArrayList<OrderItemDTO>> hashMap = new HashMap<>();
        HashMap<Long,DeliveryCompleted> hashMap2 = new HashMap<>();

        for(DeliveryCompleted deliveryCompleted : deliveryCompleteds){

            DeliveryItem deliveryItem = deliveryItemRepository.findById(deliveryCompleted.getDeliveryItemId())
                    .orElseThrow(() -> new IllegalArgumentException("deliveryItem is not found"));

            OrderItem orderItem = orderItemRepository.findById(deliveryItem.getOrderItemId())
                    .orElseThrow(() -> new IllegalArgumentException("order item not found"));

            if (!hashMap.containsKey(deliveryCompleted.getOrderId())) {
                hashMap.put(deliveryCompleted.getOrderId(), new ArrayList<OrderItemDTO>());
                hashMap.get(deliveryCompleted.getOrderId()).add(new OrderItemDTO(deliveryCompleted,deliveryItem, orderItem));
            } else {
                hashMap.get(deliveryCompleted.getOrderId()).add(new OrderItemDTO(deliveryCompleted,deliveryItem, orderItem));
            }

            if (!hashMap2.containsKey(deliveryCompleted.getOrderId())) {
                hashMap2.put(deliveryCompleted.getOrderId(), deliveryCompleted);
            }
        }
        for(Long orderId :hashMap2.keySet()){
            OrderHistDTO orderHistDTO = new OrderHistDTO();
            orderHistDTO.setOrderId(orderId);
            orderHistDTO.setOrderStatus(OrderStatus.valueOf("COMPLETED"));
            orderHistDTO.setOrderDate(String.valueOf(hashMap2.get(orderId).getDeliveredDate()));
            orderHistDTO.setOrderItemDTOList(hashMap.get(orderId));
            orderHistDTOList.add(orderHistDTO);
        }
        return OrderHistDTOResponse.builder()
                .orderHistDTOList(orderHistDTOList)
                .total_count((long) orderHistDTOList.size())
                .build();
    }
    public void delivery_check(String token, DeliveryCheckDTO deliveryCheckDTO) {

        String userId = tokenProvider.extractIdByAccessToken(token);
        for(Long id : deliveryCheckDTO.getDeliveryProgressId()){
            System.out.println("here " + id);
            DeliveryProgress deliveryProgress = deliveryProgressRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("deliveryProgress is not found"));

            if(!Long.valueOf(userId).equals(deliveryProgress.getBuyerId())){
                throw new IllegalArgumentException("not buyer");
            }

            deliveryProgress.setDeliveryStatus(DeliveryStatus.COMPLETED);
            moveToCompleted(deliveryProgress);
        }
    }

}

