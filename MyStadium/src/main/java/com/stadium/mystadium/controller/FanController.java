package com.stadium.mystadium.controller;


import com.stadium.mystadium.dao.FanDao;
import com.stadium.mystadium.service.FanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fans")
@Tag(name = "Fans management", description = "Manage fans from database")
public class FanController {

    private final FanServiceImpl fanService;

    @Autowired
    public FanController(FanServiceImpl fanService) {
        this.fanService = fanService;
    }

    @PostMapping
    @Operation(summary = "Add fan", description = "Add a fan in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully add the fan"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 fans with the same email")
    })
    public ResponseEntity<FanDao> addFan(@Parameter(description = "fan details") @Valid @RequestBody FanDao fanDao) {
        return ResponseEntity.ok(fanService.add(fanDao));
    }

    @GetMapping
    @Operation(summary = "Search fans", description = "Search fans in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found fans"),
            @ApiResponse(responseCode = "404", description = "Not found exception. There are no fans whose name or email contain the value specified in search parameter")
    })
    public ResponseEntity<List<FanDao>> searchFans(@Parameter(description = "search param for name or email") @RequestParam(required = false) String searchParam) {
        return ResponseEntity.ok(fanService.searchFans(searchParam));
    }
}
