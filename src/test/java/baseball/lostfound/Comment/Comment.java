package baseball.lostfound.Comment;

import baseball.lostfound.domain.dto.comment.CommentRequestDto;
import baseball.lostfound.domain.dto.content.ContentWriteDto;
import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import baseball.lostfound.repository.CommentRepository;
import baseball.lostfound.repository.ContentRepository;
import baseball.lostfound.repository.UserRepository;
import baseball.lostfound.service.CommentService;
import baseball.lostfound.service.ContentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class Comment {

    @Autowired
    CommentService commentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContentService contentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ContentRepository contentRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void addCommentNotPrivate() throws IOException {
        User user = userRepository.findByLoginId("qwer").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        ContentWriteDto contentWriteDto = new ContentWriteDto("aaa", "aaaa", Team.한화, Position.FIRST_BASE, null,false);
        Long contentId = contentService.writeContent(contentWriteDto, customUserDetails);
        Content content = contentRepository.findById(contentId).orElse(null);
        System.out.println(content.getCommentCnt());
        em.clear();
        Long commentId = commentService.writeComment(new CommentRequestDto("한화 오늘 이기자!!"), contentId, null, authentication);
        baseball.lostfound.domain.entity.Comment comment = commentRepository.findById(commentId).orElse(null);
        Assertions.assertThat(comment.getComment()).isEqualTo("한화 오늘 이기자!!");

    }
}
