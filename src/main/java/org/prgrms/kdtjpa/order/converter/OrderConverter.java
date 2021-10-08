package org.prgrms.kdtjpa.order.converter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.prgrms.kdtjpa.domain.order.Car;
import org.prgrms.kdtjpa.domain.order.Food;
import org.prgrms.kdtjpa.domain.order.Furniture;
import org.prgrms.kdtjpa.domain.order.Item;
import org.prgrms.kdtjpa.domain.order.Member;
import org.prgrms.kdtjpa.domain.order.Order;
import org.prgrms.kdtjpa.domain.order.OrderItem;
import org.prgrms.kdtjpa.order.dto.ItemDto;
import org.prgrms.kdtjpa.order.dto.ItemType;
import org.prgrms.kdtjpa.order.dto.MemberDto;
import org.prgrms.kdtjpa.order.dto.OrderDto;
import org.prgrms.kdtjpa.order.dto.OrderItemDto;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {   // dto <-> entity

    // dto -> entity
    public Order convertOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setUuid(orderDto.getUuid());
        order.setMemo(orderDto.getMemo());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setOrderDatetime(orderDto.getOrderDatetime());
        order.setCreatedAt(LocalDateTime.now());
        order.setCreatedBy(orderDto.getMemberDto().getName());

        order.setMember(this.convertMember(orderDto.getMemberDto()));
        this.convertOrderItems(orderDto)
            .forEach(order::addOrderItem);

        return order;
    }

    private Member convertMember(MemberDto memberDto) {
        Member member = new Member();
        member.setName(memberDto.getName());
        member.setNickName(memberDto.getNickName());
        member.setAge(memberDto.getAge());
        member.setAddress(memberDto.getAddress());
        member.setDescription(memberDto.getDescription());

        return member;
    }

    private List<OrderItem> convertOrderItems(OrderDto orderDto) {
        return orderDto.getOrderItemDtos().stream()
            .map(orderItemDto -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setPrice(orderItemDto.getPrice());
                orderItem.setQuantity(orderItemDto.getQuantity());
                orderItem.setItem(this.convertItem(orderItemDto.getItemDto()));
                return orderItem;
            })
            .collect(Collectors.toList());
    }

    private Item convertItem(ItemDto itemDto) {
        if (ItemType.FOOD.equals(itemDto.getType())) {
            Food food = new Food();
            food.setPrice(itemDto.getPrice());
            food.setStockQuantity(itemDto.getStockQuantity());
            food.setChef(itemDto.getChef());
            return food;
        }

        if (ItemType.FURNITURE.equals(itemDto.getType())) {
            Furniture furniture = new Furniture();
            furniture.setPrice(itemDto.getPrice());
            furniture.setStockQuantity(itemDto.getStockQuantity());
            furniture.setWidth(itemDto.getWidth());
            furniture.setHeight(itemDto.getHeight());
            return furniture;
        }

        if (ItemType.CAR.equals(itemDto.getType())) {
            Car car = new Car();
            car.setPrice(itemDto.getPrice());
            car.setStockQuantity(itemDto.getStockQuantity());
            car.setPower(itemDto.getPower());
            return car;
        }

        throw new IllegalArgumentException("잘못된 아이템 타입입니다.");
    }

    // entity -> dto
    public OrderDto convertOrderDto(Order order) {
        return OrderDto.builder()
            .uuid(order.getUuid())
            .memo(order.getMemo())
            .orderStatus(order.getOrderStatus())
            .orderDatetime(order.getOrderDatetime())
            .memberDto(this.convertMemberDto(order.getMember()))
            .orderItemDtos(order.getOrderItems().stream()
                .map(this::convertOrderItemDto)
                .collect(Collectors.toList())
            )
            .build();
    }

    private MemberDto convertMemberDto(Member member) {
        return MemberDto.builder()
            .id(member.getId())
            .name(member.getName())
            .nickName(member.getNickName())
            .age(member.getAge())
            .address(member.getAddress())
            .description(member.getDescription())
            .build();
    }

    private OrderItemDto convertOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
            .id(orderItem.getId())
            .price(orderItem.getPrice())
            .quantity(orderItem.getQuantity())
            .itemDto(this.convertItemDto(orderItem.getItem()))
            .build();
    }

    private ItemDto convertItemDto(Item item) {
        if (item instanceof Food) {
            return ItemDto.builder()
                .id(item.getId())
                .type(ItemType.FOOD)
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .chef(((Food)item).getChef())
                .build();
        }

        if (item instanceof Furniture) {
            return ItemDto.builder()
                .id(item.getId())
                .type(ItemType.FURNITURE)
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .width(((Furniture)item).getWidth())
                .height(((Furniture)item).getHeight())
                .build();
        }

        if (item instanceof Car) {
            return ItemDto.builder()
                .id(item.getId())
                .type(ItemType.CAR)
                .price(item.getPrice())
                .stockQuantity(item.getStockQuantity())
                .power(((Car)item).getPower())
                .build();
        }

        throw new IllegalArgumentException("잘못된 아이템 타입입니다.");
    }
}
