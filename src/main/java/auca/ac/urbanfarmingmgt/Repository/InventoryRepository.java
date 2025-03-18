package auca.ac.urbanfarmingmgt.Repository;

import auca.ac.urbanfarmingmgt.Model.Inventory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByProduceType(String produceType);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.stock = i.stock + :harvestAmount WHERE i.inventoryID = :harvestId")
    void updateStock(int harvestId, int harvestAmount);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.freshnessStatus = true, i.quantity = i.quantity + :harvestYield WHERE i.inventoryID = :harvestId")
    void trackHarvest(int harvestId, double harvestYield);

    @Modifying
    @Transactional
    @Query("UPDATE Inventory i SET i.orderList = :orderId WHERE i.inventoryID = :inventoryId")
    void linkToOrders(int inventoryId, int orderId);
}