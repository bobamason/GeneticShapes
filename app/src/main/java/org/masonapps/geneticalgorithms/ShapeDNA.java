package org.masonapps.geneticalgorithms;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by ims_3 on 11/25/2015.
 */
public class ShapeDNA extends ImageDNA {

    private static final int NUM_POINTS = 18;
    private static final float ANGLE_STEP = (float) Math.PI * 2f / (float) NUM_POINTS;
    private Path path;
    private Paint paint;
    public float[] rads = new float[NUM_POINTS];
    public float[] colors = new float[3];

    public ShapeDNA() {
        super();
        init();
        for (int i = 0; i < NUM_POINTS; i++) {
            rads[i] = random.nextFloat() * 0.85f + 0.05f;
        }
        for (int i = 0; i < colors.length; i++) {
            colors[i] = random.nextFloat();
        }
    }

    public ShapeDNA(ShapeDNA p1, ShapeDNA p2) {
        super();
        init();
        for (int i = 0; i < NUM_POINTS; i++) {
            rads[i] = random.nextInt(2) == 0 ? p1.rads[i] : p2.rads[i];
        }
        for (int i = 0; i < colors.length; i++) {
            colors[i] = random.nextInt(2) == 0 ? p1.colors[i] : p2.colors[i];
        }
    }

    private void init() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public ImageDNA breed(ImageDNA partner) {
        return new ShapeDNA(this, (ShapeDNA) partner);
    }

    @Override
    public void mutate(float prob) {
        for (int i = 0; i < rads.length; i++) {
            if (random.nextFloat() < prob)
                rads[i] = random.nextFloat() * 0.85f + 0.05f;
        }

        for (int i = 0; i < colors.length; i++) {
            if (random.nextFloat() < prob)
                colors[i] = random.nextFloat();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        float a;
        float cx = canvas.getWidth() / 2f;
        float cy = canvas.getHeight() / 2f;
        float s = Math.min(cx, cy);
        paint.setColor(Color.rgb(Math.round(255 * colors[0]), Math.round(255 * colors[1]), Math.round(255 * colors[2])));
        path.reset();
        path.moveTo(s * rads[0] + cx, cy);
        for (int i = 1; i < NUM_POINTS; i++) {
            a = i * ANGLE_STEP;
            path.lineTo((float) Math.cos(a) * s * rads[i] + cx, (float) Math.sin(a) * s * rads[i] + cy);
        }
        path.close();
        canvas.drawPath(path, paint);
    }
}
