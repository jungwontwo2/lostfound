package baseball.lostfound.User;

import baseball.lostfound.domain.dto.user.JoinUserDto;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.domain.error.CustomException;
import baseball.lostfound.repository.UserRepository;
import baseball.lostfound.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class Join {
    @Autowired
    UserService userService;

    @PersistenceContext     //em.clear를 위해 필요한 엔티티매니저
    private EntityManager em;

    @Autowired
    private Validator validator;
    @Autowired
    UserRepository userRepository;
    @Test
    public void JoinUserO(){
        JoinUserDto joinUserDto = new JoinUserDto("qqqq", "qwerqwer", "qwerqwer", "qqqq");
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(joinUserDto);
        userService.saveUser(joinUserDto,"ROLE_USER");
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(3);
    }
    @Test
    public void JoinsUserLoginIdShortFail(){
        JoinUserDto joinUserDto = new JoinUserDto("q","qwerqwer","qwerqwer","qwerr");
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(joinUserDto); //validator를 통해서 @Validate 실행한것과 같은 결과
        assertFalse(violations.isEmpty());//만약 validate를 통해서 에러가 있으면 violations안에는 error가 있다. 그러니까 isEmpty는 False가 된다.
        for (ConstraintViolation<JoinUserDto> violation : violations) {
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }
        boolean hasLoginIdSizeViolation = violations.stream()//violations안에 돌면서
                .anyMatch(violation -> "loginId".equals(violation.getPropertyPath().toString())//검증 실패한 필드의 이름이 loginId인지 체크
                        && violation.getMessage().contains("최소 4자 이상"));//검증 실패 메세지에 '최소 4가 이상'이라는 문자열이 들어있는지
        assertTrue(hasLoginIdSizeViolation);
    }
    @Test
    public void JoinUserLoginIdLongFail(){
        JoinUserDto joinUserDto = new JoinUserDto("qqqqqqqqqqqqqqq","qwerqwer","qwerqwer","qwerr");
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(joinUserDto); //validator를 통해서 @Validate 실행한것과 같은 결과
        assertFalse(violations.isEmpty());//만약 validate를 통해서 에러가 있으면 violations안에는 error가 있다. 그러니까 isEmpty는 False가 된다.
        boolean hasLoginIdSizeViolation = violations.stream()//violations안에 돌면서
                .anyMatch(violation -> "loginId".equals(violation.getPropertyPath().toString())//검증 실패한 필드의 이름이 loginId인지 체크
                        && violation.getMessage().contains("10자 이하"));//검증 실패 메세지에 '최소 4가 이상'이라는 문자열이 들어있는지
        assertTrue(hasLoginIdSizeViolation);
    }
    @Test
    public void JoinUserPatternFail(){
        JoinUserDto joinUserDto = new JoinUserDto("!!@!","qwerqwer","qwerqwer","qwerr");
        Set<ConstraintViolation<JoinUserDto>> violations = validator.validate(joinUserDto); //validator를 통해서 @Validate 실행한것과 같은 결과
        assertFalse(violations.isEmpty());//만약 validate를 통해서 에러가 있으면 violations안에는 error가 있다. 그러니까 isEmpty는 False가 된다.
        boolean hasLoginIdSizeViolation = violations.stream()//violations안에 돌면서
                .anyMatch(violation -> "loginId".equals(violation.getPropertyPath().toString())//검증 실패한 필드의 이름이 loginId인지 체크
                        && violation.getMessage().contains("알파벳 소문자(a~z), 숫자(0~9)만 입력 가능합니다."));//검증 실패 메세지에 '최소 4가 이상'이라는 문자열이 들어있는지
        assertTrue(hasLoginIdSizeViolation);
    }
    @Test
    public void JoinUserSameLoginIdFail(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
        CustomException exception = assertThrows(CustomException.class,
                ()->{userService.saveUser(joinUserDto,"ROLE_USER");});
        assertEquals("해당 ID가 중복됩니다.",exception.getErrorCode().getMessage());
    }
    @Test
    public void JoinUserSameNicknameFail(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        JoinUserDto joinUserDto2 = new JoinUserDto("qwef","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
        CustomException exception = assertThrows(CustomException.class,
                ()->{userService.saveUser(joinUserDto2,"ROLE_USER");});
        assertEquals("닉네임이 중복됩니다.",exception.getErrorCode().getMessage());
    }
    @Test
    public void findById(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
        User user = userRepository.findByLoginId("qwerr").orElse(null);
        org.assertj.core.api.Assertions.assertThat(user.getNickname()).isEqualTo("qwerr");
    }
    @Test
    public void deleteUserById(){
        JoinUserDto joinUserDto = new JoinUserDto("qwerr","qwerqwer","qwerqwer","qwerr");
        userService.saveUser(joinUserDto,"ROLE_USER");
        em.clear();
        User user = userRepository.findByLoginId("qwerr").orElse(null);
        userService.deleteUser(user.getId());
        User user1 = userRepository.findByLoginId("qwerr").orElse(null);
        org.assertj.core.api.Assertions.assertThat(user1).isNull();
    }
}
