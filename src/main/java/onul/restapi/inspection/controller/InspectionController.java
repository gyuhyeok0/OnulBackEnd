package onul.restapi.inspection.controller;

import onul.restapi.inspection.entity.InspectionEntity;
import onul.restapi.inspection.service.InspectionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/inspection")
public class InspectionController {

    private final InspectionService inspectionService;

    @Autowired
    public InspectionController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @GetMapping(value = "/selectInspection", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InspectionEntity> getInspectionStatus() {
        InspectionEntity status = inspectionService.getInspectionStatus();
        return ResponseEntity.ok(status);
    }

}
