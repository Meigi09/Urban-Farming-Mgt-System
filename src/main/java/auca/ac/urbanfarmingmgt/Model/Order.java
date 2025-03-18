package auca.ac.urbanfarmingmgt.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderID;
    private Date orderDate;
    private Double quantityOrdered;
    private String deliveryStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Client> clientList;

    @ManyToOne
    @JoinColumn(name = "inventoryID", nullable = false)
    private Inventory inventory;
}
