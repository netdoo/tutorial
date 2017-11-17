package com.exxodusdb.domain;

public class EPFileLine {

    String id;
    String title;
    String pcPrice;
    String pcMobile = "";
    String normalPrice = "";
    String link = "";
    String mobileLink ="";
    String imageLink = "";
    String categoryName1 = "";
    String categoryName2 = "";
    String categoryName3 = "";
    String categoryName4 = "";
    String naverCategory = "";
    String naverProductId = "";
    String condition = "";
    String importFlag = "";
    String parallelImport = "";
    String orderMade = "";
    String productFlag = "";
    String adult = "";
    String modelNumber = "";
    String brand = "";
    String maker = "";
    String eventWords = "";
    String partnerCouponDownload = "";
    String installationCosts = "";
    String preMatchCode = "";
    String searchTag = "";
    String groupId = "";
    String coordiId = "";
    String minimumPurchaseQuantity = "";
    String reviewCount = "";
    String shipping = "";
    String deliveryGrade = "";
    String deliveryDetail = "";

    String opClass;
    String updateTime = "2017-11-16 06:15:11";

    public EPFileLine(String id, String title, String pcPrice) {
        this.id = id;
        this.title = title;
        this.pcPrice = pcPrice;
    }

    public EPFileLine(String id, String title, String pcPrice, String opClass) {
        this.id = id;
        this.title = title;
        this.pcPrice = pcPrice;
        this.opClass = opClass;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setPcPrice(String pcPrice) {
        this.pcPrice = pcPrice;
    }

    public String getPcPrice() {
        return this.pcPrice;
    }

    public void setOpClass(String opClass) {
        this.opClass = opClass;
    }

    public String getOpClass() {
        return this.opClass;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(id).append("\t")
                .append(title).append("\t")
                .append(pcPrice).append("\t")
                .append(pcMobile).append("\t")
                .append(normalPrice).append("\t")
                .append(link).append("\t")
                .append(mobileLink).append("\t")
                .append(imageLink).append("\t")
                .append(categoryName1).append("\t")
                .append(categoryName2).append("\t")
                .append(categoryName3).append("\t")
                .append(categoryName4).append("\t")
                .append(naverCategory).append("\t")
                .append(naverProductId).append("\t")
                .append(condition).append("\t")
                .append(importFlag).append("\t")
                .append(parallelImport).append("\t")
                .append(orderMade).append("\t")
                .append(productFlag).append("\t")
                .append(adult).append("\t")
                .append(modelNumber).append("\t")
                .append(brand).append("\t")
                .append(maker).append("\t")
                .append(eventWords).append("\t")
                .append(partnerCouponDownload).append("\t")
                .append(installationCosts).append("\t")
                .append(preMatchCode).append("\t")
                .append(searchTag).append("\t")
                .append(groupId).append("\t")
                .append(coordiId).append("\t")
                .append(minimumPurchaseQuantity).append("\t")
                .append(reviewCount).append("\t")
                .append(shipping).append("\t")
                .append(deliveryGrade).append("\t")
                .append(deliveryDetail).append("\t")
                ;

        if (this.opClass != null) {
            builder.append(opClass).append("\t");
            builder.append(updateTime);
        }

        return builder.toString();
    }
}
