package baseball.lostfound.controller;

import baseball.lostfound.domain.dto.content.ContentPagingDto;
import baseball.lostfound.domain.dto.user.JoinUserDto;
import baseball.lostfound.domain.dto.user.LoginUserDto;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.service.ContentService;
import baseball.lostfound.service.LoginService;
import baseball.lostfound.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final ContentService contentService;
    private final LoginService loginService;

    private final UserService userService;
    @GetMapping("/")
    public String home(){
        return "home/home";
    }
    @GetMapping("/users/join")
    public String addUserForm(@ModelAttribute("user") JoinUserDto user) {
        return "users/addMemberForm";
    }

    //회원가입 폼 제출
    @PostMapping("/users/join")
    public String addUser(@Validated @ModelAttribute("user") JoinUserDto user, BindingResult bindingResult) {
        if (!user.getPassword().equals(user.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "passwordCheckValid", "비밀번호가 일치하지 않습니다.");
        }
        System.out.println(bindingResult);
        if (bindingResult.hasErrors()) {
            return "users/addMemberForm";
        }
        userService.saveUser(user,"ROLE_USER");
        return "redirect:/";
    }
    @GetMapping("/boards/free")
    public String home(@PageableDefault(page = 1)Pageable pageable,
                       @RequestParam(name = "searchWord",required = false)String searchWord,
                       @RequestParam(value = "orderby",required = false,defaultValue = "id") String orderCriteria,
                       Model model){
        if(searchWord==null){
            Page<ContentPagingDto> contentDtos = contentService.paging(pageable,orderCriteria);
            int blockLimit = 3;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());



            model.addAttribute("contentDtos", contentDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        else {
            //Page<ContentDto> contentDtos = contentService.getBoardListBySearchword(pageable, searchWord);
            Page<ContentPagingDto> contentDtos = contentService.getBoardListBySearchword(pageable, searchWord);
            int blockLimit = 3;
            int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
            int endPage = Math.min((startPage + blockLimit - 1), contentDtos.getTotalPages());

            model.addAttribute("contentDtos", contentDtos);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        }
        return "content/free";
    }
    @GetMapping("/users/login")
    public String loginForm(@RequestParam(value = "error",required = false)String error,
                            @RequestParam(value = "exception",required = false)String exception,
                            Model model,@ModelAttribute("user") LoginUserDto user) {
        if(error!=null){
            model.addAttribute("error", error);
            model.addAttribute("exception", exception);
        }
        return "users/login";
    }
    @PostMapping("/users/login")
    public String login(@Validated @ModelAttribute("user") LoginUserDto user,
                        BindingResult bindingResult,HttpServletRequest request,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return "users/login";
        }
        User loginUser = loginService.login(user.getLoginId(), user.getPassword());
        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "users/login";
        }
        return "redirect:/";
    }
}
