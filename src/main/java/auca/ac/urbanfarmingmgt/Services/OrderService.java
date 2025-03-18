package auca.ac.urbanfarmingmgt.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import auca.ac.urbanfarmingmgt.Repository.OrderRepository;
import auca.ac.urbanfarmingmgt.Model.Order;
import auca.ac.urbanfarmingmgt.Model.Inventory;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Save the order into the repository
    public String saveOrder(Order order) {
        if (order.getInventory() == null) {
            return "The inventory must be specified for the order";
        } else {
            orderRepository.save(order);
            return "Order saved successfully";
        }
    }

    // Update the delivery status of an existing order
    public String updateOrderStatus(Integer orderId, String deliveryStatus) {
        Optional<Order> getOrder = orderRepository.findById(orderId);

        if (getOrder.isPresent()) {
            Order order = getOrder.get();
            order.setDeliveryStatus(deliveryStatus);
            orderRepository.save(order);
            return "Order status updated successfully";
        } else {
            return "Order not found";
        }
    }

    // Place an order and update inventory accordingly
    @Transactional
    public String placeOrder(Integer orderId) {
        Optional<Order> getOrder = orderRepository.findById(orderId);

        if (getOrder.isPresent()) {
            Order order = getOrder.get();
            Inventory inventory = order.getInventory();

            // Check if inventory has enough quantity for the order
            if (inventory.getQuantity() >= order.getQuantityOrdered()) {
                // Update inventory by reducing stock
                orderRepository.keepInventory(inventory.getInventoryID(), order.getQuantityOrdered());

                // Update the order status
                order.setDeliveryStatus("Processing");

                // Save the order and mark it as ordered
                orderRepository.save(order);
                orderRepository.ordering(orderId);

                return "Order placed and inventory updated successfully";
            } else {
                return "Insufficient inventory for this order";
            }
        } else {
            return "Order not found";
        }
    }

    // Cancel an order and revert changes to inventory if applicable
    @Transactional
    public String cancelOrder(Integer orderId) {
        Optional<Order> getOrder = orderRepository.findById(orderId);

        if (getOrder.isPresent()) {
            Order order = getOrder.get();

            // Only allow cancellation if the order has not been delivered
            if (!order.getDeliveryStatus().equals("Delivered")) {
                order.setDeliveryStatus("Cancelled");

                // Revert inventory changes (return stock to inventory)
                orderRepository.keepInventory(order.getInventory().getInventoryID(), -order.getQuantityOrdered());

                // Save the order with updated status
                orderRepository.save(order);

                return "Order cancelled and inventory reverted successfully";
            } else {
                return "Cannot cancel an already delivered order";
            }
        } else {
            return "Order not found";
        }
    }

    // Get a list of orders by their delivery status
    public List<Order> getOrdersByStatus(String deliveryStatus) {
        return orderRepository.findByDeliveryStatus(deliveryStatus);
    }

    // Get all orders in the system
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get a specific order by its ID
    public Optional<Order> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }
}
