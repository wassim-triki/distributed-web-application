package tn.esprit.microservice.reclamation_service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ReclamationRepository  extends JpaRepository<Reclamation, Integer> {

    List<Reclamation> findByType(TypeReclamation type);

}
