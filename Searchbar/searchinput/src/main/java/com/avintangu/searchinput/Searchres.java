package com.avintangu.searchinput;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Searchres {

    private static Context gContext;
    Progressbar progressindc;

    public Searchres(Context context) {
        gContext = context;
    }

    /*Generate unique ids*/
    public static int generateViewId() {
        AtomicInteger sNextGeneratedId = new AtomicInteger(1);

        if (Build.VERSION.SDK_INT < 17) {
            for (; ; ) {
                int result = sNextGeneratedId.get();

                int newValue = result + 1;
                if (newValue > 0x00FFFFFF)
                    newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

    /*  Build search dialogue */
    public void spinMod(final TextView txtmod, final double eunica, final ArrayList txtlist) {

        final String detocs = "";

        txtmod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtmod.setClickable(false);
                txtmod.setFocusable(false);

                progressindc = new Progressbar(gContext);
                progressindc.showProgress();

                /*Build dialogue*/
                AlertDialog.Builder dialogbld = new AlertDialog.Builder(gContext);
                LayoutInflater inflater = (LayoutInflater) gContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.spinsearch, null);
                LinearLayout lnlay = dialogView.findViewById(R.id.cotactlay);
                dialogbld.setView(dialogView);

                ImageView btncancel = dialogView.findViewById(R.id.cnclButton);
                final EditText inpocs = dialogView.findViewById(R.id.ttlehead);
                final AlertDialog dilog = dialogbld.create();
                dilog.setCancelable(false);
                dilog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


                btncancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dilog.dismiss();
                        txtmod.setClickable(true);
                        txtmod.setFocusable(true);

                    }
                });

                /*Build dialogue*/
                new Searchres.srchSpin(txtmod, eunica, lnlay, inpocs, dilog, txtlist).execute(detocs);

            }
        });
    }

    /*Set dialogue data*/
    private class srchSpin extends AsyncTask<String, Void, String> {
        final TextView param;
        final double parama;
        final LinearLayout paramb;
        final EditText paramc;
        final AlertDialog paramd;
        final ArrayList<String> txtlistpr;

        public srchSpin(final TextView txtmod, final double eunica, LinearLayout lnlay, EditText inpocs, AlertDialog dilog,
                        ArrayList txtlist) {
            this.param = txtmod;
            this.parama = eunica;
            this.paramb = lnlay;
            this.paramc = inpocs;
            this.paramd = dilog;
            this.txtlistpr = txtlist;

        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            if (txtlistpr.size() > 0) {
                if (txtlistpr.get(0).equals("Select")) {
                    txtlistpr.remove(0);
                }
            }

            /*Search*/
            paramc.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    String stSrch = paramc.getText().toString();
                    ArrayList<String> nwArray = new ArrayList<>();

                    for (int i = 0; i < txtlistpr.size(); i++) {
                        if (txtlistpr.get(i).toLowerCase().contains(stSrch.toLowerCase())) {
                            nwArray.add(txtlistpr.get(i));
                        }
                    }

                    paramb.removeAllViews();
                    for (int deg = 0; deg < nwArray.size(); deg++) {
                        shContent(paramb, nwArray.get(deg), parama, paramd, param, txtlistpr);
                    }
                }
            });
            /*Search*/
            for (int meg = 0; meg < txtlistpr.size(); meg++) {
                shContent(paramb, txtlistpr.get(meg), parama, paramd, param, txtlistpr);
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            paramd.show();

            new Handler().postDelayed(() -> {
                try {
                    progressindc.hideProgress();
                } catch (Exception ex) {

                }
            }, 100);

        }
    }

    /* Set selected data option*/
    public void shContent(LinearLayout tbl, final String title, final double eunica, final AlertDialog dilog,
                          final TextView txtmod, final ArrayList items) {
        ArrayList<Integer> uniqid = new ArrayList<>();

        for (int i = 0; i < 6; ++i) {
            uniqid.add(generateViewId());
        }

        LayoutInflater inflater = (LayoutInflater) gContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cont = inflater.inflate(R.layout.spnsrchresult, null);

        View child = ((ViewGroup) cont).getChildAt(0);
        final TextView itemz = (TextView) child;
        itemz.setId(Math.abs(uniqid.get(0)));
        itemz.setText(title);
        itemz.setTextSize((float) eunica + 5.0f);
        itemz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtmod.setClickable(true);
                txtmod.setFocusable(true);

                txtmod.setText(title);
                dilog.dismiss();

            }
        });

        tbl.addView(cont, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

    }
}
