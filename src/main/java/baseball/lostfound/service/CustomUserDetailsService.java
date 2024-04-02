package baseball.lostfound.service;

import baseball.lostfound.domain.dto.user.CustomUserDetails;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId).orElse(null);
        if(user!=null){
            return new CustomUserDetails(user);
        }
        System.out.println("return null");
        return null;
    }
}
