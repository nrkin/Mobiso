package com.nrk.mobiso;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {
	public static boolean isConnected(Context context){
		ConnectivityManager cmgr = 
				(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cmgr.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
}
