package com.stadium.mystadium.controller;

import com.stadium.mystadium.dao.GameDetailsDao;
import com.stadium.mystadium.dao.GameScheduleDao;
import com.stadium.mystadium.dao.GamesFiltersDao;
import com.stadium.mystadium.dao.NewGameDao;
import com.stadium.mystadium.service.GameServiceImpl;
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
@RequestMapping("/games")
@Tag(name = "Games management", description = "Manage games from database")
public class GameController {

    private final GameServiceImpl gameService;

    @Autowired
    public GameController(GameServiceImpl gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @Operation(summary = "Add game", description = "Add a game in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully add the game"),
            @ApiResponse(responseCode = "404", description = "Not found exception. One of the players specified for this game doesn't exist in database"),
    })
    public ResponseEntity<GameDetailsDao> addGame(@Parameter(description = "game details") @Valid @RequestBody NewGameDao gameDao) {
        return ResponseEntity.ok(gameService.add(gameDao));
    }

    @PostMapping("/search")
    @Operation(summary = "Search games", description = "Search games in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found games"),
    })
    public ResponseEntity<List<GameDetailsDao>> searchMovies(@Parameter(description = "filter game after name, schedule date or competition") @RequestBody GamesFiltersDao gamesFiltersDao) {
        return ResponseEntity.ok(gameService.searchGames(gamesFiltersDao.getName(), gamesFiltersDao.getDate(), gamesFiltersDao.getCompetition()));
    }

    @PostMapping("/schedule")
    @Operation(summary = "Schedule a game", description = "Schedule a game according to defined parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully schedule the game"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The game can't be scheduled in a past date"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The stadium is not available at the requested time"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The game doesn't exist"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The stadium doesn't exist")
    })
    public ResponseEntity<GameScheduleDao> scheduleGame(@Parameter(description = "details of the game, the stadium and schedule date and time") @Valid @RequestBody GameScheduleDao gameScheduleDao) {
        return ResponseEntity.ok(gameService.scheduleGame(gameScheduleDao));
    }
}
