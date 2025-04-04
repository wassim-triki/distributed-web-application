package tn.esprit.microservice.reclamation_service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;


@Service
public class   ReclamationService {

    private final ReclamationRepository reclamationRepository;

   /* public ReclamationService(ReclamationRepository reclamationRepository) {
        this.reclamationRepository = reclamationRepository;
    }*/
   public ReclamationService(ReclamationRepository reclamationRepository ) {
       this.reclamationRepository = reclamationRepository;
    }

    /*public Reclamation addReclamation(Reclamation reclamation) {
        // Default values if not set
        if (reclamation.getStatut() == null) {
            reclamation.setStatut("En attente");
        }
        if (reclamation.getDateReclamation() == null) {
            reclamation.setDateReclamation(new java.util.Date());
        }
        return reclamationRepository.save(reclamation);
    }*/

    public Reclamation addReclamation(Reclamation reclamation) {
        if (reclamation.getStatut() == null) {
            reclamation.setStatut("En attente");
        }
        if (reclamation.getDateReclamation() == null) {
            reclamation.setDateReclamation(new java.util.Date());
        }

        Reclamation saved = reclamationRepository.save(reclamation);
        return saved;
    }


    public Reclamation updateReclamation(int id, Reclamation updatedReclamation) {
        return reclamationRepository.findById(id).map(existing -> {
            existing.setDescription(updatedReclamation.getDescription());
            existing.setOrderId(updatedReclamation.getOrderId());
            existing.setType(updatedReclamation.getType());
            existing.setEmailClient(updatedReclamation.getEmailClient());
            existing.setStatut(updatedReclamation.getStatut());
            return reclamationRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Reclamation non trouvée avec ID: " + id));
    }

    public void deleteReclamation(int id) {
        if (!reclamationRepository.existsById(id)) {
            throw new RuntimeException("Reclamation non trouvée avec ID: " + id);
        }
        reclamationRepository.deleteById(id);
    }

    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public Optional<Reclamation> getReclamationById(int id) {
        return reclamationRepository.findById(id);
    }



    // filter reclamations by type
    // http://localhost:8083/reclamations/filter?type=PRODUIT_ENDOMMAGE
    public List<Reclamation> getReclamationsByType(TypeReclamation type) {
        return reclamationRepository.findByType(type);
    }


    // Get reclamation statistics
    //  http://localhost:8083/reclamations/stats
    public Map<String, Object> getReclamationStats() {
        List<Reclamation> all = reclamationRepository.findAll();

        long total = all.size();

        Map<String, Long> byType = all.stream()
                .collect(Collectors.groupingBy(r -> r.getType().toString(), Collectors.counting()));

        Map<String, Long> byStatus = all.stream()
                .collect(Collectors.groupingBy(Reclamation::getStatut, Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("byType", byType);
        stats.put("byStatus", byStatus);

        return stats;
    }






}
