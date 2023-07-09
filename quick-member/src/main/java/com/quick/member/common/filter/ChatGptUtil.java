package com.quick.member.common.filter;

import com.google.gson.*;
import com.quick.member.common.config.params.ChatGptParamsConfig;
import com.quick.member.domain.dto.req.ChatGptModelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class ChatGptUtil {

    @Autowired
    private ChatGptParamsConfig config;

    /**
     * 获取chatgpt的答案
     */
    @Async
    public void getRespost(List<ChatGptModelDTO> messagelist) throws IOException {
//        建立连接
        String url = "https://api.openai.com/v1/chat/completions";
        HashMap<String, Object> bodymap = new HashMap<>();
        bodymap.put("model", "gpt-3.5-turbo");
        bodymap.put("temperature", 0.7);
        bodymap.put("messages", messagelist);
        bodymap.put("stream", true);
        Gson gson = new Gson();
        String s = gson.toJson(bodymap);
        URL url1 = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getHost(), config.getPort())));
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + config.getApiKey());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("stream", "true");
        conn.setDoOutput(true);
        //    写入请求参数
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, Charset.forName("UTF-8")));
        writer.write(s);
        writer.close();
        os.close();

        InputStream inputStream = conn.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {

            line = line.replace("data:", "");
            JsonElement jsonElement = JsonParser.parseString(line);
            if (!jsonElement.isJsonObject()) {
                continue;
            }
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            JsonArray choices = asJsonObject.get("choices").getAsJsonArray();
            if (choices.size() > 0) {
                JsonObject choice = choices.get(0).getAsJsonObject();
                JsonObject delta = choice.get("delta").getAsJsonObject();
                if (delta != null) {
                    if (delta.has("content")) {
//                        发送消息
                        String content = delta.get("content").getAsString();
//                      打印在控制台中
                        log.info(content);
                    }
                }
            }

        }
    }
}
