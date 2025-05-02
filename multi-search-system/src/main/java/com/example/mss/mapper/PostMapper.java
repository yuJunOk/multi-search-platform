package com.example.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mss.pojo.domain.PostDo;

import java.util.Date;
import java.util.List;

/**
* @author pengYuJun
* @description 针对表【tb_post(帖子)】的数据库操作Mapper
* @createDate 2025-04-30 14:30:29
* @Entity com.example.mss.pojo.domain.Post
*/
public interface PostMapper extends BaseMapper<PostDo> {
    /**
     * 查询帖子列表（包括已被删除的数据）
     * @param minUpdateTime
     * @return
     */
    List<PostDo> listPostWithDelete(Date minUpdateTime);
}




