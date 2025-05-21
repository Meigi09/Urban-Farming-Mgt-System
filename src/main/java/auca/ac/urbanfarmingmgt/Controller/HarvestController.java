package auca.ac.urbanfarmingmgt.Controller;

import auca.ac.urbanfarmingmgt.Services.HarvestService;
import auca.ac.urbanfarmingmgt.Model.Harvest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/harvests")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class HarvestController {

    @Autowired
    private HarvestService harvestService;

    // Save a new harvest
    @PostMapping
    public ResponseEntity<String> saveHarvest(@RequestBody Harvest harvest) {
        try {
            String response = harvestService.saveHarvest(harvest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get all harvests
    @GetMapping
    public ResponseEntity<List<Harvest>> getAllHarvests() {
        List<Harvest> harvests = harvestService.getAllHarvests();
        return ResponseEntity.ok(harvests);
    }

    // Get a harvest by ID
    @GetMapping("/{harvestId}")
    public ResponseEntity<Harvest> getHarvestById(@PathVariable Integer harvestId) {
        try {
            Harvest harvest = harvestService.getHarvestById(harvestId);
            return ResponseEntity.ok(harvest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get harvests by quality rating
    @GetMapping("/quality-rating/{qualityRating}")
    public ResponseEntity<List<Harvest>> getHarvestsByQualityRating(@PathVariable Integer qualityRating) {
        List<Harvest> harvests = harvestService.getHarvestsByQualityRating(qualityRating);
        return ResponseEntity.ok(harvests);
    }

    // Delete a harvest by ID
    @DeleteMapping("/{harvestId}")
    public ResponseEntity<String> deleteHarvest(@PathVariable Integer harvestId) {
        try {
            String response = harvestService.deleteHarvest(harvestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Record yield for a crop and create a harvest record
    @PostMapping("/record-yield")
    public ResponseEntity<String> recordYield(
            @RequestParam Integer cropId,
            @RequestParam Double yield,
            @RequestParam Integer qualityRating) {
        try {
            String response = harvestService.recordYield(cropId, yield, qualityRating);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get harvests by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<Harvest>> getHarvestsByDateRange(
            @RequestParam Date startDate,
            @RequestParam Date endDate) {
        List<Harvest> harvests = harvestService.getHarvestsByDateRange(startDate, endDate);
        return ResponseEntity.ok(harvests);
    }

    // Get harvests by farm ID
    @GetMapping("/farm/{farmId}")
    public ResponseEntity<List<Harvest>> getHarvestsByFarm(@PathVariable Integer farmId) {
        List<Harvest> harvests = harvestService.getHarvestsByFarm(farmId);
        return ResponseEntity.ok(harvests);
    }

    // Get harvests by crop ID
    @GetMapping("/crop/{cropId}")
    public ResponseEntity<List<Harvest>> getHarvestsByCrop(@PathVariable Integer cropId) {
        List<Harvest> harvests = harvestService.getHarvestsByCrop(cropId);
        return ResponseEntity.ok(harvests);
    }

    // Get harvests by inventory ID
    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<List<Harvest>> getHarvestsByInventory(@PathVariable Integer inventoryId) {
        List<Harvest> harvests = harvestService.getHarvestsByInventory(inventoryId);
        return ResponseEntity.ok(harvests);
    }

    // Update the quality rating of a harvest
    @PutMapping("/{harvestId}/update-quality")
    public ResponseEntity<String> updateHarvestQuality(
            @PathVariable Integer harvestId,
            @RequestParam Integer qualityRating) {
        try {
            String response = harvestService.updateHarvestQuality(harvestId, qualityRating);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Update the yield of a harvest
    @PutMapping("/{harvestId}/update-yield")
    public ResponseEntity<String> updateHarvestYield(
            @PathVariable Integer harvestId,
            @RequestParam Double yield) {
        try {
            String response = harvestService.updateHarvestYield(harvestId, yield);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get total yield across all harvests
    @GetMapping("/total-yield")
    public ResponseEntity<Double> getTotalYield() {
        Double totalYield = harvestService.getTotalYield();
        return ResponseEntity.ok(totalYield);
    }

    // Get total yield for a specific farm
    @GetMapping("/total-yield/farm/{farmId}")
    public ResponseEntity<Double> getTotalYieldByFarm(@PathVariable Integer farmId) {
        Double totalYield = harvestService.getTotalYieldByFarm(farmId);
        return ResponseEntity.ok(totalYield);
    }

    // Get total yield for a specific crop
    @GetMapping("/total-yield/crop/{cropId}")
    public ResponseEntity<Double> getTotalYieldByCrop(@PathVariable Integer cropId) {
        Double totalYield = harvestService.getTotalYieldByCrop(cropId);
        return ResponseEntity.ok(totalYield);
    }

    // Get average quality rating across all harvests
    @GetMapping("/average-quality-rating")
    public ResponseEntity<Double> getAverageQualityRating() {
        Double averageQualityRating = harvestService.getAverageQualityRating();
        return ResponseEntity.ok(averageQualityRating);
    }

    // Get harvests with yield above a threshold
    @GetMapping("/yield-above-threshold")
    public ResponseEntity<List<Harvest>> getHarvestsAboveYieldThreshold(@RequestParam Double threshold) {
        List<Harvest> harvests = harvestService.getHarvestsAboveYieldThreshold(threshold);
        return ResponseEntity.ok(harvests);
    }

    // Get most recent harvests
    @GetMapping("/most-recent")
    public ResponseEntity<List<Harvest>> getMostRecentHarvests(@RequestParam int limit) {
        List<Harvest> harvests = harvestService.getMostRecentHarvests(limit);
        return ResponseEntity.ok(harvests);
    }

    // Transfer a harvest to a different inventory
    @PostMapping("/{harvestId}/transfer-to-inventory/{inventoryId}")
    public ResponseEntity<String> transferHarvestToInventory(
            @PathVariable Integer harvestId,
            @PathVariable Integer inventoryId) {
        try {
            String response = harvestService.transferHarvestToInventory(harvestId, inventoryId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}