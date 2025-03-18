package auca.ac.urbanfarmingmgt.Repository;

import auca.ac.urbanfarmingmgt.Model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Integer> {

    List<Farm> findByNameContaining(String name);

    @Query("SELECT f FROM Farm f JOIN f.crops c WHERE c.cropType = :cropType")
    List<Farm> findByCropType(@Param("cropType") String cropType);

    @Query("SELECT f FROM Farm f JOIN f.assignedStaff s WHERE s.personID = :staffId")
    List<Farm> findByStaffId(@Param("staffId") Integer staffId);

    @Query("SELECT f FROM Farm f JOIN f.sustainabilityMetrics sm WHERE sm.metricID = :metricId")
    List<Farm> findByMetricId(@Param("metricId") Integer metricId);
}