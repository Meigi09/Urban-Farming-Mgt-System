package auca.ac.urbanfarmingmgt.Controller;

import auca.ac.urbanfarmingmgt.Services.StaffAndVolunteerService;
import auca.ac.urbanfarmingmgt.Model.StaffAndVolunteer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff-and-volunteers")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StaffAndVolunteerController {

    @Autowired
    private StaffAndVolunteerService staffAndVolunteerService;

    // Save a staff or volunteer
    @PostMapping
    public ResponseEntity<StaffAndVolunteer> saveStaffOrVolunteer(@RequestBody StaffAndVolunteer staffAndVolunteer) {
        StaffAndVolunteer savedPerson = staffAndVolunteerService.saveStaffOrVolunteer(staffAndVolunteer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }

    // Get all staff and volunteers
    @GetMapping
    public ResponseEntity<List<StaffAndVolunteer>> getAllStaffAndVolunteers() {
        List<StaffAndVolunteer> personnel = staffAndVolunteerService.getAllStaffAndVolunteers();
        return ResponseEntity.ok(personnel);
    }

    // Get a staff or volunteer by ID
    @GetMapping("/{id}")
    public ResponseEntity<StaffAndVolunteer> getStaffOrVolunteerById(@PathVariable Integer id) {
        Optional<StaffAndVolunteer> person = staffAndVolunteerService.getStaffOrVolunteerById(id);
        return person.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update a staff or volunteer
    @PutMapping("/{id}")
    public ResponseEntity<StaffAndVolunteer> updateStaffOrVolunteer(
            @PathVariable Integer id,
            @RequestBody StaffAndVolunteer updatedPerson) {
        Optional<StaffAndVolunteer> updated = staffAndVolunteerService.updateStaffOrVolunteer(id, updatedPerson);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete a staff or volunteer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaffOrVolunteer(@PathVariable Integer id) {
        boolean isDeleted = staffAndVolunteerService.deleteStaffOrVolunteer(id);
        if (isDeleted) {
            return ResponseEntity.ok("Staff or volunteer deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Staff or volunteer not found");
        }
    }


    // Get all staff (excluding volunteers)
    @GetMapping("/staff")
    public ResponseEntity<List<StaffAndVolunteer>> getAllStaff() {
        List<StaffAndVolunteer> staff = staffAndVolunteerService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    // Get all volunteers
    @GetMapping("/volunteers")
    public ResponseEntity<List<StaffAndVolunteer>> getAllVolunteers() {
        List<StaffAndVolunteer> volunteers = staffAndVolunteerService.getAllVolunteers();
        return ResponseEntity.ok(volunteers);
    }

    // Assign a staff or volunteer to a farm
    @PostMapping("/{personId}/assign-to-farm/{farmId}")
    public ResponseEntity<StaffAndVolunteer> assignToFarm(
            @PathVariable Integer personId,
            @PathVariable Integer farmId) {
        Optional<StaffAndVolunteer> assignedPerson = staffAndVolunteerService.assignToFarm(personId, farmId);
        return assignedPerson.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Assign a task to a staff or volunteer
    @PostMapping("/{personId}/assign-task")
    public ResponseEntity<StaffAndVolunteer> assignTask(
            @PathVariable Integer personId,
            @RequestParam String task) {
        Optional<StaffAndVolunteer> assignedPerson = staffAndVolunteerService.assignTask(personId, task);
        return assignedPerson.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get personnel assigned to a specific farm
    @GetMapping("/farm/{farmId}")
    public ResponseEntity<List<StaffAndVolunteer>> getPersonnelByFarm(@PathVariable Integer farmId) {
        List<StaffAndVolunteer> personnel = staffAndVolunteerService.getPersonnelByFarm(farmId);
        return ResponseEntity.ok(personnel);
    }

    // Calculate total work hours for a farm
    @GetMapping("/farm/{farmId}/total-work-hours")
    public ResponseEntity<Double> calculateTotalWorkHoursForFarm(@PathVariable Integer farmId) {
        Double totalWorkHours = staffAndVolunteerService.calculateTotalWorkHoursForFarm(farmId);
        return ResponseEntity.ok(totalWorkHours);
    }

    // Find staff and volunteers with minimum work hours
    @GetMapping("/minimum-work-hours/{minHours}")
    public ResponseEntity<List<StaffAndVolunteer>> findByMinimumWorkHours(@PathVariable Double minHours) {
        List<StaffAndVolunteer> personnel = staffAndVolunteerService.findByMinimumWorkHours(minHours);
        return ResponseEntity.ok(personnel);
    }

    // Find staff and volunteers with multiple tasks
    @GetMapping("/multiple-tasks")
    public ResponseEntity<List<StaffAndVolunteer>> findWithMultipleTasks() {
        List<StaffAndVolunteer> personnel = staffAndVolunteerService.findWithMultipleTasks();
        return ResponseEntity.ok(personnel);
    }

    // Get personnel distribution by role
    @GetMapping("/role-distribution")
    public ResponseEntity<Map<String, Long>> getPersonnelDistributionByRole() {
        Map<String, Long> distribution = staffAndVolunteerService.getPersonnelDistributionByRole();
        return ResponseEntity.ok(distribution);
    }

    // Update work hours for a staff or volunteer
    @PutMapping("/{personId}/update-work-hours")
    public ResponseEntity<StaffAndVolunteer> updateWorkHours(
            @PathVariable Integer personId,
            @RequestParam Double newHours) {
        Optional<StaffAndVolunteer> updatedPerson = staffAndVolunteerService.updateWorkHours(personId, newHours);
        return updatedPerson.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}