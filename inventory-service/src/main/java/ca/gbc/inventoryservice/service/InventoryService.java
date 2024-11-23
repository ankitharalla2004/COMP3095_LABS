package ca.gbc.inventoryservice.service;

/**
 * @project microservice-parent
 * @authorparam on
 **/
public interface InventoryService {

    public boolean isInStock(String skuCode, Integer quantity);

}
