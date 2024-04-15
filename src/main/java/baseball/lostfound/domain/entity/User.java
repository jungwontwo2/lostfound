package baseball.lostfound.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String loginId;
    private String password;
    private String nickname;
    private String role;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Content> contents;

    public User(String loginId, String password, String nickname,String role) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.role=role;
    }
    public void updateNickname(String updatedNickName){
        this.nickname=updatedNickName;
    }
}
