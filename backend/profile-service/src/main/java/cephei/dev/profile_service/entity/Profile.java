package cephei.dev.profile_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "profiles")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, name = "user_id")
    private Integer userId;

    @Column(nullable = false)
    private String firstname;
    private String lastname;

    @Column(name = "birth_date")
    private LocalDate birthDate;
    private String bio;
}
