package com.earthquake.se.timetodrop;

/**
 * Created by patsawut on 4/17/2015.
 */
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

public class RoundedRectTransformation implements com.squareup.picasso.Transformation {
    private final int radius;
    private final int margin;  // dp
    private final int stroke;

    // radius is corner radii in dp
    // margin is the board in dp
    public RoundedRectTransformation(final int radius,final int stroke, final int margin) {
        this.radius = radius;
        this.margin = margin;
        this.stroke = stroke;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF rF = new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin);
        canvas.drawRoundRect(rF, radius, radius, paint);

        // border
        Paint paintST = new Paint();
        paintST.setStyle(Paint.Style.STROKE);
        paintST.setStrokeWidth(stroke);
        paintST.setColor(0xff3d8ec6);    //stoke color
        canvas.drawRoundRect(rF, radius, radius, paintST);
        if (source != output) {
            source.recycle();
        }

        return output;
    }

    @Override
    public String key() {
        return "rounded(radius=" + radius + ", margin=" + margin + ")";
    }

}