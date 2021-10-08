package org.prgrms.kdtjpa.order.controller;

import org.prgrms.kdtjpa.order.ApiResponse;
import org.prgrms.kdtjpa.order.dto.OrderDto;
import org.prgrms.kdtjpa.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javassist.NotFoundException;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @ExceptionHandler(NotFoundException.class)  // 공통 응답에 대한 예외처리
    public ApiResponse<String> notFoundHandler(NotFoundException e) {
        return ApiResponse.fail(404, e.getMessage());
    }

    @ExceptionHandler(Exception.class)  // 공통 응답에 대한 예외처리
    public ApiResponse<String> internalServerErrorHandler(Exception e) {
        return ApiResponse.fail(500, e.getMessage());
    }

    @PostMapping("/orders")
    public ApiResponse<String> save(@RequestBody OrderDto orderDto) {
        String uuid = orderService.save(orderDto);
        return ApiResponse.ok(uuid);
    }

    @GetMapping("/orders/{uuid}")
    public ApiResponse<OrderDto> getOne(@PathVariable String uuid) throws NotFoundException {
        OrderDto one = orderService.findOne(uuid);
        return ApiResponse.ok(one);
    }

    @GetMapping("/orders")
    public ApiResponse<Page<OrderDto>> getAll(Pageable pageable) {
        Page<OrderDto> all = orderService.findAll(pageable);
        return ApiResponse.ok(all);
    }
}
