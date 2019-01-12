package com.example.android.bakeme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable {

    @SerializedName("quantity")
    private Double mQuantity;

    @SerializedName("measure")
    private String mUnit;

    @SerializedName("ingredient")
    private String mIngredientName;

    public Ingredient(){}

    public Ingredient(double quantity, String unit, String name){

        this.mQuantity = quantity;
        this.mUnit = unit;
        this.mIngredientName = name;

    }

    public Double getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(Double mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getmUnit() {
        return mUnit;
    }

    public void setmUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    public String getmIngredientName() {
        return mIngredientName;
    }

    public void setmIngredientName(String mIngredientName) {
        this.mIngredientName = mIngredientName;
    }


    protected Ingredient(Parcel in) {
        mQuantity = in.readDouble();
        mUnit = in.readString();
        mIngredientName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mQuantity);
        dest.writeString(mUnit);
        dest.writeString(mIngredientName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
