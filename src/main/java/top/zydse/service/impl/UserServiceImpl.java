package top.zydse.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.zydse.dto.AliResponseDTO;
import top.zydse.dto.RegisterDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.dto.VerificationDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;
import top.zydse.mapper.CommonExtensionMapper;
import top.zydse.mapper.UserMapper;
import top.zydse.mapper.UserRoleMapper;
import top.zydse.model.*;
import top.zydse.provider.AliMessageProvider;
import top.zydse.service.UserService;

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
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonExtensionMapper extensionMapper;
    @Autowired
    private AliMessageProvider aliMessageProvider;
    @Autowired
    private UserRoleMapper userRoleMapper;

    //保存一个github用户的资料
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

    //通过用户名查询用户是否存在
    public User selectByName(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList == null || userList.size() != 1)
            return null;
        return userList.get(0);
    }

    //新增一个用户
    @Transactional
    public ResultDTO register(RegisterDTO registerDTO, VerificationDTO verificationCode) {
        if (verificationCode == null || !registerDTO.getPhoneNumber().equals(verificationCode.getPhoneNumber())) {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFICATION_CODE_ERROR);
        }
        long gap = (registerDTO.getGmtCreate() - verificationCode.getGmtCreate()) / 1000 / 60;
        if (verificationCode.getCode().equals(registerDTO.getVerifyCode()) && gap < 15L) {
            User user = new User();
            user.setName(registerDTO.getUsername());
            user.setSalt(registerDTO.getGmtCreate());
            Md5Hash hash = new Md5Hash(registerDTO.getPassword(), user.getSalt().toString(), 1);
            user.setPassword(hash.toString());
            user.setPhoneNumber(registerDTO.getPhoneNumber());
            user.setGmtCreate(registerDTO.getGmtCreate());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl("https://avatars1.githubusercontent.com/u/35904888");
            user.setToken(UUID.randomUUID().toString());
            userMapper.insertSelective(user);
            UserRole record = new UserRole();
            record.setGmtCreate(System.currentTimeMillis());
            //注册后默认是初级用户
            record.setRoleId(3);
            userRoleMapper.insert(record);
            return ResultDTO.successOf();
        } else {
            return ResultDTO.errorOf(CustomizeErrorCode.VERIFICATION_CODE_INACTIVE);
        }
    }

    @Override
    public User findByName(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        log.info("user : {}", userList);
        if (userList.size() != 1)
            return null;
        User user = userList.get(0);
        user.setToken(UUID.randomUUID().toString());
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    public ResultDTO<VerificationDTO> sendSms(String phoneNumber, long timestamp) {
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

    @Override
    public List<String> getPermCode(Long userId) {
        return extensionMapper.getPermCodeByUser(userId);
    }
}
