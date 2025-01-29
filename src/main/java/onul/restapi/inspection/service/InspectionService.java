package onul.restapi.inspection.service;

import onul.restapi.inspection.entity.InspectionEntity;
import onul.restapi.inspection.repository.InspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InspectionService {

    private final InspectionRepository inspectionRepository;

    @Autowired
    public InspectionService(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    public InspectionEntity getInspectionStatus() {
        return inspectionRepository.findTopByOrderByUpdatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No inspection status found"));
    }
}
