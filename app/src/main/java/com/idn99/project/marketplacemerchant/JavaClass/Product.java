package com.idn99.project.marketplacemerchant.JavaClass;

public class Product {

    private int productId;
    private String productName;
    private String productSlug;
    private int productQty;
    private String productImage;
    private Merchant merchant;
    private Categories category;

    public Product(int productId, String productName, String productSlug, int productQty, String productImage, Merchant merchant, Categories category) {
        this.productId = productId;
        this.productName = productName;
        this.productSlug = productSlug;
        this.productQty = productQty;
        this.productImage = productImage;
        this.merchant = merchant;
        this.category = category;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductSlug() {
        return productSlug;
    }

    public int getProductQty() {
        return productQty;
    }

    public String getProductImage() {
        return productImage;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Categories getCategory() {
        return category;
    }
}
