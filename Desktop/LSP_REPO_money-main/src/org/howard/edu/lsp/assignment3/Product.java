package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;

/**
 * Model class representing a product record (data container).
 * Fields correspond to columns in data/products.csv.
 *
 * This class encapsulates the fields and provides getters/setters used
 * during transformation. It mirrors your Assignment 2 inner Product.
 */
public class Product {
    private final int id;
    private String name;
    private String category;
    private final String originalCategory;
    private String priceRange;
    private BigDecimal price;

    /**
     * Construct a Product instance.
     *
     * @param id product id
     * @param name product name
     * @param price product price
     * @param category original category
     */
    public Product(int id, String name, BigDecimal price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.originalCategory = category;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getOriginalCategory() { return originalCategory; }
    public String getPriceRange() { return priceRange; }
    public BigDecimal getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }
    public void setPrice(BigDecimal price) { this.price = price; }

    @Override
    public String toString() {
        return String.format("Product[%d,%s,%s,%s,%s]",
            id, name, price == null ? "null" : price.toPlainString(), category, priceRange);
    }
}
