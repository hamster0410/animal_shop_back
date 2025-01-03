    package animal_shop.shop.item.service;

    import animal_shop.community.member.entity.Member;
    import animal_shop.community.member.repository.MemberRepository;
    import animal_shop.global.admin.entity.StopItem;
    import animal_shop.global.admin.repository.StopItemRepository;
    import animal_shop.global.security.TokenProvider;
    import animal_shop.shop.item.ItemSellStatus;
    import animal_shop.shop.item.dto.*;
    import animal_shop.shop.item.entity.Item;
    import animal_shop.shop.item.entity.ItemQuery;
    import animal_shop.shop.item.entity.Option;
    import animal_shop.shop.item.repository.ItemQueryRepository;
    import animal_shop.shop.item.repository.ItemRepository;
    import animal_shop.shop.item.repository.OptionRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.data.jpa.domain.Specification;
    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class ItemService {
        @Autowired
        private ItemRepository itemRepository;

        @Autowired
        private TokenProvider tokenProvider;

        @Autowired
        private MemberRepository memberRepository;

        @Autowired
        private OptionRepository optionRepository;

        @Autowired
        private ItemQueryRepository itemQueryRepository;

        @Autowired
        private JavaMailSender javaMailSender;

        @Autowired
        private StopItemRepository stopItemRepository;

        @Transactional
        public void save(String token, ItemDTOList itemDTOList) {
            String userId = tokenProvider.extractIdByAccessToken(token);

            Member member = memberRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new IllegalArgumentException("member is not found"));
            // seller가 아닌 경우 예외 처리
            if (member.getRole().toString().equals("USER")) {
                throw new IllegalStateException("User is not a seller");
            }

            // Option 리스트 가져오기
            List<Option> options = itemDTOList.getOption();
            // Item 객체 생성
            Item item = Item.builder()
                    .name(itemDTOList.getName())
                    .category(itemDTOList.getCategory())
                    .detailed_category(itemDTOList.getDetailed_category())
                    .itemDetail(itemDTOList.getItem_detail())
                    .comment_count(0L)
                    .species(itemDTOList.getSpecies())
                    .stock_number(itemDTOList.getStock_number())
                    .itemSellStatus(itemDTOList.getSell_status())
                    .member(member)
                    .image_url(itemDTOList.getImageUrl())
                    .thumbnail_url(itemDTOList.getThumbnailUrls())
                    .build();
            // Item 저장
            Item savedItem = itemRepository.save(item);  // 여기서 item이 저장되고 id가 생성됨
            // 옵션을 저장할 때, 각각의 Option에 item_id 설정
            for (Option option : options) {
                option.setItem(savedItem); // 저장된 Item의 id를 Option에 설정
            }

            // Option 저장
            optionRepository.saveAll(options); // 여러 개의 Option 저장
        }


        @Transactional
        public void update(String token, ItemDTOList itemDTOList) {
            // 1. 사용자 인증 (SELLER 권한 확인)
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("member is not found"));

            if (member.getRole().toString().equals("USER")) {
                throw new IllegalStateException("is not seller");
            }

            // 2. 수정할 아이템 인증 (아이템 ID로 조회)
            Item item = itemRepository.findById(itemDTOList.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Item not found"));

            // 3. 아이템 속성 수정
            if (itemDTOList.getName() != null) {
                item.setName(itemDTOList.getName());
            }
            if (itemDTOList.getItem_detail() != null) {
                item.setItemDetail(itemDTOList.getItem_detail());
            }
            if (itemDTOList.getCategory() != null) {
                item.setCategory(itemDTOList.getCategory());
            }
            if (itemDTOList.getStock_number() != null) {
                item.setStock_number(itemDTOList.getStock_number());
            }
            if (itemDTOList.getSpecies() != null) {
                item.setSpecies(itemDTOList.getSpecies());
            }
            if (itemDTOList.getThumbnailUrls() != null) {
                item.setThumbnail_url(itemDTOList.getThumbnailUrls());
            }
            if (itemDTOList.getImageUrl() != null) {
                item.setImage_url(itemDTOList.getImageUrl());
            }
            if (itemDTOList.getDetailed_category() != null) {
                item.setDetailed_category(itemDTOList.getDetailed_category());
            }

            item.getOptions().clear(); // 기존 옵션 제거

            boolean nowdiscount = false;
            for (Option newOption : itemDTOList.getOption()) {
                if(newOption.getDiscount_rate() > 0) nowdiscount = true;
                newOption.setItem(item); // 새 옵션에 아이템 연결
                item.getOptions().add(newOption); // 아이템의 옵션 리스트에 추가
            }
            // 4. 수정된 아이템 저장
            if(nowdiscount){
                item.setItemSellStatus(ItemSellStatus.valueOf("DISCOUNT"));
            }else{
                item.setItemSellStatus(itemDTOList.getSell_status());
            }

            itemRepository.save(item);
        }

        @Transactional
        public void delete(String token, String itemId) {
            //1. 사용자 인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("member is not found"));

            if (!member.getRole().toString().equals("SELLER")) {
                throw new IllegalStateException("is not seller");
            }
            //삭제할 아이템 찾기
            Item item = itemRepository.findById(Long.valueOf(itemId))
                    .orElseThrow(() -> new IllegalArgumentException("Item not found"));

            //아이템 삭제
            item.setItemSellStatus(ItemSellStatus.valueOf("SOLD_OUT"));

        }

        //개별 조회
        @Transactional(readOnly = true)
        public ItemDetailDTO selectItem(String token, String itemId) {
            //1. 사용자인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("member is not found"));
            // 2. 판매자 자격조건
            if (!member.getRole().toString().equals("SELLER")) {
                throw new IllegalStateException("is not seller");
            }
            //3. 상품 조회하기
            Item item = itemRepository.findById(Long.valueOf(itemId))
                    .orElseThrow(() -> new IllegalArgumentException("Item not found"));
            //4. 엔티티 -> DTO로 변환
            return new ItemDetailDTO(item);
        }

        //전체 조회
        @Transactional(readOnly = true)
        public ItemDTOListResponse selectAll(String token, int page) {
            // 1. 사용자 인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("member is not found"));

            // 2. 판매자 자격조건
            if (!member.getRole().toString().equals("SELLER")) {
                throw new IllegalStateException("is not seller");
            }

            Pageable pageable = (Pageable) PageRequest.of(page, 20);


            // 3. 전체 상품 조회하기
            Page<Item> items = itemRepository.findByMemberId(Long.valueOf(userId), pageable);

            ItemDTOListResponse itemDTOListResponse = ItemDTOListResponse.builder()
                    .itemDTOLists(items.stream()
                            .map(ItemDetailDTO::new)
                            .collect(Collectors.toList()))
                    .total_count(items.getTotalElements())
                    .build();
            return itemDTOListResponse;
            // 4. 엔티티 -> DTO로 변환
        }

        public ItemDTOListResponse stopItemLIst(String token, int page) {
            // 1. 사용자 인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("member is not found"));

            // 2. 판매자 자격조건
            if (!member.getRole().toString().equals("SELLER")) {
                throw new IllegalStateException("is not seller");
            }

            Pageable pageable = (Pageable) PageRequest.of(page, 20);

            Specification<Item> specification = Specification.where(null);
            // Pageable 설정 (페이지 당 10개로 제한)
            specification = specification.and(ItemSpecification.searchByItemStatusStop());

            specification = specification.and(ItemSpecification.searchByUserId(member));

            Page<Item> items = itemRepository.findAll(specification, pageable);
            // 검색 결과 반환
            return ItemDTOListResponse.builder()
                    .itemDTOLists(items.stream()
                            .map(item -> {
                                StopItem stopItem = stopItemRepository.findById(item.getId())
                                        .orElseThrow(() -> new IllegalArgumentException("stop item is not found"));
                                return new ItemDetailDTO(item, stopItem.getSuspensionReason());
                            })
                            .collect(Collectors.toList()))
                    .total_count(items.getTotalElements())
                    .build();
            // 4. 엔티티 -> DTO로 변환
        }
        public ItemDetailDTO findById(String itemId) {
            Item item = itemRepository.findById(Long.valueOf(itemId))
                    .orElseThrow(() -> new IllegalArgumentException("item not found"));

            return new ItemDetailDTO(item);
        }

        @Transactional
        public RequestItemQueryDTO register_enquery(String token,
                                     RequestItemQueryDTO requestItemQueryDTO) {
            // 1. 사용자 인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("member is not found"));

            // 2. 문의사항 등록
            ItemQuery itemQuery = new ItemQuery();

            // RequestItemQueryDTO의 데이터를 ItemQuery 엔티티로 변환

            Item item = itemRepository.findById(Long.valueOf(requestItemQueryDTO.getItem_id()))
                    .orElseThrow(() -> new IllegalArgumentException("member is not found"));

            Member seller = item.getMember();

            itemQuery.setSeller(seller);
            itemQuery.setCustomer(member);
            itemQuery.setItem(item);
            itemQuery.setContents(requestItemQueryDTO.getContents());
            itemQuery.setOption_name(requestItemQueryDTO.getOption_name());
            itemQuery.setOption_price(requestItemQueryDTO.getOption_price());
            itemQuery.setReply(requestItemQueryDTO.getReply());

            // 3. 데이터베이스 등록
            itemQueryRepository.save(itemQuery);

            return requestItemQueryDTO;
        }
        @Transactional
        public void delete_query(String token,
                                 String questionId) {
            // 1. 사용자 인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("member is not found"));

            //2. 문의 내용 확인
            ItemQuery itemQuery = itemQueryRepository.findById(Long.valueOf(questionId))
                    .orElseThrow(()->new IllegalArgumentException("dont find iq"));

            //3.문의 내용 삭제
            itemQueryRepository.delete(itemQuery);
        }

        @Transactional
        public QueryResponse find_orders(String token, int page) {
            //1.사용자 인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() ->new RuntimeException("member is not found"));

            // 2. 판매자 자격조건
            if (!member.getRole().toString().equals("SELLER")) {
                throw new IllegalStateException("is not seller");
            }
            //3. 페이징 정보 생성
            Pageable pageable = PageRequest.of(page,10, Sort.by("createdDate").descending());
            Page<ItemQuery> itemQueries = itemQueryRepository.findOrdersBySellerId(member.getId(), pageable);

            //4.주문 목록 조회
            List<ResponseItemQueryDTO> requestItemQueryDTOList = itemQueries
                    .getContent()
                    .stream()
                    .map(ResponseItemQueryDTO::new).toList();

            return QueryResponse.builder()
                    .responseItemQueryDTOList(requestItemQueryDTOList)
                    .total_count(itemQueries.getTotalElements())
                    .build();
        }

        @Transactional
        public QueryResponse select_query(String token, String itemId, int page) {

            // 2. 페이징 요청 생성
            Pageable pageable = PageRequest.of(page, 10, Sort.by("createdDate").descending());

            // 3. 문의사항 조회
            Long itemIdLong = Long.valueOf(itemId);
            Page<ItemQuery> itemQueries = itemQueryRepository.findByItemId(itemIdLong, pageable);

            // 4. DTO 변환
            List<ResponseItemQueryDTO> responseItemQueryDTOList = itemQueries
                    .getContent()
                    .stream()
                    .map(ResponseItemQueryDTO::new)
                    .toList();
            // 5. QueryResponse 생성 및 반환
            return QueryResponse.builder()
                    .responseItemQueryDTOList(responseItemQueryDTOList)
                    .total_count(itemQueries.getTotalElements())
                    .build();
        }
        @Transactional
        public void query_comment(String token, Long queryId, SellerReplyDTO sellerReplyDTO) {
            //1.사용자 인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(()->new IllegalArgumentException("Member is not found"));
            //2.판매자 인증
            if (!member.getRole().toString().equals("SELLER")) {
                throw new IllegalStateException("is not seller");
            }

            //3. 문의사항 조회
            ItemQuery itemQuery = itemQueryRepository.findById(queryId).orElseThrow(()->new IllegalArgumentException("Don't find Query"));
            //3.문의사항DTO -> Entitiy로 변환

            //4. 답변 작성

            itemQuery.setReply(sellerReplyDTO.getReply());

            //5. 등록
            itemQueryRepository.save(itemQuery);
        }

        @Transactional
        public void delete_reply(String token, Long queryId) {
            // 1. 사용자 인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("Member is not found"));

            // 2. 판매자 인증
            if (!member.getRole().toString().equals("SELLER")) {
                throw new IllegalStateException("User is not a seller");
            }

            // 3. 문의사항 조회
            ItemQuery itemQuery = itemQueryRepository.findById(queryId)
                    .orElseThrow(() -> new IllegalArgumentException("Query not found"));

            // 4. 답변이 이미 없는 경우 예외 처리
            if (itemQuery.getReply() == null) {
                throw new IllegalStateException("Reply is already deleted");
            }

            // 5. 답변 삭제 (reply 필드 null 처리)
            itemQuery.setReply(null);

            // 6. 수정된 엔티티 저장
            itemQueryRepository.save(itemQuery);
        }

            @Transactional
            public void discount_rate(String token, ItemDiscountDTO itemDiscountDTO) {
                //1.사용자 인증
                String userId = tokenProvider.extractIdByAccessToken(token);
                Member member = memberRepository.findById(Long.valueOf(userId))
                        .orElseThrow(()->new IllegalArgumentException("Member is not found"));

                //2.판매자 인증
                if(!member.getRole().toString().equals("SELLER")){
                    throw new IllegalStateException("User is not a SELLER");
                }
                Option option = optionRepository.findById(itemDiscountDTO.getOption_id())
                        .orElseThrow(() -> new IllegalArgumentException("option is not found"));

                option.setDiscount_rate(itemDiscountDTO.getOption_discount_rate());
                optionRepository.save(option);

            }

        public void no_discount(String token, ItemDiscountDTO itemDiscountDTO) {
            //1. 사용자 인증
            String userId = tokenProvider.extractIdByAccessToken(token);
            Member member = memberRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new IllegalArgumentException("Member is not found"));

            //2.판매자 인증
            if(!member.getRole().toString().equals("SELLER")){
                throw new IllegalStateException("User is not a SELLER");
            }

            Option option = optionRepository.findById(itemDiscountDTO.getOption_id())
                    .orElseThrow(() -> new IllegalArgumentException("option is not found"));

            // 4. 할인율을 null로 설정
            option.setDiscount_rate(0L);
            optionRepository.save(option);
        }

//        public ItemDTOListResponse search_item(String token,String searchBy, String searchTerm, String species, String category, String detailed_category, int page) {
//            // 사용자 인증
//            String userId = tokenProvider.extractIdByAccessToken(token);
//
//            // Pageable 설정 (페이지 당 10개로 제한)
//            Pageable pageable = PageRequest.of(page, 10, Sort.by("createdDate").descending());
//
//            // 아이템 검색
//            Page<Item> items;
//            if (searchTerm != null && searchBy != null && !searchTerm.isEmpty() && !searchBy.isEmpty()) {
//                if (searchBy.equals("item")) {
//                    if(species == null){   //전체 조회
//                        items = itemRepository.findByItemNameContainingIgnoreCase(searchTerm,pageable);
//                    }else if(category == null){ //종으로 조회
//                        items = itemRepository.findBySpeciesWithThumbnailsItemNameContainingIgnoreCase(species,searchTerm,pageable);
//                    }else if(detailed_category == null){ //카테고리로 조회
//                        items = itemRepository.findBySpeciesAndCategoryWithThumbnailsItemNameContainingIgnoreCase(species,category,searchTerm,pageable);
//                    }else{ //세부 카테고리로 조회
//                        items = itemRepository.findBySpeciesAndCategoryAndDetailedCategoryWithThumbnailsItemNameContainingIgnoreCase(species,category,detailed_category,searchTerm,pageable);
//                    }
//                }else if(searchBy.equals("seller")){
//                    if(species == null){   //전체 조회
//                        items = itemRepository.findByMemberNicknameContainingWithOptions(searchTerm,pageable);
//                    }else if(category == null){ //종으로 조회
//                        items = itemRepository.findBySpeciesWithThumbnailsMemberNicknameContainingIgnoreCase(species,searchTerm,pageable);
//                    }else if(detailed_category == null){ //카테고리로 조회
//                        items = itemRepository.findBySpeciesAndCategoryWithThumbnailsMemberNicknameContainingIgnoreCase(species,category,searchTerm,pageable);
//                    }else{ //세부 카테고리로 조회
//                        items = itemRepository.findBySpeciesAndCategoryAndDetailedCategoryWithThumbnailsMemberNicknameContainingIgnoreCase(species,category,detailed_category,searchTerm,pageable);
//                    }
//                }else{ //둘 다 종합 검색
//                    if(species == null){   //전체 조회
//                        items = itemRepository.findByMemberNicknameAndItemNameContainingWithOptions(searchTerm,pageable);
//                    }else if(category == null){ //종으로 조회
//                        items = itemRepository.findBySpeciesWithThumbnailsMemberNicknameAndItemNameContainingIgnoreCase(species,searchTerm,pageable);
//                    }else if(detailed_category == null){ //카테고리로 조회
//                        items = itemRepository.findBySpeciesAndCategoryWithThumbnailsMemberNicknameAndItemNameContainingIgnoreCase(species,category,searchTerm,pageable);
//                    }else{ //세부 카테고리로 조회
//                        items = itemRepository.findBySpeciesAndCategoryAndDetailedCategoryWithThumbnailsMemberNicknameAndItemNameContainingIgnoreCase(species,category,detailed_category,searchTerm,pageable);
//                    }
//                }
//            } else {
//                items = itemRepository.findAllByOrderByCreatedDateDesc(pageable); // 검색어가 없으면 전체 조회
//            }
//
//            // DTO 변환 (Item -> ItemDetailDTO)
//            List<ItemDetailDTO> itemDTOs = items.map(ItemDetailDTO::new).getContent();
//
//            // 검색 결과 반환
//            return ItemDTOListResponse.builder()
//                    .itemDTOLists(itemDTOs)
//                    .total_count(items.getTotalElements())
//                    .build();
//        }

        //사랑해요 specification
        public ItemDTOListResponse searchItems(String searchBy, String searchTerm,  String species, String category, String detailedCategory, String status, Integer page, int pageCount) {
            Specification<Item> specification = Specification.where(null);
            // Pageable 설정 (페이지 당 10개로 제한)


            if (searchTerm != null && !searchTerm.isEmpty()) {
                if (searchBy.equals("item")) {
                    specification = specification.and(ItemSpecification.searchByItemName(searchTerm));
                } else if (searchBy.equals("seller")) {
                    specification = specification.and(ItemSpecification.searchByMemberNickname(searchTerm));
                }else if (searchBy.equals("with")) {
                    specification = specification.and(ItemSpecification.searchByItemNameOrMemberNickname(searchTerm));
                }
            }

            if (species != null) {
                specification = specification.and(ItemSpecification.searchBySpecies(species));
            }

            if (category != null) {
                specification = specification.and(ItemSpecification.searchByCategory(category));
            }

            if (detailedCategory != null) {
                specification = specification.and(ItemSpecification.searchByDetailedCategory(detailedCategory));
            }

            if (status != null){
                specification = specification.and(ItemSpecification.searchByStatus(status));
            }

            List<ItemDetailDTO> itemDTOs;
            long total_count;
            if(page != null){
                Pageable pageable = PageRequest.of(page-1, pageCount, Sort.by("createdDate").descending());
                Page<Item> items = itemRepository.findAll(specification, pageable);
                total_count = items.getTotalElements();
                itemDTOs = items.map(ItemDetailDTO::new).getContent();
            }else{
                List<Item> items = itemRepository.findAll(specification);
                itemDTOs = items.stream().map(ItemDetailDTO::new).toList();
                total_count = items.size();
            }
            // DTO 변환 (Item -> ItemDetailDTO)


            // 검색 결과 반환
            return ItemDTOListResponse.builder()
                    .itemDTOLists(itemDTOs)
                    .total_count(total_count)
                    .build();
        }


        public ItemDTOListResponse searchItemsBySeller(String token, String searchTerm, String species, String category, String detailedCategory, String status, Boolean discount, Integer page, Integer pageCount) {
            String userId = tokenProvider.extractIdByAccessToken(token);

            Specification<Item> specification = Specification.where(null);
            // Pageable 설정 (페이지 당 10개로 제한)

            if (searchTerm != null && !searchTerm.isEmpty()) {
                specification = specification.and(ItemSpecification.searchByItemName(searchTerm));
            }

            if (userId != null) {
                specification = specification.and(ItemSpecification.searchByMemberId(userId));
            }

            if (species != null) {
                specification = specification.and(ItemSpecification.searchBySpecies(species));
            }

            if (category != null) {
                specification = specification.and(ItemSpecification.searchByCategory(category));
            }

            if (detailedCategory != null) {
                specification = specification.and(ItemSpecification.searchByDetailedCategory(detailedCategory));
            }

            if (status != null){
                specification = specification.and(ItemSpecification.searchByStatus(status));
            }

            if (discount != null){
                specification = specification.and(ItemSpecification.searchByDiscount(discount));
            }

            List<ItemDetailDTO> itemDTOs;
            long total_count;
            if(page != null){
                Pageable pageable = PageRequest.of(page-1, pageCount, Sort.by("createdDate").descending());
                Page<Item> items = itemRepository.findAll(specification, pageable);
                total_count = items.getTotalElements();
                itemDTOs = items.map(ItemDetailDTO::new).getContent();
            }else{
                List<Item> items = itemRepository.findAll(specification);
                itemDTOs = items.stream().map(ItemDetailDTO::new).toList();
                total_count = items.size();
            }
            // DTO 변환 (Item -> ItemDetailDTO)


            // 검색 결과 반환
            return ItemDTOListResponse.builder()
                    .itemDTOLists(itemDTOs)
                    .total_count(total_count)
                    .build();
        }
    }


