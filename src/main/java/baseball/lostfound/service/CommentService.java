package baseball.lostfound.service;

import baseball.lostfound.domain.dto.comment.CommentRequestDto;
import baseball.lostfound.domain.dto.comment.CommentResponseDto;
import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.Comment;
import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.repository.CommentRepository;
import baseball.lostfound.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final ContentRepository contentRepository;

    //댓글작성
    public Long writeComment(CommentRequestDto commentRequestDto, Long contentId, String parentId, Authentication authentication,boolean isPrivate){
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUserEntity();
        //사용자와 게시물을 찾음
        Content content = contentRepository.findById(contentId).orElse(null);
        //부모 댓글을 찾는 과정
        //일단 부모가 없다고 설정함
        Comment parentComment = null;
        //만약 parentId가 null이 아니면 부모 댓글이 있다는 뜻
//        if(!Objects.equals(parentId, "")){
        if(parentId!=null){
            long parentIdLong = Long.parseLong(parentId);
            //commentRepository에서 parentIdLong을 통해서 부모 댓글을 찾는다.
            parentComment = commentRepository.findById(parentIdLong).orElse(null);
        }
        content.addCommentCnt();
        //Comment.builder를 통해서 comment를 생성함
        //comment에는 commentRequestDto.getComment()를 통한 댓글내용
        //content에는 어떤 게시물에 관한건지에 대한 정보(content_id가 조인컬럼 되어있음)
        //user에는 어떤 유저가 작성했는지에 대한 정보(user_id가 조인컬럼 되어있음)
        Comment comment = Comment.builder()
                .comment(commentRequestDto.getComment())
                .content(content)
                .user(user)
                .parent(parentComment)//부모 댓글 설정
                .isPrivate(isPrivate)
                .build();
        if(parentComment!=null){
            parentComment.addChildrenComment(comment);
            commentRepository.save(parentComment);
        }
        //comment를 만들었다면 save함
        commentRepository.save(comment);
        //contentRepository.save(content);
        return comment.getId();
    }
    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        commentRepository.delete(comment);
    }
    @Transactional
    public Long updateComment(CommentRequestDto commentRequestDto,Long commentId){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        comment.update(commentRequestDto.getComment());
        commentRepository.save(comment);
        return comment.getId();
    }

    public List<CommentResponseDto> commentDtoList(Long id) {
        Content content = contentRepository.findById(id).orElse(null);
        List<Comment> commentList = commentRepository.findByContent(content);
        List<CommentResponseDto> collect = commentList.stream().map(comment -> new CommentResponseDto(comment)).toList();
        return collect;
    }
}
