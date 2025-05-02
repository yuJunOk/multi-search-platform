package com.example.mss.job.sycle;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.mss.esdao.PostEsDao;
import com.example.mss.mapper.PostMapper;
import com.example.mss.pojo.domain.PostDo;
import com.example.mss.pojo.dto.post.PostEsDto;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

/**
 * 增量同步帖子到 es
 *
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class IncSyncPostToEs {

    @Resource
    private PostMapper postMapper;

    @Resource
    private PostEsDao postEsDao;

    // 原创_项目 [鱼皮](https://github.com/liyupi)

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // 查询近 5 分钟内的数据
        Date fiveMinutesAgoDate = new Date(System.currentTimeMillis() - 5 * 60 * 1000L);
        List<PostDo> postList = postMapper.listPostWithDelete(fiveMinutesAgoDate);
        if (CollectionUtils.isEmpty(postList)) {
            log.info("no inc post");
            return;
        }
        List<PostEsDto> postEsDtoList = postList.stream()
                .map(PostEsDto::objToDto)
                .toList();
        final int pageSize = 500;
        int total = postEsDtoList.size();
        log.info("IncSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            postEsDao.saveAll(postEsDtoList.subList(i, end));
        }
        log.info("IncSyncPostToEs end, total {}", total);
    }
}
