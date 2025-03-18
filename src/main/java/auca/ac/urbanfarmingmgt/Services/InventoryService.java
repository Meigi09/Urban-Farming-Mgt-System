package auca.ac.urbanfarmingmgt.Services;

import auca.ac.urbanfarmingmgt.Model.Inventory;
import auca.ac.urbanfarmingmgt.Model.Order;
import auca.ac.urbanfarmingmgt.Repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // Fetch all inventory items
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    // Fetch a specific inventory item by its ID
    public Optional<Inventory> getInventoryById(Integer id) {
        return inventoryRepository.findById(id);
    }

    // Fetch inventory by produce type
    public List<Inventory> getInventoryByProduceType(String produceType) {
        return inventoryRepository.findByProduceType(produceType);
    }

    // Save or update an inventory item
    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    // Delete an inventory item by its ID
    public void deleteInventory(Integer id) {
        inventoryRepository.deleteById(id);
    }

    // Update stock of the inventory based on harvest
    public void updateInventoryStock(int harvestId, int harvestAmount) {
        inventoryRepository.updateStock(harvestId, harvestAmount);
    }

    // Link inventory item to an order
    @Transactional
    public void linkToOrders(int inventoryId, Order order) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            inventory.addOrder(order); // Use the helper method
            inventoryRepository.save(inventory);
        }
    }

    // Track harvest in inventory (update status and quantity)
    @Transactional
    public void trackHarvestInInventory(int harvestId, double harvestYield) {
        inventoryRepository.trackHarvest(harvestId, harvestYield);
    }

    // Check if inventory has enough quantity to fulfill order
    public boolean checkAvailability(Integer inventoryId, Double requiredQuantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            return inventory.getQuantity() >= requiredQuantity;
        }
        return false;
    }

    // Update inventory quantity after order placement
    @Transactional
    public boolean updateQuantityAfterOrder(Integer inventoryId, Double orderedQuantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findById(inventoryId);
        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            if (inventory.getQuantity() >= orderedQuantity) {
                inventory.setQuantity(inventory.getQuantity() - orderedQuantity);
                inventoryRepository.save(inventory); // Persist updated inventory
                return true;
            }
        }
        return false;
    }

}