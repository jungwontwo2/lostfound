package baseball.lostfound.domain.entity;

import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
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
    @OneToOne(fetch = FetchType.LAZY,orphanRemoval = true,mappedBy = "content")
    private Image image;

    @OneToMany(mappedBy = "content",cascade = CascadeType.REMOVE,fetch = FetchType.LAZY)
    @OrderBy("id asc")
    private List<Comment> comments;

    private boolean isImportant;
}