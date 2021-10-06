package org.prgrms.kdtjpa.domain.order;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ProxyTest {

    @Autowired
    private EntityManagerFactory emf;

    private String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // 주문 엔티티
        Order order = new Order();
        order.setUuid(uuid);
        order.setMemo("부재시 전화주세요.");
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.OPENED);

        entityManager.persist(order);

        // 회원 엔티티
        Member member = new Member();
        member.setName("jungmi park");
        member.setNickName("jummi");
        member.setAge(20);
        member.setAddress("서울시 동작구");
        member.setDescription("백엔드 개발자");

        member.addOrder(order); // 연관관계 편의 메소드 사용
        entityManager.persist(member);

        transaction.commit();
    }

    @Test
    void proxy() {
        EntityManager entityManager = emf.createEntityManager();

        Order order = entityManager.find(Order.class, uuid);
        Member member = order.getMember();  // member 객체는 proxy 객체이다.
        log.info("MEMBER USE BEFORE IS-LOADED: {}",
            emf.getPersistenceUnitUtil().isLoaded(member)); // proxy 객체인지 확인하는 메소드

        String nickName = member.getNickName(); // 실제 사용
        log.info("MEMBER USE AFTER IS-LOADED: {}", emf.getPersistenceUnitUtil().isLoaded(member));
    }
}
