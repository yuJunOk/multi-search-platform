package com.example.mss.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mss.common.ResponseCode;
import com.example.mss.exception.BusinessException;
import com.example.mss.pojo.vo.PictureVo;
import com.example.mss.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pengYuJun
 */
@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Page<PictureVo> searchPicture(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%s", searchText, current);
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.ERROR, "数据获取异常");
        }
        List<PictureVo> pictures = new ArrayList<>();
        Elements elements = doc.select(".iuscp.isv");
        for (Element element : elements) {
            // 取图片地址（murl）
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            pictures.add(new PictureVo(murl, title));
            if (pictures.size() >= pageSize) {
                break;
            }
        }
        Page<PictureVo> picturePage = new Page<>(pageNum, pageSize);
        picturePage.setRecords(pictures);
        return picturePage;
    }
}
