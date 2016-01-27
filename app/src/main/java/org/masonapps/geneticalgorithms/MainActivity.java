package org.masonapps.geneticalgorithms;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int POPULATION_SIZE = 50;
    private RecyclerView recyclerView;
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private ArrayList<ImageDNA> population = new ArrayList<>();
    private Random random = new Random();
    private DisplayMetrics displayMetrics;
    private View loadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingLayout = findViewById(R.id.loadingLayout);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        recyclerView.setLayoutManager(new GridLayoutManager(this, displayMetrics.widthPixels / Math.round(displayMetrics.density * 120)));

        new InitializeBitmapsTask().execute();

        findViewById(R.id.newGenBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGeneration();
            }
        });

        findViewById(R.id.resetBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPopulation();
            }
        });
    }

    public void setLoadingVisibility(boolean visible){
        loadingLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void resetPopulation() {
        new ResetPopulationTask().execute();
    }

    private void newGeneration() {
        new NewGenerationTask().execute();
    }

    private class InitializeBitmapsTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            setLoadingVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < POPULATION_SIZE; i++) {
                final int w = Math.round(displayMetrics.density * (120 - 16));
                final Bitmap bitmap = Bitmap.createBitmap(w, w, Bitmap.Config.ARGB_8888);
                ShapeDNA dna = new ShapeDNA();
                dna.writeToBitmap(bitmap);
                population.add(dna);
                bitmaps.add(bitmap);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                recyclerView.setAdapter(new SelectPopulationAdapter(bitmaps, population));
                setLoadingVisibility(false);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class NewGenerationTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            setLoadingVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<ImageDNA> selected = new ArrayList<>();
            for (ImageDNA dna : population) {
                if (dna.selected) {
                    selected.add(dna);
                }
            }
            if (selected.size() < 2) {
                Toast.makeText(MainActivity.this, "2 or more images must be selected to create a new generation", Toast.LENGTH_LONG).show();
                return null;
            }
            ArrayList<ImageDNA> nextGen = new ArrayList<>();
            while (nextGen.size() < POPULATION_SIZE) {
                final int i1 = random.nextInt(selected.size());
                final int i2 = random.nextInt(selected.size());
                if (i1 != i2) {
                    final ShapeDNA shapeDNA = new ShapeDNA((ShapeDNA) selected.get(i1), (ShapeDNA) selected.get(i2));
                    shapeDNA.mutate(0.05f);
                    nextGen.add(shapeDNA);
                }
            }
            population.clear();
            population.addAll(nextGen);
            for (int i = 0; i < POPULATION_SIZE; i++) {
                population.get(i).writeToBitmap(bitmaps.get(i));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                recyclerView.getAdapter().notifyDataSetChanged();
                setLoadingVisibility(false);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class ResetPopulationTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            setLoadingVisibility(true);
            population.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < POPULATION_SIZE; i++) {
                ShapeDNA dna = new ShapeDNA();
                dna.writeToBitmap(bitmaps.get(i));
                population.add(dna);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                recyclerView.getAdapter().notifyDataSetChanged();
                setLoadingVisibility(false);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
