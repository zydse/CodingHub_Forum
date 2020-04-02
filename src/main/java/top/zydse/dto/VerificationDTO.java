package top.zydse.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VerificationDTO implements Serializable {
    private Long id;
    private String phoneNumber;
    private String code;
    private Long gmtCreate;
}