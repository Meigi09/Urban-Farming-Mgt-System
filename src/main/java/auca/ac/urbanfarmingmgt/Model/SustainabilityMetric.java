package auca.ac.urbanfarmingmgt.model;

import jakarta.persistence.*;

@Entity
@Table(name = "SustainabilityMetrics")
public class SustainabilityMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer metricID;
    private Double waterUsage;
    private Double soilHealth;
    private Double pesticideApplication;
    private Double energyUsage;

    @ManyToOne
    @JoinColumn(name = "farmID", nullable = false)
    private Farm farm;

    @ManyToOne
    @JoinColumn(name = "cropID", nullable = false)
    private Crop crop;
}
