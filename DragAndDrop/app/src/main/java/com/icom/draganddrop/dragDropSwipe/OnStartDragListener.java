package com.icom.draganddrop.dragDropSwipe;

import android.support.v7.widget.RecyclerView;

/**
 * Created by davidcordova on 11/09/15.
 */
public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
