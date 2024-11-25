package animal_shop.shop.item.dto;

import animal_shop.shop.item.entity.ItemQuery;
import lombok.Getter;

import java.time.LocalDateTime;

//판매자에게 보여줄 정보
@Getter
public class ResponseItemQueryDTO {

    private String customer;
    private String product;
    private Long item_query_id;
    private String contents;
    private String option_name;
    private String option_price;
    private String reply;
    private LocalDateTime createdDate;

    //디비로부터 정보 끄집어내는 함수
    public ResponseItemQueryDTO(ItemQuery itemQuery){
        customer = itemQuery.getCustomer().getNickname();
        product = itemQuery.getItem().getName();
        contents = itemQuery.getContents();
        item_query_id = itemQuery.getId();
        reply = itemQuery.getReply();
        option_name = itemQuery.getOption_name();
        option_price = itemQuery.getOption_price();
        createdDate = itemQuery.getCreatedDate();
    }
}
