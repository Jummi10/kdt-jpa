package org.prgrms.kdtjpa.domain.order;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.jupiter.api.Test;
import org.prgrms.kdtjpa.domain.parent.Parent;
import org.prgrms.kdtjpa.domain.parent.ParentId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ImproveMappingTest {

    @Autowired
    private EntityManagerFactory emf;

    @Test
    void inheritance_test() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Food food = new Food();
        food.setPrice(2000);
        food.setStockQuantity(200);
        food.setChef("백종원");
        entityManager.persist(food);

        transaction.commit();
        //entityManager.close();
    }

    @Test
    void mapped_super_class_test() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Order order = new Order();
        order.setUuid(UUID.randomUUID().toString());
        order.setOrderStatus(OrderStatus.OPENED);
        order.setMemo("---");
        order.setOrderDatetime(LocalDateTime.now());

        order.setCreatedBy("jummi");
        order.setCreatedAt(LocalDateTime.now());

        entityManager.persist(order);

        transaction.commit();
    }

    @Test
    void composite_key_test() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        String id1 = "ID1", id2 = "ID2";
        Parent parent = new Parent();
        parent.setId(new ParentId(id1, id2));

        entityManager.persist(parent);
        transaction.commit();

        entityManager.clear();
        Parent found = entityManager.find(Parent.class, new ParentId(id1, id2));// 식별자로 사용할 객체 전달
        log.info("{} {}", found.getId().getId1(), found.getId().getId2());
    }
}
