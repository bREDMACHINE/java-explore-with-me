package ru.practicum.explorewithme.mappers;

import ru.practicum.explorewithme.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.dto.NewEventDto;
import ru.practicum.explorewithme.models.Location;

public class LocationMapper {

    public static Location toLocation(NewEventDto.LocationDto locationDto) {
        Location location = new Location();
        location.setLon(locationDto.getLon());
        location.setLat(locationDto.getLat());
        return location;
    }

    public static Location toLocation(AdminUpdateEventRequest.LocationDto locationDto) {
        Location location = new Location();
        location.setLon(locationDto.getLon());
        location.setLat(locationDto.getLat());
        return location;
    }
}
