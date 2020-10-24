package com.blucor.vsfarm.customecomponent;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.blucor.vsfarm.R;


public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Typeface fontType;


    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode())
            return;
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String fontName = ta.getString(R.styleable.CustomTextView_font_name);
        if (fontName != null) {
            final Typeface font = FontCache.getInstance().getFont(context, "fonts/" + fontName);
            setTypeface(font);
        }
        ta.recycle();
    }


}