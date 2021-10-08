package org.prgrms.kdtjpa.order.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prgrms.kdtjpa.domain.order.OrderRepository;
import org.prgrms.kdtjpa.domain.order.OrderStatus;
import org.prgrms.kdtjpa.order.dto.ItemDto;
import org.prgrms.kdtjpa.order.dto.ItemType;
import org.prgrms.kdtjpa.order.dto.MemberDto;
import org.prgrms.kdtjpa.order.dto.OrderDto;
import org.prgrms.kdtjpa.order.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void saveTest() {
        // given
        OrderDto orderDto = OrderDto.builder()
            .uuid(uuid)
            .memo("문앞 보관 해주세요.")
            .orderDatetime(LocalDateTime.now())
            .orderStatus(OrderStatus.OPENED)
            .memberDto(
                MemberDto.builder()
                    .name("강홍구")
                    .nickName("guppy.kang")
                    .address("서울시 동작구만 움직이면 쏜다.")
                    .age(33)
                    .description("---")
                    .build()
            )
            .orderItemDtos(List.of(
                OrderItemDto.builder()
                    .price(1000)
                    .quantity(100)
                    .itemDto(ItemDto.builder()
                        .type(ItemType.FOOD)
                        .chef("백종원")
                        .price(1000)
                        .build()
                    )
                    .build()
            ))
            .build();

        // when
        String savedUuid = orderService.save(orderDto);

        // then
        assertThat(savedUuid).isEqualTo(uuid);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @Test
    void findOneTest() throws NotFoundException {
        // given
        String orderUuid = uuid;

        // when
        OrderDto one = orderService.findOne(orderUuid);

        // then
        assertThat(one.getUuid()).isEqualTo(orderUuid);
    }

    @Test
    void findAllTest() {
        // given
        PageRequest page = PageRequest.of(0, 10);

        // when
        Page<OrderDto> all = orderService.findAll(page);

        // then
        assertThat(all.getTotalElements()).isEqualTo(1);
    }

}