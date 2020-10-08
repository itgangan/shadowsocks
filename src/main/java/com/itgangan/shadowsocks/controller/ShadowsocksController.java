package com.itgangan.shadowsocks.controller;

import com.itgangan.shadowsocks.config.Config;
import com.itgangan.shadowsocks.service.ImportConfigService;
import com.itgangan.shadowsocks.service.ShadowsocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shadowsocks")
public class ShadowsocksController {

    @Autowired
    ShadowsocksService shadowsocksService;

    @Autowired
    ImportConfigService importConfigService;

    @GetMapping("/config")
    public List<String> config() throws IOException {
        List<Config> configs = shadowsocksService.getConfigFromHtml();

        return configs.stream().map(o -> importConfigService.importConfig(o)).collect(Collectors.toList());
    }
}
