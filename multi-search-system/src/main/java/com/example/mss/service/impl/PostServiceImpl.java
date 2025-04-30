package com.example.mss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mss.pojo.domain.PostDo;
import com.example.mss.service.PostService;
import com.example.mss.mapper.PostMapper;
import org.springframework.stereotype.Service;

/**
* @author pengYuJun
* @description 针对表【tb_post(帖子)】的数据库操作Service实现
* @createDate 2025-04-30 14:30:29
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, PostDo>
    implements PostService{

}




