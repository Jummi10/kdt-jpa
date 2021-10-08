package org.prgrms.kdtjpa.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.prgrms.kdtjpa.domain.order.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String uuid;
    private LocalDateTime orderDatetime;
    private OrderStatus orderStatus;
    private String memo;

    private MemberDto memberDto;
    private List<OrderItemDto> orderItemDtos;
}
