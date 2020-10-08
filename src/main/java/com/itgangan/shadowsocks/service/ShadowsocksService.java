package com.itgangan.shadowsocks.service;

import com.itgangan.shadowsocks.config.Config;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShadowsocksService {

    private static final String HTTPS_3_WEIWEI_IN_2020_URL = "https://3.weiwei.in/2020.html";

    public List<Config> getConfigFromHtml() throws IOException {
        List<Config> configs = new ArrayList<>(6);
        Document doc = Jsoup.connect(HTTPS_3_WEIWEI_IN_2020_URL).get();
        Element table = doc.getElementsByTag("table").get(0);
        Elements trs = table.select("tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String ip = tds.get(0).html();
            int port = Integer.parseInt(tds.get(1).html());
            String passType = tds.get(2).html();
            String password = tds.get(3).html();

            configs.add(new Config(ip, port, passType, password));
        }
        return configs;
    }
}
