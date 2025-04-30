package com.example.mss.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mss.datasource.*;
import com.example.mss.pojo.dto.search.SearchDto;
import com.example.mss.pojo.enums.SearchTypeEnum;
import com.example.mss.pojo.vo.PictureVo;
import com.example.mss.pojo.vo.PostVo;
import com.example.mss.pojo.vo.SearchVo;
import com.example.mss.pojo.vo.UserVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** 搜索门面模式设计
 * @author pengYuJun
 */
@Component
@Slf4j
public class SearchFacade {
    @Resource
    private DataSourceRegistry dataSourceRegistry;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private PostDataSource postDataSource;


    public SearchVo searchAll(SearchDto searchDto, HttpServletRequest request) {
        SearchVo searchVo = new SearchVo();
        String searchText = searchDto.getSearchText();
        long current = searchDto.getCurrent();
        long pageSize = searchDto.getPageSize();
        String type = searchDto.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        if (searchTypeEnum == null) {
            Page<PictureVo> pictureVoPage = pictureDataSource.doSearch(searchText, current, pageSize);
            searchVo.setPictureList(pictureVoPage.getRecords());

            Page<UserVo> userVoPage = userDataSource.doSearch(searchText, current, pageSize);
            searchVo.setUserList(userVoPage.getRecords());

            Page<PostVo> postVoPage = postDataSource.doSearch(searchText, current, pageSize);
            searchVo.setPostList(postVoPage.getRecords());
        }else {
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            Page<?> page = dataSource.doSearch(searchText, current, pageSize);
            searchVo.setDataList(page.getRecords());
        }
        return searchVo;
    }
}
