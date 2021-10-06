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
class MemberTest {

    @Autowired
    EntityManagerFactory emf;

    @Test
    public void member_insert() {
        Member member = createMember("jummi");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        entityManager.persist(member);
        transaction.commit();

        entityManager.close();
    }

    @Test
    void 잘못된_설계() {
        Member member = createMember("loopy");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        entityManager.persist(member);
        Member memberEntity = entityManager.find(Member.class, 1L); // 영속화된 회원

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OPENED);
        order.setMemo("부재시 전화주세요.");
        order.setMemberId(memberEntity.getId());    // 외래키 직접 지정

        entityManager.persist(order);
        transaction.commit();

        // ERD 중심의 설계
        Order orderEntity = entityManager.find(Order.class, order.getUuid());   // select orders
        // FK를 이용해 회원 다시 조회
        Member orderMemberEntity = entityManager.find(Member.class, orderEntity.getMemberId()); // select member
        // orderEntity.getMember()  // 객체중심 설계라면 객체그래프 탐색을 해야하지 않을까?
        log.info("nickname: {}", orderMemberEntity.getNickName());

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
