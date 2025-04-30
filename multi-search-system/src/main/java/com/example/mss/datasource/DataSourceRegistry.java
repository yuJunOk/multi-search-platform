package com.example.mss.datasource;

import com.example.mss.pojo.enums.SearchTypeEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** 注册模式
 * @author pengYuJun
 */
@Component
public class DataSourceRegistry {

    @Resource
    private PostDataSource postDataSource;

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    private Map<String, DataSource<?>> typeDataSourceMap;

    @PostConstruct
    public void doInit() {
        HashMap<String, DataSource<?>> hashMap = new HashMap<>(3) {{
            put(SearchTypeEnum.POST.getValue(), postDataSource);
            put(SearchTypeEnum.USER.getValue(), userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
        }};
        typeDataSourceMap = Collections.unmodifiableMap(hashMap);
    }

    public DataSource<?> getDataSourceByType(String type) {
        if (typeDataSourceMap == null) {
            return null;
        }
        return typeDataSourceMap.get(type);
    }
}