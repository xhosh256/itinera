package cephei.dev.event_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "host_id", nullable = false)
    private Integer hostId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @ElementCollection
    @Builder.Default
    @CollectionTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "user_id")
    private Set<Integer> participantIds = new HashSet<>();
}
