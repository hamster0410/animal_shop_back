package animal_shop.shop.cart.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.cart.dto.CartDetailDTO;
import animal_shop.shop.cart.dto.CartDetailDTOResponse;
import animal_shop.shop.cart.dto.CartItemDTO;
import animal_shop.shop.cart.entity.Cart;
import animal_shop.shop.cart.repository.CartRepository;
import animal_shop.shop.cart_item.dto.CartItemDetailRequest;
import animal_shop.shop.cart_item.dto.CartItemDetailResponse;
import animal_shop.shop.cart_item.dto.CartItemOptionDTO;
import animal_shop.shop.cart_item.entity.CartItem;
import animal_shop.shop.cart_item.repository.CartItemRepository;
import animal_shop.shop.item.entity.Item;
import animal_shop.shop.item.entity.Option;
import animal_shop.shop.item.repository.ItemRepository;
import animal_shop.shop.item.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private TokenProvider tokenProvider;

    public Long addCart(CartItemDTO cartItemDTO, String token){
        Item item = itemRepository.findById(cartItemDTO.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("item not found"));

        Long userId = Long.valueOf(tokenProvider.extractIdByAccessToken(token));

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("member not found"));

        System.out.println(cartItemDTO.getItemId() + " " + cartItemDTO.getOptionId());

        Cart cart = cartRepository.findByMemberId(userId);
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }
        //옵션처리
        Option option = null;
        if(cartItemDTO.getOptionId() == null){
            option = item.getOptions().get(0);
        }else{
            option = optionRepository.findById(cartItemDTO.getOptionId())
                    .orElseThrow(()->new IllegalArgumentException("option not found"));
        }

        //현재 상품이 장바구니에 들어가있는지 조회
        CartItem savedCartItem =
                cartItemRepository.findByCartIdAndItemIdAndOptionId(cart.getId(), item.getId(), option.getId());

        //장바구니에 있는 상품일 경우 기존 수량에 현재 장바구니에 담을 수량만큼 더해줍니다.
        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDTO.getCount());
            return savedCartItem.getId();
        }else{
            //장바구니 엔티티, 상품 엔티티, 장바구니에 담을 수량을 이용하여 CartItem엔티티를 생성합니다.
            CartItem cartItem =
                    CartItem.createCartItem(cart, item, cartItemDTO.getCount(), option);
            //장바구니에 들어갈 상품을 저장
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    public CartDetailDTOResponse getCartList(String token, int page) {

        Pageable pageable = (Pageable) PageRequest.of(page,10);

        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();
        Page<CartItem> cartItems = null;
        String userId = tokenProvider.extractIdByAccessToken(token);
        Cart cart = cartRepository.findByMemberId(Long.valueOf(userId));

        CartDetailDTOResponse cartDetailDTOResponse = null;

        if(cart == null){
            return cartDetailDTOResponse;
        }

//        cartDetailDTOList = cartItemRepository.findCartDetailDtoList(cart.getId(),pageable);
        cartItems = cartItemRepository.findByCartIdOrderByCreatedDateDesc(cart.getId(),pageable);
        for(CartItem ci: cartItems){
            CartDetailDTO cartDetailDTO = new CartDetailDTO(ci);
            cartDetailDTOList.add(cartDetailDTO);
        }

        cartDetailDTOResponse = CartDetailDTOResponse.builder()
                .cartDetailDTOList(cartDetailDTOList)
                .total_count(cartItems.getTotalElements())
                .build();
        return cartDetailDTOResponse;

    }

    public CartItemDetailResponse getCartItemDetail(Long cartItemId, CartItemDetailRequest cartItemDetailRequest) {

        System.out.println("here 1");
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("cart Item not found"));
        List<CartItemOptionDTO> ciod = new ArrayList<>();
        List<CartItemOptionDTO> optionList = new ArrayList<>();
        System.out.println("here 2");
        //장바구니 특정 아이템의 옵션들
        List<Option> options = cartItem.getItem().getOptions();
        System.out.println("here 3");
        //현재 장바구니의 아이템 목록들 조회
        for(CartDetailDTO cartDetailDTO : cartItemDetailRequest.getCartDetailDTOList()){

            System.out.println("here 4");
            //만약 내가 고치려고 하는 아이템이 장바구니의 아이템과 같으면
           if(cartDetailDTO.getCartItemId().equals(cartItemId)){
               System.out.println(cartDetailDTO.getCartItemId() + " " + cartItemId);
               //해당아이템에 전체 옵션을 조회한다.
               for(Option o : options){

                   //내가 선택한 옵션인 경우에는 건너 뛴다.
                   if(o.equals(cartItem.getOption())) {
                       optionList.add(new CartItemOptionDTO(o));
                       continue;
                   }

                   //이미 있는 아이템의 옵션은 제거한다.
                   if(o.getName().equals(cartDetailDTO.getOption_name())){
                       optionList.add(new CartItemOptionDTO(o));
                   }else{
                       continue;
                   }
               }
           }
        }

        return CartItemDetailResponse.builder()
                .cartItemId(cartItemId)
                .cartItemImg(cartItem.getItem().getThumbnail_url().get(0))
                .cartItemName(cartItem.getItem().getName())
                .options(optionList)
                .total_count(cartItemDetailRequest.getTotal_count())
                .build();
    }
}

