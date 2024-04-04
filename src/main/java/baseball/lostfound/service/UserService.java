package baseball.lostfound.service;

import baseball.lostfound.domain.dto.user.JoinUserDto;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.domain.error.CustomException;
import baseball.lostfound.domain.error.ErrorCode;
import baseball.lostfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void saveUser(JoinUserDto userDto, String role){
        if(userRepository.existsByLoginId(userDto.getLoginId())){
            throw new CustomException(ErrorCode.HAS_LOGIN_ID);
        } else if (userRepository.existsByNickname(userDto.getNickname())) {
            throw new CustomException(ErrorCode.HAS_NICKNAME);
        }
        User user = JoinUserDto.toEntity(userDto.getLoginId(),bCryptPasswordEncoder.encode(userDto.getPassword()),userDto.getNickname(),role);
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElse(null);
        userRepository.delete(user);
    }
    public User getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElse(null);
    }
    public User getUserByNickName(String nickName) {
        return userRepository.findByNickNameCheck(nickName).orElse(null);
    }
}
