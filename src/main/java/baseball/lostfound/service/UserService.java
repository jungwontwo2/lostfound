package baseball.lostfound.service;

import baseball.lostfound.domain.dto.user.EditUserDto;
import baseball.lostfound.domain.dto.user.JoinUserDto;
import baseball.lostfound.domain.dto.user.UserNicknameUpdateDto;
import baseball.lostfound.domain.entity.User;
import baseball.lostfound.domain.error.CustomException;
import baseball.lostfound.domain.error.ErrorCode;
import baseball.lostfound.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public EditUserDto findMember(String loginId){
        User user = userRepository.findByLoginId(loginId).orElse(null);
        EditUserDto editUserDto = new EditUserDto(loginId, user.getNickname());
        return editUserDto;
    }
    @Transactional(readOnly = true)
    public boolean checkNicknameDuplication(String nickname){
        return userRepository.existsByNickname(nickname);
    }
    @Transactional
    public User updateUserNickname(UserNicknameUpdateDto userDto){
        Optional<User> optionalUser = userRepository.findByLoginId(userDto.getLoginId());
        User updateUser = optionalUser.get();
        updateUser.updateNickname(userDto.getNickname());
        return updateUser;
    }
}
