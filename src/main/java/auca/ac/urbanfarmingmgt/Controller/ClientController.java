package auca.ac.urbanfarmingmgt.Controller;

import auca.ac.urbanfarmingmgt.Services.ClientService;
import auca.ac.urbanfarmingmgt.Model.Client;
import auca.ac.urbanfarmingmgt.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Save a new client
    @PostMapping
    public ResponseEntity<String> saveClient(@RequestBody Client client) {
        try {
            String response = clientService.saveClient(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update client information
    @PutMapping("/{clientId}")
    public ResponseEntity<String> updateClientInfo(@PathVariable Integer clientId, @RequestBody Client updatedClient) {
        try {
            String response = clientService.updateClientInfo(clientId, updatedClient);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Assign an order to a client
    @PostMapping("/{clientId}/assign-order")
    public ResponseEntity<String> assignOrderToClient(@PathVariable Integer clientId, @RequestBody Order order) {
        try {
            String response = clientService.assignOrderToClient(clientId, order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Update client payment history
    @PutMapping("/{clientId}/payment-history")
    public ResponseEntity<String> updateClientPaymentHistory(@PathVariable Integer clientId, @RequestBody String paymentHistory) {
        try {
            String response = clientService.updateClientPaymentHistory(clientId, paymentHistory);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Update client preferences
    @PutMapping("/{clientId}/preferences")
    public ResponseEntity<String> updateClientPreferences(@PathVariable Integer clientId, @RequestBody String orderPreferences) {
        try {
            String response = clientService.updateClientPreferences(clientId, orderPreferences);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Delete a client
    @DeleteMapping("/{clientId}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer clientId) {
        try {
            String response = clientService.deleteClient(clientId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get all clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    // Get a client by ID
    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Integer clientId) {
        try {
            Client client = clientService.getClientById(clientId);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get clients by contact information
    @GetMapping("/contact-info/{contactInfo}")
    public ResponseEntity<List<Client>> getClientsByContactInfo(@PathVariable String contactInfo) {
        List<Client> clients = clientService.getClientsByContactInfo(contactInfo);
        return ResponseEntity.ok(clients);
    }

    // Place an order for a client
    @PostMapping("/{clientId}/place-order")
    public ResponseEntity<Order> placeClientOrder(
            @PathVariable Integer clientId,
            @RequestParam Integer inventoryId,
            @RequestParam Double quantityOrdered) {
        try {
            Order order = clientService.placeClientOrder(clientId, inventoryId, quantityOrdered);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Handle delivery receipt for an order
    @PutMapping("/orders/{orderId}/deliver")
    public ResponseEntity<String> handleDeliveryReceipt(
            @PathVariable Integer orderId,
            @RequestParam Integer inventoryId) {
        try {
            clientService.handleDeliveryReceipt(inventoryId, orderId);
            return ResponseEntity.ok("Delivery receipt processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}