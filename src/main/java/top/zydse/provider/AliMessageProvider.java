package top.zydse.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.zydse.dto.AliResponseDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.exception.CustomizeException;

/**
 * CreateBy: zydse
 * ClassName: AliMessageProvider
 * Description:
 *
 * @Date: 2020/3/27
 */
@Component
@Slf4j
public class AliMessageProvider {
    @Value("${aliyun.regionId}")
    private String regionId;
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessSecret}")
    private String accessSecret;
    @Value("${aliyun.signName}")
    private String signName;
    @Value("${aliyun.templateCode}")
    private String templateCode;

    public AliResponseDTO sendSms(String phone, String code) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        log.info("code in ali request : {}", code);
        request.putQueryParameter("TemplateParam", "{\"code\":" + code + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            return JSON.parseObject(response.getData(), AliResponseDTO.class);
        } catch (ClientException e) {
            throw new CustomizeException(CustomizeErrorCode.ALI_SERVER_ERROR);
        }
    }
}
