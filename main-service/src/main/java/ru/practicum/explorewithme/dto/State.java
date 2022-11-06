package ru.practicum.explorewithme.dto;

import java.util.Optional;

public enum State {

    PENDING, PUBLISHED, CANCELED;

    public static Optional<State> from(String stateString) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stateString)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
