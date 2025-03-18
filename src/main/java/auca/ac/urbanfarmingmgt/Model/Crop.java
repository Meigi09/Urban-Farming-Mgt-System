package auca.ac.urbanfarmingmgt.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Crops")
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cropID;
    private String cropType;
    private Date plantingSchedule;
    private String growingConditions;
    private Double averageYield;

    @ManyToOne
    @JoinColumn(name = "farmID", nullable = false)
    private Farm farm;

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<Harvest> harvests;

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<Inventory> inventory;

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<SustainabilityMetric> sustainabilityMetrics;
}