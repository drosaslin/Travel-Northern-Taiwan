package com.example.android.trip_organizer;

public class ExpenseItem {
    private String mCategoryName;
    private int mCategoryImage;

    public ExpenseItem(String categoryName, int categoryImage){
        mCategoryName = categoryName;
        mCategoryImage = categoryImage;
    }

    public String getmCategoryName(){
        return mCategoryName;
    }

    public int getmCategoryImage(){
        return mCategoryImage;
    }
}
