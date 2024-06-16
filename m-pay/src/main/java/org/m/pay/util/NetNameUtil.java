package org.m.pay.util;

import cn.hutool.http.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class NetNameUtil {
    public static String URL = "https://www.bzcm88.com/wangming/";

    public static String getNetWorkName() {
        try {
            Map<String, String> header = new HashMap();
            Map<String, Object> form = new HashMap();
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
            header.put("Content-Type", "ext/html; charset=utf-8");
            String body = HttpUtil.createPost(URL + "?wmtype=%E6%B2%99%E9%9B%95").addHeaders(header).execute().body();
            Document document = Jsoup.parse(body);
            Elements result = document.getElementsByClass("names-warp");
            Elements titleEle = result.select("span");
            int random = (int) (Math.random() * titleEle.size());
            return titleEle.get(random).text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
