    package animal_shop.shop.item.dto;

    import lombok.Data;

    @Data
    public class RequestItemQueryDTO {
        private String item_id;
        private String contents;
        private String reply;
        private String option_name;
        private String option_price;

    }
