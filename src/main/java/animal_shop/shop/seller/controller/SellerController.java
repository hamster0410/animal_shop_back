package animal_shop.shop.seller.controller;

import animal_shop.global.dto.ResponseDTO;
import animal_shop.shop.cart_item.dto.CartItemSearchResponse;
import animal_shop.shop.cart_item.service.CartItemService;
import animal_shop.shop.delivery.dto.*;
import animal_shop.shop.delivery.service.DeliveryService;
import animal_shop.shop.item.dto.*;
import animal_shop.shop.item.service.ItemService;
import animal_shop.shop.order_item.dto.MyItemDTO;
import animal_shop.shop.order_item.dto.OrderedItemInfoList;
import animal_shop.shop.point.dto.MyPointDTO;
import animal_shop.shop.point.dto.PointProfitDTOResponse;
import animal_shop.shop.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private PointService pointService;

    @PostMapping("/item/new")
    public ResponseEntity<?> registerItem(@RequestHeader(value = "Authorization") String token, @RequestBody ItemDTOList itemDTOList) {
        ResponseDTO responseDTO = null;

        try {
            itemService.save(token, itemDTOList);
            System.out.println("here 5");
            responseDTO = ResponseDTO.builder()
                    .message("save success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //상품 마이페이지에 뜰려면 보내야 하니까
    @GetMapping("/item/update")
    public ResponseEntity<?> getItems(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody ItemDTOList itemDTOList) {
        ResponseDTO responseDTO = null;
        try {
            itemService.update(token, itemDTOList);
            responseDTO = ResponseDTO.builder()
                    .message("update success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //상품 업데이트
    @PatchMapping("/item/update")
    public ResponseEntity<?> updateItem(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody ItemDTOList itemDTOList) {
        ResponseDTO responseDTO = null;
        try {
            itemService.update(token, itemDTOList);
            responseDTO = ResponseDTO.builder()
                    .message("update success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //상품 삭제
    @PatchMapping("/item/discontinue/{itemId}")
    public ResponseEntity<?> deleteItem(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "itemId") String id) {
        ResponseDTO responseDTO = null;
        try {
            itemService.delete(token, id);
            responseDTO = ResponseDTO.builder()
                    .message("discontinue success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //개별 상품 조회
    @GetMapping("/item/select/{itemId}")
    public ResponseEntity<?> selectItem(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "itemId") String id) {
        ResponseDTO responseDTO = null;
        try {
            ItemDetailDTO itemDetailDTO = itemService.selectItem(token, id);

            return ResponseEntity.ok().body(itemDetailDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //전체 상품 조회
    @GetMapping("/item/select")
    public ResponseEntity<?> selectAll(@RequestHeader(value = "Authorization") String token,
                                       @RequestParam(value = "page", defaultValue = "1") int page) {
        ResponseDTO responseDTO;
        try {
            ItemDTOListResponse items = itemService.selectAll(token, page - 1);
            return ResponseEntity.ok().body(items);
        } catch (IllegalArgumentException | IllegalStateException e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error("An unexpected error occurred")
                    .build();
            return ResponseEntity.internalServerError().body(responseDTO);
        }
    }


    @GetMapping("/stop-item/select")
    public ResponseEntity<?> stopItemList(@RequestHeader(value = "Authorization") String token,
                                       @RequestParam(value = "page", defaultValue = "1") int page) {
        ResponseDTO responseDTO;
        try {
            ItemDTOListResponse items = itemService.stopItemLIst(token, page - 1);
            return ResponseEntity.ok().body(items);
        } catch (IllegalArgumentException | IllegalStateException e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
        //문의 내용 리스트 보기
    @GetMapping("/query/list")
    public ResponseEntity<?> seller_CI(@RequestHeader(value = "Authorization") String token,
                                       @RequestParam(value = "page", defaultValue = "1") int page) {
        ResponseDTO responseDTO = null;
        try {
            QueryResponse orderPage = itemService.find_orders(token, page - 1);

            return ResponseEntity.ok().body(orderPage);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //소비자 문의에 대한 판매자의 답변
    @PatchMapping("/query/comment/{queryId}")
    public ResponseEntity<?> query_comment(@RequestHeader(value = "Authorization") String token,
                                           @PathVariable(value = "queryId") Long queryId,
                                           @RequestBody SellerReplyDTO sellerReplyDTO) {
        ResponseDTO responseDTO = null;
        try {
            itemService.query_comment(token, queryId, sellerReplyDTO);

            responseDTO = ResponseDTO.builder()
                    .message("register Comment")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //소비자 문의에 대한 판매자 답변 삭제
    @PatchMapping("/query/reply/{queryId}")
    ResponseEntity<?> delete_reply(@RequestHeader(value = "Authorization") String token,
                                   @PathVariable(value = "queryId") Long queryId) {
        ResponseDTO responseDTO = null;
        try {
            itemService.delete_reply(token, queryId);

            responseDTO = ResponseDTO.builder()
                    .message("delete reply")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //판매자 물품 주문 리스트
    @GetMapping("/delivery/list")
    ResponseEntity<?> delivery_list(@RequestHeader(value =  "Authorization")String token,
                                    @RequestParam(name = "page", defaultValue = "1")int page){
        ResponseDTO responseDTO = null;
        try{
            DeliveryDTOResponse deliveryDTOResponse = deliveryService.get_list(token,page - 1);

            return ResponseEntity.ok().body(deliveryDTOResponse);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/delivery/detail")
    ResponseEntity<?> delivery_detail(@RequestHeader(value =  "Authorization")String token,
                                    @RequestParam(value = "orderItemId") Long orderItemId){
        ResponseDTO responseDTO = null;
        try{
            DeliveryDetailDTO deliveryDetailDTO = deliveryService.get_detail(orderItemId,token);

            return ResponseEntity.ok().body(deliveryDetailDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @PostMapping("/delivery/approve")
    ResponseEntity<?> delivery_approve(@RequestHeader(value = "Authorization")String token,
                                       @RequestBody DeliveryRequestDTO deliveryRequestDTO) {
        ResponseDTO responseDTO = null;
        try{
            deliveryService.approve(deliveryRequestDTO,token);
            responseDTO = ResponseDTO.builder()
                    .message("delivery approve success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @PostMapping("/delivery/approve_detail")
    ResponseEntity<?> delivery_approve_detail(@RequestHeader(value = "Authorization")String token,
                                       @RequestBody DeliveryApproveDetailDTO deliveryApproveDetailDTO) {
        ResponseDTO responseDTO = null;
        try{
            deliveryService.approve_detail(deliveryApproveDetailDTO,token);
            responseDTO = ResponseDTO.builder()
                    .message("delivery approve success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/delivery/revoke")
    ResponseEntity<?> delivery_revoke(@RequestHeader(value = "Authorization")String token,
                                       @RequestBody DeliveryRequestDTO deliveryRequestDTO) {
        ResponseDTO responseDTO = null;
        try{
            DeliveryRevokeResponse deliveryRevokeResponse = deliveryService.revoke(deliveryRequestDTO,token);
            return ResponseEntity.ok().body(deliveryRevokeResponse);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/delivery/revoke_detail")
    ResponseEntity<?> delivery_revoke_detail(@RequestHeader(value = "Authorization")String token,
                                             @RequestBody DeliveryRevokeDTO deliveryRevokeDTO) {
        ResponseDTO responseDTO = null;
        try{
            DeliveryRevokeResponse deliveryRevokeResponse = deliveryService.revoke_detail(deliveryRevokeDTO,token);

            return ResponseEntity.ok().body(deliveryRevokeResponse);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }



    @PatchMapping("/discount/ok")
    ResponseEntity<?> item_discount(@RequestHeader(value = "Authorization") String token,
                                    @RequestBody ItemDiscountDTO itemDiscountDTO){
        ResponseDTO responseDTO = null;
        try {
            itemService.discount_rate(token,itemDiscountDTO);
            responseDTO = ResponseDTO.builder()
                    .message("Discount success")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
    @PatchMapping("/discount/revoke")
    ResponseEntity<?>no_discount(@RequestHeader(value= "Authorization")String token,
                                 @RequestBody ItemDiscountDTO itemDiscountDTO){
        ResponseDTO responseDTO = null;
        try{
            itemService.no_discount(token,itemDiscountDTO);
            responseDTO = ResponseDTO.builder()
                    .message("Discount failure")
                    .build();
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/cart-item-info")
    ResponseEntity<?>cartItemInfo(@RequestHeader(value= "Authorization")String token,
                                  @RequestParam(value = "year", required = false) Integer year,
                                  @RequestParam(value = "month", required = false) Integer month
                                  ){
        ResponseDTO responseDTO = null;
        try{
            System.out.println("controller " + year + " " + month);
            CartItemSearchResponse cartItemInfo = cartItemService.cartItemInfo(token,year,month);
            return ResponseEntity.ok().body(cartItemInfo);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/profit-item-info")
    ResponseEntity<?>profitItemInfo(@RequestHeader(value= "Authorization")String token,
                                  @RequestParam(value = "year", required = false) Integer year,
                                  @RequestParam(value = "month", required = false) Integer month,
                                    @RequestParam(value = "day", required = false) Integer day
    ){
        ResponseDTO responseDTO = null;
        try{
            System.out.println("controller " + year + " " + month);
            PointProfitDTOResponse pointProfitDTOResponse= cartItemService.ProfitItemInfo(token,year,month,day);
            return ResponseEntity.ok().body(pointProfitDTOResponse);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/ordered-item-info")
    ResponseEntity<?>orderedItemInfo(@RequestHeader(value= "Authorization")String token,
                                    @RequestParam(value = "year", required = false) Integer year,
                                    @RequestParam(value = "month", required = false) Integer month,
                                    @RequestParam(value = "day", required = false) Integer day
    ){
        ResponseDTO responseDTO = null;
        try{
            System.out.println("controller " + year + " " + month);
            OrderedItemInfoList orderedItemInfoList = cartItemService.OrderedItemInfo(token,year,month,day);
            return ResponseEntity.ok().body(orderedItemInfoList);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/total-item-info")
    ResponseEntity<?>totalItemInfo(@RequestHeader(value= "Authorization")String token,
                                   @RequestParam(value = "time", required = false) String time,
                                   @RequestParam(value = "start", required = false) String start,
                                   @RequestParam(value = "end", required = false) String end){
        ResponseDTO responseDTO;
        try{
            List<MyItemDTO> myPointDTOList = pointService.totalItem(token,time, start, end);

            return ResponseEntity.ok().body(myPointDTOList);
        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }


    @GetMapping("/point-time-info")
    public ResponseEntity<?> pointByTime(@RequestHeader(value = "Authorization") String token,
                                         @RequestParam(value = "time", required = false) String time,
                                         @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        ResponseDTO responseDTO;
        try{
            List<MyPointDTO> myPointDTOList = pointService.pointByTime(token,time,page -1);

            return ResponseEntity.ok().body(myPointDTOList);
        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/entire-ci-info")
    public ResponseEntity<?> ciByTime(@RequestHeader(value = "Authorization") String token,
                                         @RequestParam(value = "time", required = false) String time,
                                      @RequestParam(value = "start", required = false) String start,
                                      @RequestParam(value = "end", required = false) String end){
        ResponseDTO responseDTO;
        try{
            List<MyItemDTO> myPointDTOList = cartItemService.entireCartItemInfo(token,time,start,end);

            return ResponseEntity.ok().body(myPointDTOList);
        }catch (Exception e){

            responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?>search_item(@RequestHeader(value = "Authorization",required = false)String token,
                                        @RequestParam(value = "species", required = false) String species,
                                        @RequestParam(value = "category", required = false) String category,
                                        @RequestParam(value = "detailed_category", required = false) String detailed_category,
                                        @RequestParam(value = "searchTerm",required = false)String searchTerm,
                                        @RequestParam(value = "page", required = false) Integer page,
                                        @RequestParam(value = "pageCount", defaultValue = "10", required = false) Integer pageCount,
                                        @RequestParam(value = "status", required = false)String status){
        ResponseDTO responseDTO = null;
        try {
            ItemDTOListResponse item = itemService.searchItemsBySeller(token, searchTerm, species, category, detailed_category, status, page, pageCount);
            return ResponseEntity.ok().body(item);
        }catch (Exception e){
            responseDTO = ResponseDTO.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
