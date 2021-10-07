package org.prgrms.kdtjpa.domain.order;

import static org.prgrms.kdtjpa.domain.order.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Test
    void test() {
        String uuid = UUID.randomUUID().toString();
        String memo = "----";

        Order order = new Order();  // 준영속 상태
        order.setUuid(uuid);
        order.setOrderStatus(OPENED);
        order.setOrderDatetime(LocalDateTime.now());
        order.setMemo(memo);
        order.setCreatedBy("jungmi.park");
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);    // entityManage ~ transaction ~ flush

        Order order1 = orderRepository.findById(uuid).get();
        List<Order> all = orderRepository.findAll();

        orderRepository.findAllByOrderStatus(OPENED);
        orderRepository.findAllByOrderStatusOrderByOrderDatetime(OPENED);

        orderRepository.findByMemo(memo);
    }

}