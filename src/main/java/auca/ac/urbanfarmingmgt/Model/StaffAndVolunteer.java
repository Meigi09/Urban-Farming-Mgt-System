package auca.ac.urbanfarmingmgt.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "staff_and_volunteers")
public class StaffAndVolunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer personID;
    private String name;
    private String role;
    private String assignedTask;
    private Double workHours;

    @ManyToOne
    @JoinColumn(name = "assigned_farm", nullable = true)
    private Farm assignedFarm;
}