package baseball.lostfound.controller;

import baseball.lostfound.domain.dto.ResponseDto;
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
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/join/loginIdCheck")
    public @ResponseBody ResponseDto<?> check(@RequestBody(required = false) String loginId)  {
        if(loginId==null || loginId.isEmpty()){
            return new ResponseDto<>(-1,"아이디를 입력해주세요.",null);
        }
        if(!loginId.matches("^[a-z0-9]+$") || loginId.length()<4 || loginId.length()>10) {
            return new ResponseDto<>(-1, "아이디는 알파벳 소문자와 숫자만 포함할 수 있습니다. 4글자 이상 10글자 이하로 입력해주세요.", null);
        }
        User user = userService.getUserByLoginId(loginId);
        if(user==null){
            return new ResponseDto<>(1,"사용 가능한 ID입니다.",true);
        }
        else {
            return new ResponseDto<>(1,"중복된 아이디입니다.",false);
        }
    }
    @RequestMapping(value = "/join/nickNameCheck")
    public @ResponseBody ResponseDto<?> checkNickname(@RequestBody(required = false) String nickName)  {
        if(nickName==null || nickName.isEmpty()){
            return new ResponseDto<>(-1,"닉네임을 입력해주세요.",null);
        }
        if(!nickName.matches("^[a-zA-Z0-9가-힣]+$") || nickName.length()<2 || nickName.length()>10) {
            return new ResponseDto<>(-1, "닉네임은 알파벳 대,소문자와 한글과 숫자만 포함할 수 있습니다. 2글자 이상 10글자 이하로 입력해주세요.", null);
        }
        User user = userService.getUserByNickName(nickName);
        if(user==null){
            return new ResponseDto<>(1,"사용 가능한 닉네임입니다.",true);
        }
        else {
            return new ResponseDto<>(1,"중복된 닉네임입니다.",false);
        }
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
    @GetMapping("/users/logout")
    public String logout(){
        return "redirect:/";
    }
}
