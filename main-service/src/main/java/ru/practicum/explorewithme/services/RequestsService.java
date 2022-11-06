package ru.practicum.explorewithme.services;

import ru.practicum.explorewithme.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestsService {

    List<ParticipationRequestDto> findAllRequestsByRequester(Long userId);

    List<ParticipationRequestDto> findAllRequestsByInitiator(Long userId, Long eventId);

    ParticipationRequestDto confirmRequestByInitiator(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto rejectRequestByInitiator(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto createRequestByRequester(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestByRequester(Long userId, Long requestId);
}
