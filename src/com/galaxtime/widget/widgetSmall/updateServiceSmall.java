package com.galaxtime.widget.widgetSmall;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.galaxtime.widget.DataUtils;
import com.galaxtime.widget.UpdateUtils;
import com.galaxtime.widget.WidgetModel;

public class updateServiceSmall extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {	
		WidgetModel model=WidgetModel.retrieveModel(context, intent.getExtras().getInt("id"));
		if((model!=null)&&(model.getPlanet()!=null)){
			if(!model.getSynchronize().equals("")){
				final Context ctx=context;
				final WidgetModel wm=model;
				Thread thrd=new Thread(){
					@Override
					public void run(){
						UpdateUtils.Synchonize(ctx,wm);
					}
				};
				thrd.start();
			}
			UpdateUtils.updateWidget(context,AppWidgetManager.getInstance(context), 
				model,UpdateUtils.SMALL);
			DataUtils.RecalculateTime(context,model,UpdateUtils.SMALL);
		}
	}
 
}
