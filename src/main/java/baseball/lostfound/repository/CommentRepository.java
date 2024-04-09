package baseball.lostfound.repository;

import baseball.lostfound.domain.entity.Comment;
import baseball.lostfound.domain.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.content = :content and c.parent is null")
    List<Comment> findByContent(@Param("content") Content content);
}
