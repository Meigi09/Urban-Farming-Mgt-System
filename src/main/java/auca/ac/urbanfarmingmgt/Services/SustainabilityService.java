package auca.ac.urbanfarmingmgt.Services;

import auca.ac.urbanfarmingmgt.Repository.SustainabilityMetricRepository;
import auca.ac.urbanfarmingmgt.Model.SustainabilityMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SustainabilityService {

    @Autowired
    private SustainabilityMetricRepository sustainabilityMetricRepository;


    // Saves a new sustainability metric to the database.
    public SustainabilityMetric saveSustainabilityMetric(SustainabilityMetric sustainabilityMetric) {
        return sustainabilityMetricRepository.save(sustainabilityMetric);
    }

    // Retrieves all sustainability metrics from the database.
    public List<SustainabilityMetric> getAllSustainabilityMetrics() {
        return sustainabilityMetricRepository.findAll();
    }

    // Retrieves a sustainability metric by its ID.
    public Optional<SustainabilityMetric> getSustainabilityMetricById(Integer id) {
        return sustainabilityMetricRepository.findById(id);
    }

    // Updates an existing sustainability metric with new data.
    public Optional<SustainabilityMetric> updateSustainabilityMetric(Integer id, SustainabilityMetric updatedMetric) {
        Optional<SustainabilityMetric> existingMetric = sustainabilityMetricRepository.findById(id);

        if (existingMetric.isPresent()) {
            SustainabilityMetric metric = existingMetric.get();

            // Update fields with new values
            metric.setWaterUsage(updatedMetric.getWaterUsage());
            metric.setSoilHealth(updatedMetric.getSoilHealth());
            metric.setPesticideApplication(updatedMetric.getPesticideApplication());
            metric.setEnergyUsage(updatedMetric.getEnergyUsage());

            // Update farm and crop references if provided
            if (updatedMetric.getFarm() != null) {
                metric.setFarm(updatedMetric.getFarm());
            }

            if (updatedMetric.getCrop() != null) {
                metric.setCrop(updatedMetric.getCrop());
            }

            return Optional.of(sustainabilityMetricRepository.save(metric));
        }

        return Optional.empty();
    }

    // Deletes a sustainability metric by its ID.
    public boolean deleteSustainabilityMetric(Integer id) {
        if (sustainabilityMetricRepository.existsById(id)) {
            sustainabilityMetricRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Retrieves sustainability metrics for a specific crop.
    public List<SustainabilityMetric> trackPerCrop(Integer cropId) {
        return sustainabilityMetricRepository.findByCropId(cropId);
    }

    // Retrieves sustainability metrics for a specific farm.
    public List<SustainabilityMetric> monitorPerFarm(Integer farmId) {
        return sustainabilityMetricRepository.findByFarmId(farmId);
    }

    // Retrieves sustainability metrics with a specific water usage value.
    public List<SustainabilityMetric> getMetricsByWaterUsage(Double waterUsage) {
        return sustainabilityMetricRepository.findByWaterUsage(waterUsage);
    }

    // Retrieves sustainability metrics with water usage below a specified threshold.
    public List<SustainabilityMetric> getMetricsByWaterUsageBelowThreshold(Double threshold) {
        return sustainabilityMetricRepository.findByWaterUsageBelowThreshold(threshold);
    }

    // Retrieves sustainability metrics with soil health above a specified threshold.
    public List<SustainabilityMetric> getMetricsBySoilHealthAboveThreshold(Double threshold) {
        return sustainabilityMetricRepository.findBySoilHealthAboveThreshold(threshold);
    }

    // Retrieves sustainability metrics with pesticide application below a specified threshold.
    public List<SustainabilityMetric> getMetricsByPesticideApplicationBelowThreshold(Double threshold) {
        return sustainabilityMetricRepository.findByPesticideApplicationBelowThreshold(threshold);
    }

    // Retrieves sustainability metrics with energy usage below a specified threshold.
    public List<SustainabilityMetric> getMetricsByEnergyUsageBelowThreshold(Double threshold) {
        return sustainabilityMetricRepository.findByEnergyUsageBelowThreshold(threshold);
    }

    // Calculates the average water usage for a specific farm.
    public Double calculateAverageWaterUsageForFarm(Integer farmId) {
        List<SustainabilityMetric> farmMetrics = monitorPerFarm(farmId);

        if (farmMetrics.isEmpty()) {
            return null;
        }

        return farmMetrics.stream()
                .filter(metric -> metric.getWaterUsage() != null)
                .mapToDouble(SustainabilityMetric::getWaterUsage)
                .average()
                .orElse(0.0);
    }

    // Calculates the average soil health for a specific farm.
    public Double calculateAverageSoilHealthForFarm(Integer farmId) {
        List<SustainabilityMetric> farmMetrics = monitorPerFarm(farmId);

        if (farmMetrics.isEmpty()) {
            return null;
        }

        return farmMetrics.stream()
                .filter(metric -> metric.getSoilHealth() != null)
                .mapToDouble(SustainabilityMetric::getSoilHealth)
                .average()
                .orElse(0.0);
    }

    // Calculates a sustainability score for a farm based on its metrics.
    public Double calculateFarmSustainabilityScore(Integer farmId) {
        List<SustainabilityMetric> farmMetrics = monitorPerFarm(farmId);

        if (farmMetrics.isEmpty()) {
            return null;
        }

        // Calculate averages for each metric
        double avgWaterUsage = farmMetrics.stream()
                .filter(metric -> metric.getWaterUsage() != null)
                .mapToDouble(SustainabilityMetric::getWaterUsage)
                .average()
                .orElse(0.0);

        double avgSoilHealth = farmMetrics.stream()
                .filter(metric -> metric.getSoilHealth() != null)
                .mapToDouble(SustainabilityMetric::getSoilHealth)
                .average()
                .orElse(0.0);

        double avgPesticideApp = farmMetrics.stream()
                .filter(metric -> metric.getPesticideApplication() != null)
                .mapToDouble(SustainabilityMetric::getPesticideApplication)
                .average()
                .orElse(0.0);

        double avgEnergyUsage = farmMetrics.stream()
                .filter(metric -> metric.getEnergyUsage() != null)
                .mapToDouble(SustainabilityMetric::getEnergyUsage)
                .average()
                .orElse(0.0);

        // Calculate individual scores for each metric
        double waterScore = 100 - (avgWaterUsage * 10); // Lower water usage is better
        double soilScore = avgSoilHealth * 10; // Higher soil health is better
        double pesticideScore = 100 - (avgPesticideApp * 20); // Lower pesticide usage is better
        double energyScore = 100 - (avgEnergyUsage * 5); // Lower energy usage is better

        // Calculate the overall sustainability score
        double overallScore = (waterScore + soilScore + pesticideScore + energyScore) / 4.0;

        // Ensure the score is within the range of 0 to 100
        return Math.max(0, Math.min(100, overallScore));
    }

    // Generates sustainability recommendations for a farm based on its metrics.
    public List<String> getSustainabilityRecommendations(Integer farmId) {
        List<SustainabilityMetric> farmMetrics = monitorPerFarm(farmId);

        if (farmMetrics.isEmpty()) {
            return List.of("No sustainability metrics available for this farm.");
        }

        // Calculate averages for each metric
        double avgWaterUsage = farmMetrics.stream()
                .filter(metric -> metric.getWaterUsage() != null)
                .mapToDouble(SustainabilityMetric::getWaterUsage)
                .average()
                .orElse(0.0);

        double avgSoilHealth = farmMetrics.stream()
                .filter(metric -> metric.getSoilHealth() != null)
                .mapToDouble(SustainabilityMetric::getSoilHealth)
                .average()
                .orElse(0.0);

        double avgPesticideApp = farmMetrics.stream()
                .filter(metric -> metric.getPesticideApplication() != null)
                .mapToDouble(SustainabilityMetric::getPesticideApplication)
                .average()
                .orElse(0.0);

        double avgEnergyUsage = farmMetrics.stream()
                .filter(metric -> metric.getEnergyUsage() != null)
                .mapToDouble(SustainabilityMetric::getEnergyUsage)
                .average()
                .orElse(0.0);

        // Generate recommendations based on metric thresholds
        List<String> recommendations = new ArrayList<>();

        if (avgWaterUsage > 5.0) {
            recommendations.add("Consider implementing water conservation techniques like drip irrigation or rainwater harvesting to reduce water usage.");
        }

        if (avgSoilHealth < 7.0) {
            recommendations.add("Improve soil health by incorporating organic matter, implementing crop rotation, or using cover crops.");
        }

        if (avgPesticideApp > 2.0) {
            recommendations.add("Reduce pesticide use by implementing integrated pest management practices or using organic alternatives.");
        }

        if (avgEnergyUsage > 10.0) {
            recommendations.add("Reduce energy consumption by using energy-efficient equipment or implementing renewable energy sources like solar panels.");
        }

        if (recommendations.isEmpty()) {
            recommendations.add("Your farm is performing well in terms of sustainability. Continue your current practices.");
        }

        return recommendations;
    }
}