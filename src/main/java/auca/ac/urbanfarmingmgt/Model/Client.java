package auca.ac.urbanfarmingmgt.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientID;
    private String name;
    private String contactInfo;
    private String orderPreferences;
    private String paymentHistory;

    @ManyToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Order order;
}
