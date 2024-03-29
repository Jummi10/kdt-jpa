package org.prgrms.kdtjpa.domain.order;

import static org.assertj.core.api.Assertions.*;
import static org.prgrms.kdtjpa.domain.order.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class AssociationMappingTest {

    @Autowired
    EntityManagerFactory emf;

    @Test
    void Member_Order_연관관계_테스트() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Member member = createMember("jummi");
        entityManager.persist(member);

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OPENED);
        order.setMemo("부재시 전화주세요.");
        order.setMember(member);
        // member.setOrders(Lists.newArrayList(order));    // 객체가 따로 참조할 수 있게 서로 set을 해주어야 한다.

        entityManager.persist(order);
        transaction.commit();

        // entityManager.clear();
        Order foundOrder = entityManager.find(Order.class, order.getUuid());

        log.info("{}", foundOrder.getMember().getNickName());    // 객체 그래프 탐색
        log.info("{}", foundOrder.getMember().getOrders().size());
        log.info("{}", order.getMember().getOrders().size());
        log.info("{}, {}", member.getOrders().get(0).getUuid(), order.getUuid());

        // entityManager.close();
    }

    @Test
    void Order_OrderItem_연관관계_매핑_테스트() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Member member = createMember("loopy");
        entityManager.persist(member);

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderStatus(OPENED);
        order.setOrderDatetime(LocalDateTime.now());
        order.setMember(member);
        entityManager.persist(order);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setPrice(1000);
        orderItem1.setQuantity(2);
        orderItem1.setOrder(order);
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setPrice(2000);
        orderItem2.setQuantity(3);
        orderItem2.setOrder(order);
        entityManager.persist(orderItem1);
        entityManager.persist(orderItem2);

        order.setOrderItems(Lists.newArrayList(orderItem1, orderItem2));

        transaction.commit();

        // then
        // entityManager.clear();
        OrderItem foundOrderItem1 = entityManager.find(OrderItem.class, orderItem1.getId());

        assertThat(foundOrderItem1.getOrder()).isNotNull();
        assertThat(foundOrderItem1.getOrder().getUuid()).isEqualTo(order.getUuid());

        assertThat(order.getOrderItems().size()).isEqualTo(2);
        assertThat(order.getOrderItems().get(0).getPrice()).isEqualTo(1000);
        assertThat(order.getOrderItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(order.getOrderItems().get(1).getPrice()).isEqualTo(2000);
        assertThat(order.getOrderItems().get(1).getQuantity()).isEqualTo(3);
    }

    @Test
    void Item_OrderItem_연관관계_매핑_테스트() {
        //given
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        int price = 5000;
        int stockQuantity = 10;
        Item item = new Food();
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        entityManager.persist(item);

        int quantity = 5;
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(price);
        orderItem.setQuantity(quantity);
        orderItem.setItem(item);
        entityManager.persist(orderItem);

        transaction.commit();

        //when
        // entityManager.clear();
        OrderItem foundOrderItem = entityManager.find(OrderItem.class, orderItem.getId());
        Item foundItem = foundOrderItem.getItem();

        //then
        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getPrice()).isEqualTo(price);
        assertThat(foundItem.getStockQuantity()).isEqualTo(stockQuantity);
        assertThat(foundItem.getOrderItems().size()).isEqualTo(1);
        assertThat(foundItem.getOrderItems().get(0).getQuantity()).isEqualTo(quantity);
    }

    private Member createMember(String nickname) {
        Member member = new Member();
        member.setName("jungmi park");
        member.setAddress("서울시 동작구");
        member.setAge(20);
        member.setNickName(nickname);
        member.setDescription("백엔드 개발자");
        return member;
    }

}
