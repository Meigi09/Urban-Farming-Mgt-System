package auca.ac.urbanfarmingmgt.Controller;

import auca.ac.urbanfarmingmgt.Services.CropService;
import auca.ac.urbanfarmingmgt.Model.Crop;
import auca.ac.urbanfarmingmgt.Model.Harvest;
import auca.ac.urbanfarmingmgt.Model.SustainabilityMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CropController {

    @Autowired
    private CropService cropService;

    // Save a new crop
    @PostMapping
    public ResponseEntity<String> saveCrop(@RequestBody Crop crop) {
        try {
            cropService.saveCrop(crop);
            return ResponseEntity.status(HttpStatus.CREATED).body("Crop saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get all crops
    @GetMapping
    public ResponseEntity<List<Crop>> getAllCrops() {
        List<Crop> crops = cropService.getAllCrops();
        return ResponseEntity.ok(crops);
    }

    // Get a crop by ID
    @GetMapping("/{cropId}")
    public ResponseEntity<Crop> getCropById(@PathVariable Integer cropId) {
        try {
            Crop crop = cropService.getCropById(cropId);
            return ResponseEntity.ok(crop);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get crops by type
    @GetMapping("/type/{cropType}")
    public ResponseEntity<List<Crop>> getCropsByType(@PathVariable String cropType) {
        List<Crop> crops = cropService.getCropsByType(cropType);
        return ResponseEntity.ok(crops);
    }

    // Delete a crop by ID
    @DeleteMapping("/{cropId}")
    public ResponseEntity<String> deleteCrop(@PathVariable Integer cropId) {
        try {
            cropService.deleteCrop(cropId);
            return ResponseEntity.ok("Crop deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Check if a crop is eligible for planting in a specific farm
    @GetMapping("/eligible-for-planting")
    public ResponseEntity<Boolean> isEligibleForPlanting(
            @RequestParam Integer farmId,
            @RequestParam String cropRequirement) {
        try {
            boolean isEligible = cropService.isEligibleForPlanting(farmId, cropRequirement);
            return ResponseEntity.ok(isEligible);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get crops eligible for a specific location
    @GetMapping("/eligible-for-location")
    public ResponseEntity<List<Crop>> getCropsEligibleForLocation(@RequestParam String location) {
        List<Crop> crops = cropService.getCropsEligibleForLocation(location);
        return ResponseEntity.ok(crops);
    }

    // Record a harvest for a crop
    @PostMapping("/{cropId}/record-harvest")
    public ResponseEntity<Harvest> recordHarvest(
            @PathVariable Integer cropId,
            @RequestParam Double yield,
            @RequestParam Integer qualityRating) {
        try {
            Harvest harvest = cropService.recordHarvest(cropId, yield, qualityRating);
            return ResponseEntity.status(HttpStatus.CREATED).body(harvest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Record sustainability metrics for a crop
    @PostMapping("/{cropId}/record-metrics")
    public ResponseEntity<SustainabilityMetric> recordSustainabilityMetrics(
            @PathVariable Integer cropId,
            @RequestParam Double waterUsage,
            @RequestParam Double soilHealth,
            @RequestParam Double pesticideApplication,
            @RequestParam Double energyUsage) {
        try {
            SustainabilityMetric metric = cropService.recordSustainabilityMetrics(
                    cropId, waterUsage, soilHealth, pesticideApplication, energyUsage);
            return ResponseEntity.status(HttpStatus.CREATED).body(metric);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Calculate sustainability score for a crop
    @GetMapping("/{cropId}/sustainability-score")
    public ResponseEntity<Double> calculateSustainabilityScore(@PathVariable Integer cropId) {
        try {
            Double score = cropService.calculateSustainabilityScore(cropId);
            return ResponseEntity.ok(score);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get crops by growing season
    @GetMapping("/season/{season}")
    public ResponseEntity<List<Crop>> getCropsByGrowingSeason(@PathVariable String season) {
        List<Crop> crops = cropService.getCropsByGrowingSeason(season);
        return ResponseEntity.ok(crops);
    }

    // Get crops by location requirement
    @GetMapping("/location-requirement/{locationRequirement}")
    public ResponseEntity<List<Crop>> getCropsByLocationRequirement(@PathVariable String locationRequirement) {
        List<Crop> crops = cropService.getCropsByLocationRequirement(locationRequirement);
        return ResponseEntity.ok(crops);
    }

    // Get crops by farm ID
    @GetMapping("/farm/{farmId}")
    public ResponseEntity<List<Crop>> getCropsByFarm(@PathVariable Integer farmId) {
        List<Crop> crops = cropService.getCropsByFarm(farmId);
        return ResponseEntity.ok(crops);
    }

    // Get high-yielding crops
    @GetMapping("/high-yielding")
    public ResponseEntity<List<Crop>> getHighYieldingCrops(@RequestParam Double minimumYield) {
        List<Crop> crops = cropService.getHighYieldingCrops(minimumYield);
        return ResponseEntity.ok(crops);
    }

    // Update crop details
    @PutMapping("/{cropId}")
    public ResponseEntity<String> updateCrop(@PathVariable Integer cropId, @RequestBody Crop crop) {
        try {
            crop.setCropID(cropId); // Ensure the crop ID matches the path variable
            String response = cropService.updateCrop(crop);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Assign a crop to a farm
    @PostMapping("/{cropId}/assign-to-farm/{farmId}")
    public ResponseEntity<String> assignCropToFarm(
            @PathVariable Integer cropId,
            @PathVariable Integer farmId) {
        try {
            String response = cropService.assignCropToFarm(cropId, farmId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get crops by growing condition
    @GetMapping("/growing-condition/{condition}")
    public ResponseEntity<List<Crop>> getCropsByGrowingCondition(@PathVariable boolean condition) {
        List<Crop> crops = cropService.getCropsByGrowingCondition(condition);
        return ResponseEntity.ok(crops);
    }

    // Update crop planting schedule
    @PutMapping("/{cropId}/update-planting-schedule")
    public ResponseEntity<String> updateCropPlantingSchedule(
            @PathVariable Integer cropId,
            @RequestParam Date newSchedule) {
        try {
            String response = cropService.updateCropPlantingSchedule(cropId, newSchedule);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}