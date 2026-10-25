package cephei.dev.profile_service.repository;

import aj.org.objectweb.asm.commons.Remapper;
import cephei.dev.profile_service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByUserId(Integer userId);
}
