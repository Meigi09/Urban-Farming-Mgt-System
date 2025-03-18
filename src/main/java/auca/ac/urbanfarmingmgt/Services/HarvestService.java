package auca.ac.urbanfarmingmgt.Services;

import auca.ac.urbanfarmingmgt.Repository.CropRepository;
import auca.ac.urbanfarmingmgt.Repository.HarvestRepository;
import auca.ac.urbanfarmingmgt.Repository.InventoryRepository;
import auca.ac.urbanfarmingmgt.Model.Crop;
import auca.ac.urbanfarmingmgt.Model.Farm;
import auca.ac.urbanfarmingmgt.Model.Harvest;
import auca.ac.urbanfarmingmgt.Model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class HarvestService {

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    // Save a new harvest
    @Transactional
    public String saveHarvest(Harvest harvest) {
        try {
            harvestRepository.save(harvest);
            return "Harvest saved successfully with ID: " + harvest.getHarvestID();
        } catch (Exception e) {
            return "Failed to save harvest: " + e.getMessage();
        }
    }

    // Get all harvests
    public List<Harvest> getAllHarvests() {
        return harvestRepository.findAll();
    }

    // Get a harvest by ID
    public Harvest getHarvestById(Integer harvestId) {
        return harvestRepository.findById(harvestId)
                .orElseThrow(() -> new RuntimeException("Harvest not found with ID: " + harvestId));
    }

    // Get harvests by quality rating
    public List<Harvest> getHarvestsByQualityRating(Integer qualityRating) {
        return harvestRepository.findByQualityRating(qualityRating);
    }

    // Delete a harvest by ID
    @Transactional
    public String deleteHarvest(Integer harvestId) {
        try {
            if (!harvestRepository.existsById(harvestId)) {
                return "Harvest not found with ID: " + harvestId;
            }
            harvestRepository.deleteById(harvestId);
            return "Harvest deleted successfully";
        } catch (Exception e) {
            return "Failed to delete harvest: " + e.getMessage();
        }
    }

    // Record yield for a crop and create a harvest record
    @Transactional
    public String recordYield(Integer cropId, Double yield, Integer qualityRating) {
        try {
            Crop crop = cropRepository.findById(cropId)
                    .orElseThrow(() -> new RuntimeException("Crop not found with ID: " + cropId));

            Farm farm = crop.getFarm();
            if (farm == null) {
                return "Error: Crop is not associated with a farm";
            }

            Inventory inventory = crop.getInventory();
            if (inventory == null) {
                return "Error: Crop is not associated with inventory";
            }

            // Create and save new harvest
            Harvest harvest = Harvest.builder()
                    .date(new Date())
                    .yield(yield)
                    .qualityRating(qualityRating)
                    .crop(crop)
                    .farm(farm)
                    .inventory(inventory)
                    .build();

            harvestRepository.save(harvest);

            // Update average yield for the crop
            updateCropAverageYield(crop);

            return "Yield recorded successfully for crop: " + crop.getCropType();
        } catch (Exception e) {
            return "Failed to record yield: " + e.getMessage();
        }
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

    // Get harvests by date range
    public List<Harvest> getHarvestsByDateRange(Date startDate, Date endDate) {
        return harvestRepository.findByDateRange(startDate, endDate);
    }

    // Get harvests by farm ID
    public List<Harvest> getHarvestsByFarm(Integer farmId) {
        return harvestRepository.findByFarmId(farmId);
    }

    // Get harvests by crop ID
    public List<Harvest> getHarvestsByCrop(Integer cropId) {
        return harvestRepository.findByCropId(cropId);
    }

    // Get harvests by inventory ID
    public List<Harvest> getHarvestsByInventory(Integer inventoryId) {
        return harvestRepository.findByInventoryId(inventoryId);
    }

    // Update the quality rating of a harvest
    @Transactional
    public String updateHarvestQuality(Integer harvestId, Integer qualityRating) {
        try {
            Harvest harvest = getHarvestById(harvestId);
            harvest.setQualityRating(qualityRating);
            harvestRepository.save(harvest);
            return "Harvest quality updated successfully";
        } catch (Exception e) {
            return "Failed to update harvest quality: " + e.getMessage();
        }
    }

    // Update the yield of a harvest
    @Transactional
    public String updateHarvestYield(Integer harvestId, Double yield) {
        try {
            Harvest harvest = getHarvestById(harvestId);
            Double oldYield = harvest.getYield();
            harvest.setYield(yield);
            harvestRepository.save(harvest);

            // Update average yield for the crop
            Crop crop = harvest.getCrop();
            if (crop != null) {
                updateCropAverageYield(crop);
            }

            return "Harvest yield updated successfully";
        } catch (Exception e) {
            return "Failed to update harvest yield: " + e.getMessage();
        }
    }

    // Get total yield across all harvests
    public Double getTotalYield() {
        return harvestRepository.findAll().stream()
                .mapToDouble(Harvest::getYield)
                .sum();
    }

    // Get total yield for a specific farm
    public Double getTotalYieldByFarm(Integer farmId) {
        return getHarvestsByFarm(farmId).stream()
                .mapToDouble(Harvest::getYield)
                .sum();
    }

    // Get total yield for a specific crop
    public Double getTotalYieldByCrop(Integer cropId) {
        return getHarvestsByCrop(cropId).stream()
                .mapToDouble(Harvest::getYield)
                .sum();
    }

    // Get average quality rating across all harvests
    public Double getAverageQualityRating() {
        List<Harvest> harvests = harvestRepository.findAll();
        if (harvests.isEmpty()) {
            return 0.0;
        }
        return harvests.stream()
                .mapToDouble(Harvest::getQualityRating)
                .average()
                .orElse(0.0);
    }

    // Get harvests with yield above a threshold
    public List<Harvest> getHarvestsAboveYieldThreshold(Double threshold) {
        return harvestRepository.findByYieldAboveThreshold(threshold);
    }

    // Get most recent harvests
    public List<Harvest> getMostRecentHarvests(int limit) {
        return harvestRepository.findMostRecentHarvests().stream()
                .limit(limit)
                .toList();
    }

    // Transfer a harvest to a different inventory
    @Transactional
    public String transferHarvestToInventory(Integer harvestId, Integer inventoryId) {
        try {
            Harvest harvest = getHarvestById(harvestId);
            Inventory inventory = inventoryRepository.findById(inventoryId)
                    .orElseThrow(() -> new RuntimeException("Inventory not found with ID: " + inventoryId));

            harvest.setInventory(inventory);
            harvestRepository.save(harvest);

            return "Harvest transferred to inventory successfully";
        } catch (Exception e) {
            return "Failed to transfer harvest: " + e.getMessage();
        }
    }
}