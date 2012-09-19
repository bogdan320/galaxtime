package com.galaxtime.widget.widgetMedium;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.galaxtime.widget.DataUtils;
import com.galaxtime.widget.UpdateUtils;
import com.galaxtime.widget.WidgetModel;

public class WidgetProviderMedium extends AppWidgetProvider{

	@Override
	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);
		if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
			ComponentName thisAppWidget = new ComponentName(context.getPackageName(),
				WidgetProviderMedium.class.getName());
			int[] appWidgetIds =
				AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidget);
			if(appWidgetIds.length!=0){
				for (int i=0;i<appWidgetIds.length;i++){
					WidgetModel model=WidgetModel.retrieveModel(context, appWidgetIds[i]);
					if(model!=null){
						DataUtils.RecalculateTime(context, model, UpdateUtils.MEDIUM);
					}
				}
			}
		}
	}
	@Override
	public void onDeleted(Context context, int[] appWidgetIds){
		for (int i = 0; i < appWidgetIds.length; ++i) {
			WidgetModel model=WidgetModel.retrieveModel(context, appWidgetIds[i]);
			model.removePrefs(context);
			if(model!=null){
				if((!model.getSynchronize().equals(""))&(model.getSynchronize()!=null)){
					String synchro=model.getSynchronize();
					int index=model.getSynchronize().indexOf("/");
					int id=Integer.valueOf(synchro.substring(0,index-1));
					String sizeStr=synchro.substring(index-1,index);
					int size=2;
					if(sizeStr.equals("s")){size=UpdateUtils.SMALL;}
					if(sizeStr.equals("m")){size=UpdateUtils.MEDIUM;}
					if(sizeStr.equals("l")){size=UpdateUtils.LARGE;}
					String newSynchro=synchro.substring(index+1);
					WidgetModel m=WidgetModel.retrieveModel(context, id);
					if(m!=null){
						m.setSynchronize(newSynchro);
						m.SavePreferences(context);
						DataUtils.RecalculateTime(context, m, size);
					}
				}
				UpdateUtils.stopAlarmManager(context,appWidgetIds[i], UpdateUtils.MEDIUM);
				Log.v("mes","Deleted widget "+appWidgetIds[i]);
			}
		}
	}

}
