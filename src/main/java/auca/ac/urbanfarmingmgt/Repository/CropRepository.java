package auca.ac.urbanfarmingmgt.Repository;

import auca.ac.urbanfarmingmgt.Model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, Integer> {

    List<Crop> findByCropType(String cropType);

    List<Crop> findByGrowingSeason(String growingSeason);

    List<Crop> findByLocationRequirement(String locationRequirement);

    List<Crop> findByFarmFarmID(Integer farmID);

    List<Crop> findByAverageYieldGreaterThan(Double yield);

    @Query("SELECT c FROM Crop c WHERE c.locationRequirement LIKE %:location%")
    List<Crop> findCropsEligibleForLocation(@Param("location") String location);

    List<Crop> findByGrowingConditions(boolean condition);

}