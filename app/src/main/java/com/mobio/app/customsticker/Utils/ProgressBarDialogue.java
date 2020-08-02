package com.mobio.app.customsticker.Utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by nasif on 6/1/16.
 */
public class ProgressBarDialogue {
    Context context;
    ProgressDialog progressDialog;

    public ProgressBarDialogue(Context context) {
        this.context = context;
    }

    public void showProgressDialogue (String progressMessage) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage (progressMessage);
        progressDialog.setProgressStyle (ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate (true);
        progressDialog.setCancelable (false);
        progressDialog.show ();
    }

    public void dissmissProgressDialogue () {
        if (progressDialog.isShowing ())
            progressDialog.dismiss ();
    }
}
