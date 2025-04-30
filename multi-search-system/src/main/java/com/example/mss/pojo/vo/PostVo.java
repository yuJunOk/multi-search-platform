package com.example.mss.pojo.vo;

import cn.hutool.json.JSONUtil;
import com.example.mss.pojo.domain.PostDo;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author pengYuJun
 */
@Data
public class PostVo {
    /**
     * id
     */
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
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 创建人信息
     */
    private UserVo user;

    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

    /**
     * 是否已收藏
     */
    private Boolean hasFavour;

    /**
     * 包装类转对象
     *
     * @param postVo
     * @return
     */
    public static PostDo voToObj(PostVo postVo) {
        if (postVo == null) {
            return null;
        }
        PostDo post = new PostDo();
        BeanUtils.copyProperties(postVo, post);
        List<String> tagList = postVo.getTagList();
        if (tagList != null && !tagList.isEmpty()) {
            post.setTags(JSONUtil.toJsonStr(postVo));
        }
        return post;
    }

    /**
     * 对象转包装类
     *
     * @param post
     * @return
     */
    public static PostVo objToVo(PostDo post) {
        if (post == null) {
            return null;
        }
        PostVo postVO = new PostVo();
        BeanUtils.copyProperties(post, postVO);
        if (StringUtils.hasText(post.getTags())) {
            postVO.setTagList(JSONUtil.toList(post.getTags(), String.class));
        }
        return postVO;
    }
}
