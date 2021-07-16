package com.sh303.circle.service;

import com.sh303.circle.CircleBootstrap;
import com.sh303.circle.api.DiscussPostService;
import com.sh303.circle.api.dto.DiscussPostDTO;
import com.sh303.common.domain.PageVO;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CircleBootstrap.class)
class DiscussPostServiceImplTest {

    @Reference
    private DiscussPostService discussPostService;

    @Test
    void selectDiscussPosts() {
        PageVO<DiscussPostDTO> discussPostDTOS = discussPostService.findDiscussPosts("101", 1, 10, 0);
        List<DiscussPostDTO> items = discussPostDTOS.getItems();
        Vector<DiscussPostDTO> vector = new Vector<DiscussPostDTO>();
        items.forEach(discussPostDTO -> {
            vector.add(discussPostDTO);
        });
        vector.forEach(discussPostDTO -> {
            System.out.println(discussPostDTO.toString());
        });
    }

    @Test
    void selectDiscussPostsRows() {
        System.out.println(discussPostService.findDiscussPostsRows("101"));
    }
}
