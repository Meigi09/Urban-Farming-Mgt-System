package auca.ac.urbanfarmingmgt.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Farms")
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer farmID;
    private String name;
    private String location;
    private Double totalPlantingArea;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL)
    private List<Crop> crops;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL)
    private List<Harvest> harvests;

    @OneToMany(mappedBy = "assignedFarm", cascade = CascadeType.ALL)
    private List<StaffAndVolunteer> assignedStaff;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL)
    private List<SustainabilityMetric> sustainabilityMetrics;
}
