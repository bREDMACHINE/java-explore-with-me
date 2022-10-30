package ru.practicum.explorewithme.mappers;

import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.dto.EventShortDto;
import ru.practicum.explorewithme.dto.NewCompilationDto;
import ru.practicum.explorewithme.models.Compilation;
import ru.practicum.explorewithme.models.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(events, compilation.getId(), compilation.getPinned(), compilation.getTitle());
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());
        return compilation;
    }
}
