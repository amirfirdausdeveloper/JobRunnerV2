package com.jobrunner.app.Activity.SplashScreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jobrunner.app.Activity.Introduction.IntroductionActivity;
import com.jobrunner.app.BuildConfig;
import com.jobrunner.app.Common.NetworkStateReceiver;
import com.jobrunner.app.Connection.NukeSSLCerts;
import com.jobrunner.app.Connection.UrlLink;
import com.jobrunner.app.MainActivity;
import com.jobrunner.app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    private int network = 0;
    private AlertDialog alertDialog;
    private NetworkStateReceiver networkStateReceiver;
    Handler handler;
    Context mContext;
    int click = 0;
    Boolean status_handler = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    /*-----------------------------ON START FUNCTION ---------------------------------------------*/
    @Override
    protected void onStart() {
        super.onStart();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener((NetworkStateReceiver.NetworkStateReceiverListener) this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /*-----------------------------ON STOP FUNCTION ---------------------------------------------*/
    @Override
    protected void onStop() {
        super.onStop();

        try {
            this.unregisterReceiver(networkStateReceiver);
        } catch (Exception e) {
        }
        if(status_handler){
            handler.removeMessages(0);
        }


    }

    /*-----------------------------ON RESUME FUNCTION ---------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();

        //REGISTER NETWORK
        this.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mContext = getApplicationContext();

        Handler handlers = new Handler();
        handlers.postDelayed(new Runnable() {
            @Override
            public void run() {

                //IF NETWORK ONLINE  NETWORK = 1 (ONLINE) , NETWORK = 0 (OFFLINE)
                if(network == 1){
                    //TU CHECK VERSION OF PHONE
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                        dialogVersion();
                    }else{
                        getVersion(); //GET APPS VERSION FROM DATABASE
                    }
                }

            }
        }, 1500);

    }

    /*-----------------------------NETWORK AVAILABLE FUNCTION ---------------------------------------------*/
    @Override
    public void networkAvailable() {
        network = 1;
    }

    /*-----------------------------NETWORK UNAVAILABLE FUNCTION ---------------------------------------------*/
    public void networkUnavailable() {

        //IF STATUS_HANDLE TRUE CAN REMOVE HANDLE
        if(status_handler){
            handler.removeMessages(0);
        }
        network = 0;
        alertDialog = new AlertDialog.Builder(SplashScreenActivity.this, R.style.AlertDialogTheme)
                .setMessage(getString(R.string.no_network_notification))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isOnline()) {
                            click++;
                            if(click == 1){
                               getVersion();
                                dialog.dismiss();
                            }
                        } else {
                            networkUnavailable();
                            click = 0;
                        }
                    }
                })
                .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    /*-----------------------------IS ONLINE FUNCTION ---------------------------------------------*/
    public boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    /*-----------------------------GETVERSION APPS FUNCTION ---------------------------------------------*/
    public void getVersion(){
        NukeSSLCerts.nuke();
        class AsyncTaskRunner extends AsyncTask<String, String, JSONObject> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected JSONObject doInBackground(String... params) {
                UrlLink uLink = new UrlLink();
                return uLink.getVersion(BuildConfig.VERSION_NAME);
            }
            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                if(jsonObject != null){
                    try {
                        if(jsonObject.getString("status").equals("true")){
                            status_handler = true;
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent next = new Intent(getApplicationContext(), IntroductionActivity.class);
                                    startActivity(next);
                                    }
                                }, 3500);
                        }else{
                            if(jsonObject.getString("message").contains("update")){
                                dialogUpdateApps(jsonObject.getString("message"));
                            }else{
                                dialogUnderMaintance(jsonObject.getString("message"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    dialogPromptError("Somethong error");
                }
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }
        new AsyncTaskRunner().execute();
    }

    /*-----------------------------LAUNCH PLAYSTORE FUNCTION ---------------------------------------------*/
    public void launchPlayStore(Context context, String packageName) {
        packageName = "com.bateriku.customer";
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    /*-----------------------------ALL DIALOG FUNCTION ---------------------------------------------*/
    public void dialogUnderMaintance(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(SplashScreenActivity.this, R.style.AlertDialogTheme)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Exit Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    public void dialogPromptError(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(SplashScreenActivity.this, R.style.AlertDialogTheme)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                       recreate();
                    }
                })
                .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    public void dialogUpdateApps(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(SplashScreenActivity.this, R.style.AlertDialogTheme)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        launchPlayStore(getApplicationContext(),"");
                        finish();

                    }
                }).show();
    }

    public void dialogVersion(){
        new AlertDialog.Builder(SplashScreenActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Device not compatible")
                .setMessage("Sorry your phone version is not supported for JobRunner")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }
}
