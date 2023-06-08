package com.stadium.mystadium.controller;

import com.stadium.mystadium.dao.OrderDao;
import com.stadium.mystadium.dao.OrderDetailsDao;
import com.stadium.mystadium.service.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders management", description = "Manage orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Add an order", description = "Order tickets for a game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully place the order"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The game is not scheduled at requested date and time"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The order can't be made for a past date"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. There are not enough tickets available"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The fan doesn't exist"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The game doesn't exist"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The stadium doesn't exist")
    })
    public ResponseEntity<OrderDetailsDao> addOrder(@Parameter(description = "details about fan, game, stadium, date and time, number of tickets") @Valid @RequestBody OrderDao orderDao) {
        return ResponseEntity.ok(orderService.add(orderDao));
    }
}
