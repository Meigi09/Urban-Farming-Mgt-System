package auca.ac.urbanfarmingmgt.Services;

import auca.ac.urbanfarmingmgt.Repository.ClientRepository;
import auca.ac.urbanfarmingmgt.Repository.OrderRepository;
import auca.ac.urbanfarmingmgt.Model.Client;
import auca.ac.urbanfarmingmgt.Model.Inventory;
import auca.ac.urbanfarmingmgt.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryService inventoryService;

    private Client getClientByIdOrThrow(Integer clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
    }

    public String saveClient(Client client) {
        if (client.getOrder() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The order must be specified for the client");
        }
        clientRepository.save(client);
        return "Client saved successfully";
    }

    // Update client information
    public String updateClientInfo(Integer clientId, Client updatedClient) {
        Client client = getClientByIdOrThrow(clientId);

        client.setName(updatedClient.getName());
        client.setContactInfo(updatedClient.getContactInfo());
        client.setOrderPreferences(updatedClient.getOrderPreferences());
        client.setPaymentHistory(updatedClient.getPaymentHistory());

        if (updatedClient.getOrder() != null) {
            client.setOrder(updatedClient.getOrder());
        }

        clientRepository.save(client);
        return "Client information updated successfully";
    }

    // Assign order to client
    public String assignOrderToClient(Integer clientId, Order order) {
        Client client = getClientByIdOrThrow(clientId);

        // Check if the inventory has enough stock for the order
        if (!inventoryService.checkAvailability(order.getInventory().getInventoryID(), order.getQuantityOrdered())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient inventory for this order");
        }

        // Save the order
        orderRepository.save(order);

        // Assign the order to the client
        client.setOrder(order);
        clientRepository.save(client);

        return "Order assigned to client successfully";
    }

    // Update client payment history
    public String updateClientPaymentHistory(Integer clientId, String paymentHistory) {
        Client client = getClientByIdOrThrow(clientId);

        client.setPaymentHistory(paymentHistory);
        clientRepository.save(client);
        return "Payment history updated successfully";
    }

    // Update client preferences
    public String updateClientPreferences(Integer clientId, String orderPreferences) {
        Client client = getClientByIdOrThrow(clientId);

        client.setOrderPreferences(orderPreferences);
        clientRepository.save(client);
        return "Order preferences updated successfully";
    }

    // Delete client
    public String deleteClient(Integer clientId) {
        if (clientRepository.existsById(clientId)) {
            clientRepository.deleteById(clientId);
            return "Client deleted successfully";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found");
        }
    }

    // Fetch all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Fetch client by ID
    public Client getClientById(Integer clientId) {
        return getClientByIdOrThrow(clientId);
    }

    // Fetch clients by contact information
    public List<Client> getClientsByContactInfo(String contactInfo) {
        return clientRepository.findByContactInfo(contactInfo);
    }

    // Method for placing a client order
    @Transactional
    public Order placeClientOrder(Integer clientId, Integer inventoryId, Double quantityOrdered) {
        Client client = getClientByIdOrThrow(clientId);

        // Check if the inventory has enough stock for the order
        if (!inventoryService.checkAvailability(inventoryId, quantityOrdered)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient inventory for this order");
        }

        // Create and save the order
        Order order = new Order();
        order.setQuantityOrdered(quantityOrdered);
        order.setOrderDate(new java.util.Date());
        order.setDeliveryStatus("Pending");

        // Fetchs the inventory and associate it with the order
        Inventory inventory = inventoryService.getInventoryById(inventoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found"));
        order.setInventory(inventory);

        // Save the order
        orderRepository.save(order);

        inventoryService.updateQuantityAfterOrder(inventoryId, quantityOrdered);

        client.setOrder(order);
        clientRepository.save(client);

        return order; // Return created order
    }

    // Handle delivery receipt (updating inventory and order status)
    @Transactional
    public void handleDeliveryReceipt(Integer inventoryId, Integer orderId) {
        // Fetch the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        order.setDeliveryStatus("Delivered");
        orderRepository.save(order);

    }
}