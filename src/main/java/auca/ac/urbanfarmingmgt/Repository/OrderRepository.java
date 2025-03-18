package auca.ac.urbanfarmingmgt.Repository;

import auca.ac.urbanfarmingmgt.Model.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByDeliveryStatus(String deliveryStatus);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.deliveryStatus = 'Placed' WHERE o.orderID = :orderId")
    void ordering(int orderId);

    @Transactional
    @Modifying
    @Query("UPDATE Inventory i SET i.stock = i.stock - :orderQuantity WHERE i.inventoryID = :inventoryId")
    void keepInventory(int inventoryId, double orderQuantity);
}
