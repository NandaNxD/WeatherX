package com.mathx.weatherx;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class CheckNetwork extends AsyncTask<Void,Void,Boolean> {
    private Context context;
    CheckNetwork(Context context){
        this.context=context;
    }
    @Override
    protected Boolean doInBackground(Void... voids) {
        ConnectivityManager cm = (ConnectivityManager)(this.context).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.i("Connection",String.valueOf(isConnected));
        return isConnected;
    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(!aBoolean){
            Toast.makeText(this.context,"No Connection Detected",Toast.LENGTH_SHORT).show();
        }
    }
}
