package cephei.dev.event_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Data
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
}
