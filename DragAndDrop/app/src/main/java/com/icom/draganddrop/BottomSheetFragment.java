package com.icom.draganddrop;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by davidcordova on 19/07/16.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TEXT = "text";
    private static final String TAG = BottomSheetFragment.class.getSimpleName();
    private String text;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    public BottomSheetFragment() {

    }

    public static BottomSheetFragment newInstance(String text) {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        Bundle arguments = new Bundle();
        arguments.putString(TEXT, text);
        bottomSheetFragment.setArguments(arguments);
        return bottomSheetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        text = arguments.containsKey(TEXT) ? arguments.getString(TEXT) : "No hay texto";
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        Log.d(TAG, "setupDialog() called with: " + "dialog = [" + dialog + "], style = [" + style + "]");
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet, null);
        TextView tvBottomSheet = ((TextView) contentView.findViewById(R.id.tv_bottom_sheet));
//        tvBottomSheet.setText(text);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}
