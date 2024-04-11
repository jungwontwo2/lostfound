package baseball.lostfound.controller;

import baseball.lostfound.domain.dto.comment.CommentRequestDto;
import baseball.lostfound.domain.dto.comment.CommentResponseDto;
import baseball.lostfound.domain.dto.content.ContentPagingDto;
import baseball.lostfound.domain.dto.content.ContentResponseDto;
import baseball.lostfound.domain.dto.content.ContentWriteDto;
import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.Content;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.domain.enums.Position;
import baseball.lostfound.domain.enums.Team;
import baseball.lostfound.service.CommentService;
import baseball.lostfound.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final CommentService commentService;

    @GetMapping("/contents")
    public String home(@PageableDefault(page = 1) Pageable pageable,
                       @RequestParam(name = "team",required = false)Team team,
                       @RequestParam(name = "position",required = false)String position,
                       @RequestParam(name = "searchWord",required = false)String searchWord,
                       Model model){
        model.addAttribute("team",team);
        model.addAttribute("position",position);
        Position positionValue= null;
        if(searchWord==null){
            if (position != null && !position.isEmpty()) {
                positionValue = Position.ofDescription(position);
            }
            Page<ContentPagingDto> contentDtos = contentService.paging(pageable,team,positionValue);
            int blockLimit = 5;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());

            model.addAttribute("contentDtos", contentDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        else {
            //Page<ContentDto> contentDtos = contentService.getBoardListBySearchword(pageable, searchWord);
            Page<ContentPagingDto> contentDtos = contentService.getBoardListBySearchword(pageable, searchWord);
            int blockLimit = 5;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());

            model.addAttribute("contentDtos", contentDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        return "content/free";
    }

    @GetMapping("/contents/write")
    public String getWriteContentPage(@ModelAttribute("content") ContentWriteDto contentWriteDto, Model model) {
        model.addAttribute("teams", Team.values());
        model.addAttribute("positions", Position.values());
        return "content/write-page";
    }
    @PostMapping("/contents/write")
    public String writeContent(@Validated @ModelAttribute("content")ContentWriteDto contentWriteDto,
                               BindingResult bindingResult,
                               Authentication authentication,Model model) throws IOException {
        if(bindingResult.hasErrors()){
            String errorMessage ="제목, 내용, 팀, 위치를 올바르게 입력해주세요.\n";
            model.addAttribute("teams", Team.values());
            model.addAttribute("positions", Position.values());
            model.addAttribute("errorMessage",errorMessage);
            return "content/write-page";
        }
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        contentService.writeContent(contentWriteDto,principal);
        return "redirect:/contents";
    }

    @GetMapping("/contents/{id}")
    public String getContent(@PathVariable("id") Long id, Authentication authentication, Model model,
                             @ModelAttribute("commentRequestDto")CommentRequestDto commentRequestDto) {
        ContentResponseDto contentDto = contentService.getContent(id);
        List<CommentResponseDto> commentResponseDtos = commentService.commentDtoList(id);
        try{
            CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
            String nickname = principal.getNickname();
            model.addAttribute("nickName",nickname);
        }catch (Exception e){
        }
        model.addAttribute("content",contentDto);
        model.addAttribute("comments",commentResponseDtos);
        return "content/content-page";
    }
}
