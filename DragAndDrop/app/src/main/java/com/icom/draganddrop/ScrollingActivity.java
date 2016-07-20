package com.icom.draganddrop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.icom.draganddrop.dragDropSwipe.OnStartDragListener;
import com.icom.draganddrop.dragDropSwipe.SimpleItemTouchHelperCallback;
import com.icom.draganddrop.notification.NotificationService;

import java.util.ArrayList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements
        View.OnClickListener,
        AppBarLayout.OnOffsetChangedListener,
        OnStartDragListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = ScrollingActivity.class.getSimpleName();
    IItemStuff iItemStuff = new IItemStuff() {
        @Override
        public void onItemPressed(MyBean myBean) {
            BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetFragment.newInstance(myBean.getText());
            bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        }
    };
    private ItemTouchHelper itemTouchHelper;
    private Intent mServiceIntent;
    private AppBarLayout appBarLayout;
    private RecyclerView rvGrid;
    private SwipeRefreshLayout srl;
    private AdapterRecyclerView mAdapter;
    private boolean isLayoutGrid = true;

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

        mServiceIntent = new Intent(getApplicationContext(), NotificationService.class);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        rvGrid = (RecyclerView) findViewById(R.id.rv_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_2);

        setSupportActionBar(toolbar);

        mAdapter = new AdapterRecyclerView(this, getAdapterList(10), this, iItemStuff);
        rvGrid.setAdapter(mAdapter);
        rvGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvGrid);

        fab.setOnClickListener(this);
        fab2.setOnClickListener(this);
        srl.setOnRefreshListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_change_layout_manager:
                if (isLayoutGrid) {
                    rvGrid.setLayoutManager(new LinearLayoutManager(ScrollingActivity.this));
                    isLayoutGrid = false;
                } else {
                    rvGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                    isLayoutGrid = true;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        srl.setEnabled(verticalOffset == 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Snackbar.make(view, "Do you wanna add an item?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mAdapter.addItem(new MyBean(
                                        "http://lorempixel.com/400/200",
                                        "The foundational elements of print based design typography, grids, space, scale, color"
                                ));
                                Log.i(TAG, "onClick: adapterListSize: " + mAdapter.getAdapterList().size());
                            }
                        }).show();
                break;

            case R.id.fab_2:
                mServiceIntent.putExtra("MESSAGE", "Aquí va un mensaje");
                mServiceIntent.putExtra("TIMER", 3000);
                mServiceIntent.setAction("ACTION");
                startService(mServiceIntent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.updateList(getAdapterList(10));
                srl.setRefreshing(false);
            }
        }, 1500);
    }

    public interface IItemStuff {
        void onItemPressed(MyBean myBean);
    }
}
