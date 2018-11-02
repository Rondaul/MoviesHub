package com.rondaulagupu.ronx.movieshub.broadcast_receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Objects;

/**
 * BroadcastReceiver to recieve the network state changes.
 */
public class NetworkBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //run the method as soon as the onRecieve method is called.
            runTask(context, intent);
    }
    //Method to check if internet is available or not. Method keeps on running showing the no
    //internet alert untill the network is available again.
    public void runTask (final Context context,final  Intent intent) {
        checkConnection(context);

    }

    public void checkConnection(Context context) {
        //getting the network info whether the network is available or not.
        NetworkInfo info = ((ConnectivityManager)
                Objects.requireNonNull(context.getSystemService(Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo();

        boolean isConnected = info != null &&
                info.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
        }
        else{
            Log.d("Network","Not Connected");
            try {
                //Alert Dialog to show no internet alert when network is not available.
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("No Internet");
                alertDialogBuilder.setMessage("Check your internet connection and try again");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int n) {
                                dialog.cancel();
                    }
                });
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
            } catch (Exception e) {
                //Log.d(Constants.TAG, "Show Dialog: "+e.getMessage());
            }
        }
    }
}
