package com.example.demo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;

@Entity
@Table(name="inventory")
public class ConstantInventoryFoodItem implements FoodItem {
    @Id
    private Long id;

    private String name;

    @JdbcTypeCode(SqlTypes.NUMERIC)

    private Date expiry_date;
    private Date purchase_date;

    private Boolean is_expiry_estimated = false;
    private Boolean soft_delete = false;

    private HashSet<String> owners = new HashSet<>();

    public ConstantInventoryFoodItem() { }

    public ConstantInventoryFoodItem(String foodName) {
        this.name = foodName;
        this.expiry_date = new Date(System.currentTimeMillis());
    }

    public ConstantInventoryFoodItem(String foodName, Date expiry_date) {
        this.name = foodName;
        this.expiry_date = expiry_date;
    }

    public void copy_to(FoodItem dest) {
        dest.setId(id);
        dest.setName(name);

        dest.setExpiryDate(expiry_date);
        dest.setPurchaseDate(purchase_date);
        dest.setIsExpiryEstimated(is_expiry_estimated);
        dest.setSoftDelete(soft_delete);
        dest.setOwners(new HashSet<String>(owners));
    }

    public String getName() {
        return name;
    }


    public Date getExpiryDate() { return expiry_date; }



    public Boolean getIsExpiryEstimated() {
        return is_expiry_estimated;
    }

    public Long getId() { return id; }

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

    @Override
    public void setAmount(Number amount) {
        // no behavior implementation, as item is constant
    }

    @Override
    public void setAmountUnit(String amount_unit) {
        // no behavior implementation, as item is constant
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


    public void setIsExpiryEstimated(Boolean is_expiry_estimated) {
        this.is_expiry_estimated = is_expiry_estimated;
    }

    public void setOwners(HashSet<String> owners) {
        this.owners = owners;
    }

    @Override
    public boolean validateFoodItem() {
        boolean validConditions = true;

        // user will have this ingredient constantly, hence no amount checks are needed

        //purchase date cannot be later than the current date, as the item is in the inventory
        LocalDate purchase_date_local = purchase_date.toLocalDate();
        LocalDate present = LocalDate.now();
        if(purchase_date_local.isAfter(present)){
            validConditions=false;
        }

        return validConditions;
    }

}
