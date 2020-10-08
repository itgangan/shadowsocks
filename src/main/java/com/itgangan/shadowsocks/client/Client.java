package com.itgangan.shadowsocks.client;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itgangan.shadowsocks.config.Config;
import com.itgangan.shadowsocks.service.ShadowsocksService;
import org.jsoup.internal.StringUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Client {

    public static void main(String[] args) throws IOException {
        String shadowSocksConfigPath = args[0];
        ShadowsocksService service = new ShadowsocksService();

        // 从网站得到配置
        List<Config> shadowSocksConfigServer = service.getConfigFromHtml();

        // 读取原来配置
        String shadowSocksConfigLocal = readConfig(shadowSocksConfigPath);

        // 用得到的服务器替换原有配置中的服务器
        String shadowSocksConfig = updateConfig(shadowSocksConfigServer, shadowSocksConfigLocal);

        // 将配置写回硬盘
        writeConfig(shadowSocksConfigPath, shadowSocksConfig);

    }

    private static String readConfig(String shadowSocksConfigPath) {
        if (StringUtil.isBlank(shadowSocksConfigPath)) {
            System.err.println("配置文件路径不对");
        }
        FileReader fr = new FileReader(new File(shadowSocksConfigPath));
        return fr.readString();
    }

    private static void writeConfig(String shadowSocksConfigPath, String guiConfig) throws IOException {
        if (StringUtil.isBlank(guiConfig)) {
            return;
        }

        try (FileWriter writer = new FileWriter(shadowSocksConfigPath)) {
            writer.write(guiConfig);
        }
    }

    private static String updateConfig(List<Config> servers, String shadowSocksConfigLocal) {
        JSONObject json;
        try {
            json = JSON.parseObject(shadowSocksConfigLocal);
        } catch (Exception e) {
            System.err.println("配置文件格式不对");
            return "";
        }
        JSONArray configs = createConfig(servers);
        json.put("configs", configs);
        return JSON.toJSONString(json);
    }

    private static JSONArray createConfig(List<Config> servers) {
        JSONArray configs = new JSONArray();
        servers.forEach(o -> {
            JSONObject config = new JSONObject();
            config.put("server", o.getIp());
            config.put("server_port", o.getPassword());
            config.put("password", o.getPassword());
            config.put("method", o.getMethod());
            config.put("plugin", "");
            config.put("plugin_opts", "");
            config.put("plugin_args", "");
            config.put("remarks", "");
            config.put("timeout", 5);
            configs.add(config);
        });
        return configs;
    }


}
