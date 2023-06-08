package com.stadium.mystadium.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDao {

    @NotNull(message = "The section must be defined!")
    @Min(value = 1, message = "The section must be a positive number!")
    private Integer section;

    @NotNull(message = "The row must be defined!")
    @Min(value = 1, message = "The row must be a positive number!")
    private Integer row;

    @NotNull(message = "The seat must be defined!")
    @Min(value = 1, message = "The seat must be a positive number!")
    private Integer seat;
}
