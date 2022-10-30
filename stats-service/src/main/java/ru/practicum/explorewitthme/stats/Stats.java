package ru.practicum.explorewitthme.stats;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "stats", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @Column(name = "date_time_request")
    private LocalDateTime dateTimeRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Stats stats = (Stats) o;
        return id != null && Objects.equals(id, stats.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
