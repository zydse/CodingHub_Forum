package top.zydse.service;

import top.zydse.dto.RegisterDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.dto.VerificationDTO;
import top.zydse.model.Permission;
import top.zydse.model.User;

import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: UserService
 * Description:
 *
 * @Date: 2020/3/31
 */
public interface UserService {
    void saveGithubUser(User user);

    User selectByName(String username);

    ResultDTO register(RegisterDTO registerDTO, VerificationDTO verificationCode);

    User findByName(String username);

    ResultDTO<VerificationDTO> sendSms(String phoneNumber, long timestamp);

    List<String> getPermCode(Long userId);

    User selectByPhone(String phoneNumber);

    void resetPassword(String password, String phone);
}
