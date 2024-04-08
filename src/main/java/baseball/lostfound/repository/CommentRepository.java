package baseball.lostfound.repository;

import baseball.lostfound.domain.entity.Comment;
import baseball.lostfound.domain.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByContent(Content content);
}
