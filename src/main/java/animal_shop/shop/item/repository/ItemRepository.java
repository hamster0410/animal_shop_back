package animal_shop.shop.item.repository;

import animal_shop.shop.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
//    Page<Item> findBySpecies(Pageable pageable, String species);
    Page<Item> findAllByOrderByCreatedDateDesc(Pageable pageable);

    // fetch join을 사용한 쿼리
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +  // thumbnail_url 컬렉션을 fetch join으로 가져옴
            "WHERE i.species = :species")
    Page<Item> findBySpecies( @Param("species") String species, Pageable pageable);

    // fetch join을 사용한 쿼리
    @Query("SELECT i FROM Item i " +
            "LEFT JOIN FETCH i.thumbnail_url " +  // thumbnail_url 컬렉션을 fetch join으로 가져옴
            "WHERE i.species = :species AND i.category = :category")
    Page<Item> findBySpeciesAndCategoryWithThumbnails(
            @Param("species") String species,
            @Param("category") String category,
            Pageable pageable);
}
