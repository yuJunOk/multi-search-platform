package com.example.mss.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mss.pojo.domain.PostDo;
import com.example.mss.pojo.dto.post.PostQueryPageDto;
import com.example.mss.pojo.vo.PostVo;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author pengYuJun
* @description 针对表【tb_post(帖子)】的数据库操作Service
* @createDate 2025-04-30 14:30:29
*/
public interface PostService extends IService<PostDo> {

    /**
     *
     * @param postQueryPageDto
     * @return
     */
    Page<PostVo> searchFromEs(PostQueryPageDto postQueryPageDto);

    /**
     * 分页查询帖子
     * @param postQueryPageDto
     * @param request
     * @return
     */
    Page<PostVo> listPostVoByPage(PostQueryPageDto postQueryPageDto, HttpServletRequest request);
}
