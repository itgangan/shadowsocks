package com.itgangan.shadowsocks;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Client {

    public static final String HTTPS_3_WEIWEI_IN_2020_URL = "https://3.weiwei.in/2020.html";

    public static void main(String[] args) throws IOException {
        String shadowSocksConfigPath = args[0];

        // 从网站得到配置
        List<Config> shadowSocksConfigServer = getConfigFromHtml();

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
        try (FileWriter writer = new FileWriter(shadowSocksConfigPath)) {
            writer.write(guiConfig);
        }
    }

    private static String updateConfig(List<Config> servers, String shadowSocksConfigLocal) {
        JSONObject json = null;
        try {
            json = JSON.parseObject(shadowSocksConfigLocal);
        } catch (Exception e) {
            System.err.println("配置文件格式不对");
        }
        JSONArray configs = createConfig(servers);
        json.put("configs", configs);
        return JSON.toJSONString(json);
    }

    private static JSONArray createConfig(List<Config> servers) {
        JSONArray configs = new JSONArray();
        servers.stream().forEach((o) -> {
            JSONObject config = new JSONObject();
            config.put("server", o.ip);
            config.put("server_port", o.port);
            config.put("password", o.password);
            config.put("method", o.method);
            config.put("plugin", "");
            config.put("plugin_opts", "");
            config.put("plugin_args", "");
            config.put("remarks", "");
            config.put("timeout", 5);
            configs.add(config);
        });
        return configs;
    }

    private static List<Config> getConfigFromHtml() throws IOException {
        List<Config> configs = new ArrayList<>(6);
        Document doc = Jsoup.connect(HTTPS_3_WEIWEI_IN_2020_URL).get();
        Element table = doc.getElementsByTag("table").get(0);
        Elements trs = table.select("tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String ip = tds.get(0).html();
            int port = Integer.valueOf(tds.get(1).html());
            String passType = tds.get(2).html();
            String password = tds.get(3).html();

            configs.add(new Config(ip, port, passType, password));
        }
        return configs;
    }

    private static class Config {
        private String ip;
        private int port;
        private String method;
        private String password;

        Config(String ip, int port, String method, String password) {
            this.ip = ip;
            this.port = port;
            this.method = method;
            this.password = password;
        }


    }
}
