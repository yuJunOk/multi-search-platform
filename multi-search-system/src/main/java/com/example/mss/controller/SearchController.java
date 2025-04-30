package com.example.mss.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mss.common.ResponseEntity;
import com.example.mss.pojo.dto.PageDto;
import com.example.mss.pojo.dto.post.PostQueryPageDto;
import com.example.mss.pojo.dto.search.SearchDto;
import com.example.mss.pojo.dto.user.UserDto;
import com.example.mss.pojo.enums.SearchTypeEnum;
import com.example.mss.pojo.vo.PictureVo;
import com.example.mss.pojo.vo.PostVo;
import com.example.mss.pojo.vo.SearchVo;
import com.example.mss.pojo.vo.UserVo;
import com.example.mss.service.PictureService;
import com.example.mss.service.PostService;
import com.example.mss.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pengYuJun
 */
@RestController
@RequestMapping("search")
public class SearchController {

    @Resource
    private PictureService pictureService;

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @PostMapping("all")
    public ResponseEntity<SearchVo> searchAll(@RequestBody SearchDto searchDto, HttpServletRequest request) {
        SearchVo searchVo = new SearchVo();

        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(searchDto.getType());
        if (searchTypeEnum == null) {
            Page<PictureVo> pictureVoPage = pictureService.searchPicture(searchDto.getSearchText(), searchDto.getCurrent(), searchDto.getPageSize());
            searchVo.setPictureList(pictureVoPage.getRecords());

            PageDto pageDto = new PageDto();
            pageDto.setCurrent(searchDto.getCurrent());
            pageDto.setPageSize(searchDto.getPageSize());
            UserDto userDto = new UserDto();
            userDto.setUserName(searchDto.getSearchText());
            Page<UserVo> userVoPage = userService.searchUser(userDto, pageDto);
            searchVo.setUserList(userVoPage.getRecords());

            PostQueryPageDto postQueryPageDto = new PostQueryPageDto();
            postQueryPageDto.setCurrent(searchDto.getCurrent());
            postQueryPageDto.setPageSize(searchDto.getPageSize());
            postQueryPageDto.setSearchText(searchDto.getSearchText());
            Page<PostVo> postVoPage = postService.listPostVoByPage(postQueryPageDto, request);
            searchVo.setPostList(postVoPage.getRecords());
        }else {
            switch (searchTypeEnum) {
                case PICTURE -> {
                    Page<PictureVo> pictureVoPage = pictureService.searchPicture(searchDto.getSearchText(), searchDto.getCurrent(), searchDto.getPageSize());
                    searchVo.setPictureList(pictureVoPage.getRecords());
                }
                case USER -> {
                    PageDto pageDto = new PageDto();
                    pageDto.setCurrent(searchDto.getCurrent());
                    pageDto.setPageSize(searchDto.getPageSize());
                    UserDto userDto = new UserDto();
                    userDto.setUserName(searchDto.getSearchText());
                    Page<UserVo> userVoPage = userService.searchUser(userDto, pageDto);
                    searchVo.setUserList(userVoPage.getRecords());
                }
                case POST -> {
                    PostQueryPageDto postQueryPageDto = new PostQueryPageDto();
                    postQueryPageDto.setCurrent(searchDto.getCurrent());
                    postQueryPageDto.setPageSize(searchDto.getPageSize());
                    postQueryPageDto.setSearchText(searchDto.getSearchText());
                    Page<PostVo> postVoPage = postService.listPostVoByPage(postQueryPageDto, request);
                    searchVo.setPostList(postVoPage.getRecords());
                }
                default -> {}
            }
        }
        return ResponseEntity.success(searchVo);
    }

}
