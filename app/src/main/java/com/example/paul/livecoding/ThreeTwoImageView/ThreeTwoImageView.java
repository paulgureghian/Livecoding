package com.example.paul.livecoding.threetwoimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ThreeTwoImageView extends ImageView {

    public ThreeTwoImageView(Context context) {
        super(context);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThreeTwoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        int threeTwoHeight = MeasureSpec.getSize(widthSpec) * 2 / 3;
        int threeTwoHeightSpec = MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthSpec, threeTwoHeightSpec);
    }
}

