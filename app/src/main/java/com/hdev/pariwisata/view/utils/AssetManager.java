package com.hdev.pariwisata.view.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * 12 Agustus 2019
 * @author gdev (Hendriyawan)
 */
public class AssetManager {

    /**
     *
     * @param context, context
     * @return, return json content
     */
    public static String loadJson(Context context) {
        String jsonResult = "";
        try {
            InputStream stream = context.getAssets().open("pariwisata.json");
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
            jsonResult = new String(buffer, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }


    /**
     *
     * @param context, context
     * @param filename, file name of jpg in asset folder
     * @return, return result drawable
     */
    public static Drawable drawableFromAsset(Context context, String filename) {
        Drawable drawable = null;
        try {
            InputStream inputStream = context.getAssets().open(filename);
            drawable = Drawable.createFromStream(inputStream, filename);
            inputStream.close();

        } catch (IOException e) {
            //handle exception
            e.printStackTrace();
        }
        return drawable;
    }

    /**
     *
     * @param context, context
     * @param fontName, font file name
     * @return
     */
    public static Typeface loadTypeface(Context context, String fontName){
        return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
    }
}
