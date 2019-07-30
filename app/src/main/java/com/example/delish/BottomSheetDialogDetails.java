package com.example.delish;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogDetails extends BottomSheetDialogFragment {
    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_dialog, null);

        dialog.setContentView(contentView);

    }
}
