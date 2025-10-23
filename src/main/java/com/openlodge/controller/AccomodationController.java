package com.openlodge.controller;

import com.openlodge.dto.AccomodationDTO;
import com.openlodge.entities.Accomodation;
import com.openlodge.service.AccomodationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accomodations")
public class AccomodationController {

    private final AccomodationService accomodationService;

    public AccomodationController(AccomodationService accomodationService) {
        this.accomodationService = accomodationService;
    }

    @GetMapping
    public List<Accomodation> getAll() {
        return accomodationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Accomodation> getById(@PathVariable Long id) {
        Optional<Accomodation> accomodatOptional = accomodationService.findById(id);
        if (accomodatOptional.isPresent()) {
            return ResponseEntity.ok(accomodatOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Accomodation> create(@RequestBody AccomodationDTO dto) {
        return ResponseEntity.ok(accomodationService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Accomodation> update(@PathVariable Long id, @RequestBody AccomodationDTO dto) {
        return ResponseEntity.ok(accomodationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accomodationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
