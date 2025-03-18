package auca.ac.urbanfarmingmgt.Controller;

import auca.ac.urbanfarmingmgt.Services.SustainabilityService;
import auca.ac.urbanfarmingmgt.Model.SustainabilityMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sustainability")
public class SustainabilityController {

    @Autowired
    private SustainabilityService sustainabilityService;

    // Save a new sustainability metric
    @PostMapping("/metrics")
    public ResponseEntity<SustainabilityMetric> saveSustainabilityMetric(@RequestBody SustainabilityMetric sustainabilityMetric) {
        SustainabilityMetric savedMetric = sustainabilityService.saveSustainabilityMetric(sustainabilityMetric);
        return ResponseEntity.ok(savedMetric);
    }

    // Get all sustainability metrics
    @GetMapping("/metrics")
    public ResponseEntity<List<SustainabilityMetric>> getAllSustainabilityMetrics() {
        List<SustainabilityMetric> metrics = sustainabilityService.getAllSustainabilityMetrics();
        return ResponseEntity.ok(metrics);
    }

    // Get a sustainability metric by ID
    @GetMapping("/metrics/{id}")
    public ResponseEntity<SustainabilityMetric> getSustainabilityMetricById(@PathVariable Integer id) {
        Optional<SustainabilityMetric> metric = sustainabilityService.getSustainabilityMetricById(id);
        return metric.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a sustainability metric
    @PutMapping("/metrics/{id}")
    public ResponseEntity<SustainabilityMetric> updateSustainabilityMetric(@PathVariable Integer id, @RequestBody SustainabilityMetric updatedMetric) {
        Optional<SustainabilityMetric> updated = sustainabilityService.updateSustainabilityMetric(id, updatedMetric);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a sustainability metric
    @DeleteMapping("/metrics/{id}")
    public ResponseEntity<Void> deleteSustainabilityMetric(@PathVariable Integer id) {
        boolean isDeleted = sustainabilityService.deleteSustainabilityMetric(id);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Track sustainability metrics for a specific crop
    @GetMapping("/metrics/crop/{cropId}")
    public ResponseEntity<List<SustainabilityMetric>> trackPerCrop(@PathVariable Integer cropId) {
        List<SustainabilityMetric> metrics = sustainabilityService.trackPerCrop(cropId);
        return ResponseEntity.ok(metrics);
    }

    // Monitor sustainability metrics for a specific farm
    @GetMapping("/metrics/farm/{farmId}")
    public ResponseEntity<List<SustainabilityMetric>> monitorPerFarm(@PathVariable Integer farmId) {
        List<SustainabilityMetric> metrics = sustainabilityService.monitorPerFarm(farmId);
        return ResponseEntity.ok(metrics);
    }

    // Get sustainability metrics by water usage
    @GetMapping("/metrics/water-usage/{waterUsage}")
    public ResponseEntity<List<SustainabilityMetric>> getMetricsByWaterUsage(@PathVariable Double waterUsage) {
        List<SustainabilityMetric> metrics = sustainabilityService.getMetricsByWaterUsage(waterUsage);
        return ResponseEntity.ok(metrics);
    }

    // Get sustainability metrics with water usage below a threshold
    @GetMapping("/metrics/water-usage/below/{threshold}")
    public ResponseEntity<List<SustainabilityMetric>> getMetricsByWaterUsageBelowThreshold(@PathVariable Double threshold) {
        List<SustainabilityMetric> metrics = sustainabilityService.getMetricsByWaterUsageBelowThreshold(threshold);
        return ResponseEntity.ok(metrics);
    }

    // Get sustainability metrics with soil health above a threshold
    @GetMapping("/metrics/soil-health/above/{threshold}")
    public ResponseEntity<List<SustainabilityMetric>> getMetricsBySoilHealthAboveThreshold(@PathVariable Double threshold) {
        List<SustainabilityMetric> metrics = sustainabilityService.getMetricsBySoilHealthAboveThreshold(threshold);
        return ResponseEntity.ok(metrics);
    }

    // Get sustainability metrics with pesticide application below a threshold
    @GetMapping("/metrics/pesticide-application/below/{threshold}")
    public ResponseEntity<List<SustainabilityMetric>> getMetricsByPesticideApplicationBelowThreshold(@PathVariable Double threshold) {
        List<SustainabilityMetric> metrics = sustainabilityService.getMetricsByPesticideApplicationBelowThreshold(threshold);
        return ResponseEntity.ok(metrics);
    }

    // Get sustainability metrics with energy usage below a threshold
    @GetMapping("/metrics/energy-usage/below/{threshold}")
    public ResponseEntity<List<SustainabilityMetric>> getMetricsByEnergyUsageBelowThreshold(@PathVariable Double threshold) {
        List<SustainabilityMetric> metrics = sustainabilityService.getMetricsByEnergyUsageBelowThreshold(threshold);
        return ResponseEntity.ok(metrics);
    }

    // Calculate average water usage for a farm
    @GetMapping("/metrics/farm/{farmId}/average-water-usage")
    public ResponseEntity<Double> calculateAverageWaterUsageForFarm(@PathVariable Integer farmId) {
        Double averageWaterUsage = sustainabilityService.calculateAverageWaterUsageForFarm(farmId);
        return averageWaterUsage != null ? ResponseEntity.ok(averageWaterUsage) : ResponseEntity.notFound().build();
    }

    // Calculate average soil health for a farm
    @GetMapping("/metrics/farm/{farmId}/average-soil-health")
    public ResponseEntity<Double> calculateAverageSoilHealthForFarm(@PathVariable Integer farmId) {
        Double averageSoilHealth = sustainabilityService.calculateAverageSoilHealthForFarm(farmId);
        return averageSoilHealth != null ? ResponseEntity.ok(averageSoilHealth) : ResponseEntity.notFound().build();
    }

    // Calculate sustainability score for a farm based on all metrics
    @GetMapping("/metrics/farm/{farmId}/sustainability-score")
    public ResponseEntity<Double> calculateFarmSustainabilityScore(@PathVariable Integer farmId) {
        Double sustainabilityScore = sustainabilityService.calculateFarmSustainabilityScore(farmId);
        return sustainabilityScore != null ? ResponseEntity.ok(sustainabilityScore) : ResponseEntity.notFound().build();
    }

    // Get sustainability recommendations for a farm based on its metrics
    @GetMapping("/metrics/farm/{farmId}/recommendations")
    public ResponseEntity<List<String>> getSustainabilityRecommendations(@PathVariable Integer farmId) {
        List<String> recommendations = sustainabilityService.getSustainabilityRecommendations(farmId);
        return ResponseEntity.ok(recommendations);
    }
}