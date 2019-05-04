package com.nyit.datausagemonitor;

public class StoreTimestamp {

    long [][] mNDaysData;

    public StoreTimestamp() {

    }

    public long[][] getLastNDays(int n){


        mNDaysData =new long[n][2];
        int size = n-1;
        long ends = System.currentTimeMillis();
        long starts = ends-86400000l;

        for(int i =0; i <=size; i++){

            mNDaysData[size-i][1]= ends;
            mNDaysData[size-i][0]=starts;

            n--;

            ends = starts;
            starts = starts -86400000l;
        }

        return mNDaysData;
    }
}
