package org.wgu.c482.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    private final String BELOW_ZERO_ERR_MSG = "cannot be below 0!";

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.associatedParts = FXCollections.observableArrayList();
        this.id = id;
        this.setName(name);
        this.setPrice(price);
        this.setStock(stock);
        this.setMax(max);
        this.setMin(min);
    }

    public void addAssociatedParts(Part part) {
        this.associatedParts.add(part);
    }

    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        return this.associatedParts.remove(selectedAssociatedPart);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String invalidNameMsg = "Name cannot be blank or empty";

        if(name.isEmpty() || name.isBlank())
            throw new IllegalArgumentException(invalidNameMsg);
        else this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price < 0)
            throw new IllegalArgumentException("Price " + BELOW_ZERO_ERR_MSG);
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if(stock < 0)
            throw new IllegalArgumentException("Stock " + BELOW_ZERO_ERR_MSG);

        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        String invalidMinMsg = "Min cannot be higher than current max!";

        if(min < 0)
            throw new IllegalArgumentException("Min " + BELOW_ZERO_ERR_MSG);
        else if(min > this.max)
            throw new IllegalArgumentException(invalidMinMsg);
        else this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        String invalidMaxMsg = "Max cannot be lower than current min!";

        if(max < 0)
            throw new IllegalArgumentException("Max " + BELOW_ZERO_ERR_MSG);
        else if (max < this.min)
            throw new IllegalArgumentException(invalidMaxMsg);
        else this.max = max;
    }

    @Override
    public String toString() {
        return "Product{" +
                "associatedParts=" + associatedParts +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
