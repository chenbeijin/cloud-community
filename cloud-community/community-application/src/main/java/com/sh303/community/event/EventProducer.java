package com.sh303.community.event;

import com.sh303.community.entity.Event;
import org.springframework.stereotype.Component;

/**
 * @program: cloud-community
 * @description: 生产者.
 * @author: Chen Bei Jin
 * @create: 2021-08-16 09:15
 */

@Component
public class EventProducer {

    /*@Autowired
    private KafkaTemplate kafkaTemplate;*/

    /**
     * 处理事件.
     * @param event
     */
    public void fireEvent(Event event) {
        /*// 将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));*/
    }

}
