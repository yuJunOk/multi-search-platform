package com.example.mss.datasource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mss.constant.CommonConstant;
import com.example.mss.mapper.PostFavourMapper;
import com.example.mss.mapper.PostMapper;
import com.example.mss.mapper.PostThumbMapper;
import com.example.mss.pojo.domain.PostDo;
import com.example.mss.pojo.domain.PostFavourDo;
import com.example.mss.pojo.domain.PostThumbDo;
import com.example.mss.pojo.domain.UserDo;
import com.example.mss.pojo.dto.post.PostQueryDto;
import com.example.mss.pojo.dto.post.PostQueryPageDto;
import com.example.mss.pojo.vo.PostVo;
import com.example.mss.pojo.vo.UserVo;
import com.example.mss.service.PostService;
import com.example.mss.service.UserService;
import com.example.mss.utils.SqlUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author pengYuJun
* @description 针对表【tb_post(帖子)】的数据库操作Service实现
* @createDate 2025-04-30 14:30:29
*/
@Service
public class PostDataSource implements DataSource<PostVo> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVo> doSearch(String searchText, long current, long pageSize) {
        PostQueryPageDto postQueryRequest = new PostQueryPageDto();
        postQueryRequest.setSearchText(searchText);
        postQueryRequest.setCurrent(current);
        postQueryRequest.setPageSize(pageSize);

        ServletRequestAttributes servletRequestAttributes =  (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        return postService.listPostVoByPage(postQueryRequest, request);
    }
}




