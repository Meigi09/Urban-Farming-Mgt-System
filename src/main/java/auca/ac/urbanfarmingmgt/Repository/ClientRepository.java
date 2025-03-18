package auca.ac.urbanfarmingmgt.Repository;

import auca.ac.urbanfarmingmgt.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findByContactInfo(String contactInfo);
}
