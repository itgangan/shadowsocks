package com.itgangan.shadowsocks.service;

import cn.hutool.core.codec.Base64Encoder;
import com.itgangan.shadowsocks.config.Config;
import org.springframework.stereotype.Service;

@Service
public class ImportConfigService {

    public String importConfig(Config config) {

        String plain = config.getMethod() + ":" + config.getPassword() + "@" + config.getIp() + ":" + config.getPort();

        return "ss://" + Base64Encoder.encode(plain.getBytes()) + "#" + config.getIp() + ":" + config.getPort();
    }

}
