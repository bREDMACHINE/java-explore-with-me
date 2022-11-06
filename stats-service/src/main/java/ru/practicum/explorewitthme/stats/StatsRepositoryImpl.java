package ru.practicum.explorewitthme.stats;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class StatsRepositoryImpl implements StatsRepositoryCustom {

    private final EntityManager entityManager;

    public StatsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Stats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Stats> cr = cb.createQuery(Stats.class);
        Root<Stats> root = cr.from(Stats.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(root.get("uri").in(uris));
        predicates.add(cb.between(root.get("dateTimeRequest"), start, end));
        cr.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
        return entityManager.createQuery(cr).getResultList();
    }
}