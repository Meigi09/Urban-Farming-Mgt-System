package auca.ac.urbanfarmingmgt.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Farms")
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer farmID;
    private String name;
    private String address;
    private Double totalPlantingArea;
    private String assignedStaff;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL)
    private List<Crop> crops;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL)
    private List<Harvest> harvests;

    @OneToMany(mappedBy = "assignedFarm", cascade = CascadeType.ALL)
    private List<StaffAndVolunteer> staffAndVolunteers;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL)
    private List<SustainabilityMetric> sustainabilityMetrics;
}
