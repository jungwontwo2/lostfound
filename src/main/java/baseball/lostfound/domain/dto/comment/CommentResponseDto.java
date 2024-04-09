package baseball.lostfound.domain.dto.comment;

import baseball.lostfound.domain.entity.Comment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentResponseDto {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
    private String comment;
    private String nickname;
    private List<CommentResponseDto> children;
    private boolean hasChildren;
    private boolean hasParent;
    private boolean isPrivate;
    public CommentResponseDto(Comment comment) {
        System.out.println(comment.getCreatedAt());
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.nickname = comment.getUser().getNickname();
        this.children=new ArrayList<>();
        this.hasChildren=!comment.getChildren().isEmpty();
        this.hasParent= comment.getParent() != null;//comment.getParent가 null이 아니면 parent가 있다는 뜻(true반환)
        this.isPrivate=comment.isPrivate();
    }
}
