package top.zydse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import top.zydse.dto.AccessTokenDTO;
import top.zydse.dto.GithubUserDTO;
import top.zydse.model.User;
import top.zydse.provider.GithubProvider;
import top.zydse.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * CreateBy: zydse
 * ClassName: AuthorizeController
 * Description:
 *
 * @Date: 2020/3/3
 */
@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirectUri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(String code, Integer state,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUserDTO githubUser = githubProvider.getGithubUser(accessToken);
        if (githubUser != null && githubUser.getId() != null) {
            User user = new User();
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setBio(githubUser.getBio());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.saveGithubUser(user);
            response.addCookie(new Cookie("token", user.getToken()));
            return "redirect:/";
        } else {
            //重新登录
            log.error("Fail to get message from github {}",githubUser);
            return "redirect:/";
        }
    }
}
