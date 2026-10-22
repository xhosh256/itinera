package cephei.dev.profile_service.repository;

import cephei.dev.profile_service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}
