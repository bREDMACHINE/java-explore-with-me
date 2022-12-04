package ru.practicum.explorewithme.repositories.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.Sort;
import ru.practicum.explorewithme.dto.State;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.repositories.EventsRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventsRepositoryImpl implements EventsRepositoryCustom {

    private final EntityManager entityManager;

    public List<Event> findAllEventsByAdmin(List<Integer> users,
                                     List<State> states,
                                     List<Integer> categories,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cr = cb.createQuery(Event.class);
        Root<Event> root = cr.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (rangeStart != null && rangeEnd != null) {
            predicates.add(cb.between(root.get("eventDate"), rangeStart, rangeEnd));
        }
        if (states.size() != 0) {
            predicates.add(root.get("state").in(states));
        }
        if (categories.size() != 0) {
            predicates.add(root.get("category").in(categories));
        }
        if (users.size() != 0) {
            predicates.add(root.get("initiator").in(users));
        }
        cr.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        TypedQuery<Event> q = entityManager.createQuery(cr);
        q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        q.setMaxResults(pageable.getPageSize());
        return q.getResultList();
    }

    @Override
    public List<Event> findAllEventsPublic(String text,
                                           List<Integer> categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Sort sort,
                                           Boolean onlyAvailable,
                                           Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cr = cb.createQuery(Event.class);
        Root<Event> root = cr.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(root.get("state").in(State.PUBLISHED));
        if (rangeStart != null && rangeEnd != null) {
            predicates.add(cb.between(root.get("eventDate"), rangeStart, rangeEnd));
        } else {
            predicates.add(cb.greaterThan(root.get("eventDate"), LocalDateTime.now()));
        }
        if (text != null) {
            predicates.add(
                    cb.or(
                            cb.like(root.get("annotation".toLowerCase()), "%" + text + "%".toLowerCase()),
                            cb.like(root.get("description".toLowerCase()), "%" + text + "%".toLowerCase())
                    )
            );
        }
        if (categories != null) {
            predicates.add(root.get("category").in(categories));
        }
        if (onlyAvailable) {
            predicates.add(cb.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
        }
        cr.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        if (sort.equals(Sort.EVENT_DATE)) {
            cr.orderBy(cb.asc(root.get("eventDate")));
        }
        TypedQuery<Event> q = entityManager.createQuery(cr);
        q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        q.setMaxResults(pageable.getPageSize());
        return q.getResultList();
    }
}
