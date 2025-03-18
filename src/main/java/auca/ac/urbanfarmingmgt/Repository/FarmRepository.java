package auca.ac.urbanfarmingmgt.Repository;

import auca.ac.urbanfarmingmgt.model.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Integer> {
    List<Farm> findByNameContaining(String name);
    void trackCrops(String cropType,Date plantingSchedule,boolean growingConditions);
    void manageStaff(int staffId);
    void trackMetrics(int metricsId);
}
