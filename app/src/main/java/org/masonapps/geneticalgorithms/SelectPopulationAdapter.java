package org.masonapps.geneticalgorithms;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by ims_3 on 11/30/2015.
 */
public class SelectPopulationAdapter extends RecyclerView.Adapter<SelectPopulationAdapter.ImageViewHolder> {

    private final ArrayList<Bitmap> images;
    private final ArrayList<ImageDNA> population;

    public SelectPopulationAdapter(ArrayList<Bitmap> images, ArrayList<ImageDNA> population) {
        this.images = images;
        this.population = population;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false), population);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.onBind(images.get(position), position);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;
        private final ImageView selectionView;
        private ArrayList<ImageDNA> population;

        public ImageViewHolder(View itemView, ArrayList<ImageDNA> population) {
            super(itemView);
            this.population = population;
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageDNAView);
            selectionView = (ImageView) itemView.findViewById(R.id.selectionView);
        }

        public void onBind(Bitmap bitmap, int position) {
            selectionView.setVisibility(population.get(position).selected ? View.VISIBLE : View.GONE);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            final ImageDNA dna = population.get(getAdapterPosition());
            dna.selected = !dna.selected;
            selectionView.setVisibility(dna.selected ? View.VISIBLE : View.GONE);
        }
    }
}
