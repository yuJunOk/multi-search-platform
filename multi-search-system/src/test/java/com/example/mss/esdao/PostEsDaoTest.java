package com.example.mss.esdao;

import com.example.mss.pojo.dto.post.PostEsDto;
import com.example.mss.pojo.dto.post.PostQueryPageDto;
import com.example.mss.pojo.vo.PostVo;
import com.example.mss.service.PostService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PostEsDaoTest {
    @Resource
    private PostEsDao postEsDao;

    @Resource
    private PostService postService;

    @Test
    void test() {
        PostQueryPageDto postQueryPageDto = new PostQueryPageDto();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<PostVo> page =
                postService.searchFromEs(postQueryPageDto);
        System.out.println(page);
    }

    @Test
    void testSelect() {
        System.out.println(postEsDao.count());
        Page<PostEsDto> postEsDtoPage = postEsDao.findAll(
                PageRequest.of(0, 5, Sort.by("createTime")));
        List<PostEsDto> postList = postEsDtoPage.getContent();
        Optional<PostEsDto> byId = postEsDao.findById(1L);
        System.out.println(byId);
        System.out.println(postList);
    }

    @Test
    void testAdd() {
        PostEsDto postEsDto = new PostEsDto();
        postEsDto.setId(1L);
        postEsDto.setTitle("鱼皮是小黑子");
        postEsDto.setContent("鱼皮的知识星球：https://yupi.icu，直播带大家做项目");
        postEsDto.setTags(Arrays.asList("java", "python"));
        postEsDto.setUserId(1L);
        postEsDto.setCreateTime(new Date());
        postEsDto.setUpdateTime(new Date());
        postEsDto.setDeleted(0);
        postEsDao.save(postEsDto);
        System.out.println(postEsDto.getId());
    }

    @Test
    void testFindById() {
        Optional<PostEsDto> postEsDto = postEsDao.findById(1L);
        System.out.println(postEsDto);
    }

    @Test
    void testCount() {
        System.out.println(postEsDao.count());
    }

    @Test
    void testFindByCategory() {
        List<PostEsDto> postEsDaoTestList = postEsDao.findByUserId(1L);
        System.out.println(postEsDaoTestList);
    }

    @Test
    void testFindByTitle() {
        List<PostEsDto> postEsDtoS = postEsDao.findByTitle("鱼狗");
        System.out.println(postEsDtoS);
    }
}
