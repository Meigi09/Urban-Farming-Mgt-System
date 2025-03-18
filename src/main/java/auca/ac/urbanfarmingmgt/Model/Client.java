package auca.ac.urbanfarmingmgt.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientID;
    private String name;
    private String contactInfo;
    private String orderPreferences;
    private String paymentHistory;
}
