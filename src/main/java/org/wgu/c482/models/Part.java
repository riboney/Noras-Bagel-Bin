package org.wgu.c482.models; /**
* Supplied class Part.java 
 */

/**
 *
 * @author Place Your Name Here
 */
public abstract class Part {
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    private final String BELOW_ZERO_ERR_MSG = "cannot be below 0!";

    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.setName(name);
        this.setPrice(price);
        this.setStock(stock);
        this.setMax(max);
        this.setMin(min);
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        String invalidNameMsg = "Name cannot be blank or empty";

        if(name.isEmpty() || name.isBlank())
            throw new IllegalArgumentException(invalidNameMsg);
        else this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        if(price < 0)
            throw new IllegalArgumentException("Price " + BELOW_ZERO_ERR_MSG);
        this.price = price;
    }
    
    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        if(stock < 0)
            throw new IllegalArgumentException("Stock " + BELOW_ZERO_ERR_MSG);

        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        String invalidMinMsg = "Min cannot be higher than current max!";

        if(min < 0)
            throw new IllegalArgumentException("Min " + BELOW_ZERO_ERR_MSG);
        else if(min > this.max)
            throw new IllegalArgumentException(invalidMinMsg);
        else this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
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
        return "Part{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}