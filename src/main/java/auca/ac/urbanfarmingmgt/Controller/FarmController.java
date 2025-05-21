package auca.ac.urbanfarmingmgt.Controller;

import auca.ac.urbanfarmingmgt.Services.FarmService;
import auca.ac.urbanfarmingmgt.Model.Farm;
import auca.ac.urbanfarmingmgt.Model.SustainabilityMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/farms")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FarmController {

    @Autowired
    private FarmService farmService;

    // Save a farm
    @PostMapping
    public ResponseEntity<String> saveFarm(@RequestBody Farm farm) {
        try {
            String response = farmService.saveFarm(farm);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get all farms
    @GetMapping
    public ResponseEntity<List<Farm>> getAllFarms() {
        List<Farm> farms = farmService.getAllFarms();
        return ResponseEntity.ok(farms);
    }

    // Get a farm by ID
    @GetMapping("/{farmId}")
    public ResponseEntity<Farm> getFarmById(@PathVariable Integer farmId) {
        try {
            Farm farm = farmService.getFarmById(farmId);
            return ResponseEntity.ok(farm);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get farms by name containing a search term
    @GetMapping("/name/{name}")
    public ResponseEntity<List<Farm>> getFarmsByName(@PathVariable String name) {
        List<Farm> farms = farmService.getFarmsByName(name);
        return ResponseEntity.ok(farms);
    }

    // Delete a farm by ID
    @DeleteMapping("/{farmId}")
    public ResponseEntity<String> deleteFarm(@PathVariable Integer farmId) {
        try {
            String response = farmService.deleteFarm(farmId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Track crops for a specific farm
    @PostMapping("/{farmId}/track-crops")
    public ResponseEntity<String> trackCrops(
            @PathVariable Integer farmId,
            @RequestParam String cropType,
            @RequestParam Date plantingSchedule,
            @RequestParam boolean growingConditions) {
        try {
            String response = farmService.trackCrops(farmId, cropType, plantingSchedule, growingConditions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Manage staff assignments for a farm
    @PostMapping("/{farmId}/manage-staff/{staffId}")
    public ResponseEntity<String> manageStaff(
            @PathVariable Integer farmId,
            @PathVariable Integer staffId) {
        try {
            String response = farmService.manageStaff(farmId, staffId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Track sustainability metrics for a farm
    @PostMapping("/{farmId}/track-metrics/{metricsId}")
    public ResponseEntity<String> trackMetrics(
            @PathVariable Integer farmId,
            @PathVariable Integer metricsId) {
        try {
            String response = farmService.trackMetrics(farmId, metricsId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Calculate total yield for a farm
    @GetMapping("/{farmId}/total-yield")
    public ResponseEntity<Double> calculateTotalYield(@PathVariable Integer farmId) {
        try {
            Double totalYield = farmService.calculateTotalYield(farmId);
            return ResponseEntity.ok(totalYield);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get sustainability report for a farm
    @GetMapping("/{farmId}/sustainability-report")
    public ResponseEntity<List<SustainabilityMetric>> getSustainabilityReport(@PathVariable Integer farmId) {
        try {
            List<SustainabilityMetric> report = farmService.getSustainabilityReport(farmId);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Find farms by crop type
    @GetMapping("/crop-type/{cropType}")
    public ResponseEntity<List<Farm>> getFarmsByCropType(@PathVariable String cropType) {
        List<Farm> farms = farmService.getFarmsByCropType(cropType);
        return ResponseEntity.ok(farms);
    }

    // Find farms by staff member ID
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<Farm>> getFarmsByStaffId(@PathVariable Integer staffId) {
        List<Farm> farms = farmService.getFarmsByStaffId(staffId);
        return ResponseEntity.ok(farms);
    }

    // Find farms by sustainability metric ID
    @GetMapping("/metric/{metricId}")
    public ResponseEntity<List<Farm>> getFarmsByMetricId(@PathVariable Integer metricId) {
        List<Farm> farms = farmService.getFarmsByMetricId(metricId);
        return ResponseEntity.ok(farms);
    }
}