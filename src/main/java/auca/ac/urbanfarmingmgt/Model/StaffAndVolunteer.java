package auca.ac.urbanfarmingmgt.model;

import jakarta.persistence.*;

@Entity
@Table(name = "StaffAndVolunteers")
public class StaffAndVolunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personID;
    private String name;
    private String role;
    private Double workHours;

    @ManyToOne
    @JoinColumn(name = "assignedFarm", nullable = false)
    private Farm assignedFarm;
}
