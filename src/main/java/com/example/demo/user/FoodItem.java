package com.example.demo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="inventory")
public class FoodItem {
    @Id
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String name;

    @JdbcTypeCode(SqlTypes.NUMERIC)
    private BigDecimal amount;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String amount_unit;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date expiry_date;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date purchase_date;

    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private Boolean is_expiry_estimated = false;

    @JdbcTypeCode(SqlTypes.BOOLEAN)
    private Boolean soft_delete = false;

    private List<String> owners = new ArrayList<>();

    public FoodItem() { }

    public FoodItem(String name) { this.name = name; }

    public void copy_to(FoodItem dest) {
        dest.setId(id);
        dest.setName(name);
        dest.setAmount(amount);
        dest.setAmountUnit(amount_unit);
        dest.setExpiryDate(expiry_date);
        dest.setPurchaseDate(purchase_date);
        dest.setIsExpiryEstimated(is_expiry_estimated);
        dest.setSoftDelete(soft_delete);
        dest.setOwners(owners);
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("amount")
    public Number getAmount() {
        return amount;
    }

    @JsonProperty("expiry_date")
    public Date getExpiryDate() { return expiry_date; }


    @JsonProperty("amount_unit")
    public String getAmountUnit() {
        return amount_unit;
    }

    @JsonProperty("is_expiry_estimated")
    public Boolean getIsExpiryEstimated() {
        return is_expiry_estimated;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("purchase_date")
    public Date getPurchaseDate() {
        return purchase_date;
    }

    @JsonProperty("soft_delete")
    public Boolean getSoftDelete() {
        return soft_delete;
    }

    @JsonProperty("owners")
    public List<String> getOwners() {
        return owners;
    }

    @JsonProperty("name")
    public void setName(String foodName) {
        this.name = foodName;
    }

    @JsonProperty("amount")
    public void setAmount(BigDecimal quantity) {
        this.amount = quantity;
    }

    @JsonProperty("purchase_date")
    public void setPurchaseDate(Date purchase_date) {
        this.purchase_date = purchase_date;
    }

    @JsonProperty("soft_delete")
    public void setSoftDelete(Boolean soft_delete) {
        this.soft_delete = soft_delete;
    }

    @JsonProperty("expiry_date")
    public void setExpiryDate(Date expiry_date) { this.expiry_date = expiry_date; }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("amount_unit")
    public void setAmountUnit(String amount_unit) {
        this.amount_unit = amount_unit;
    }

    @JsonProperty("is_expiry_estimated")
    public void setIsExpiryEstimated(Boolean is_expiry_estimated) {
        this.is_expiry_estimated = is_expiry_estimated;
    }

    @JsonProperty("owners")
    public void setOwners(List<String> owners) {
        this.owners = owners;
    }

}
