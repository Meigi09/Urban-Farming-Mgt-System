package auca.ac.urbanfarmingmgt.Services;

import auca.ac.urbanfarmingmgt.Repository.CropRepository;
import auca.ac.urbanfarmingmgt.Repository.FarmRepository;
import auca.ac.urbanfarmingmgt.Repository.HarvestRepository;
import auca.ac.urbanfarmingmgt.Repository.SustainabilityMetricRepository;
import auca.ac.urbanfarmingmgt.Model.Crop;
import auca.ac.urbanfarmingmgt.Model.Farm;
import auca.ac.urbanfarmingmgt.Model.Harvest;
import auca.ac.urbanfarmingmgt.Model.SustainabilityMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CropService {

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private FarmRepository farmRepository;

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private SustainabilityMetricRepository metricsRepository;

    // Save a new crop
    @Transactional
    public void saveCrop(Crop crop) {
        try {
            cropRepository.save(crop);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save crop: " + e.getMessage(), e);
        }
    }

    // Get all crops
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    // Get a crop by ID
    public Crop getCropById(Integer cropId) {
        return cropRepository.findById(cropId)
                .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + cropId));
    }

    // Get crops by type
    public List<Crop> getCropsByType(String cropType) {
        return cropRepository.findByCropType(cropType);
    }

    // Delete a crop by ID
    @Transactional
    public void deleteCrop(Integer cropId) {
        try {
            if (!cropRepository.existsById(cropId)) {
                throw new RuntimeException("Crop not found with ID: " + cropId);
            }
            cropRepository.deleteById(cropId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete crop: " + e.getMessage(), e);
        }
    }

    // Check if a crop is eligible for planting in a specific farm
    public boolean isEligibleForPlanting(Integer farmId, String cropRequirement) {
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new RuntimeException("Farm not found with ID: " + farmId));

        String farmLocation = farm.getLocation();
        return farmLocation != null && farmLocation.contains(cropRequirement);
    }

    // Get crops eligible for a specific location
    public List<Crop> getCropsEligibleForLocation(String location) {
        return cropRepository.findCropsEligibleForLocation(location);
    }

    // Record a harvest for a crop
    @Transactional
    public Harvest recordHarvest(Integer cropId, Double yield, Integer qualityRating) {
        Crop crop = getCropById(cropId);
        Farm farm = crop.getFarm();

        Harvest harvest = Harvest.builder()
                .crop(crop)
                .farm(farm)
                .date(new Date())
                .yield(yield)
                .qualityRating(qualityRating)
                .inventory(crop.getInventory())
                .build();

        harvest = harvestRepository.save(harvest);

        // Update average yield for the crop
        updateCropAverageYield(crop);

        return harvest;
    }

    // Update the average yield of a crop
    private void updateCropAverageYield(Crop crop) {
        List<Harvest> harvests = harvestRepository.findByCropId(crop.getCropID());

        if (harvests.isEmpty()) {
            return;
        }

        double totalYield = harvests.stream()
                .mapToDouble(Harvest::getYield)
                .sum();

        double avgYield = totalYield / harvests.size();
        crop.setAverageYield(avgYield);
        cropRepository.save(crop);
    }

    // Record sustainability metrics for a crop
    @Transactional
    public SustainabilityMetric recordSustainabilityMetrics(Integer cropId, Double waterUsage,
                                                            Double soilHealth, Double pesticideApplication,
                                                            Double energyUsage) {
        Crop crop = getCropById(cropId);
        Farm farm = crop.getFarm();

        SustainabilityMetric metric = SustainabilityMetric.builder()
                .crop(crop)
                .farm(farm)
                .waterUsage(waterUsage)
                .soilHealth(soilHealth)
                .pesticideApplication(pesticideApplication)
                .energyUsage(energyUsage)
                .build();

        return metricsRepository.save(metric);
    }

    // Calculate sustainability score for a crop
    public Double calculateSustainabilityScore(Integer cropId) {
        Crop crop = getCropById(cropId);
        List<SustainabilityMetric> metrics = metricsRepository.findByCropId(crop.getCropID());

        if (metrics.isEmpty()) {
            return 0.0;
        }

        // Get most recent metrics
        SustainabilityMetric latest = metrics.getFirst();

        // Calculate score (example calculation - adjust weights as needed)
        double waterScore = Math.max(0, 100 - latest.getWaterUsage() * 10); // Lower water usage is better
        double soilScore = latest.getSoilHealth() * 10; // Higher soil health is better
        double pesticideScore = Math.max(0, 100 - latest.getPesticideApplication() * 20); // Lower pesticide use is better
        double energyScore = Math.max(0, 100 - latest.getEnergyUsage() * 5); // Lower energy use is better

        // Weighted average
        return (waterScore * 0.3 + soilScore * 0.3 + pesticideScore * 0.25 + energyScore * 0.15);
    }

    // Get crops by growing season
    public List<Crop> getCropsByGrowingSeason(String season) {
        return cropRepository.findByGrowingSeason(season);
    }

    // Get crops by location requirement
    public List<Crop> getCropsByLocationRequirement(String locationRequirement) {
        return cropRepository.findByLocationRequirement(locationRequirement);
    }

    // Get crops by farm ID
    public List<Crop> getCropsByFarm(Integer farmId) {
        return cropRepository.findByFarmFarmID(farmId);
    }

    // Get high-yielding crops
    public List<Crop> getHighYieldingCrops(Double minimumYield) {
        return cropRepository.findByAverageYieldGreaterThan(minimumYield);
    }

    // Update crop details
    @Transactional
    public String updateCrop(Crop crop) {
        try {
            if (!cropRepository.existsById(crop.getCropID())) {
                return "Crop not found with ID: " + crop.getCropID();
            }
            cropRepository.save(crop);
            return "Crop updated successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to update crop: " + e.getMessage(), e);
        }
    }

    // Assign a crop to a farm
    @Transactional
    public String assignCropToFarm(Integer cropId, Integer farmId) {
        try {
            Crop crop = getCropById(cropId);
            Farm farm = farmRepository.findById(farmId)
                    .orElseThrow(() -> new RuntimeException("Farm not found with ID: " + farmId));

            crop.setFarm(farm);
            cropRepository.save(crop);
            return "Crop successfully assigned to farm";
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign crop to farm: " + e.getMessage(), e);
        }
    }

    // Get crops by growing condition
    public List<Crop> getCropsByGrowingCondition(boolean condition) {
        return cropRepository.findByGrowingConditions(condition);
    }

    // Update crop planting schedule
    @Transactional
    public String updateCropPlantingSchedule(Integer cropId, Date newSchedule) {
        try {
            Crop crop = getCropById(cropId);
            crop.setPlantingSchedule(newSchedule);
            cropRepository.save(crop);
            return "Crop planting schedule updated successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to update crop planting schedule: " + e.getMessage(), e);
        }
    }
}