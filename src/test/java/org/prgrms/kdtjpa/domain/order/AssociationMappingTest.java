package org.prgrms.kdtjpa.domain.order;

import static org.prgrms.kdtjpa.domain.order.OrderStatus.*;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

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

        entityManager.clear();
        Order foundOrder = entityManager.find(Order.class, order.getUuid());

        log.info("{}", foundOrder.getMember().getNickName());    // 객체 그래프 탐색
        log.info("{}", foundOrder.getMember().getOrders().size());
        log.info("{}", order.getMember().getOrders().size());
        log.info("{}, {}", member.getOrders().get(0).getUuid(), order.getUuid());

        entityManager.close();
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
