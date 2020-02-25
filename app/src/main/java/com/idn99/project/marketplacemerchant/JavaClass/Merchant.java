package com.idn99.project.marketplacemerchant.JavaClass;

public class Merchant {

    private int merchantId;
    private String merchantName;
    private String merchantSlug;

    public Merchant(int merchantId, String merchantName, String merchantSlug) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.merchantSlug = merchantSlug;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantSlug() {
        return merchantSlug;
    }
}
