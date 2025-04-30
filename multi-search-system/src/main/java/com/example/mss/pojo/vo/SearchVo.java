package com.example.mss.pojo.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author pengYuJun
 */
@Data
public class SearchVo implements Serializable {

    private List<UserVo> userList;

    private List<PostVo> postList;

    private List<PictureVo> pictureList;

    private List<?> dataList;

    @Serial
    private static final long serialVersionUID = 1L;
}
