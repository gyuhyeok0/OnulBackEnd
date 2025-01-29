package onul.restapi.inspection.repository;

import onul.restapi.inspection.entity.InspectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InspectionRepository extends JpaRepository<InspectionEntity, Long> {
    Optional<InspectionEntity> findTopByOrderByUpdatedAtDesc();
}
