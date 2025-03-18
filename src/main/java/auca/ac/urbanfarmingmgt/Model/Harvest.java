package auca.ac.urbanfarmingmgt.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Harvests")
public class Harvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer harvestID;
    private Date date;
    private Double yield;
    private Integer qualityRating;

    @ManyToOne
    @JoinColumn(name = "cropID", nullable = false)
    private Crop crop;

    @ManyToOne
    @JoinColumn(name = "farmID", nullable = false)
    private Farm farm;

    @ManyToOne
    @JoinColumn(name = "inventoryID", nullable = false)
    private Inventory inventory;
}

