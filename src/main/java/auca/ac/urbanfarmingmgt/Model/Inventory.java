package auca.ac.urbanfarmingmgt.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventoryID;
    private Double quantity;
    private boolean freshnessStatus;
    private String storageLocation;
    private Integer stock;
    private String produceType;

    @OneToMany(mappedBy = "inventory", fetch = FetchType.LAZY)
    private List<Harvest> harvestList;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    private List<Crop> cropList;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL)
    private List<Order> orderList;


    public void addOrder(Order order) {
        if (this.orderList == null) {
            this.orderList = new ArrayList<>();
        }
        this.orderList.add(order);
        order.setInventory(this);
    }
}