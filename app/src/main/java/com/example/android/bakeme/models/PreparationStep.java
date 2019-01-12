package com.example.android.bakeme.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PreparationStep implements Parcelable {

    @SerializedName("id")
    private Integer mStepId;

    @SerializedName("shortDescription")
    private String mShortDescription;

    @SerializedName("description")
    private String mLongDescription;

    @SerializedName("videoURL")
    private String mVideoURL;

    public PreparationStep(){}

    public PreparationStep(int id, String shortDesc, String longDesc, String videoURL) {

        this.mStepId = id;
        this.mShortDescription = shortDesc;
        this.mLongDescription = longDesc;
        this.mVideoURL = videoURL;
    }

    public Integer getmStepId() {
        return mStepId;
    }

    public void setmStepId(Integer mStepId) {
        this.mStepId = mStepId;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public void setmShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getmLongDescription() {
        return mLongDescription;
    }

    public void setmLongDescription(String mLongDescription) {
        this.mLongDescription = mLongDescription;
    }

    public String getmVideoURL() {
        return mVideoURL;
    }

    public void setmVideoURL(String mVideoURL) {
        this.mVideoURL = mVideoURL;
    }



    protected PreparationStep(Parcel in) {
        mStepId = in.readInt();
        mShortDescription = in.readString();
        mLongDescription = in.readString();
        mVideoURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mStepId);
        dest.writeString(mShortDescription);
        dest.writeString(mLongDescription);
        dest.writeString(mVideoURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PreparationStep> CREATOR = new Parcelable.Creator<PreparationStep>() {
        @Override
        public PreparationStep createFromParcel(Parcel in) {
            return new PreparationStep(in);
        }

        @Override
        public PreparationStep[] newArray(int size) {
            return new PreparationStep[size];
        }
    };
}
