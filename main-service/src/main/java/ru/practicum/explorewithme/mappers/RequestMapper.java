package ru.practicum.explorewithme.mappers;

import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.State;
import ru.practicum.explorewithme.dto.Status;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.models.Request;
import ru.practicum.explorewithme.models.User;

import java.time.LocalDateTime;

public class RequestMapper {
    public static ParticipationRequestDto toRequestDto(Request request) {
        return new ParticipationRequestDto(request.getCreated(),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public static Request toRequest(User user, Event event) {
        Request request = new Request();
        request.setStatus(Status.PENDING);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        return request;
    }
}
