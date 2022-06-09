package org.wgu.c482.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Outsourced;
import org.wgu.c482.models.Part;
import org.wgu.c482.utils.InvUtils;

import java.util.List;
import java.util.Optional;

import static org.wgu.c482.models.Inventory.getAllParts;
import static org.wgu.c482.utils.InvUtils.findAnyByCondition;
import static org.wgu.c482.utils.InvUtils.isInteger;

public class PartService {
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

    public static class Builder{
        private Integer id;
        private String name;
        private double price;
        private int stock;
        private int min;
        private int max;
        private Integer machineId;
        private String companyName;

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

        public Builder setMachineId(Integer machineId){
            if(machineId != null){
                this.machineId = machineId;
                this.companyName = null;
            }
            return this;
        }

        public Builder setCompanyName(String companyName){
            if(companyName != null){
                this.companyName = companyName;
                this.machineId = null;
            }

            return this;
        }

        private int autogenPartId() {
            int numOfParts = Inventory.getAllParts().size();
            int indexOfLastPart = numOfParts - 1;

            return Inventory.getAllParts().get(indexOfLastPart).getId() + 1;
        }

        public Part build(){
            if(this.id == null){
                this.id = autogenPartId();
            }

            if(this.machineId == null)
                return new Outsourced(id, name, price, stock, min, max, companyName);
            else
                return new InHouse(id, name, price, stock, min, max, machineId);
        }
    }
}
