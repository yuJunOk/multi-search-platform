package com.example.mss.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author pengYuJun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureVo  implements Serializable {

    private String title;

    private String url;

    @Serial
    private static final long serialVersionUID = 1L;
}
