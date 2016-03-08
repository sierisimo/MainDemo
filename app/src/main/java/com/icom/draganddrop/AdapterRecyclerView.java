package com.icom.draganddrop;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
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

    public AdapterRecyclerView(Activity context, List<MyBean> list, OnStartDragListener onStartDragListener) {
        this.activity = context;
        this.list = list;
        this.onStartDragListener = onStartDragListener;
    }

    @Override
    public AdapterRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterRecyclerView.ViewHolder holder, int position) {
        MyBean myBean = list.get(position);

        Picasso.with(activity).load(myBean.getUrl()).into(holder.ivImg);

        holder.tvText.setText(myBean.getText());
        holder.b1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
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
            ItemTouchHelperViewHolder {

        public ImageView ivImg;
        public TextView tvText;
        public Button b1;

        public ViewHolder(View itemView) {
            super(itemView);

            ivImg = (ImageView) itemView.findViewById(R.id.iv_img);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
            b1 = (Button) itemView.findViewById(R.id.b_1);
        }

        @Override
        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
//            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
