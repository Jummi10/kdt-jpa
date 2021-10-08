package org.prgrms.kdtjpa.order.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prgrms.kdtjpa.domain.order.OrderStatus;
import org.prgrms.kdtjpa.order.dto.ItemDto;
import org.prgrms.kdtjpa.order.dto.ItemType;
import org.prgrms.kdtjpa.order.dto.MemberDto;
import org.prgrms.kdtjpa.order.dto.OrderDto;
import org.prgrms.kdtjpa.order.dto.OrderItemDto;
import org.prgrms.kdtjpa.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc   // mock mvc를 이용한 rest 호출 테스트를 쉽게 할 수 있다.
@SpringBootTest
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;    // Application을 띄우지 않고 rest controller end point를 호출해볼 수 있는 test

    @Autowired
    OrderService orderService;

    @Autowired
    ObjectMapper objectMapper;

    String uuid = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
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

    @Test
    void saveCallTest() throws Exception {
        // given - create RequestBody
        OrderDto orderDto = OrderDto.builder()
            .uuid(UUID.randomUUID().toString())
            .memo("문앞 보관 해주세요.")
            .orderDatetime(LocalDateTime.now())
            .orderStatus(OrderStatus.OPENED)
            .memberDto(
                MemberDto.builder()
                    .name("박정미")
                    .nickName("jummi")
                    .address("서울시 동작구")
                    .age(20)
                    .description("===")
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

        // when, then
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void getOneTest() throws Exception {
        mockMvc.perform(get("/orders/{uuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void getAllTest() throws Exception {
        mockMvc.perform(get("/orders")
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(10))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
