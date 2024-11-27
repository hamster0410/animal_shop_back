package animal_shop.shop.cart.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartDetailDTOResponse {
    private String tid;
    private List<CartDetailDTO> cartDetailDTOList;
    private Long total_count;

}
