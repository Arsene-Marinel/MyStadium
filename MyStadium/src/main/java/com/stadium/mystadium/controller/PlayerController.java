package com.stadium.mystadium.controller;

import com.stadium.mystadium.dao.PlayerDao;
import com.stadium.mystadium.dao.PlayerDetailsDao;
import com.stadium.mystadium.service.PlayerServiceImpl;
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
@RequestMapping("/players")
@Tag(name = "Players management", description = "Manage players from database")
public class PlayerController {

    private final PlayerServiceImpl playerService;

    @Autowired
    public PlayerController(PlayerServiceImpl playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    @Operation(summary = "Get all players", description = "Get information about all players from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The players list.")
    })
    public ResponseEntity<List<PlayerDetailsDao>> getPlayers() {
        return ResponseEntity.ok(playerService.getPlayers());
    }

    @PostMapping
    @Operation(summary = "Add a player", description = "Add a player in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully add the player"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 players with the same name")
    })
    public ResponseEntity<PlayerDao> addPlayer(@Parameter(description = "player details") @Valid @RequestBody PlayerDao playerDao) {
        return ResponseEntity.ok(playerService.add(playerDao));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit a player", description = "Edit details of a player from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edit the player"),
            @ApiResponse(responseCode = "404", description = "Not found exception. There is no player having this id"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 players with the same name")
    })
    public ResponseEntity<PlayerDao> editPlayer(@Parameter(description = "the id of the player that will be edited") @PathVariable Long id,
                                              @Parameter(description = "the new details of the player") @Valid @RequestBody PlayerDao playerDao) {
        return ResponseEntity.ok(playerService.editPlayer(id, playerDao));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a player", description = "Delete a player from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edit the player"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The player play in a game and can't be deleted"),
            @ApiResponse(responseCode = "404", description = "Not found exception. There is no player having this id")
    })
    public ResponseEntity<PlayerDao> deletePlayer(@Parameter(description = "the id of the player that will be deleted") @PathVariable Long id) {
        return ResponseEntity.ok(playerService.deletePlayer(id));
    }
}
