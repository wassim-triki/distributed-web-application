package tn.esprit.microservice.reclamation_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/reclamations")
public class ReclamationRestAPI {

    private final ReclamationService reclamationService;

    public ReclamationRestAPI(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

    @PostMapping
    public ResponseEntity<Reclamation> createReclamation(@RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.addReclamation(reclamation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reclamation> updateReclamation(@PathVariable int id, @RequestBody Reclamation reclamation) {
        return ResponseEntity.ok(reclamationService.updateReclamation(id, reclamation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable int id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Reclamation>> getReclamationById(@PathVariable int id) {
        return ResponseEntity.ok(reclamationService.getReclamationById(id));
    }

    // Optional: simple hello endpoint
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from Reclamation Microservice!";
    }

    // Optional: endpoint to get reclamations by type

    @GetMapping("/filter")
    public ResponseEntity<List<Reclamation>> getReclamationsByType(@RequestParam TypeReclamation type) {
        return ResponseEntity.ok(reclamationService.getReclamationsByType(type));
    }

    // Optional: endpoint to get reclamation statistics

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getReclamationStats() {
        return ResponseEntity.ok(reclamationService.getReclamationStats());
    }

    // Optional: endpoint to get monthly statistics

    @GetMapping("/stats/monthly")
    public ResponseEntity<Map<String, Long>> getMonthlyStats() {
        return ResponseEntity.ok(reclamationService.getMonthlyReclamationStats());
    }


}
