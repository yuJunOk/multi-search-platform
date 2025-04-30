package com.example.mss.pojo.dto.search;

import com.example.mss.pojo.dto.PageDto;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author pengYuJun
 */
@Data
public class SearchDto extends PageDto implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 类型
     */
    private String type;

    @Serial
    private static final long serialVersionUID = 1L;
}
