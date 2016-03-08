package com.icom.draganddrop;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.icom.draganddrop.dragDropSwipe.OnStartDragListener;
import com.icom.draganddrop.dragDropSwipe.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ScrollingActivity extends AppCompatActivity implements
        AppBarLayout.OnOffsetChangedListener,
        OnStartDragListener {

    private final String TAG = ScrollingActivity.class.getSimpleName();
    private ItemTouchHelper itemTouchHelper;

    private AppBarLayout appBarLayout;
//    private SwipeRefreshLayout srl;

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
//        RecyclerView rvGrid = (RecyclerView) findViewById(R.id.rv_grid);
        CardView cv = (CardView) findViewById(R.id.cv);

        SwipeDismissBehavior<CardView> swipe = new SwipeDismissBehavior<>();
        swipe.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        swipe.setListener(new SwipeDismissBehavior.OnDismissListener() {
            @Override
            public void onDismiss(View view) {
                Toast.makeText(ScrollingActivity.this, "Card swiped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDragStateChanged(int state) {

            }
        });

        LayoutParams params = (LayoutParams) cv.getLayoutParams();
        params.setBehavior(swipe);

//        final AdapterRecyclerView mAdapter = new AdapterRecyclerView(this, getAdapterList(10), this);
//        rvGrid.setAdapter(mAdapter);
//        rvGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//
//        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mAdapter.updateList(getAdapterList(10));
//                                srl.setRefreshing(false);
//                            }
//                        });
//                    }
//                }, 1500);
//            }
//        });

//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
//        itemTouchHelper = new ItemTouchHelper(callback);
//        itemTouchHelper.attachToRecyclerView(rvGrid);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Do you wanna add an item?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                mAdapter.addItem(new MyBean(
//                                        "http://lorempixel.com/400/200",
//                                        "The foundational elements of print based design typography, grids, space, scale, color"
//                                ));
//                                Log.i(TAG, "onClick: adapterListSize: " + mAdapter.getAdapterList().size());
                            }
                        }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<MyBean> getAdapterList(int size) {
        List<MyBean> list = new ArrayList<>();

        for (int i = 0; i != size; i++) {
            MyBean myBean = new MyBean();
            switch (i % 3) {
                case 0:
                    myBean.setUrl("http://s3.india.com/wp-content/uploads/2015/07/species1.jpg");
                    myBean.setText("The foundational elements of print based design typography, grids, space, scale, colorThe foundational elements of print based design typography, grids, space, scale, colorThe foundational elements of print based design typography, grids, space, scale, color");
                    break;
                case 1:
                    myBean.setUrl("http://lorempixel.com/400/200");
                    myBean.setText("The foundational elements of print based design typography, grids, space, scale, colorThe foundational elements of print based design typography, grids, space, scale, color");
                    break;
                case 2:
                    myBean.setUrl("http://atraccionla.com/comoconquistaraunamujer/wp-content/uploads/2014/03/ave-fenix.jpg");
                    myBean.setText("The foundational elements of print based design typography, grids, space, scale, color");
                    break;
            }
            list.add(myBean);
        }

        return list;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        srl.setEnabled(verticalOffset == 0);
    }
}
