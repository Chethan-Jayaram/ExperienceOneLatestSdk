
package com.example.experienceone.model.dinningmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("ItemCode")
    @Expose
    private String itemCode;
    @SerializedName("SubMenuCode")
    @Expose
    private String subMenuCode;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("ItemDescription")
    @Expose
    private Object itemDescription;
    @SerializedName("ItemImageUrl")
    @Expose
    private String itemImageUrl;
    @SerializedName("ItemPrice")
    @Expose
    private Integer itemPrice;
    @SerializedName("ItemHappyHourPrice")
    @Expose
    private Integer itemHappyHourPrice;
    @SerializedName("ItemType")
    @Expose
    private String itemType;
    @SerializedName("ItemAccount")
    @Expose
    private String itemAccount;
    @SerializedName("ItemAccountName")
    @Expose
    private String itemAccountName;
    @SerializedName("ItemPriceWithTax")
    @Expose
    private Double itemPriceWithTax;
    @SerializedName("ItemHappyHourPriceWithTax")
    @Expose
    private Double itemHappyHourPriceWithTax;
    @SerializedName("AddOnCategories")
    @Expose
    private Object addOnCategories;
    @SerializedName("PackageConstituents")
    @Expose
    private Object packageConstituents;
    @SerializedName("MenuItemClassifierInfo")
    @Expose
    private Object menuItemClassifierInfo;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSubMenuCode() {
        return subMenuCode;
    }

    public void setSubMenuCode(String subMenuCode) {
        this.subMenuCode = subMenuCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Object getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(Object itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public Integer getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getItemHappyHourPrice() {
        return itemHappyHourPrice;
    }

    public void setItemHappyHourPrice(Integer itemHappyHourPrice) {
        this.itemHappyHourPrice = itemHappyHourPrice;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemAccount() {
        return itemAccount;
    }

    public void setItemAccount(String itemAccount) {
        this.itemAccount = itemAccount;
    }

    public String getItemAccountName() {
        return itemAccountName;
    }

    public void setItemAccountName(String itemAccountName) {
        this.itemAccountName = itemAccountName;
    }

    public Double getItemPriceWithTax() {
        return itemPriceWithTax;
    }

    public void setItemPriceWithTax(Double itemPriceWithTax) {
        this.itemPriceWithTax = itemPriceWithTax;
    }

    public Double getItemHappyHourPriceWithTax() {
        return itemHappyHourPriceWithTax;
    }

    public void setItemHappyHourPriceWithTax(Double itemHappyHourPriceWithTax) {
        this.itemHappyHourPriceWithTax = itemHappyHourPriceWithTax;
    }

    public Object getAddOnCategories() {
        return addOnCategories;
    }

    public void setAddOnCategories(Object addOnCategories) {
        this.addOnCategories = addOnCategories;
    }

    public Object getPackageConstituents() {
        return packageConstituents;
    }

    public void setPackageConstituents(Object packageConstituents) {
        this.packageConstituents = packageConstituents;
    }

    public Object getMenuItemClassifierInfo() {
        return menuItemClassifierInfo;
    }

    public void setMenuItemClassifierInfo(Object menuItemClassifierInfo) {
        this.menuItemClassifierInfo = menuItemClassifierInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
