package baseball.lostfound.service;

import baseball.lostfound.domain.entity.User;
import baseball.lostfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User login(String userId,String password){
        Optional<User> user = userRepository.findByLoginId(userId);
        System.out.println("zz =");
        if(bCryptPasswordEncoder.matches(password,user.get().getPassword())){
            return user.get();
        }
        else {
            System.out.println("zzzzzzz =");
            return null;
        }
    }
}
