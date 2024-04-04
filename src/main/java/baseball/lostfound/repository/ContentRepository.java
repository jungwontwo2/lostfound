package baseball.lostfound.repository;

import baseball.lostfound.domain.entity.Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content,Long> {
    //@Query(value = "SELECT c FROM Content c WHERE c.title LIKE %:searchword% ORDER BY c.isImportant DESC, c.id DESC")

    @Query(value = "SELECT c FROM Content c join fetch c.user WHERE c.title LIKE %:searchword% ORDER BY c.isImportant DESC, c.id DESC")
    Page<Content> findByTitleContainingOrderByIsImportantDescAndContentIdDesc(Pageable pageable, @Param("searchword") String searchwordWithWildcards);

    List<Content> findAll();

    @Query("select c from Content c join fetch c.images where c.id=:id")
    Content findByIdWithImage(@Param("id")Long id);
}
