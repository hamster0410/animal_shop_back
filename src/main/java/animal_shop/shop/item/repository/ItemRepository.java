package animal_shop.shop.item.repository;

import animal_shop.shop.item.ItemSellStatus;
import animal_shop.shop.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long>, JpaSpecificationExecutor<Item> {
//    Page<Item> findBySpecies(Pageable pageable, String species);
    Page<Item> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Page<Item> findByMemberId(Long memberId,Pageable pageable);

    // 종에 따른 아이템 찾기 - 스탑상품 제외
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " + // thumbnail_url 컬렉션을 fetch join으로 가져옴
            "WHERE i.species = :species " +
            "AND i.itemSellStatus != :itemSellStatus " +
            "ORDER BY i.createdDate DESC") // 날짜 역순으로 정렬
    Page<Item> findBySpecies(
            @Param("species") String species,
            @Param("itemSellStatus") ItemSellStatus itemSellStatus,
            Pageable pageable);

    // 종과 카테고리에 따른 아이템 찾기 - 스탑상품 제외
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +  // thumbnail_url 컬렉션을 fetch join으로 가져옴
            "WHERE i.species = :species " +
            "AND i.category = :category " +
            "AND i.itemSellStatus != :itemSellStatus ")
    Page<Item> findBySpeciesAndCategoryWithThumbnails(
            @Param("species") String species,
            @Param("category") String category,
            @Param("itemSellStatus") ItemSellStatus itemSellStatus,
            Pageable pageable);

    // 종과 카테고리와 디테일 카테고리에 따른 상품 찾기 - 스탑상품 제외
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +  // thumbnail_url 컬렉션을 fetch join으로 가져옴
            "WHERE i.species = :species " +
            "AND i.category = :category " +
            "AND i.detailed_category = :detailedCategory " +
            "AND i.itemSellStatus != :itemSellStatus ")
    Page<Item> findBySpeciesCategoryAndDetailedCategoryWithThumbnails(
            @Param("species") String species,
            @Param("category") String category,
            @Param("detailedCategory") String detailedCategory,
            @Param("itemSellStatus") ItemSellStatus itemSellStatus,
            Pageable pageable);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 검색 : 아이템이름과 비슷한 모든 상품 조회 - 스탑상품 비 제외
    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "WHERE i.name LIKE %:searchTerm%")
    Page<Item> findByItemNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);

    // 검색 : 멤버이름과 비슷한 모든 상품 조회 - 스탑상품 비 제외
    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "WHERE i.member.nickname LIKE %:searchTerm%")
    Page<Item> findByMemberNicknameContainingWithOptions(@Param("searchTerm") String searchTerm, Pageable pageable);

    // 검색 : 멤버이름과 비슷하고 아이템이름과 비슷한 모든 상품 조회 - 스탑상품 비 제외
    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "WHERE i.member.nickname LIKE %:searchTerm% " +
            "AND i.name LIKE %:searchTerm%")
    Page<Item> findByMemberNicknameAndItemNameContainingWithOptions(@Param("searchTerm") String searchTerm, Pageable pageable);
    //--------------------------------------------------------------------------------------------------------------------------------------------------------
    //검색 : 종에 따른 이름이 비슷한 아이템 검색 - 스탑상품 비 제외
//    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +
            "WHERE i.species = :species " +
            "AND LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Item> findBySpeciesWithThumbnailsItemNameContainingIgnoreCase(
            @Param("species") String species,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    //검색 : 종에 따른 아이템의 멤버이름이 비슷한 아이템 검색 - 스탑상품 비 제외
    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +
            "WHERE i.species = :species " +
            "AND LOWER(i.member.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Item> findBySpeciesWithThumbnailsMemberNicknameContainingIgnoreCase(
            @Param("species") String species,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    //검색 : 종에 따른 아이템의 멤버이름과 아이템 이름이 비슷한 아이템 검색 - 스탑상품 비 제외
    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +
            "WHERE i.species = :species " +
            "AND LOWER(i.member.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "AND LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Item> findBySpeciesWithThumbnailsMemberNicknameAndItemNameContainingIgnoreCase(
            @Param("species") String species,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
    //--------------------------------------------------------------------------------------------------------------------------------------------------------

    //검색 : 종과 카테고리에 따른 이름이 비슷한 아이템 검색 - 스탑상품 비 제외
//    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +
            "WHERE i.species = :species " +
            "AND i.category = :category " +
            "AND LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Item> findBySpeciesAndCategoryWithThumbnailsItemNameContainingIgnoreCase(
            @Param("species") String species,
            @Param("category") String category,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    //검색 : 종과 카테고리에 따른 아이템의 멤버이름이 비슷한 아이템 검색 - 스탑상품 비 제외
//    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +
            "WHERE i.species = :species " +
            "AND i.category = :category " +
            "AND LOWER(i.member.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Item> findBySpeciesAndCategoryWithThumbnailsMemberNicknameContainingIgnoreCase(
            @Param("species") String species,
            @Param("category") String category,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    //검색 : 종과 카테고리에 따른 아이템의 멤버이름과 아이템 이름이 비슷한 아이템 검색 - 스탑상품 비 제외
    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +
            "WHERE i.species = :species " +
            "AND i.category = :category " +
            "AND LOWER(i.member.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "AND LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Item> findBySpeciesAndCategoryWithThumbnailsMemberNicknameAndItemNameContainingIgnoreCase(
            @Param("species") String species,
            @Param("category") String category,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
    //--------------------------------------------------------------------------------------------------------------------------------------------------------
    //검색 : 종과 카테고리와 디테일 카테고리에 따른 이름이 비슷한 아이템 검색 - 스탑상품 비 제외
//    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +
            "WHERE i.species = :species " +
            "AND i.category = :category " +
            "AND i.detailed_category = :detailedCategory " +
            "AND LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Item> findBySpeciesAndCategoryAndDetailedCategoryWithThumbnailsItemNameContainingIgnoreCase(
            @Param("species") String species,
            @Param("category") String category,
            @Param("detailedCategory") String detailedCategory,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    //검색 : 종과 카테고리와 디테일 카테고리에 따른 아이템의 멤버이름이 비슷한 아이템 검색 - 스탑상품 비 제외
    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +
            "WHERE i.species = :species " +
            "AND i.category = :category " +
            "AND i.detailed_category = :detailedCategory " +
            "AND LOWER(i.member.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Item> findBySpeciesAndCategoryAndDetailedCategoryWithThumbnailsMemberNicknameContainingIgnoreCase(
            @Param("species") String species,
            @Param("category") String category,
            @Param("detailedCategory") String detailedCategory,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    //검색 : 종과 카테고리와 디테일에 따른 아이템의 멤버이름과 아이템 이름이 비슷한 아이템 검색 - 스탑상품 비 제외
    @EntityGraph(attributePaths = {"options"})
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +
            "WHERE i.species = :species " +
            "AND i.category = :category " +
            "AND i.detailed_category = :detailedCategory " +
            "AND LOWER(i.member.nickname) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
            "AND LOWER(i.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Item> findBySpeciesAndCategoryAndDetailedCategoryWithThumbnailsMemberNicknameAndItemNameContainingIgnoreCase(
            @Param("species") String species,
            @Param("category") String category,
            @Param("detailedCategory") String detailedCategory,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 스탑 상품 조회
    @Query("SELECT i FROM Item i " +
            "WHERE i.itemSellStatus = :itemSellStatus")
    Page<Item> findByItemSellStatus(
            @Param("itemSellStatus") ItemSellStatus itemSellStatus,
            Pageable pageable);

    // 전체 아이템 조회
    @Query("SELECT i FROM Item i " +
            "WHERE i.itemSellStatus != :itemSellStatus")
    Page<Item> findAllSearch(
            @Param("itemSellStatus") ItemSellStatus itemSellStatus,
            Pageable pageable);

    List<Item> findByMemberId(Long userId);

}
