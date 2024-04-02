package baseball.lostfound.domain.entity;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    private Content content;
}
