package baseball.lostfound.domain.dto.comment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    private String comment;

    private boolean isPrivate;

    public boolean getIsPrivate(){
        return isPrivate;
    }
}
