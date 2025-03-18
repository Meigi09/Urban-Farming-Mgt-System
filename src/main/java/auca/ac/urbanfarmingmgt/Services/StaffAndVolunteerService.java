package auca.ac.urbanfarmingmgt.Services;

import auca.ac.urbanfarmingmgt.Repository.StaffAndVolunteerRepository;
import auca.ac.urbanfarmingmgt.Repository.FarmRepository;
import auca.ac.urbanfarmingmgt.Model.StaffAndVolunteer;
import auca.ac.urbanfarmingmgt.Model.Farm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffAndVolunteerService {

    @Autowired
    private StaffAndVolunteerRepository staffAndVolunteerRepository;

    @Autowired
    private FarmRepository farmRepository;

    // Save a new staff or volunteer
    public StaffAndVolunteer saveStaffOrVolunteer(StaffAndVolunteer staffAndVolunteer) {
        return staffAndVolunteerRepository.save(staffAndVolunteer);
    }

    // Retrieve all staff and volunteers
    public List<StaffAndVolunteer> getAllStaffAndVolunteers() {
        return staffAndVolunteerRepository.findAll();
    }

    // Retrieve a staff or volunteer by ID
    public Optional<StaffAndVolunteer> getStaffOrVolunteerById(Integer id) {
        return staffAndVolunteerRepository.findById(id);
    }

    // Update a staff or volunteer's details
    public Optional<StaffAndVolunteer> updateStaffOrVolunteer(Integer id, StaffAndVolunteer updatedPerson) {
        Optional<StaffAndVolunteer> existingPerson = staffAndVolunteerRepository.findById(id);

        if (existingPerson.isPresent()) {
            StaffAndVolunteer person = existingPerson.get();

            // Update fields
            person.setName(updatedPerson.getName());
            person.setRole(updatedPerson.getRole());
            person.setWorkHours(updatedPerson.getWorkHours());
            person.setAssignedTask(updatedPerson.getAssignedTask());

            // Update assigned farm if provided
            if (updatedPerson.getAssignedFarm() != null) {
                person.setAssignedFarm(updatedPerson.getAssignedFarm());
            }

            return Optional.of(staffAndVolunteerRepository.save(person));
        }

        return Optional.empty();
    }

    // Delete a staff or volunteer by ID
    public boolean deleteStaffOrVolunteer(Integer id) {
        if (staffAndVolunteerRepository.existsById(id)) {
            staffAndVolunteerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Retrieve all staff (excluding volunteers)
    public List<StaffAndVolunteer> getAllStaff() {
        return staffAndVolunteerRepository.findAll().stream()
                .filter(person -> person.getRole() != null && !person.getRole().toLowerCase().contains("volunteer"))
                .collect(Collectors.toList());
    }

    // Retrieve all volunteers
    public List<StaffAndVolunteer> getAllVolunteers() {
        return staffAndVolunteerRepository.findAll().stream()
                .filter(person -> person.getRole() != null && person.getRole().toLowerCase().contains("volunteer"))
                .collect(Collectors.toList());
    }

    // Assign a staff or volunteer to a farm
    public Optional<StaffAndVolunteer> assignToFarm(Integer personId, Integer farmId) {
        Optional<StaffAndVolunteer> person = staffAndVolunteerRepository.findById(personId);
        Optional<Farm> farm = farmRepository.findById(farmId);

        if (person.isPresent() && farm.isPresent()) {
            StaffAndVolunteer staffOrVolunteer = person.get();
            staffOrVolunteer.setAssignedFarm(farm.get());
            return Optional.of(staffAndVolunteerRepository.save(staffOrVolunteer));
        }

        return Optional.empty();
    }

    // Assign a task to a staff or volunteer
    public Optional<StaffAndVolunteer> assignTask(Integer personId, String task) {
        Optional<StaffAndVolunteer> person = staffAndVolunteerRepository.findById(personId);

        if (person.isPresent()) {
            StaffAndVolunteer staffOrVolunteer = person.get();
            staffOrVolunteer.setAssignedTask(task);
            return Optional.of(staffAndVolunteerRepository.save(staffOrVolunteer));
        }

        return Optional.empty();
    }

    // Retrieve all personnel assigned to a specific farm
    public List<StaffAndVolunteer> getPersonnelByFarm(Integer farmId) {
        Optional<Farm> farm = farmRepository.findById(farmId);

        if (farm.isPresent()) {
            return staffAndVolunteerRepository.findAll().stream()
                    .filter(person -> person.getAssignedFarm() != null &&
                            person.getAssignedFarm().getFarmID().equals(farmId))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    // Calculate the total work hours for personnel assigned to a farm
    public Double calculateTotalWorkHoursForFarm(Integer farmId) {
        List<StaffAndVolunteer> farmPersonnel = getPersonnelByFarm(farmId);

        return farmPersonnel.stream()
                .filter(person -> person.getWorkHours() != null)
                .mapToDouble(StaffAndVolunteer::getWorkHours)
                .sum();
    }

    // Retrieve personnel with work hours greater than or equal to the specified minimum
    public List<StaffAndVolunteer> findByMinimumWorkHours(Double minHours) {
        return staffAndVolunteerRepository.findAll().stream()
                .filter(person -> person.getWorkHours() != null && person.getWorkHours() >= minHours)
                .collect(Collectors.toList());
    }

    // Retrieve personnel with multiple tasks assigned
    public List<StaffAndVolunteer> findWithMultipleTasks() {
        return staffAndVolunteerRepository.findAll().stream()
                .filter(person -> person.getAssignedTask() != null &&
                        (person.getAssignedTask().contains(",") ||
                                person.getAssignedTask().contains(";")))
                .collect(Collectors.toList());
    }

    // Get the distribution of personnel by role
    public java.util.Map<String, Long> getPersonnelDistributionByRole() {
        return staffAndVolunteerRepository.findAll().stream()
                .filter(person -> person.getRole() != null)
                .collect(Collectors.groupingBy(
                        StaffAndVolunteer::getRole,
                        Collectors.counting()
                ));
    }

    // Update the work hours for a specific staff or volunteer
    public Optional<StaffAndVolunteer> updateWorkHours(Integer personId, Double newHours) {
        Optional<StaffAndVolunteer> person = staffAndVolunteerRepository.findById(personId);

        if (person.isPresent()) {
            StaffAndVolunteer staffOrVolunteer = person.get();
            staffOrVolunteer.setWorkHours(newHours);
            return Optional.of(staffAndVolunteerRepository.save(staffOrVolunteer));
        }

        return Optional.empty();
    }
}