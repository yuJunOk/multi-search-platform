package com.example.mss.pojo.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author pengYuJun
 */
@Data
public class IdDto implements Serializable {
    /**
     * id
     */
    private Long id;

    @Serial
    private static final long serialVersionUID = 1L;
}
