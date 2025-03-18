package auca.ac.urbanfarmingmgt.Repository;

import auca.ac.urbanfarmingmgt.Model.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Integer> {

    List<Harvest> findByQualityRating(Integer qualityRating);

    @Query("SELECT h FROM Harvest h WHERE h.date BETWEEN :startDate AND :endDate")
    List<Harvest> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT h FROM Harvest h WHERE h.farm.farmID = :farmId")
    List<Harvest> findByFarmId(@Param("farmId") Integer farmId);

    @Query("SELECT h FROM Harvest h WHERE h.crop.cropID = :cropId")
    List<Harvest> findByCropId(@Param("cropId") Integer cropId);

    @Query("SELECT h FROM Harvest h WHERE h.inventory.inventoryID = :inventoryId")
    List<Harvest> findByInventoryId(@Param("inventoryId") Integer inventoryId);

    @Query("SELECT h FROM Harvest h WHERE h.yield > :threshold")
    List<Harvest> findByYieldAboveThreshold(@Param("threshold") Double threshold);

    @Query("SELECT h FROM Harvest h ORDER BY h.date DESC")
    List<Harvest> findMostRecentHarvests();
}