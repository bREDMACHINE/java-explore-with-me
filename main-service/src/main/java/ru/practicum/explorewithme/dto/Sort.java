package ru.practicum.explorewithme.dto;

import java.util.Optional;

public enum Sort {
    EVENT_DATE, VIEWS;

    public static Optional<Sort> from(String sortParam) {
        for (Sort sort : values()) {
            if (sort.name().equalsIgnoreCase(sortParam)) {
                return Optional.of(sort);
            }
        }
        return Optional.empty();
    }
}
