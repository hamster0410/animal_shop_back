package animal_shop.community.post.repository;

import animal_shop.community.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByOrderByHitsDesc(Pageable pageable);

    Page<Post> findByCategory(Pageable pageable, String category);

    Page<Post> findAllByOrderByCreatedDateDesc(Pageable pageable);

    @EntityGraph(attributePaths = "member")
    Page<Post> findByTitleContaining(Pageable pageable, String keyword);

    Page<Post> findByContentsContaining(Pageable pageable, String keyword);

    @Query("SELECT p FROM Post p JOIN p.member m WHERE m.nickname = :nickname")
    Page<Post> findByMemberNickname(Pageable pageable, @Param("nickname") String nickname);

}
