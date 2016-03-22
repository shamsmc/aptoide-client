package com.aptoide.amethyst.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptoide.amethyst.R;
import com.aptoide.amethyst.ui.IMediaObject;
import com.aptoide.amethyst.ui.Screenshot;
import com.aptoide.amethyst.ui.Video;
import com.aptoide.amethyst.ui.listeners.MediaObjectListener;
import com.aptoide.amethyst.utils.AptoideUtils;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;


import com.aptoide.amethyst.viewholders.main.ScreenshotsViewHolder;

/**
 * Created by gmartinsribeiro on 01/12/15.
 */
public class ScreenshotsAdapter extends RecyclerView.Adapter<ScreenshotsViewHolder> {

    private final RequestManager glide;
    private ArrayList<IMediaObject> items;
    private int numberOfVideos;
    private ArrayList<String> imagesUrl;

    public ScreenshotsAdapter(final Context context, final RequestManager glide, ArrayList<IMediaObject> items) {
        this.glide = glide;
        this.items = items;
        this.imagesUrl = new ArrayList<>();
        this.numberOfVideos = 0;
        for (IMediaObject item : items) {
            if (item instanceof Screenshot) {
                imagesUrl.add(AptoideUtils.UI.screenshotToThumb(context, item.getImageUrl(), ((Screenshot) item).getOrient()));
            }else if(item instanceof Video){
                numberOfVideos++;
            }
        }
    }
    @Override
    public ScreenshotsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        View inflate = LayoutInflater.from(context).inflate(R.layout.row_item_screenshots_gallery, parent, false);

        return new ScreenshotsViewHolder(inflate, viewType);
    }

    @Override
    public void onBindViewHolder(ScreenshotsViewHolder holder, int position) {
        IMediaObject item = items.get(position);

        if (item instanceof Screenshot) {
            String thumbnail = AptoideUtils.UI.screenshotToThumb(holder.itemView.getContext(), item.getImageUrl(), ((Screenshot) item).getOrient());
            holder.media_layout.setForeground(null);
            holder.play_button.setVisibility(View.GONE);
            glide.load(thumbnail).placeholder(getPlaceholder(((Screenshot) item).getOrient())).into(holder.screenshot);
            holder.screenshot.setOnClickListener(new MediaObjectListener.ScreenShotsListener(holder.itemView.getContext(), imagesUrl, position - numberOfVideos));
            holder.media_layout.setOnClickListener(new MediaObjectListener.ScreenShotsListener(holder.itemView.getContext(), imagesUrl, position - numberOfVideos));
        }else if (item instanceof Video) {
            glide.load(item.getImageUrl()).placeholder(R.drawable.placeholder_300x300).into(holder.screenshot);
            holder.media_layout.setForeground(holder.itemView.getContext().getResources().getDrawable(R.color.overlay_black));
            holder.play_button.setVisibility(View.VISIBLE);
            holder.screenshot.setOnClickListener(new MediaObjectListener.VideoListener(holder.itemView.getContext(), ((Video) item).getVideoUrl()));
            holder.media_layout.setOnClickListener(new MediaObjectListener.VideoListener(holder.itemView.getContext(), ((Video) item).getVideoUrl()));
        }
    }

    private int getPlaceholder(String orient) {
        int id;
        if(orient != null && orient.equals("portrait")){
            id = R.drawable.placeholder_144x240;
        }else{
            id = R.drawable.placeholder_256x160;
        }
        return id;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
