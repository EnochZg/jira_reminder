package com.ibu.reminder.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class WeChatPusher {
    public String push(String msg, String[] mentionedList) throws JSONException {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=11498f6a-f049-42ac-bfd6-49003be93d0f";

        RestTemplate restTemplate = new RestTemplate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msgtype", "text");

        JSONObject textObject = new JSONObject();
        textObject.put("content", msg);
        textObject.put("mentioned_list", new JSONArray(mentionedList));
//        textObject.put("mentioned_mobile_list", new JSONArray(mentionedList));



        jsonObject.put("text", textObject);

        log.info(jsonObject.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(url, request, String.class);
        return entity.getBody();
    }
}
