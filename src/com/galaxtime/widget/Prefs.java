package com.galaxtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.galaxtime.R;
import com.galaxtime.widget.widgetLarge.WidgetProviderLarge;
import com.galaxtime.widget.widgetMedium.WidgetProviderMedium;
import com.galaxtime.widget.widgetSmall.WidgetProviderSmall;

public class Prefs extends PreferenceActivity{
	private Context context;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		context=this;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.v("mes","defe");
		ComponentName thisAppWidgetSmall = new ComponentName(context.getPackageName(),
				WidgetProviderSmall.class.getName());
			int[] appWidgetIdsSmall =
				AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidgetSmall);
			if(appWidgetIdsSmall.length!=0){
				for (int i=0;i<appWidgetIdsSmall.length;i++){
					WidgetModel model=WidgetModel.retrieveModel(context, appWidgetIdsSmall[i]);
					if(model!=null){
						UpdateUtils.updateWidget(context,AppWidgetManager.getInstance(context),
							model,UpdateUtils.SMALL);
					}
				}
			}	
			ComponentName thisAppWidgetMedium = new ComponentName(context.getPackageName(),
				WidgetProviderMedium.class.getName());
			int[] appWidgetIdsMedium =
				AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidgetMedium);
			if(appWidgetIdsMedium.length!=0){
				for (int i=0;i<appWidgetIdsMedium.length;i++){
					WidgetModel model=WidgetModel.retrieveModel(context, appWidgetIdsMedium[i]);
					if(model!=null){
						UpdateUtils.updateWidget(context,AppWidgetManager.getInstance(context),
							model,UpdateUtils.MEDIUM);
					}
				}
			}	
			ComponentName thisAppWidgetLarge = new ComponentName(context.getPackageName(),
				WidgetProviderLarge.class.getName());
			int[] appWidgetIdsLarge =
				AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidgetLarge);
			if(appWidgetIdsLarge.length!=0){
				for (int i=0;i<appWidgetIdsLarge.length;i++){
					WidgetModel model=WidgetModel.retrieveModel(context, appWidgetIdsLarge[i]);
					if(model!=null){
						UpdateUtils.updateWidget(context,AppWidgetManager.getInstance(context),
							model,UpdateUtils.LARGE);
					}
				}
			}	
	}
}
