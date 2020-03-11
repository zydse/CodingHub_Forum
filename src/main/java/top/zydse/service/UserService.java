package top.zydse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zydse.mapper.UserMapper;
import top.zydse.model.User;
import top.zydse.model.UserExample;

import java.util.List;

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
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
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

}
