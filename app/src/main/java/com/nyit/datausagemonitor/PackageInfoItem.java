package com.nyit.datausagemonitor;


public class PackageInfoItem {
    private String mName, mListName;
    private int mId;
    private Long mUsage;


    public PackageInfoItem(String mName, int mId,String mListName) {
        this.mName = mName;
        this.mId = mId;
        this.mListName = mListName;
    }

    public PackageInfoItem(String mName, String mListName, int mId, long mUsage) {
        this.mName = mName;
        this.mListName = mListName;
        this.mId = mId;
        this.mUsage = mUsage;
    }

    public PackageInfoItem(String mListName, int mId, long mUsage) {
        this.mListName = mListName;
        this.mId = mId;
        this.mUsage = mUsage;
    }

    public long getmUsage() {
        return mUsage;
    }

    public void setmUsage(long mUsage) {
        this.mUsage = mUsage;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmListName() {
        return mListName;
    }

    public void setmListName(String mListName) {
        this.mListName = mListName;
    }
}
