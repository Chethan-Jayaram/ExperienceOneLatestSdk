package com.example.experienceone.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.experienceone.R;



public class CustomMessageHelper {

    private Dialog lDialogresume;
    private Context context;

    public CustomMessageHelper(Context context) {
        this.context = context;
        lDialogresume = new Dialog(context);
    }

    public void showCustomMessage(final Activity activity, String title, String msg, final Boolean bFlag, final Boolean activityfinishflag) {
        // TODO Auto-generated method stub
        try {
            if (lDialogresume != null && lDialogresume.isShowing()) {
                if (!((Activity) context).isFinishing()) {
                    lDialogresume.dismiss();
                }

            }
            //if (lDialogresume != null && lDialogresume.isShowing())return;

            lDialogresume = new Dialog(context);
            lDialogresume.requestWindowFeature(Window.FEATURE_NO_TITLE);
            lDialogresume.setCanceledOnTouchOutside(false);
            lDialogresume.setCancelable(false);
            lDialogresume.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lDialogresume.setContentView(R.layout.error_display_dailoug);
            ((TextView) lDialogresume.findViewById(R.id.dialog_title))
                    .setText(R.string.app_name);
            ((TextView) lDialogresume.findViewById(R.id.dialog_message))
                    .setText(msg);
            ((Button) lDialogresume.findViewById(R.id.ok)).setText("OK");

            ( lDialogresume.findViewById(R.id.ok))
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            try {
                                if (bFlag) {
                                    if (lDialogresume != null && lDialogresume.isShowing()) {
                                        if (!((Activity) context).isFinishing()) {
                                            lDialogresume.dismiss();
                                            dismissMessage(activity, activityfinishflag);
                                        }
                                    }
                                } else {
                                    if (lDialogresume != null && lDialogresume.isShowing()) {
                                        if (!((Activity) context).isFinishing()) {
                                            lDialogresume.dismiss();
                                            dismissMessage(activity, activityfinishflag);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
            if (!((Activity) context).isFinishing()) {
                lDialogresume.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dismissMessage(final Activity activity, Boolean activityfinishflag) {
        // TODO Auto-generated method stub
        if (activityfinishflag) {
            activity.finish();
        }
    }


}
