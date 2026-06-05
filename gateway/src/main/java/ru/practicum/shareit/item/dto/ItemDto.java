package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @NotBlank
    @NotNull
    private String name;
    @NotNull
    @NotBlank
    private String description;
    private Boolean available;
    private Long requestId;
}
