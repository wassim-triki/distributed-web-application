package tn.esprit.microservice.customer;


// CustomerRepository.java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Customer> searchByNom(@Param("nom") String nom);

    @Query("SELECT c FROM Customer c WHERE c.telephone LIKE CONCAT('%', :tel, '%')")
    List<Customer> searchByTelephone(@Param("tel") String tel);
}
