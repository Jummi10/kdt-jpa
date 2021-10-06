package org.prgrms.kdtjpa.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class CustomerRepositoryTest {

    Customer customer;
    long id;
    String firstName, lastName;

    @Autowired
    CustomerRepository repository;

    @BeforeEach
    void setup() {
        id = 1L;
        firstName = "jungmi";
        lastName = "park";

        customer = new Customer();
        customer.setId(id);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
    }

    @Test
    public void 고객을_생성한다() {
        //when
        Customer saved = saveCustomer(customer);

        //then
        assertThat(saved.getId()).isEqualTo(id);
        assertThat(saved.getFirstName()).isEqualTo(firstName);
        assertThat(saved.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void 아이디로_고객을_조회한다() throws Exception {
        //given
        saveCustomer(customer);

        //when
        Optional<Customer> found = repository.findById(id);

        //then
        assertThat(found).isNotEmpty();
        Customer foundCustomer = found.get();
        assertThat(foundCustomer.getId()).isEqualTo(id);
        assertThat(foundCustomer.getFirstName()).isEqualTo(firstName);
        assertThat(foundCustomer.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void 고객_리스트를_조회한다() throws Exception {
        //given
        long id2 = 2L;
        String firstName2 = "suman", lastName2 = "lee";
        Customer customer2 = new Customer();
        customer2.setId(id2);
        customer2.setFirstName(firstName2);
        customer2.setLastName(lastName2);

        saveCustomer(customer);
        saveCustomer(customer2);

        //when
        List<Customer> all = repository.findAll();

        //then
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    public void 고객_이름을_변경한다() throws Exception {
        //given
        String newLastName = "kim";
        Customer saved = saveCustomer(this.customer);

        //when
        saved.setLastName(newLastName);
        saveCustomer(saved);    // update

        //then
        Customer found = repository.findById(id).get();
        assertThat(found.getLastName()).isEqualTo(newLastName);
    }

    @Test
    public void 고객을_삭제한다() throws Exception {
        //given
        Customer saved = saveCustomer(this.customer);

        //when
        repository.delete(saved);
        Optional<Customer> found = repository.findById(id);

        //then
        assertThat(found).isEmpty();
    }

    private Customer saveCustomer(Customer customer) {
        return repository.save(customer);
    }

}