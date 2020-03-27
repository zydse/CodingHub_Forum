package top.zydse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zydse.dto.RegisterDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.mapper.UserMapper;
import top.zydse.model.User;
import top.zydse.model.UserExample;

import java.util.List;
import java.util.UUID;

/**
 * CreateBy: zydse
 * ClassName: UserService
 * Description:
 *
 * @Date: 2020/3/10
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void saveOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
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
            example.createCriteria().andIdEqualTo(users.get(0).getId());
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


    public ResultDTO save(RegisterDTO registerDTO) {
        User user = new User();
        user.setName(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setGmtCreate(registerDTO.getGmtCreate());
        user.setGmtModified(user.getGmtCreate());
        user.setAvatarUrl("https://avatars1.githubusercontent.com/u/35904888");
        userMapper.insertSelective(user);
        return ResultDTO.successOf();
    }

    public User checkUser(String username, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andNameEqualTo(username)
                .andPasswordEqualTo(password);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList == null || userList.size() != 1)
            return null;
        User user = userList.get(0);
        user.setToken(UUID.randomUUID().toString());
        user.setGmtModified(System.currentTimeMillis());
        userMapper.updateByPrimaryKeySelective(user);
        return user;
    }
}
