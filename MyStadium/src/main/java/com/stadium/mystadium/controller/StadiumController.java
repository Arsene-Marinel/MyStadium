package com.stadium.mystadium.controller;

import com.stadium.mystadium.dao.StadiumDao;
import com.stadium.mystadium.dao.StadiumDetailsDao;
import com.stadium.mystadium.service.StadiumServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/stadiums")
@Tag(name = "Stadiums management", description = "Manage stadiums from database")
public class StadiumController {

    private final StadiumServiceImpl stadiumService;

    @Autowired
    public StadiumController(StadiumServiceImpl stadiumService) {
        this.stadiumService = stadiumService;
    }

    @GetMapping
    @Operation(summary = "Get all stadiums", description = "Get information about all stadiums from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The stadiums list.")
    })
    public ResponseEntity<List<StadiumDetailsDao>> getStadiums() {
        return ResponseEntity.ok(stadiumService.getStadiums());
    }

    @PostMapping
    @Operation(summary = "Add a stadium", description = "Add a stadium in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully add the stadium"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 stadiums with the same name")
    })
    public ResponseEntity<StadiumDao> addStadium(@Parameter(description = "stadium details") @Valid @RequestBody StadiumDao stadiumDao) {
        return ResponseEntity.ok(stadiumService.add(stadiumDao));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit a stadium", description = "Edit details of a stadium from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edit the stadium"),
            @ApiResponse(responseCode = "404", description = "Not found exception. There is no stadium having this id"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 stadiums with the same name")
    })
    public ResponseEntity<StadiumDao> editStadium(@Parameter(description = "the id of the stadium that will be edited") @PathVariable Long id,
                                            @Parameter(description = "the new details of the stadium") @Valid @RequestBody StadiumDao stadiumDao) {
        return ResponseEntity.ok(stadiumService.editStadium(id, stadiumDao));
    }
}
