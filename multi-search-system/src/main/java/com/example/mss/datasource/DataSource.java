package com.example.mss.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mss.pojo.vo.PictureVo;

/** 数据源接口
 *
 * @author pengYuJun
 */
public interface DataSource<T>{

    /**
     * doSearch
     * @param searchText
     * @param current
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long current, long pageSize);

}
