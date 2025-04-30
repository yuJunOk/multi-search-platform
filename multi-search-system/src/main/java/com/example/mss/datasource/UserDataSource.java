package com.example.mss.datasource;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mss.common.ResponseCode;
import com.example.mss.exception.BusinessException;
import com.example.mss.mapper.UserMapper;
import com.example.mss.pojo.domain.UserDo;
import com.example.mss.pojo.dto.PageDto;
import com.example.mss.pojo.dto.user.UserDto;
import com.example.mss.pojo.vo.UserVo;
import com.example.mss.service.UserService;
import com.example.mss.utils.CommonUtils;
import com.example.mss.utils.MailUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.mss.constant.UserConstant.*;

/**
* @author pengYuJun
* @description 针对表【tb_user(用户表)】的数据库操作Service实现
* @createDate 2025-03-21 10:LOGIN_NAME_MIN_LEN6:02
*/
@Slf4j
@Service
public class UserDataSource implements DataSource<UserVo> {

    @Resource
    private UserService userService;

    @Override
    public Page<UserVo> doSearch(String searchText, long current, long pageSize) {
        PageDto pageDto = new PageDto();
        pageDto.setCurrent(current);
        pageDto.setPageSize(pageSize);

        UserDto userDto = new UserDto();
        userDto.setUserName(searchText);

        return userService.searchUser(userDto, pageDto);
    }
}




