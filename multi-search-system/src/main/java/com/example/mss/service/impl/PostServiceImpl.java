package com.example.mss.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mss.constant.CommonConstant;
import com.example.mss.esdao.PostEsDao;
import com.example.mss.mapper.PostFavourMapper;
import com.example.mss.mapper.PostThumbMapper;
import com.example.mss.pojo.domain.PostDo;
import com.example.mss.pojo.domain.PostFavourDo;
import com.example.mss.pojo.domain.PostThumbDo;
import com.example.mss.pojo.domain.UserDo;
import com.example.mss.pojo.dto.post.PostEsDto;
import com.example.mss.pojo.dto.post.PostQueryPageDto;
import com.example.mss.pojo.vo.PostVo;
import com.example.mss.pojo.vo.UserVo;
import com.example.mss.service.PostService;
import com.example.mss.mapper.PostMapper;
import com.example.mss.service.UserService;
import com.example.mss.utils.SqlUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author pengYuJun
* @description 针对表【tb_post(帖子)】的数据库操作Service实现
* @createDate 2025-04-30 14:30:29
*/
@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, PostDo>
    implements PostService{

    @Resource
    private UserService userService;

    @Resource
    private PostThumbMapper postThumbMapper;

    @Resource
    private PostFavourMapper postFavourMapper;

    @Resource
    private PostEsDao postEsDao;

    @Override
    public Page<PostVo> searchFromEs(PostQueryPageDto postQueryPageDto) {
        // TODO: 在使用springboot3时候引入 spring-boot-starter-data-elasticsearch，无法使用BoolQueryBuilder和 ElasticsearchRestTemplate
        //https://docs.spring.io/spring-data/elasticsearch/reference/migration-guides.html 弃用了，es 哥更新太快，建议还是用 boot 2.x
        long current = Optional.ofNullable(postQueryPageDto.getCurrent()).orElse(1L);
        if (current > 0){
            // es分页从0开始
            current--;
        }
        long pageSize = Optional.ofNullable(postQueryPageDto.getPageSize()).orElse(10L);
        Pageable pageable = PageRequest.of(Math.toIntExact(current), Math.toIntExact(pageSize));
        String searchText = postQueryPageDto.getSearchText();
        org.springframework.data.domain.Page<PostEsDto> postEsDtoPage;
        if (StringUtils.isEmpty(searchText)){
            postEsDtoPage = postEsDao.findAll(pageable);
        }else {
            postEsDtoPage = postEsDao.searchByTitleOrContent(searchText, pageable);
        }
        List<PostEsDto> list = postEsDtoPage.stream().toList();
        Page<PostVo> postPage = new Page<>(current, pageSize, postEsDtoPage.getTotalElements());
        List<PostVo> postDoList = list.stream().map(dto -> {
            PostVo postVo = new PostVo();
            BeanUtils.copyProperties(dto, postVo);
            return postVo;
        }).toList();
        postPage.setRecords(postDoList);
        return postPage;
    }

    @Override
    public Page<PostVo> listPostVoByPage(PostQueryPageDto postQueryPageDto, HttpServletRequest request) {
        long current = postQueryPageDto.getCurrent();
        long pageSize = postQueryPageDto.getPageSize();
        Page<PostDo> postPage = this.page(new Page<>(current, pageSize), this.getQueryWrapper(postQueryPageDto));
        return this.getPostVoPage(postPage, request);
    }

    public Page<PostVo> getPostVoPage(Page<PostDo> postPage, HttpServletRequest request) {
        List<PostDo> postList = postPage.getRecords();
        Page<PostVo> postVoPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        if (CollectionUtils.isEmpty(postList)) {
            return postVoPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = postList.stream().map(PostDo::getUserId).collect(Collectors.toSet());
        Map<Long, List<UserDo>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(UserDo::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> postIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> postIdHasFavourMap = new HashMap<>();
        UserVo loginUser = userService.getCurrentUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> postIdSet = postList.stream().map(PostDo::getId).collect(Collectors.toSet());
            loginUser = userService.getCurrentUser(request);
            // 获取点赞
            QueryWrapper<PostThumbDo> postThumbQueryWrapper = new QueryWrapper<>();
            postThumbQueryWrapper.in("post_id", postIdSet);
            postThumbQueryWrapper.eq("user_id", loginUser.getId());
            List<PostThumbDo> postPostThumbList = postThumbMapper.selectList(postThumbQueryWrapper);
            postPostThumbList.forEach(postPostThumb -> postIdHasThumbMap.put(postPostThumb.getPostId(), true));
            // 获取收藏
            QueryWrapper<PostFavourDo> postFavourQueryWrapper = new QueryWrapper<>();
            postFavourQueryWrapper.in("post_id", postIdSet);
            postFavourQueryWrapper.eq("user_id", loginUser.getId());
            List<PostFavourDo> postFavourList = postFavourMapper.selectList(postFavourQueryWrapper);
            postFavourList.forEach(postFavour -> postIdHasFavourMap.put(postFavour.getPostId(), true));
        }
        // 填充信息
        List<PostVo> postVoList = postList.stream().map(post -> {
            PostVo postVo = PostVo.objToVo(post);
            Long userId = post.getUserId();
            UserDo user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            if (user != null) {
                UserVo userVo = new UserVo();
                BeanUtils.copyProperties(user, userVo);
                postVo.setUser(userVo);
            }
            postVo.setHasThumb(postIdHasThumbMap.getOrDefault(post.getId(), false));
            postVo.setHasFavour(postIdHasFavourMap.getOrDefault(post.getId(), false));
            return postVo;
        }).toList();
        postVoPage.setRecords(postVoList);
        return postVoPage;
    }

    public QueryWrapper<PostDo> getQueryWrapper(PostQueryPageDto postQueryRequest) {
        QueryWrapper<PostDo> queryWrapper = new QueryWrapper<>();
        if (postQueryRequest == null) {
            return queryWrapper;
        }
        String searchText = postQueryRequest.getSearchText();
        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();
        Long id = postQueryRequest.getId();
        String title = postQueryRequest.getTitle();
        String content = postQueryRequest.getContent();
        List<String> tagList = postQueryRequest.getTags();
        Long userId = postQueryRequest.getUserId();
        Long notId = postQueryRequest.getNotId();
        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("title", searchText).or().like("content", searchText);
        }
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        if (CollectionUtils.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




