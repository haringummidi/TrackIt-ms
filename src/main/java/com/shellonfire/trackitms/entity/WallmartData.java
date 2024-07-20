package com.shellonfire.trackitms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "WALLMART_DATA")
public class WallmartData {
    @Id
    @Column(name = "PRODUCT_URL", nullable = false, length = 500)
    private String productUrl;

    @Column(name = "`index`")
    private Long index;

    @Column(name = "SHIPPING_LOCATION")
    private Long shippingLocation;

    @Lob
    @Column(name = "DEPARTMENT")
    private String department;

    @Lob
    @Column(name = "CATEGORY")
    private String category;

    @Lob
    @Column(name = "SUBCATEGORY")
    private String subcategory;

    @Lob
    @Column(name = "BREADCRUMBS")
    private String breadcrumbs;

    @Column(name = "SKU")
    private Long sku;

    @Lob
    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Lob
    @Column(name = "BRAND")
    private String brand;

    @Column(name = "PRICE_RETAIL")
    private Double priceRetail;

    @Column(name = "PRICE_CURRENT")
    private Double priceCurrent;

    @Lob
    @Column(name = "PRODUCT_SIZE")
    private String productSize;

    @Column(name = "PROMOTION")
    private Double promotion;

    @Lob
    @Column(name = "RunDate")
    private String runDate;

    @Column(name = "tid")
    private Long tid;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

}