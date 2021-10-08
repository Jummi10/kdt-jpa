package org.prgrms.kdtjpa.order.service;

import org.prgrms.kdtjpa.domain.order.Order;
import org.prgrms.kdtjpa.domain.order.OrderRepository;
import org.prgrms.kdtjpa.order.converter.OrderConverter;
import org.prgrms.kdtjpa.order.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javassist.NotFoundException;

@Service
public class OrderService {

    @Autowired
    OrderConverter orderConverter;
    @Autowired
    private OrderRepository orderRepository;

    @Transactional  // AOP - entityManager 관리 영역으로 묶어준다.
    public String save(OrderDto orderDto) {
        // tx.begin()   (X)
        // 1. dto -> entity 변환 (준영속)
        Order order = orderConverter.convertOrder(orderDto);
        // 2. orderRepository.save(entity) -> 영속화
        Order entity = orderRepository.save(order);
        // 3. 결과 반환. Transaction 밖으로 끌고나가는 것은 좋지 않음. => OSIV
        return entity.getUuid();    // entity는 RDB와 통신하는 객체, 원치않는 곳에서 엔티티 조작 방지.
        // tx.commit()   (X)
    }

    @Transactional
    public OrderDto findOne(String uuid) throws NotFoundException {
        // 1. 조회를 위한 키값 인자로 받기
        // 2. orderRepository.findById(uuid) -> 조회 (영속화된 엔티티)
        return orderRepository.findById(uuid)
            .map(orderConverter::convertOrderDto)  // 3. entity -> dto
            .orElseThrow(() -> new NotFoundException("주문을 찾을 수 없습니다."));
    }

    @Transactional
    public Page<OrderDto> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable)   // page query
            .map(orderConverter::convertOrderDto);
    }
}
