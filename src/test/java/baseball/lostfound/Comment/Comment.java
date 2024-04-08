package baseball.lostfound.Comment;

import baseball.lostfound.domain.dto.comment.CommentRequestDto;
import baseball.lostfound.domain.dto.content.ContentWriteDto;
import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.dto.user.JoinUserDto;
import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import baseball.lostfound.repository.CommentRepository;
import baseball.lostfound.repository.ContentRepository;
import baseball.lostfound.repository.UserRepository;
import baseball.lostfound.service.CommentService;
import baseball.lostfound.service.ContentService;
import baseball.lostfound.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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

    @Autowired
    UserService userService;

    @Test
    public void addCommentNotPrivate() throws IOException {
        JoinUserDto joinUserDto = new JoinUserDto("qqqq", "qwerqwer", "qwerqwer", "qqqq");
        userService.saveUser(joinUserDto,"ROLE_USER");
        User user = userRepository.findByLoginId("qqqq").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        ContentWriteDto contentWriteDto = new ContentWriteDto("aaa", "aaaa", Team.한화, Position.FIRST_BASE, null);
        Long contentId = contentService.writeContent(contentWriteDto, customUserDetails);
        Long commentId = commentService.writeComment(new CommentRequestDto("한화 오늘 이기자!!",false), contentId, null, authentication,false);
        baseball.lostfound.domain.entity.Comment comment = commentRepository.findById(commentId).orElse(null);
        Assertions.assertThat(comment.getComment()).isEqualTo("한화 오늘 이기자!!");
    }
    @Test
    public void addCommentPrivate() throws IOException {
        JoinUserDto joinUserDto = new JoinUserDto("qqqq", "qwerqwer", "qwerqwer", "qqqq");
        userService.saveUser(joinUserDto,"ROLE_USER");
        User user = userRepository.findByLoginId("qqqq").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        ContentWriteDto contentWriteDto = new ContentWriteDto("aaa", "aaaa", Team.한화, Position.FIRST_BASE, null,false);
        Long contentId = contentService.writeContent(contentWriteDto, customUserDetails);
        Long commentId = commentService.writeComment(new CommentRequestDto("한화 오늘 이기자!!",false), contentId, null, authentication,true);
        baseball.lostfound.domain.entity.Comment comment = commentRepository.findById(commentId).orElse(null);
        Assertions.assertThat(comment.isPrivate()).isEqualTo(true);
    }

    @Test
    public void deleteComment() throws IOException {
        JoinUserDto joinUserDto = new JoinUserDto("qqqq", "qwerqwer", "qwerqwer", "qqqq");
        userService.saveUser(joinUserDto,"ROLE_USER");
        User user = userRepository.findByLoginId("qqqq").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        ContentWriteDto contentWriteDto = new ContentWriteDto("aaa", "aaaa", Team.한화, Position.FIRST_BASE, null,false);
        Long contentId = contentService.writeContent(contentWriteDto, customUserDetails);
        Long commentId = commentService.writeComment(new CommentRequestDto("한화 오늘 이기자!!",false), contentId, null, authentication,true);
        commentService.deleteComment(commentId);
        Assertions.assertThat(commentRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    public void updateComment() throws IOException {
        JoinUserDto joinUserDto = new JoinUserDto("qqqq", "qwerqwer", "qwerqwer", "qqqq");
        userService.saveUser(joinUserDto,"ROLE_USER");
        User user = userRepository.findByLoginId("qqqq").orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        ContentWriteDto contentWriteDto = new ContentWriteDto("aaa", "aaaa", Team.한화, Position.FIRST_BASE, null,false);
        Long contentId = contentService.writeContent(contentWriteDto, customUserDetails);
        Long commentId = commentService.writeComment(new CommentRequestDto("한화 오늘 이기자!!",false), contentId, null, authentication,true);
        em.flush();
        em.clear();
        commentService.updateComment(new CommentRequestDto("4월 9일 한화 이기자!!",false),commentId);
        Assertions.assertThat(commentRepository.findById(commentId).orElse(null).getComment()).isEqualTo("4월 9일 한화 이기자!!");
    }
}
