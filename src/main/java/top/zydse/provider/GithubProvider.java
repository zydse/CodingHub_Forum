package top.zydse.provider;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.zydse.dto.AccessTokenDTO;
import top.zydse.dto.GithubUserDTO;

import java.io.IOException;

/**
 * CreateBy: zydse
 * ClassName: GithubProvider
 * Description:
 *
 * @Date: 2020/3/3
 */
@Component
@Slf4j
public class GithubProvider {
    private OkHttpClient client;

    @Value("${github.oauthUrl}")
    private String oauthUrl;
    @Value("${github.getTokenUrl}")
    private String getTokenUrl;

    public GithubProvider() {
        client = new OkHttpClient();
    }

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO), mediaType);
        Request request = new Request.Builder()
                .url(oauthUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            return result.split("&")[0].split("=")[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUserDTO getGithubUser(String token) {
        log.info("使用token获取用户信息");
        Request request = new Request.Builder()
                .url(getTokenUrl)
                .header("Authorization", "token " + token )
                .build();

        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            return JSON.parseObject(string, GithubUserDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
