package auca.ac.urbanfarmingmgt.Controller;

import auca.ac.urbanfarmingmgt.Services.InventoryService;
import auca.ac.urbanfarmingmgt.Model.Inventory;
import auca.ac.urbanfarmingmgt.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // Fetch all inventory items
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventoryList);
    }

    // Fetch a specific inventory item by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Integer id) {
        return inventoryService.getInventoryById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Fetch inventory by produce type
    @GetMapping("/produce-type/{produceType}")
    public ResponseEntity<List<Inventory>> getInventoryByProduceType(@PathVariable String produceType) {
        List<Inventory> inventoryList = inventoryService.getInventoryByProduceType(produceType);
        return ResponseEntity.ok(inventoryList);
    }

    // Save or update an inventory item
    @PostMapping
    public ResponseEntity<Inventory> saveInventory(@RequestBody Inventory inventory) {
        Inventory savedInventory = inventoryService.saveInventory(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedInventory);
    }

    // Delete an inventory item by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    // Update stock of the inventory based on harvest
    @PutMapping("/update-stock/{harvestId}/{harvestAmount}")
    public ResponseEntity<Void> updateInventoryStock(@PathVariable int harvestId, @PathVariable int harvestAmount) {
        inventoryService.updateInventoryStock(harvestId, harvestAmount);
        return ResponseEntity.ok().build();
    }

    // Link inventory item to an order
    @PostMapping("/link-to-orders/{inventoryId}")
    public ResponseEntity<Void> linkToOrders(@PathVariable int inventoryId, @RequestBody Order order) {
        inventoryService.linkToOrders(inventoryId, order);
        return ResponseEntity.ok().build();
    }

    // Track harvest in inventory (update status and quantity)
    @PutMapping("/track-harvest/{harvestId}/{harvestYield}")
    public ResponseEntity<Void> trackHarvestInInventory(@PathVariable int harvestId, @PathVariable double harvestYield) {
        inventoryService.trackHarvestInInventory(harvestId, harvestYield);
        return ResponseEntity.ok().build();
    }

    // Check if inventory has enough quantity to fulfill order
    @GetMapping("/check-availability/{inventoryId}/{requiredQuantity}")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable Integer inventoryId, @PathVariable Double requiredQuantity) {
        boolean isAvailable = inventoryService.checkAvailability(inventoryId, requiredQuantity);
        return ResponseEntity.ok(isAvailable);
    }

    // Update inventory quantity after order placement
    @PutMapping("/update-quantity/{inventoryId}/{orderedQuantity}")
    public ResponseEntity<Boolean> updateQuantityAfterOrder(@PathVariable Integer inventoryId, @PathVariable Double orderedQuantity) {
        boolean isUpdated = inventoryService.updateQuantityAfterOrder(inventoryId, orderedQuantity);
        return ResponseEntity.ok(isUpdated);
    }

}