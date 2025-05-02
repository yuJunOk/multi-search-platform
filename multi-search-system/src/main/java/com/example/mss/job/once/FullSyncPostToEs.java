package com.example.mss.job.once;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.mss.esdao.PostEsDao;
import com.example.mss.pojo.domain.PostDo;
import com.example.mss.pojo.dto.post.PostEsDto;
import com.example.mss.service.PostService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 全量同步帖子到 es
 * 
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Resource
    private PostEsDao postEsDao;

    @Override
    public void run(String... args) {
        List<PostDo> postList = postService.list();
        if (CollectionUtils.isEmpty(postList)) {
            return;
        }
        List<PostEsDto> postEsDtoList = postList.stream().map(PostEsDto::objToDto).collect(Collectors.toList());
        final int pageSize = 500;
        int total = postEsDtoList.size();
        log.info("FullSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            postEsDao.saveAll(postEsDtoList.subList(i, end));
        }
        log.info("FullSyncPostToEs end, total {}", total);
    }
}