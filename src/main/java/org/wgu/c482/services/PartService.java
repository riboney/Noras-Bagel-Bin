package org.wgu.c482.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.wgu.c482.models.InHouse;
import org.wgu.c482.models.Inventory;
import org.wgu.c482.models.Outsourced;
import org.wgu.c482.models.Part;

import java.util.List;
import java.util.Optional;

import static org.wgu.c482.Main.nextPartId;
import static org.wgu.c482.models.Inventory.getAllParts;
import static org.wgu.c482.utils.InvUtils.findAnyByCondition;
import static org.wgu.c482.utils.InvUtils.isInteger;

/** Business logic for Part */
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

    /** Part Builder */
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

        public Part build(){
            if(this.id == null){
                this.id = nextPartId;
                nextPartId++;
            }

            if(this.machineId == null)
                return new Outsourced(id, name, price, stock, min, max, companyName);
            else
                return new InHouse(id, name, price, stock, min, max, machineId);
        }
    }
}
