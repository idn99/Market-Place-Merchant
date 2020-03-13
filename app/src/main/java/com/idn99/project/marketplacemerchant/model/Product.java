package com.idn99.project.marketplacemerchant.model;

public class Product {

    private int productId;
    private String productName;
    private String productSlug;
    private int productQty;
    private String productImage;
    private int productPrice;
    private String productDesc;
    private int merchantId;
    private Merchant merchant;
    private Categories category;

    public Product(int productId, String productName, String productSlug, int productQty, String productImage, int productPrice, String productDesc, int merchantId, Merchant merchant, Categories category) {
        this.productId = productId;
        this.productName = productName;
        this.productSlug = productSlug;
        this.productQty = productQty;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productDesc = productDesc;
        this.merchantId = merchantId;
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

    public int getProductPrice() {
        return productPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Categories getCategory() {
        return category;
    }
}
