package top.zydse.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import top.zydse.model.User;
import top.zydse.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * CreateBy: zydse
 * ClassName: UserRealm
 * Description:
 *
 * @Date: 2020/3/31
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;


    @Override
    public void setName(String name) {
        super.setName("customizeRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        List<String> permissionList = userService.getPermCode(user.getId());
        if (permissionList == null)
            return null;
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissionList);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = userService.findByName(username);
        if (user == null)
            return null;
        User cookieUser = new User();
        BeanUtils.copyProperties(user, cookieUser);
        cookieUser.setPassword(String.valueOf(token.getPassword()));
        return new SimpleAuthenticationInfo(cookieUser, user.getPassword(),
                ByteSource.Util.bytes(user.getSalt().toString()), this.getName());
    }

    public void clearCache(){
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
