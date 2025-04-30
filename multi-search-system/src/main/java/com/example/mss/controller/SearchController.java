package com.example.mss.controller;

import com.example.mss.common.ResponseEntity;
import com.example.mss.manager.SearchFacade;
import com.example.mss.pojo.dto.search.SearchDto;
import com.example.mss.pojo.vo.SearchVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author pengYuJun
 */
@RestController
@RequestMapping("search")
public class SearchController {

    @Resource
    private SearchFacade searchFacade;

    @GetMapping("all")
    public ResponseEntity<SearchVo> searchAll(@RequestBody SearchDto searchDto, HttpServletRequest request) {
        return ResponseEntity.success(searchFacade.searchAll(searchDto, request));
    }

}
