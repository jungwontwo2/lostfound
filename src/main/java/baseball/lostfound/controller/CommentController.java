package baseball.lostfound.controller;

import baseball.lostfound.domain.dto.comment.CommentRequestDto;
import baseball.lostfound.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("contents/comment/{id}")
    public String writeComment(@PathVariable Long id, CommentRequestDto commentRequestDto,
                               HttpServletRequest request, Authentication authentication){
        String parentId = request.getParameter("parentId");
        System.out.println("parentId = " + parentId);
        commentService.writeComment(commentRequestDto,id,request.getParameter("parentId"),authentication,commentRequestDto.getIsPrivate());
        return "redirect:/contents/"+id;
    }
}
