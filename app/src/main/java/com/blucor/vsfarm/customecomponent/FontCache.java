package com.blucor.vsfarm.customecomponent;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class FontCache {

    private static FontCache fontCache = new FontCache();
    private final Hashtable<String, Typeface> mCache = new Hashtable<>();
    private FontCache() {
    }
    /* Static 'instance' method */
    public static FontCache getInstance() {
        return fontCache;
    }

    public Typeface getFont(Context context, String fontPath) {
        if (!mCache.containsKey(fontPath)) {
            Typeface t = Typeface.createFromAsset(context.getAssets(), fontPath);
            mCache.put(fontPath, t);
        }
        return mCache.get(fontPath);
    }


}
