package com.example.demo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Date;
import java.util.HashSet;

@Entity
@Table(name="inventory")
public class FoodItem {
    @Id
    private Long id;

    private String name;

    @JdbcTypeCode(SqlTypes.NUMERIC)
    private Number amount;
    private String amount_unit;

    private Date expiry_date;
    private Date purchase_date;

    private Boolean is_expiry_estimated = false;
    private Boolean soft_delete = false;

    private HashSet<String> owners = new HashSet<>();

    public FoodItem() { }

    public FoodItem(String foodName) {
        this.name = foodName;
        this.expiry_date = new Date(System.currentTimeMillis());
    }
    public FoodItem(String foodName, Number quantity) {
        this.name = foodName;
        this.amount = quantity;
        this.expiry_date = new Date(System.currentTimeMillis());
    }

    public FoodItem(String foodName, Number quantity, Date expiry_date) {
        this.name = foodName;
        this.amount = quantity;
        this.expiry_date = expiry_date;
    }

    public void copy_to(FoodItem dest) {
        dest.setId(id);
        dest.setName(name);
        dest.setAmount(amount);
        dest.setAmountUnit(amount_unit);
        dest.setExpiryDate(expiry_date);
        dest.setPurchaseDate(purchase_date);
        dest.setIsExpiryEstimated(is_expiry_estimated);
        dest.setSoftDelete(soft_delete);
        dest.setOwners(new HashSet<String>(owners));
    }

    public String getName() {
        return name;
    }

    public Number getAmount() {
        return amount;
    }

    public Date getExpiryDate() { return expiry_date; }


    public String getAmountUnit() {
        return amount_unit;
    }

    public Boolean getIsExpiryEstimated() {
        return is_expiry_estimated;
    }

    public Long getId() {
        return id;
    }

    public Date getPurchaseDate() {
        return purchase_date;
    }

    public Boolean getSoftDelete() {
        return soft_delete;
    }

    public HashSet<String> getOwners() {
        return owners;
    }

    public void setName(String foodName) {
        this.name = foodName;
    }

    public void setAmount(Number quantity) {
        this.amount = quantity;
    }

    public void setPurchaseDate(Date purchase_date) {
        this.purchase_date = purchase_date;
    }

    public void setSoftDelete(Boolean soft_delete) {
        this.soft_delete = soft_delete;
    }

    public void setExpiryDate(Date expiry_date) { this.expiry_date = expiry_date; }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmountUnit(String amount_unit) {
        this.amount_unit = amount_unit;
    }

    public void setIsExpiryEstimated(Boolean is_expiry_estimated) {
        this.is_expiry_estimated = is_expiry_estimated;
    }

    public void setOwners(HashSet<String> owners) {
        this.owners = owners;
    }

}
