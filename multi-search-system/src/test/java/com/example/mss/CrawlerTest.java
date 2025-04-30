package com.example.mss;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.mss.pojo.domain.PostDo;
import com.example.mss.service.PostService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 爬虫测试
 */
@Slf4j
@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;

    @Test
    void testFetchPicture() throws IOException {
        int current = 1;
        String url = "https://cn.bing.com/images/search?q=小黑子&first=" + current;
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.isv");
        for (Element element : elements) {
            // 取图片地址（murl）
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            System.out.println(murl + " " + title);
        }
    }

    @Test
    void testFetchPost() {
        // 1. 获取数据
        String json = "{   \"pageSize\": 12,   \"sortOrder\": \"descend\",   \"sortField\": \"_score\",   \"tags\": [],   \"searchText\": \"\",   \"current\": 1,   \"reviewStatus\": 1,   \"hiddenContent\": true,   \"type\": \"passage\" }";
        String url = "https://api.codefather.cn/api/search/";
        String result = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
        //System.out.println(result);
        // 2. json转对象
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONObject page = (JSONObject) data.get("searchPage");
        JSONArray records = (JSONArray) page.get("records");
        List<PostDo> postDos = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            PostDo post = new PostDo();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("plainTextDescription"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            postDos.add(post);
        }
        // 3. 并发插入数据库
        CountDownLatch countDownLatch = new CountDownLatch(postDos.size());
        postDos.forEach(postDo -> {
            // 异步插入
            CompletableFuture.runAsync(() -> {
                try{
                    postService.save(postDo);
                }catch (Exception e){
                    log.error(e.getMessage());
                }finally {
                    countDownLatch.countDown();
                }
            });
        });
        try{
            countDownLatch.await();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
