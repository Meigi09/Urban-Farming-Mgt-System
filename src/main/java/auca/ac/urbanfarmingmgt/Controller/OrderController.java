package auca.ac.urbanfarmingmgt.Controller;

import auca.ac.urbanfarmingmgt.Services.OrderService;
import auca.ac.urbanfarmingmgt.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Save an order
    @PostMapping
    public ResponseEntity<String> saveOrder(@RequestBody Order order) {
        try {
            String response = orderService.saveOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update the delivery status of an order
    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam String deliveryStatus) {
        try {
            String response = orderService.updateOrderStatus(orderId, deliveryStatus);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Place an order and update inventory
    @PostMapping("/{orderId}/place-order")
    public ResponseEntity<String> placeOrder(@PathVariable Integer orderId) {
        try {
            String response = orderService.placeOrder(orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Cancel an order and revert inventory changes
    @PostMapping("/{orderId}/cancel-order")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer orderId) {
        try {
            String response = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Get orders by delivery status
    @GetMapping("/status/{deliveryStatus}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String deliveryStatus) {
        List<Order> orders = orderService.getOrdersByStatus(deliveryStatus);
        return ResponseEntity.ok(orders);
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get an order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}