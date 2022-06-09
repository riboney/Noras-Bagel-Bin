package org.wgu.c482.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

import static org.wgu.c482.utils.InvUtils.*;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    public static Part lookupPart(int partId){
        Optional<Part> part = findOneByCondition(getAllParts(), p -> p.getId() == partId);

        return part.orElse(null);
    }

    public static Part lookupPart(String partName) {
        Optional<Part> part = findOneByCondition(getAllParts(), p -> p.getName().equalsIgnoreCase(partName));

        return part.orElse(null);
    }

    public static Product lookupProduct(int productId) {
        Optional<Product> product = findOneByCondition(allProducts, (p -> p.getId() == productId));

        return product.orElse(null);
    }

    public static Product lookupProduct(String productName) {
        Optional<Product> product = findOneByCondition(allProducts, (p -> p.getName().equalsIgnoreCase(productName)));

        return product.orElse(null);
    }

    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    public static boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }

    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
