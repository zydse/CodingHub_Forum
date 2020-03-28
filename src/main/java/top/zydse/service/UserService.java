package top.zydse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.AliResponseDTO;
import top.zydse.dto.RegisterDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.dto.VerificationDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.UserMapper;
import top.zydse.model.User;
import top.zydse.model.UserExample;
import top.zydse.provider.AliMessageProvider;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * CreateBy: zydse
 * ClassName: UserService
 * Description:
 *
 * @Date: 2020/3/10
 */
@Service
@Slf4j
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AliMessageProvider aliMessageProvider;

    public void saveGithubUser(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().
                andAccountIdEqualTo(user.getAccountId());
        List<User> githubUsers = userMapper.selectByExample(userExample);
        userExample.clear();
        userExample.createCriteria()
                .andNameEqualTo(user.getName())
                .andAccountIdIsNull();
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() != 0){
            throw new CustomizeException(CustomizeErrorCode.DUPLICATE_USERNAME);
        } else if (githubUsers.size() == 0) {
            //新增用户
            user.setRoleId(2);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insertSelective(user);
        } else {
            //更新用户资料
            User u = new User();
            u.setGmtModified(System.currentTimeMillis());
            u.setBio(user.getBio());
            u.setAvatarUrl(user.getAvatarUrl());
            u.setToken(user.getToken());
            u.setName(user.getName());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(githubUsers.get(0).getId());
            userMapper.updateByExampleSelective(u, example);
        }
    }

    public User selectByName(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList == null || userList.size() != 1)
            return null;
        return userList.get(0);
    }

    @Transactional
    public ResultDTO save(RegisterDTO registerDTO, VerificationDTO verificationCode) {
        if (verificationCode == null || !registerDTO.getPhoneNumber().equals(verificationCode.getPhoneNumber())) {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFICATION_CODE_ERROR);
        }
        long gap = (registerDTO.getGmtCreate() - verificationCode.getGmtCreate()) / 1000 / 60;
        if (verificationCode.getCode().equals(registerDTO.getVerifyCode()) && gap < 15L) {
            User user = new User();
            user.setRoleId(2);
            user.setName(registerDTO.getUsername());
            user.setPassword(registerDTO.getPassword());
            user.setPhoneNumber(registerDTO.getPhoneNumber());
            user.setGmtCreate(registerDTO.getGmtCreate());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl("https://avatars1.githubusercontent.com/u/35904888");
            userMapper.insertSelective(user);
            return ResultDTO.successOf();
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFICATION_CODE_INACTIVE);
        }
    }

    public User checkUser(String username, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(username)
                .andPasswordEqualTo(password);
        List<User> userList = userMapper.selectByExample(userExample);
        System.out.println(userList);
        if (userList == null || userList.size() != 1)
            return null;
        User user = userList.get(0);
        user.setToken(UUID.randomUUID().toString());
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    public ResultDTO sendSms(String phoneNumber, long timestamp) {
        StringBuilder code = new StringBuilder();
        UUID uuid = UUID.randomUUID();
        long bits = uuid.getLeastSignificantBits();
        Random rand = new Random(bits);
        for (int i = 0; i < 6; i++)
            code.append(rand.nextInt(10));
        AliResponseDTO aliResponseDTO = aliMessageProvider.sendSms(phoneNumber, code.toString());
        log.info(aliResponseDTO.toString());
        if ("OK".equals(aliResponseDTO.getCode())) {
            VerificationDTO record = new VerificationDTO();
            record.setCode(code.toString());
            record.setPhoneNumber(phoneNumber);
            record.setGmtCreate(timestamp);
            return ResultDTO.successOf(record);
        }
        return ResultDTO.errorOf(CustomizeErrorCode.ALI_SERVER_ERROR);
    }
}
