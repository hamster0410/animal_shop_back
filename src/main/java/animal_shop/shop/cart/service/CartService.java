package animal_shop.shop.cart.service;

import animal_shop.community.member.entity.Member;
import animal_shop.community.member.repository.MemberRepository;
import animal_shop.global.security.TokenProvider;
import animal_shop.shop.cart.dto.CartDetailDTO;
import animal_shop.shop.cart.dto.CartDetailDTOResponse;
import animal_shop.shop.cart.dto.CartItemDTO;
import animal_shop.shop.cart.dto.CartItemUpdateDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

    @Transactional
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
    @Transactional
    public CartDetailDTOResponse getCartList(String token) {


        List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();
        List<CartItem> cartItems = null;
        String userId = tokenProvider.extractIdByAccessToken(token);
        Cart cart = cartRepository.findByMemberId(Long.valueOf(userId));

        CartDetailDTOResponse cartDetailDTOResponse = null;

        if(cart == null){
            return cartDetailDTOResponse;
        }

//        cartDetailDTOList = cartItemRepository.findCartDetailDtoList(cart.getId(),pageable);
        cartItems = cartItemRepository.findByCartIdOrderByCreatedDateDesc(cart.getId());
        for(CartItem ci: cartItems){
            CartDetailDTO cartDetailDTO = new CartDetailDTO(ci);
            cartDetailDTOList.add(cartDetailDTO);
        }

        cartDetailDTOResponse = CartDetailDTOResponse.builder()
                .cartDetailDTOList(cartDetailDTOList)
                .total_count((long) cartItems.size())
                .build();
        return cartDetailDTOResponse;

    }
    @Transactional
    public CartItemDetailResponse getCartItemDetail(Long cartItemId, CartItemDetailRequest cartItemDetailRequest) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        // 기존 아이템 옵션 가져오기 및 DTO 변환
        List<CartItemOptionDTO> options = cartItem.getItem().getOptions().stream()
                .map(CartItemOptionDTO::new)
                .collect(Collectors.toList());

        // 제거할 옵션 이름 추출
        List<String> optionNamesToRemove = cartItemDetailRequest.getCartDetailDTOList().stream()
                .filter(cartDetailDTO -> cartDetailDTO.getItemNm().equals(cartItem.getItem().getName()))
                .map(CartDetailDTO::getOption_name)
                .collect(Collectors.toList());

        // 옵션 필터링
        options = options.stream()
                .filter(option -> !optionNamesToRemove.contains(option.getName()))
                .collect(Collectors.toList());

        // 현재 선택된 옵션 추가
        options.add(new CartItemOptionDTO(cartItem.getOption()));

        return CartItemDetailResponse.builder()
                .cartItemId(cartItemId)
                .cartItemImg(cartItem.getItem().getThumbnail_url().get(0))
                .cartItemName(cartItem.getItem().getName())
                .options(options)
                .total_count(cartItemDetailRequest.getTotal_count())
                .build();
    }
    @Transactional
    public void updateCartItemDetail(Long cartItemId, CartItemUpdateDTO cartItemUpdateDTO) {
        if(!cartItemId.equals(cartItemUpdateDTO.getCartItemId())){
            throw new IllegalArgumentException("item is not same");
        }
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("item is not found"));
        Option option = optionRepository.findById(cartItemUpdateDTO.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("option is not found"));

        cartItem.setOption(option);
        cartItem.setCount(cartItemUpdateDTO.getCount());
        cartItemRepository.save(cartItem);
    }
    @Transactional
    public void deleteCartItemDetail( Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("item is not found"));
        cartItemRepository.delete(cartItem);
    }
    @Transactional
    public void emptyCart(CartDetailDTOResponse cartDetailDTOResponse){
        List<CartDetailDTO> cartDetailDTOList = cartDetailDTOResponse.getCartDetailDTOList();
        for(CartDetailDTO cartDetailDTO : cartDetailDTOList){
            deleteCartItemDetail(cartDetailDTO.getCartItemId());
        }
    }


}

