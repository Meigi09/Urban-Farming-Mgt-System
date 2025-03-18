package auca.ac.urbanfarmingmgt.Repository;

import auca.ac.urbanfarmingmgt.Model.SustainabilityMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SustainabilityMetricRepository extends JpaRepository<SustainabilityMetric, Integer> {

    List<SustainabilityMetric> findByWaterUsage(Double waterUsage);

    @Query("SELECT sm FROM SustainabilityMetric sm WHERE sm.crop.cropID = :cropId")
    List<SustainabilityMetric> findByCropId(@Param("cropId") Integer cropId);

    @Query("SELECT sm FROM SustainabilityMetric sm WHERE sm.farm.farmID = :farmId")
    List<SustainabilityMetric> findByFarmId(@Param("farmId") Integer farmId);

    @Query("SELECT sm FROM SustainabilityMetric sm WHERE sm.waterUsage < :threshold")
    List<SustainabilityMetric> findByWaterUsageBelowThreshold(@Param("threshold") Double threshold);

    @Query("SELECT sm FROM SustainabilityMetric sm WHERE sm.soilHealth > :threshold")
    List<SustainabilityMetric> findBySoilHealthAboveThreshold(@Param("threshold") Double threshold);

    @Query("SELECT sm FROM SustainabilityMetric sm WHERE sm.pesticideApplication < :threshold")
    List<SustainabilityMetric> findByPesticideApplicationBelowThreshold(@Param("threshold") Double threshold);

    @Query("SELECT sm FROM SustainabilityMetric sm WHERE sm.energyUsage < :threshold")
    List<SustainabilityMetric> findByEnergyUsageBelowThreshold(@Param("threshold") Double threshold);
}