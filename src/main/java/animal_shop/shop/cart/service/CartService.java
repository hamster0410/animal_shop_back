//package animal_shop.shop.cart.service;
//
//import animal_shop.community.member.entity.Member;
//import animal_shop.community.member.repository.MemberRepository;
//import animal_shop.global.security.TokenProvider;
//import animal_shop.shop.cart.dto.CartDetailDTOResponse;
//import animal_shop.shop.cart.dto.CartItemDTO;
//import animal_shop.shop.cart.entity.Cart;
//import animal_shop.shop.cart.repository.CartRepository;
//import animal_shop.shop.cart_item.entity.CartItem;
//import animal_shop.shop.cart_item.repository.CartItemRepository;
//import animal_shop.shop.item.entity.Item;
//import animal_shop.shop.item.repository.ItemRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class CartService {
//
//    @Autowired
//    private ItemRepository itemRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    @Autowired
//    private CartItemRepository cartItemRepository;
//
//    @Autowired
//    private TokenProvider tokenProvider;
//
//    public Long addCart(CartItemDTO cartItemDTO, String token){
//        Item item = itemRepository.findById(cartItemDTO.getItemId())
//                .orElseThrow(() -> new IllegalArgumentException("item not found"));
//
//        Long userId = Long.valueOf(tokenProvider.extractIdByAccessToken(token));
//
//        Member member = memberRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("member not found"));
//
//        Cart cart = cartRepository.findByMemberId(userId);
//        if(cart == null){
//            cart = Cart.createCart(member);
//            cartRepository.save(cart);
//        }
//
//        //현재 상품이 장바구니에 들어가있는지 조회
//        CartItem savedCartItem =
//                cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
//
//        //장바구니에 있는 상품일 경우 기존 수량에 현재 장바구니에 담을 수량만큼 더해줍니다.
//        if(savedCartItem != null){
//            savedCartItem.addCount(cartItemDTO.getCount());
//            return savedCartItem.getId();
//        }else{
//            //장바구니 엔티티, 상품 엔티티, 장바구니에 담을 수량을 이용하여 CartItem엔티티를 생성합니다.
//            CartItem cartItem =
//                    CartItem.createCartItem(cart, item, cartItemDTO.getCount());
//            //장바구니에 들어갈 상품을 저장
//            cartItemRepository.save(cartItem);
//            return cartItem.getId();
//        }
//    }
//
//    public CartDetailDTOResponse getCartList(String token, int page) {
//    }
//}
