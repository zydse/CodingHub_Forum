package top.zydse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zydse.dto.AliResponseDTO;
import top.zydse.dto.ResultDTO;
import top.zydse.enums.CustomizeErrorCode;
import top.zydse.mapper.VerificationMapper;
import top.zydse.model.Verification;
import top.zydse.provider.AliMessageProvider;

import java.util.Random;
import java.util.UUID;

/**
 * CreateBy: zydse
 * ClassName: VerificationService
 * Description:
 *
 * @Date: 2020/3/27
 */
@Service
public class VerificationService {
    @Autowired
    private AliMessageProvider aliMessageProvider;
    @Autowired
    private VerificationMapper verificationMapper;

    public ResultDTO sendSms(String phoneNumber, long timestamp) {
        StringBuilder code = new StringBuilder();
        UUID uuid = UUID.randomUUID();
        long bits = uuid.getLeastSignificantBits();
        Random rand = new Random(bits);
        for(int i = 0;i < 6;i++)
            code.append(rand.nextInt(10));
        AliResponseDTO aliResponseDTO = aliMessageProvider.sendSms(phoneNumber, code.toString());
        if("OK".equals(aliResponseDTO.getCode())){
            Verification record = new Verification();
            record.setCode(code.toString());
            record.setPhoneNumber(phoneNumber);
            record.setGmtCreate(timestamp);
            verificationMapper.insertSelective(record);
            return ResultDTO.successOf();
        }
        return ResultDTO.errorOf(CustomizeErrorCode.ALI_SERVER_ERROR);
    }
}
