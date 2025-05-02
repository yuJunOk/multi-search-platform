package com.example.mss.esdao;

import com.example.mss.pojo.domain.PostDo;
import com.example.mss.pojo.dto.post.PostEsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author pengYuJun
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDto, Long> {

    /**
     * findByUserId
     * @param userId
     * @return
     */
    List<PostEsDto> findByUserId(Long userId);

    /**
     * findByTitle
     * @param title
     * @return
     */
    List<PostEsDto> findByTitle(String title);

    /**
     * 使用多条件模糊匹配（支持 title 或 content）
     * @param keyword
     * @param pageable
     * @return
     */
    @Query("{\"bool\": {\"should\": [ {\"match\": {\"title\": \"?0\"}}, {\"match\": {\"content\": \"?0\"}} ]}}")
    Page<PostEsDto> searchByTitleOrContent(String keyword, Pageable pageable);
}