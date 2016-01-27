package org.masonapps.geneticalgorithms;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by ims_3 on 11/24/2015.
 */
public abstract class ImageDNA {

    public static final Random random = new Random();
    public boolean selected = false;

    public ImageDNA() {
    }

    public abstract ImageDNA breed(ImageDNA partner);

    public abstract void mutate(float prob);

    public abstract void draw(Canvas canvas);

    public void writeToBitmap(Bitmap bitmap){
        if(!bitmap.isMutable()) throw new IllegalArgumentException("Bitmap must be mutable");
        final Canvas c = new Canvas(bitmap);
        c.drawColor(Color.BLACK);
        draw(c);
    }
}

/*      resampling wheel python
        m = 0
        for v in w:
        if v > m:
        m = v
        index = random.randint(0, N-1)
        for i in range(N):
        b = random.uniform(0, 2 * m)
        while w[index] < b:
        b = b - w[index]
        index = index + 1
        index = index % N
        print index
        p3.append(p[index]) */
