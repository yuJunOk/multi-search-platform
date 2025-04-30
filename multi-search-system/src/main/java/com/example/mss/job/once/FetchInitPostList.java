package com.example.mss.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.mss.pojo.domain.PostDo;
import com.example.mss.service.PostService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/** 每次启动 springboot 项目时会执行一次 run 方法
 *  获取初始帖子列表
 *
 * @author pengYuJun
 */
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Override
    public void run(String... args) {
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