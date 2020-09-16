package com.example.RoomChef;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class JHJ_CustomDialog extends Dialog {

    // custom ClickEvent 생성 준비
    public interface CustomDialogClickListener {
        void onPositiveClick();
        void onNegativeClick();
    }

    private Context mContext;
    private CustomDialogClickListener customDialogClickListener;

    String name;
    String date;

    public JHJ_CustomDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener) {
        super(context);
        this.mContext = context;
        this.customDialogClickListener = customDialogClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jhj_custom_dialog);

        // Custom ClickEvent 사용 -------------------------------------------------------------------
//        findViewById(R.id.jhj_Custom_Dialog_Delete).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customDialogClickListener.onPositiveClick();
//                dismiss();
//            }
//        });
//        findViewById(R.id.jhj_Custom_Dialog_Cancle).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                customDialogClickListener.onNegativeClick();
//                dismiss();
//            }
//        });
        // -----------------------------------------------------------------------------------------

    }
}
