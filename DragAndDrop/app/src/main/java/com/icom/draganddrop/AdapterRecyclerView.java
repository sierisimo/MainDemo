package com.icom.draganddrop;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.icom.draganddrop.dragDropSwipe.ItemTouchHelperAdapter;
import com.icom.draganddrop.dragDropSwipe.ItemTouchHelperViewHolder;
import com.icom.draganddrop.dragDropSwipe.OnStartDragListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by davidcordova on 04/03/16.
 */
public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> implements
        ItemTouchHelperAdapter {

    private final String TAG = AdapterRecyclerView.class.getSimpleName();

    private Activity activity;
    private List<MyBean> list;
    private OnStartDragListener onStartDragListener;
    private ScrollingActivity.IItemStuff iItemStuff;

    public AdapterRecyclerView(Activity context, List<MyBean> list, OnStartDragListener onStartDragListener, ScrollingActivity.IItemStuff iItemStuff) {
        this.activity = context;
        this.list = list;
        this.onStartDragListener = onStartDragListener;
        this.iItemStuff = iItemStuff;
    }

    @Override
    public AdapterRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterRecyclerView.ViewHolder holder, int position) {
        MyBean myBean = list.get(position);

        holder.tvText.setText(myBean.getText());
        Picasso.with(activity).load(myBean.getUrl()).into(holder.ivImg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position) {
        final MyBean itemRemoved = list.remove(position);
        notifyItemRemoved(position);
        Snackbar.make(activity.findViewById(android.R.id.content), "Card removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        list.add(position, itemRemoved);
                        notifyItemInserted(position);
                    }
                }).show();
    }

    public List<MyBean> getAdapterList() {
        return list;
    }

    public void addItem(MyBean myBean) {
        list.add(myBean);
        notifyDataSetChanged();
    }

    public void updateList(List<MyBean> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder,
            View.OnClickListener,
            View.OnTouchListener {

        public ImageView ivImg;
        public TextView tvText;
        public Button b1;

        public ViewHolder(View itemView) {
            super(itemView);

            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            b1 = (Button) itemView.findViewById(R.id.b_1);

            ((CardView) itemView.findViewById(R.id.cv)).setPreventCornerOverlap(false);

            itemView.findViewById(R.id.cv).setOnClickListener(this);
            b1.setOnTouchListener(this);
        }

        @Override
        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
//            itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cv:
                    iItemStuff.onItemPressed(list.get(getAdapterPosition()));
                    break;
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                onStartDragListener.onStartDrag(this);
            }
            return false;
        }
    }
}
