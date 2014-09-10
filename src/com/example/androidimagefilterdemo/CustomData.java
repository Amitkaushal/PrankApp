package com.example.androidimagefilterdemo;

/** This is just a simple class for holding data that is used to render our custom view */
public class CustomData {
    private int mBackgroundColor;
    private int mText;

    public CustomData(int backgroundColor, int photo1) {
        mBackgroundColor = backgroundColor;
        mText = photo1;
    }

    /**
     * @return the backgroundColor
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * @return the text
     */
    public int getText() {
        return mText;
    }
}
