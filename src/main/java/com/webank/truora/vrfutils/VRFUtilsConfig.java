package com.webank.truora.vrfutils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class VRFUtilsConfig
{
    @Value("${ecvrflib.debugLevel:0}")
    public int debuglevel ;
}
