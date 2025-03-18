package auca.ac.urbanfarmingmgt.Services;

import auca.ac.urbanfarmingmgt.Repository.CropRepository;
import auca.ac.urbanfarmingmgt.Repository.FarmRepository;
import auca.ac.urbanfarmingmgt.Repository.HarvestRepository;
import auca.ac.urbanfarmingmgt.Repository.SustainabilityMetricRepository;
import auca.ac.urbanfarmingmgt.model.Crop;
import auca.ac.urbanfarmingmgt.model.Farm;
import auca.ac.urbanfarmingmgt.model.Harvest;
import auca.ac.urbanfarmingmgt.model.SustainabilityMetric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    /**
     * Save a new crop or update an existing one
     * @param crop The crop entity to save
     * @return A message indicating the result of the operation
     */
    public String saveCrop(Crop crop) {
        try {
            cropRepository.save(crop);
            return "Crop saved successfully with ID: " + crop.getCropID();
        } catch (Exception e) {
            return "Failed to save crop: " + e.getMessage();
        }
    }

    /**
     * Get all crops in the system
     * @return List of all crops
     */
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }

    /**
     * Find a crop by its ID
     * @param cropId The ID of the crop to retrieve
     * @return The crop if found, otherwise null
     */
    public Crop getCropById(Integer cropId) {
        Optional<Crop> crop = cropRepository.findById(cropId);
        return crop.orElse(null);
    }

    /**
     * Find crops by type
     * @param cropType The type of crop to search for
     * @return List of crops matching the type
     */
    public List<Crop> getCropsByType(String cropType) {
        return cropRepository.findByCropType(cropType);
    }

    /**
     * Delete a crop by its ID
     * @param cropId The ID of the crop to delete
     * @return A message indicating the result of the operation
     */
    public String deleteCrop(Integer cropId) {
        try {
            if (cropRepository.existsById(cropId)) {
                cropRepository.deleteById(cropId);
                return "Crop deleted successfully";
            } else {
                return "Crop not found with ID: " + cropId;
            }
        } catch (Exception e) {
            return "Failed to delete crop: " + e.getMessage();
        }
    }

    /**
     * Determine planting eligibility based on farm location and crop requirements
     * @param farmId The ID of the farm
     * @param schedule The planned planting schedule
     * @param cropLocationRequirements The location requirements for the crop
     * @return A message indicating eligibility
     */
    public String plantEligibility(Integer farmId, Date schedule, String cropLocationRequirements) {
        try {
            Optional<Farm> farmOptional = farmRepository.findById(farmId);
            if (farmOptional.isPresent()) {
                Farm farm = farmOptional.get();
                String farmLocation = farm.getLocation();

                // Simple check for location compatibility
                if (farmLocation != null && farmLocation.contains(cropLocationRequirements)) {
                    return "The crop is eligible for planting at " + farmLocation + " on schedule: " + schedule;
                } else {
                    return "The crop is not suitable for planting at " + farmLocation +
                            ". Required conditions: " + cropLocationRequirements;
                }
            } else {
                return "Farm not found with ID: " + farmId;
            }
        } catch (Exception e) {
            return "Failed to check planting eligibility: " + e.getMessage();
        }
    }

    /**
     * Link a harvest to a crop
     * @param cropId The ID of the crop
     * @param yield The yield from the harvest
     * @param qualityRating The quality rating of the harvest
     * @return A message indicating the result of the operation
     */
    public String linkHarvest(Integer cropId, Double yield, Integer qualityRating) {
        try {
            Optional<Crop> cropOptional = cropRepository.findById(cropId);
            if (cropOptional.isPresent()) {
                Crop crop = cropOptional.get();
                Farm farm = crop.getFarm();

                Harvest harvest = Harvest.builder()
                        .crop(crop)
                        .farm(farm)
                        .date(new Date()) // Current date for the harvest
                        .yield(yield)
                        .qualityRating(qualityRating)
                        .build();

                // Set inventory if needed (would require additional inventory service)
                // harvest.setInventory(inventoryService.getDefaultInventory());

                harvestRepository.save(harvest);

                // Update average yield for the crop
                double totalYield = 0.0;
                int count = 0;

                for (Harvest h : crop.getHarvests()) {
                    totalYield += h.getYield();
                    count++;
                }

                // Include the new harvest
                totalYield += yield;
                count++;

                double avgYield = totalYield / count;
                crop.setAverageYield(avgYield);
                cropRepository.save(crop);

                return "Harvest linked successfully to crop: " + crop.getCropType();
            } else {
                return "Crop not found with ID: " + cropId;
            }
        } catch (Exception e) {
            return "Failed to link harvest: " + e.getMessage();
        }
    }

    /**
     * Monitor sustainability metrics for a crop
     * @param cropId The ID of the crop
     * @param waterUsage Water usage metric
     * @param soilHealth Soil health metric
     * @param pesticideApplication Pesticide application metric
     * @param energyUsage Energy usage metric
     * @return A message indicating the result of the operation
     */
    public String monitorMetrics(Integer cropId, Double waterUsage, Double soilHealth,
                                 Double pesticideApplication, Double energyUsage) {
        try {
            Optional<Crop> cropOptional = cropRepository.findById(cropId);
            if (cropOptional.isPresent()) {
                Crop crop = cropOptional.get();
                Farm farm = crop.getFarm();

                SustainabilityMetric metric = SustainabilityMetric.builder()
                        .crop(crop)
                        .farm(farm)
                        .waterUsage(waterUsage)
                        .soilHealth(soilHealth)
                        .pesticideApplication(pesticideApplication)
                        .energyUsage(energyUsage)
                        .build();

                metricsRepository.save(metric);

                return "Sustainability metrics recorded successfully for crop: " + crop.getCropType();
            } else {
                return "Crop not found with ID: " + cropId;
            }
        } catch (Exception e) {
            return "Failed to monitor metrics: " + e.getMessage();
        }
    }

    /**
     * Calculate estimated yield for a crop based on historical data
     * @param cropId The ID of the crop
     * @return The estimated yield for the crop
     */
    public Double calculateEstimatedYield(Integer cropId) {
        Optional<Crop> cropOptional = cropRepository.findById(cropId);
        if (cropOptional.isPresent()) {
            Crop crop = cropOptional.get();
            return crop.getAverageYield();
        } else {
            return 0.0;
        }
    }

    /**
     * Get all crops for a specific growing season
     * @param season The growing season to filter by
     * @return List of crops for the specified season
     */
    public List<Crop> getCropsByGrowingSeason(String season) {
        return cropRepository.findAll().stream()
                .filter(crop -> season.equals(crop.getGrowingSeason()))
                .toList();
    }
}