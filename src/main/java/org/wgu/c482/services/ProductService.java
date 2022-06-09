package org.wgu.c482.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Product;

import java.util.List;
import java.util.Optional;

import static org.wgu.c482.models.Inventory.getAllProducts;
import static org.wgu.c482.utils.InvUtils.findAnyByCondition;
import static org.wgu.c482.utils.InvUtils.isInteger;

public class ProductService {
    public static ObservableList<Product> searchProduct(String query){
        if(query.isEmpty() || query.isBlank())
            return null;

        final ObservableList<Product> newProducts = FXCollections.observableArrayList();

        if(isInteger(query)){
            int idQuery = Integer.parseInt(query);
            Optional<Product> product = Optional.ofNullable(Inventory.lookupProduct(idQuery));

            product.ifPresent(newProducts::add);
        } else {
            List<Product> products = filterProductsByName(query);

            if(!products.isEmpty())
                newProducts.addAll(FXCollections.observableArrayList(products));
        }

        return newProducts;
    }

    private static List<Product> filterProductsByName(String query){
        return findAnyByCondition(getAllProducts(), p -> p.getName().toLowerCase().startsWith(query));
    }

    public static void deleteFromProducts(Product product) {
        if(Inventory.getAllProducts().contains(product)){
            Inventory.deleteProduct(product);

            Inventory.getAllProducts().stream()
                    .filter(p -> p.getId() > product.getId())
                    .forEach(p -> p.setId(p.getId() - 1));

        }
    }

    public static class Builder{
        private Integer id;
        private String name;
        private double price;
        private int stock;
        private int min;
        private int max;

        public Builder setId(int id){
            this.id = id;
            return this;
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setPrice(double price){
            this.price = price;
            return this;
        }

        public Builder setStock(int stock){
            this.stock = stock;
            return this;
        }

        public Builder setMin(int min){
            this.min = min;
            return this;
        }

        public Builder setMax(int max){
            this.max = max;
            return this;
        }

        private static int autogenProductId() {
            int numOfParts = Inventory.getAllProducts().size();
            int indexOfLastPart = numOfParts - 1;

            return Inventory.getAllProducts().get(indexOfLastPart).getId() + 1;
        }

        public Product build(){
            if(this.id == null){
                this.id = autogenProductId();
            }

            return new Product(id, name, price, stock ,min, max);
        }
    }
}
