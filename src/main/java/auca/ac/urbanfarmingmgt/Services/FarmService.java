package auca.ac.urbanfarmingmgt.Services;

import auca.ac.urbanfarmingmgt.Repository.FarmRepository;
import auca.ac.urbanfarmingmgt.Repository.CropRepository;
import auca.ac.urbanfarmingmgt.Repository.StaffAndVolunteerRepository;
import auca.ac.urbanfarmingmgt.Repository.SustainabilityMetricRepository;
import auca.ac.urbanfarmingmgt.Model.Crop;
import auca.ac.urbanfarmingmgt.Model.Farm;
import auca.ac.urbanfarmingmgt.Model.StaffAndVolunteer;
import auca.ac.urbanfarmingmgt.Model.SustainabilityMetric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FarmService {

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private StaffAndVolunteerRepository staffRepository;

    @Autowired
    private SustainabilityMetricRepository metricsRepository;

    // Save a farm
    public String saveFarm(Farm farm) {
        try {
            farmRepository.save(farm);
            return "Farm saved successfully with ID: " + farm.getFarmID();
        } catch (Exception e) {
            return "Failed to save farm: " + e.getMessage();
        }
    }

    // Get all farms
    public List<Farm> getAllFarms() {
        return farmRepository.findAll();
    }

    // Get a farm by ID
    public Farm getFarmById(Integer farmId) {
        Optional<Farm> farm = farmRepository.findById(farmId);
        return farm.orElse(null);
    }

    // Get farms by name containing a search term
    public List<Farm> getFarmsByName(String name) {
        return farmRepository.findByNameContaining(name);
    }

    // Delete a farm by ID
    public String deleteFarm(Integer farmId) {
        try {
            if (farmRepository.existsById(farmId)) {
                farmRepository.deleteById(farmId);
                return "Farm deleted successfully";
            } else {
                return "Farm not found with ID: " + farmId;
            }
        } catch (Exception e) {
            return "Failed to delete farm: " + e.getMessage();
        }
    }

    // Track crops for a specific farm
    public String trackCrops(Integer farmId, String cropType, Date plantingSchedule, boolean growingConditions) {
        try {
            Optional<Farm> farmOptional = farmRepository.findById(farmId);
            if (farmOptional.isPresent()) {
                Farm farm = farmOptional.get();

                // Check if a similar crop already exists
                for (Crop existingCrop : farm.getCrops()) {
                    if (existingCrop.getCropType().equals(cropType) &&
                            existingCrop.getPlantingSchedule().equals(plantingSchedule)) {
                        return "Crop with type: " + cropType + " and planting schedule: " + plantingSchedule + " already exists";
                    }
                }

                // Create and save a new crop
                Crop newCrop = Crop.builder()
                        .cropType(cropType)
                        .plantingSchedule(plantingSchedule)
                        .growingConditions(growingConditions)
                        .farm(farm)
                        .build();

                cropRepository.save(newCrop);
                return "Crop tracked successfully for farm: " + farm.getName();
            } else {
                return "Farm not found with ID: " + farmId;
            }
        } catch (Exception e) {
            return "Failed to track crop: " + e.getMessage();
        }
    }

    // Manage staff assignments for a farm
    public String manageStaff(Integer farmId, Integer staffId) {
        try {
            Optional<Farm> farmOptional = farmRepository.findById(farmId);
            Optional<StaffAndVolunteer> staffOptional = staffRepository.findById(staffId);

            if (farmOptional.isPresent() && staffOptional.isPresent()) {
                Farm farm = farmOptional.get();
                StaffAndVolunteer staff = staffOptional.get();

                // Assign the staff member to the farm
                staff.setAssignedFarm(farm);
                staffRepository.save(staff);

                return "Staff member assigned successfully to farm: " + farm.getName();
            } else {
                return farmOptional.isEmpty() ? "Farm not found" : "Staff member not found";
            }
        } catch (Exception e) {
            return "Failed to manage staff: " + e.getMessage();
        }
    }

    // Track sustainability metrics for a farm
    public String trackMetrics(Integer farmId, Integer metricsId) {
        try {
            Optional<Farm> farmOptional = farmRepository.findById(farmId);
            Optional<SustainabilityMetric> metricOptional = metricsRepository.findById(metricsId);

            if (farmOptional.isPresent() && metricOptional.isPresent()) {
                Farm farm = farmOptional.get();
                SustainabilityMetric metric = metricOptional.get();

                // Assign the metric to the farm
                metric.setFarm(farm);
                metricsRepository.save(metric);

                return "Sustainability metric added successfully to farm: " + farm.getName();
            } else {
                return farmOptional.isEmpty() ? "Farm not found" : "Metric not found";
            }
        } catch (Exception e) {
            return "Failed to track metrics: " + e.getMessage();
        }
    }

    // Calculate total yield for a farm
    public Double calculateTotalYield(Integer farmId) {
        Optional<Farm> farmOptional = farmRepository.findById(farmId);
        if (farmOptional.isPresent()) {
            Farm farm = farmOptional.get();
            double totalYield = 0.0;

            // Sum the average yield of all crops in the farm
            for (Crop crop : farm.getCrops()) {
                if (crop.getAverageYield() != null) {
                    totalYield += crop.getAverageYield();
                }
            }

            return totalYield;
        } else {
            return 0.0;
        }
    }

    // Get sustainability report for a farm
    public List<SustainabilityMetric> getSustainabilityReport(Integer farmId) {
        Optional<Farm> farmOptional = farmRepository.findById(farmId);
        if (farmOptional.isPresent()) {
            Farm farm = farmOptional.get();
            return farm.getSustainabilityMetrics();
        } else {
            return List.of();
        }
    }

    // Find farms by crop type
    public List<Farm> getFarmsByCropType(String cropType) {
        return farmRepository.findByCropType(cropType);
    }

    // Find farms by staff member ID
    public List<Farm> getFarmsByStaffId(Integer staffId) {
        return farmRepository.findByStaffId(staffId);
    }

    // Find farms by sustainability metric ID
    public List<Farm> getFarmsByMetricId(Integer metricId) {
        return farmRepository.findByMetricId(metricId);
    }


}