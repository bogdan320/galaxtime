package com.galaxtime.widget;

import com.galaxtime.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider{
	private AppWidgetManager widgetMgr;
	private final String ACTION_WIDGET_RECEIVER="receiver";
	private final String WIDGET_IDS="widgetIds";
	private Context context;
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds){
	     Intent active = new Intent(context, WidgetProvider.class);
         active.setAction(ACTION_WIDGET_RECEIVER);
         widgetMgr=appWidgetManager;
         this.context=context;
         //active.putExtra(WIDGET_IDS, appWidgetIds);
         RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.widget_configure_small);
         PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
         views.setOnClickPendingIntent(R.id.widget_small_but, actionPendingIntent);
         appWidgetManager.updateAppWidget(appWidgetIds, views);
	}
	@Override
	public void onReceive(Context ctx,Intent intent){
		if(ACTION_WIDGET_RECEIVER.equals(intent.getAction())){
			
			//int[] widgetIds=intent.getExtras().getIntArray(WIDGET_IDS);
			//int N=widgetIds.length;
	        /*for (int i=0;i<N;i++){
	        	int WidgetId=widgetIds[i];
	            WidgetModel m=WidgetModel.retrieveModel(ctx, WidgetId);
	            WidgetConfigure.updateWidget(context, widgetMgr, m);
	        }*/
			//int currentHour=(int)(currentSecs/3600-currentDay-currentYear/(365.2425*24));
			//Log.v("mes", currentHour+"");
			/*Intent updateIntent=new Intent();
			updateIntent.setClass(ctx, updateService.class);
			ctx.startService(updateIntent);*/
		}
		super.onReceive(ctx, intent);
	}
}
