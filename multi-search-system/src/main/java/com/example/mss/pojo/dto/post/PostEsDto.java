package com.example.mss.pojo.dto.post;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.example.mss.pojo.domain.PostDo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author pengYuJun
 */
@Document(indexName = "post")
@Data
public class PostEsDto implements Serializable {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * id
     */
    @Id
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(index = false, store = true, type = FieldType.Date, format = {}, pattern = DATE_TIME_PATTERN)
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer deleted;

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param post
     * @return
     */
    public static PostEsDto objToDto(PostDo post) {
        if (post == null) {
            return null;
        }
        PostEsDto postEsDto = new PostEsDto();
        BeanUtils.copyProperties(post, postEsDto);
        String tagsStr = post.getTags();
        if (StringUtils.isNotBlank(tagsStr)) {
            postEsDto.setTags(JSONUtil.toList(tagsStr, String.class));
        }
        return postEsDto;
    }

    /**
     * 包装类转对象
     *
     * @param postEsDto
     * @return
     */
    public static PostDo dtoToObj(PostEsDto postEsDto) {
        if (postEsDto == null) {
            return null;
        }
        PostDo post = new PostDo();
        BeanUtils.copyProperties(postEsDto, post);
        List<String> tagList = postEsDto.getTags();
        if (CollectionUtils.isNotEmpty(tagList)) {
            post.setTags(JSONUtil.toJsonStr(tagList));
        }
        return post;
    }
}