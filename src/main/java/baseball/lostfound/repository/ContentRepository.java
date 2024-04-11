package baseball.lostfound.repository;

import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content,Long> {
    //@Query(value = "SELECT c FROM Content c WHERE c.title LIKE %:searchword% ORDER BY c.isImportant DESC, c.id DESC")

    @Query("select c from Content c join fetch c.images")
    Page<Content> findAll(Pageable pageable);

    @Query(value = "SELECT c FROM Content c WHERE c.title LIKE %:searchword% ORDER BY c.isImportant DESC, c.id DESC")
    Page<Content> findByTitleContainingOrderByIsImportantDescAndContentIdDesc(Pageable pageable, @Param("searchword") String searchwordWithWildcards);

    @Query("select c from Content c join fetch c.images where c.team = :team and c.position = :position ")
    Page<Content> findAllWithNickname(Pageable pageable, @Param("team") Team team, @Param("position") Position position);

    @Query("select c from Content c join fetch c.images where c.team = :team ")
    Page<Content> findAllWithNicknameByTeam(Pageable pageable, @Param("team") Team team);

    @Query("select c from Content c join fetch c.images where c.position = :position ")
    Page<Content> findAllWithNicknameByPosition(Pageable pageable, @Param("position") Position position);

    @Query("select c from Content c join fetch c.images where c.id=:id")
    Content findByIdWithImage(@Param("id")Long id);
}
