package com.lzq.econnect.base;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 *
 * Created by lzq on 2017/3/16.
 */

public abstract class BaseItemViewHolder extends RecyclerView.ViewHolder{

    public BaseItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }
}
