    package animal_shop.shop.item.dto;

    import animal_shop.shop.item.entity.ItemQuery;
    import lombok.Data;
    import org.springframework.stereotype.Component;

    @Data
    public class RequestItemQueryDTO {
        private String seller;
        private String product;
        private String contents;
        private String reply;
        private String option_name;
        private String option_price;


    }
