package org.wgu.c482.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wgu.c482.models.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.wgu.c482.models.Inventory.getAllParts;
import static org.wgu.c482.models.Inventory.getAllProducts;

public class InventoryUtils {

    public static <T> Optional<T> findOneByCondition(List<T> array, Predicate<T> condition) {
        return array
                .stream()
                .filter(condition)
                .findAny();
    }

    public static <T> List<T> findAnyByCondition(List<T> array, Predicate<T> condition) {
        return array
                .stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    private static boolean isInteger(String query){
        return query.chars().allMatch(Character::isDigit);
    }

    public static class PartUtils {

        private static int autogenPartId() {
            int numOfParts = Inventory.getAllParts().size();
            int indexOfLastPart = numOfParts - 1;

            return Inventory.getAllParts().get(indexOfLastPart).getId() + 1;
        }

        public static InHouse createNewInHousePart(String name, double price, int stock, int min, int max, int machineId) {
            return new InHouse(autogenPartId(), name, price, stock, min, max, machineId);
        }

        public static Outsourced createNewOutsourcedPart(String name, double price, int stock, int min, int max, String companyName){
            return new Outsourced(autogenPartId(), name, price, stock, min, max, companyName);
        }

        public static ObservableList<Part> searchPart(String query){

            if(query.isEmpty() || query.isBlank())
                return null;

            final ObservableList<Part> newParts = FXCollections.observableArrayList();

            if(isInteger(query)){
                int idQuery = Integer.parseInt(query);
                Optional<Part> part = Optional.ofNullable(Inventory.lookupPart(idQuery));

                part.ifPresent(newParts::add);
            } else {
                List<Part> parts = filterPartsByName(query);

                if(!parts.isEmpty())
                    newParts.addAll(FXCollections.observableArrayList(parts));
            }

            return newParts;
        }

        private static List<Part> filterPartsByName(String query){
            return findAnyByCondition(getAllParts(), p -> p.getName().toLowerCase().startsWith(query));
        }

        public static void deleteFromParts(Part part) {
            if(Inventory.getAllParts().contains(part)){
                Inventory.deletePart(part);

                Inventory.getAllParts().stream()
                        .filter(p -> p.getId() > part.getId())
                        .forEach(p -> p.setId(p.getId() - 1));

            }
        }
    }

    public static class ProductUtils{

        private static int autogenProductId() {
            int numOfParts = Inventory.getAllProducts().size();
            int indexOfLastPart = numOfParts - 1;

            return Inventory.getAllProducts().get(indexOfLastPart).getId() + 1;
        }

        public static Product createNewProduct(String name, double price, int stock, int min, int max){
            return new Product(autogenProductId(), name, price, stock, min, max);
        }

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
    }
}
