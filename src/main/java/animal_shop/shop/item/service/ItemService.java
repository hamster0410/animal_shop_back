            package animal_shop.shop.item.service;
            
            import animal_shop.community.member.dto.TokenDTO;
            import animal_shop.community.member.entity.Member;
            import animal_shop.community.member.repository.MemberRepository;
            import animal_shop.global.security.TokenProvider;
            import animal_shop.shop.item.dto.ItemDTOList;
            import animal_shop.shop.item.entity.Item;
            import animal_shop.shop.item.repository.ItemRepository;
            import animal_shop.shop.option.entity.Option;
            import jakarta.transaction.Transactional;
            import org.springframework.beans.factory.annotation.Autowired;
            import org.springframework.stereotype.Service;
            
            import java.util.ArrayList;
            import java.util.List;
            
            @Service
            public class ItemService {
            
                @Autowired
                private ItemRepository itemRepository;
            
                @Autowired
                private TokenProvider tokenProvider;
            
                @Autowired
                private MemberRepository memberRepository;
            
            
                public void save(String token, ItemDTOList itemDTOList) {
                    String userId = tokenProvider.extractIdByAccessToken(token);
            
                    Member member = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("member is not found"));
            
                    if(!member.getRole().toString().equals("SELLER")){
                        throw  new IllegalStateException("is not seller");
                    }
            
                    Item item = Item.builder()
                            .name(itemDTOList.getName())
                            .category(itemDTOList.getCategory())
                            .comment_count(0L)
                            .species(itemDTOList.getCategory())
                            .stock_number(itemDTOList.getStock_number())
                            .itemSellStatus(itemDTOList.getSell_status())
                            .member(member)
                            .image_url(itemDTOList.getImageUrl())
                            .thumbnail_url(itemDTOList.getThumbnailUrls())
                            .options(itemDTOList.getOption())
                            .build();
            
                    itemRepository.save(item);
                }
                @Transactional
                public void update(String token, ItemDTOList itemDTOList) {
                    String userId = tokenProvider.extractIdByAccessToken(token);
                    Member member = memberRepository.findById(Long.valueOf(userId))
                            .orElseThrow(() -> new IllegalArgumentException("member is not found"));
                    //사용자 권한 인증
                    if(!member.getRole().toString().equals("SELLER")){
                        throw  new IllegalStateException("is not seller");
                    }
                    //수정할 아이템 인증
                    Item item = itemRepository.findById(itemDTOList.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Item not found"));

                    // 아이템의 필드 수정
                    item.setName(itemDTOList.getName());
                    item.setCategory(itemDTOList.getCategory());
                    item.setSpecies(itemDTOList.getSpecies());
                    item.setStock_number(itemDTOList.getStock_number());
                    item.setItemSellStatus(itemDTOList.getSell_status());
                    item.setThumbnail_url(itemDTOList.getThumbnailUrls());
                    item.setImage_url(itemDTOList.getImageUrl());
                    item.setOptions(itemDTOList.getOption()); // 옵션 수정

                itemRepository.save(item);
                }
            }
