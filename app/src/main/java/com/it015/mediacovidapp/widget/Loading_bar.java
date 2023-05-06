package com.it015.mediacovidapp.widget;

import static com.it015.mediacovidapp.R.layout.loading_bar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.it015.mediacovidapp.R;

public class Loading_bar {
    public Context context;

    public Loading_bar(Context context) {
        this.context = context;
    }

    public Dialog loadDialog(String title){
        Dialog dialog;
        dialog=new Dialog(context);
        dialog.setContentView(loading_bar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView t=dialog.findViewById(R.id.label_loading);
        t.setText(title);
        return dialog;
    }
}
