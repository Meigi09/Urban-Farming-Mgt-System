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
@Table(name = "Crops")
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cropID;
    private String cropType;
    private Date plantingSchedule;
    private boolean growingConditions;
    private Double averageYield;
    private String growingSeason;
    private String locationRequirement;


    @ManyToOne
    @JoinColumn(name = "farmID", nullable = false)
    private Farm farm;

    @ManyToOne
    @JoinColumn(name = "inventoryID", nullable = false)
    private Inventory inventory;

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<Harvest> harvests;


    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<SustainabilityMetric> sustainabilityMetrics;
}