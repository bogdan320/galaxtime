package com.galaxtime.widget.widgetLarge;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.galaxtime.widget.DataUtils;
import com.galaxtime.widget.UpdateUtils;
import com.galaxtime.widget.WidgetModel;

public class updateServiceLarge extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		WidgetModel model=WidgetModel.retrieveModel(context, intent.getExtras().getInt("id"));
		final Context ctx=context;
		final WidgetModel wm=model;
		if((model!=null)&&(model.getPlanet()!=null)){
			if(!model.getSynchronize().equals("")){
				Thread thrd=new Thread(){
					@Override
					public void run(){
						UpdateUtils.Synchonize(ctx,wm);
					}
				};
				thrd.start();
			}
			UpdateUtils.updateWidget(context,AppWidgetManager.getInstance(context),
				wm,UpdateUtils.LARGE);
			DataUtils.RecalculateTime(context,model,UpdateUtils.LARGE);
			
		}
	}
 
}
