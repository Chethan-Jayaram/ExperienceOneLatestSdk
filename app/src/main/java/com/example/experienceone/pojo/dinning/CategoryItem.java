
package com.example.experienceone.pojo.dinning;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryItem implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("itemImages")
    @Expose
    private List<ItemImage> itemImages = null;
    @SerializedName("ItemCode")
    @Expose
    private String itemCode;
    @SerializedName("SubMenuCode")
    @Expose
    private Object subMenuCode;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("ItemDescription")
    @Expose
    private String itemDescription;
    @SerializedName("ItemPrice")
    @Expose
    private String itemPrice;
    @SerializedName("ItemHappyHourPrice")
    @Expose
    private Object itemHappyHourPrice;
    @SerializedName("ItemType")
    @Expose
    private Object itemType;
    @SerializedName("ItemAccount")
    @Expose
    private Object itemAccount;
    @SerializedName("ItemAccountName")
    @Expose
    private Object itemAccountName;
    @SerializedName("ItemPriceWithTax")
    @Expose
    private Object itemPriceWithTax;
    @SerializedName("ItemHappyHourPriceWithTax")
    @Expose
    private Object itemHappyHourPriceWithTax;
    @SerializedName("AddOnCategories")
    @Expose
    private Object addOnCategories;
    @SerializedName("PackageConstituents")
    @Expose
    private Object packageConstituents;
    @SerializedName("MenuItemClassifierInfo")
    @Expose
    private Object menuItemClassifierInfo;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("last_activity_on")
    @Expose
    private String lastActivityOn;
    @SerializedName("location_id")
    @Expose
    private Integer locationId;
    @SerializedName("last_activity_by")
    @Expose
    private Integer lastActivityBy;
    @SerializedName("category")
    @Expose
    private Integer category;
    @SerializedName("ItemImageUrl")
    @Expose
    private List<String> itemImageUrl = null;

    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ItemImage> getItemImages() {
        return itemImages;
    }

    public void setItemImages(List<ItemImage> itemImages) {
        this.itemImages = itemImages;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Object getSubMenuCode() {
        return subMenuCode;
    }

    public void setSubMenuCode(Object subMenuCode) {
        this.subMenuCode = subMenuCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Object getItemHappyHourPrice() {
        return itemHappyHourPrice;
    }

    public void setItemHappyHourPrice(Object itemHappyHourPrice) {
        this.itemHappyHourPrice = itemHappyHourPrice;
    }

    public Object getItemType() {
        return itemType;
    }

    public void setItemType(Object itemType) {
        this.itemType = itemType;
    }

    public Object getItemAccount() {
        return itemAccount;
    }

    public void setItemAccount(Object itemAccount) {
        this.itemAccount = itemAccount;
    }

    public Object getItemAccountName() {
        return itemAccountName;
    }

    public void setItemAccountName(Object itemAccountName) {
        this.itemAccountName = itemAccountName;
    }

    public Object getItemPriceWithTax() {
        return itemPriceWithTax;
    }

    public void setItemPriceWithTax(Object itemPriceWithTax) {
        this.itemPriceWithTax = itemPriceWithTax;
    }

    public Object getItemHappyHourPriceWithTax() {
        return itemHappyHourPriceWithTax;
    }

    public void setItemHappyHourPriceWithTax(Object itemHappyHourPriceWithTax) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastActivityOn() {
        return lastActivityOn;
    }

    public void setLastActivityOn(String lastActivityOn) {
        this.lastActivityOn = lastActivityOn;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getLastActivityBy() {
        return lastActivityBy;
    }

    public void setLastActivityBy(Integer lastActivityBy) {
        this.lastActivityBy = lastActivityBy;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public List<String> getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(List<String> itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
