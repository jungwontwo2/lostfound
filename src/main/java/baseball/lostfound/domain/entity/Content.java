package baseball.lostfound.domain.entity;

import baseball.lostfound.domain.dto.content.ContentEditDto;
import baseball.lostfound.domain.dto.content.ContentResponseDto;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Team team;
    @Enumerated(EnumType.STRING)
    private Position position;
    private String title;
    @Column(length = 500)
    private String texts;
    @OneToMany(fetch = FetchType.LAZY,orphanRemoval = true,mappedBy = "content")
    private List<Image> images;

    @OneToMany(mappedBy = "content",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<Comment> comments;

    @Column(columnDefinition = "integer default 0")
    private Integer commentCnt;

    private boolean isImportant;
    public void addCommentCnt(){this.commentCnt=this.commentCnt+1;}

    public static ContentEditDto toEditDto(ContentResponseDto content){
        ContentEditDto contentEditDto = ContentEditDto.builder()
                .title(content.getTitle())
                .texts(content.getTexts())
                .build();
        return contentEditDto;
    }
}
