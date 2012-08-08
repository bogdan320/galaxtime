package com.galaxtime.database;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class updateService extends Service{
	
	@Override
	public void onStart(Intent intent,int startId){
		
		Log.v("mes", "Hello");
		long currentTime=System.currentTimeMillis();
		long currentSecs=(long)currentTime/1000;
		long currentYear=(long)currentSecs/(60*60*24*365)+1970;
		Log.v("mes", currentYear+"");
		super.onStart(intent,startId);
	}
	@Override
	public void onCreate(){
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
