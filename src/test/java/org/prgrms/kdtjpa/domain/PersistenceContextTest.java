package org.prgrms.kdtjpa.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest // Sprig Context
public class PersistenceContextTest {

    @Autowired
    CustomerRepository repository;

    @Autowired
    EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    public void 저장() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        persistCustomer(entityManager, transaction);
    }

    @Test
    public void 조회_DB조회() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Customer customer = persistCustomer(entityManager, transaction);

        entityManager.detach(customer); // 영속 -> 준영속 상태
        Customer found = entityManager.find(Customer.class, 1L);    // select 연산 후 customer를 다시 1차 캐시에 저장
        log.info("{} {}", found.getFirstName(), found.getLastName());

        entityManager.close();
    }

    @Test
    public void 조회_1차캐시_이용() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        persistCustomer(entityManager, transaction);

        Customer found = entityManager.find(Customer.class, 1L);    // 여전히 영속 상태, 1차 캐시에서 조회
        log.info("{} {}", found.getFirstName(), found.getLastName());

        entityManager.close();
    }

    @Test
    public void 수정() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Customer customer = persistCustomer(entityManager, transaction);

        transaction.begin();
        customer.setFirstName("suman");
        customer.setLastName("lee");
        transaction.commit();   // flush, 스냅샷과 비교해서 변경 감지(dirty checking)

        entityManager.close();
    }

    @Test
    public void 삭제() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        Customer customer = persistCustomer(entityManager, transaction);

        transaction.begin();
        entityManager.remove(customer); // 영속 -> 삭제
        transaction.commit();

        entityManager.close();
    }

    private Customer persistCustomer(EntityManager entityManager, EntityTransaction transaction) {
        // row의 변경이 있는 경우(insert, update, delete)는 begin()과 commit() 사이에서 작업
        transaction.begin();
        Customer customer = createCustomer();
        entityManager.persist(customer);    // 비영속 -> 영속 (영속화)
        transaction.commit();   // entityManager.flush(); 쓰기 지연 저장소의 쿼리 실행
        return customer;
    }

    private Customer createCustomer() {
        Customer customer = new Customer(); // 비영속 상태
        customer.setId(1L);
        customer.setFirstName("jungmi");
        customer.setLastName("park");
        return customer;
    }
}
