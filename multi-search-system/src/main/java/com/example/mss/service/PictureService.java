package com.example.mss.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mss.pojo.vo.PictureVo;

/**
 * @author pengYuJun
 */
public interface PictureService {

    /**
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<PictureVo> searchPicture(String searchText, long pageNum, long pageSize);
}