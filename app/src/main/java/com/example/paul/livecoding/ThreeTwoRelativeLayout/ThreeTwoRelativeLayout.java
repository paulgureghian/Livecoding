package com.example.paul.livecoding.ThreeTwoRelativeLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ThreeTwoRelativeLayout extends RelativeLayout {

    public ThreeTwoRelativeLayout(Context context) {
        this(context, null);
    }

    public ThreeTwoRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThreeTwoRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widhtMeasureSpec, int heightMeasureSpec) {

        int threeTwoHeight = MeasureSpec.getSize(widhtMeasureSpec)*2/3;
        int threeTwoHeightSpec = MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widhtMeasureSpec, threeTwoHeightSpec);
    }
}
