package com.galaxtime.widget;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;

import com.galaxtime.R;
import com.galaxtime.database.ColumnNames;
import com.galaxtime.widget.widgetLarge.WidgetProviderLarge;
import com.galaxtime.widget.widgetLarge.updateServiceLarge;
import com.galaxtime.widget.widgetMedium.WidgetProviderMedium;
import com.galaxtime.widget.widgetMedium.updateServiceMedium;
import com.galaxtime.widget.widgetSmall.WidgetProviderSmall;
import com.galaxtime.widget.widgetSmall.updateServiceSmall;

public class UpdateUtils extends Activity{
	private final static int SETTINGS=1;
	private final static int OTHER_APPLICATIONS=2;
	private final static int SHARE=3;
	private final static int QUESTIONS=4;
	private final static int INSTRUCTION=5;
	private final static int RATING=6;
	private static AlarmManager alarmManager;
	
	public static final int SMALL=1;
	public static final int MEDIUM=2;
	public static final int LARGE=3;

	public static void CreateMenu(Context context,Menu menu) {
		menu.add(0, SETTINGS, 0,context.getResources().getString(R.string.settings));
		menu.add(0, OTHER_APPLICATIONS, 0,context.getResources().getString(R.string.otherApps));
		menu.add(0, SHARE, 0,context.getResources().getString(R.string.share));
		menu.add(0, QUESTIONS, 0,context.getResources().getString(R.string.questions));
		menu.add(0, INSTRUCTION, 0,context.getResources().getString(R.string.instruction));
		menu.add(0, RATING, 0,context.getResources().getString(R.string.rating));
	}
	public static void SelectMenu(Context context, MenuItem item) {
		switch(item.getItemId()){
			case SETTINGS:
				Intent intent=new Intent();
				intent.setClass(context, Prefs.class);
				context.startActivity(intent);
				break;
			case OTHER_APPLICATIONS:
				Intent otherApp = new Intent(Intent.ACTION_VIEW, Uri
						.parse("https://play.google.com/store/apps/developer?id=Globalapps+R"));
				otherApp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				context.startActivity(otherApp);
				break;
			case INSTRUCTION: 
				Intent instruction = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://androids.ru/i/igalax.htm"));
				instruction.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				context.startActivity(instruction);
				break;
			case SHARE:
				//share(getString(R.string.shareSubject), getString(R.string.shareText));
				break;
			case RATING:
				/*try {
					String appPackageName = context.getPackageName();
					Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="
							+ appPackageName));
					marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
							| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
					context.startActivity(marketIntent);
				} catch (Exception ex) {
					Log.v("mes", "Cannot open ActionView to paste rate.");
				}*/
				break;
			case QUESTIONS:// help
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				String[] recipients = new String[] { "mail@androids.ru", "", };
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
					context.getString(R.string.app_name));
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
					context.getString(R.string.text_email_message));
				emailIntent.setType("text/plain");
				context.startActivity(Intent.createChooser(emailIntent,
					context.getString(R.string.text_send_mail)));
			break;
		}
	}
	public void share(String subject, String text) {

		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(Intent.createChooser(intent, getString(R.string.share)));
	}

	/**
	 * Updates widget when it was create for the first time. Writes data to the widget model.
	 */
	public static void updateWidgetLocal(Context context,Cursor cur,int mAppWidgetId,int size) {
		alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		cur.moveToFirst();
		int StartingYear=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_YEAR));
		int StartingMonth=-1;
			int monthInYear=cur.getInt(cur.getColumnIndex(ColumnNames.MONTH_IN_YEAR));
			if(monthInYear!=-1){
				StartingMonth=1;
			}
		int StartingDay=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_DAY));
		int StartingHour=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_HOUR));
		int StartingMinute=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_MINUTE));
		
		WidgetModel m=new WidgetModel(mAppWidgetId);
		m.setCurrentTime(StartingYear,StartingMonth, StartingDay, StartingHour, StartingMinute);
		m.setStartingTime(StartingYear, StartingDay, StartingHour, StartingMinute);
		m.setLaunchingSecond(System.currentTimeMillis());
		
		SetWidgetData(context,m,cur);
		//age1=DataUtils.getAge(m);
		DataUtils.RecalculateTime(context, m, size);
		//updateWidget(context,AppWidgetManager.getInstance(context),m,size);
		//UpdateUtils.startAlarmManager(context, m.getId(),(long)(m.getSecsInMinute()*1000), size);
	}

	private static void SetWidgetData(Context context,WidgetModel m,Cursor cur) {
		cur.moveToFirst();
		String PlanetName=cur.getString(cur.getColumnIndex(ColumnNames.PLANET_NAME));
		String CountryName=cur.getString(cur.getColumnIndex(ColumnNames.COUNTRY_NAME));
		String CityName=cur.getString(cur.getColumnIndex(ColumnNames.CITY_NAME));
		float DaysInYear=cur.getFloat(cur.getColumnIndex(ColumnNames.DAYS_IN_YEAR));
		float DaysInMonth=cur.getFloat(cur.getColumnIndex(ColumnNames.DAYS_IN_MONTH));
		float MonthInYear=cur.getFloat(cur.getColumnIndex(ColumnNames.MONTH_IN_YEAR));
		float HoursInDay=cur.getFloat(cur.getColumnIndex(ColumnNames.HOURS_IN_DAY));
		float MinutesInHour=cur.getFloat(cur.getColumnIndex(ColumnNames.MINUTES_IN_HOUR));
		float SecsInMinute=cur.getFloat(cur.getColumnIndex(ColumnNames.SEСS_IN_MINUTE));

		int DateView=Integer.valueOf(cur.getString(cur.getColumnIndex(ColumnNames.DATE_VIEW)));
		String maxTemperature=cur.getString(cur.getColumnIndex(ColumnNames.MAX_TEMPERATURE));
		String minTemperature=cur.getString(cur.getColumnIndex(ColumnNames.MIN_TEMPERATURE));
		int coldestTime=cur.getInt(cur.getColumnIndex(ColumnNames.COLDEST_TIME));
		int warmestTime=cur.getInt(cur.getColumnIndex(ColumnNames.WARMEST_TIME));
		int birthdayDay=cur.getInt(cur.getColumnIndex(ColumnNames.BIRTHDAY_DAY));
		int birthdayMonth=cur.getInt(cur.getColumnIndex(ColumnNames.BIRTHDAY_MONTH));
		int birthdayYear=cur.getInt(cur.getColumnIndex(ColumnNames.BIRTHDAY_YEAR));
		int birthdayAlarm=cur.getInt(cur.getColumnIndex(ColumnNames.BIRTHDAY_ALARM));
		String weather=cur.getString(cur.getColumnIndex(ColumnNames.WEATHER));
		int iconButtonId=cur.getInt(cur.getColumnIndex(ColumnNames.ICON));
		int StartingYear=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_YEAR));
		int StartingDay=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_DAY));
		int StartingHour=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_HOUR));
		int StartingMinute=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_MINUTE));
		
		m.setStartingTime(StartingYear, StartingDay, StartingHour, StartingMinute);
		m.setTimeParams(DaysInYear, MonthInYear, DaysInMonth, HoursInDay, 
			MinutesInHour, SecsInMinute);
		m.clearSynchronize();
		m.setPlanetName(PlanetName, CountryName, CityName);
		m.setTemperaturesParams(maxTemperature, minTemperature, coldestTime, warmestTime);
		m.setDataView(DateView);
		m.setBirthday(birthdayYear, birthdayMonth, birthdayDay, birthdayAlarm);
		m.setWeather(weather);
		m.setIcon(iconButtonId);
		m.setAgeBefore(DataUtils.getAge(m));
		
		m.SavePreferences(context);
	}
	/**
	 * Updates all parameters on the widget.
	 */
	public static void updateWidget(Context context,	
			AppWidgetManager appWidgetMgr, WidgetModel m,int size) {
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(context);
		String temperature="";
		if((m.getColdestTime()!=-1)&
			(prefs.getBoolean(context.getString(R.string.prefs_temperature), true))){
				temperature=DataUtils.getCurrentTemperature(m);
		}
		double age1=m.getAgeBefore();
		double age2=DataUtils.getAge(m);
		String age="";
		if(prefs.getBoolean(context.getString(R.string.prefs_age), true)){
			age="Мне "+age2+" лет";
		}
		String name="";
		if(prefs.getBoolean(context.getResources().getString(R.string.prefs_name), true)){
			String nameView=prefs.getString(context.getResources().getString(R.string.prefs_nameView), "Планета");
			if(nameView.equals("Планета")){
				name=m.getPlanet();
			}else{
				if(nameView.equals("Страна")){
					name=m.getCountry();
				}else{
					name=m.getCity();
				}
			}
		}
		
		if((m.getBirthdayAlarm()==1)&(((int)Math.floor(age1))<((int)Math.floor(age2)))){
			NotificationManager notificationMgr=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
			Notification notification=new Notification(R.drawable.icon, 
				context.getResources().getString(R.string.birthdayNotification)+" "+m.getPlanet(), 
				System.currentTimeMillis());
			notification.flags=Notification.FLAG_AUTO_CANCEL;
			notification.defaults=Notification.DEFAULT_SOUND;
			PendingIntent pendingIntent
		     = PendingIntent.getActivity(context, 0, null,0);
			notification.setLatestEventInfo(context, 
				context.getResources().getString(R.string.happyBirthday),
				context.getResources().getString(R.string.nowYouHave)+" "+(int)Math.floor(age2)+" "+
				context.getResources().getString(R.string.years),pendingIntent);
			notificationMgr.notify(4, notification);
		}
		String currentWeather="";
		if(prefs.getBoolean(context.getString(R.string.prefs_weather), true)){
			currentWeather=DataUtils.getWeather(m.getWeather());
		}
		String timeOfDay="";
		if(prefs.getBoolean(context.getString(R.string.prefs_timeOfDay), true)){
			timeOfDay=context.getResources().getString(
				DataUtils.getTimeOfDay((int)Math.floor(m.getHoursInDay()),m.getCurrentHour()));
		}
		String date=DataUtils.getDate(m.getDateView(),m.getCurrentYear(),
				m.getCurrentMonth(),m.getCurrentDay());
		String time=DataUtils.getTime(m.getCurrentHour(),m.getCurrentMinute());
		RemoteViews viewsSmall,viewsMedium,viewsLarge;
			
				viewsSmall=new RemoteViews(context.getPackageName(), R.layout.widget_configure_small);
				viewsSmall.setTextViewText(R.id.widget_small_date,date);
				viewsSmall.setTextViewText(R.id.widget_small_temperature, temperature);
				viewsSmall.setTextViewText(R.id.widget_small_age, age);
				viewsSmall.setTextViewText(R.id.widget_small_timeOfDay, timeOfDay);
				viewsSmall.setImageViewBitmap(R.id.widget_small_time,DataUtils.TimeBitmap(context,time,SMALL));
				viewsSmall.setTextViewText(R.id.widget_small_planetName,name);
				viewsSmall.setImageViewResource(R.id.widget_small_image,DataUtils.getIconId(m.getIcon()));
				viewsSmall.setTextViewText(R.id.widget_small_weather, currentWeather);
				
				viewsMedium=new RemoteViews(context.getPackageName(), R.layout.widget_configure_medium);
				viewsMedium.setTextViewText(R.id.widget_medium_date,date);
				viewsMedium.setTextViewText(R.id.widget_medium_temperature, temperature);
				viewsMedium.setTextViewText(R.id.widget_medium_age, age);				
				viewsMedium.setTextViewText(R.id.widget_medium_timeOfDay, timeOfDay);
				viewsMedium.setImageViewBitmap(R.id.widget_medium_time,DataUtils.TimeBitmap(context,time,MEDIUM));
				viewsMedium.setTextViewText(R.id.widget_medium_planetName,name);
				viewsMedium.setImageViewResource(R.id.widget_medium_image,DataUtils.getIconId(m.getIcon()));
				viewsMedium.setTextViewText(R.id.widget_medium_weather, currentWeather);
				
				viewsLarge=new RemoteViews(context.getPackageName(), R.layout.widget_configure_large);
				viewsLarge.setImageViewBitmap(R.id.widget_large_date,dateBitmap(context,date));
				viewsLarge.setTextViewText(R.id.widget_large_temperature, temperature);
				viewsLarge.setTextViewText(R.id.widget_large_age, age);
				viewsLarge.setTextViewText(R.id.widget_large_timeOfDay, timeOfDay);
				viewsLarge.setImageViewBitmap(R.id.widget_large_time,DataUtils.TimeBitmap(context,time,LARGE));
				viewsLarge.setTextViewText(R.id.widget_large_planetName,name);
				viewsLarge.setImageViewResource(R.id.widget_large_image,DataUtils.getIconId(m.getIcon()));
				viewsLarge.setTextViewText(R.id.widget_large_weather, currentWeather);
				
			switch(size){
				case SMALL:	
					appWidgetMgr.updateAppWidget(m.iid, viewsSmall);
					break;
				case MEDIUM:
					appWidgetMgr.updateAppWidget(m.iid, viewsMedium);
					break;
				case LARGE:
					appWidgetMgr.updateAppWidget(m.iid, viewsLarge);
					break;
			}
			
		String sync=m.getSynchronize();
		String curString="";
		for (int i=0;i<sync.length();i++){
			if(sync.charAt(i)!='/'){
				curString=curString+sync.charAt(i);
			}else{
				int id=Integer.valueOf(curString.substring(0,curString.length()-1));
				String sizeStr=curString.substring(curString.length()-1);
				curString="";
				WidgetModel model=WidgetModel.retrieveModel(context, id);
				if(model==null){continue;}
				int size1=1;
				if(sizeStr.equals("s")){size1=SMALL;}
				if(sizeStr.equals("m")){size1=MEDIUM;}
				if(sizeStr.equals("l")){size1=LARGE;}
					
				switch(size1){
					case SMALL:
						appWidgetMgr.updateAppWidget(id, viewsSmall);
						break;
					case MEDIUM:
						appWidgetMgr.updateAppWidget(id, viewsMedium);
						break;
					case LARGE:
						appWidgetMgr.updateAppWidget(id, viewsLarge);
						break;
				}
			}
		}
		m.setAgeBefore(age2);
		m.SavePreferences(context);
	}
	
	public static Bitmap dateBitmap(Context context, String date) {
		 int textSize=32;
		 int width=200;

	     Bitmap myBitmap = Bitmap.createBitmap(width, textSize, Bitmap.Config.ARGB_4444);
	     Canvas myCanvas = new Canvas(myBitmap);
	     Paint paint = new Paint();
	     paint.setAntiAlias(true);
	     paint.setSubpixelText(true);
	     paint.setStyle(Paint.Style.FILL);
	     paint.setColor(Color.YELLOW);
	     paint.setTextSize(textSize);
	     paint.setTextScaleX(Float.valueOf("0.6"));
	     myCanvas.drawText(date, 0, textSize, paint);
	     paint.setStyle(Paint.Style.STROKE);
	     paint.setColor(Color.RED);
	     paint.setStrokeWidth(Float.valueOf("1"));
	     myCanvas.drawText(date, 0, textSize, paint);
	     return myBitmap;
	}

	

	public static void startAlarmManager(Context context,int WidgetId,long period,int size){
		Intent updateIntent;
		switch(size){
			case UpdateUtils.SMALL:
				updateIntent=new Intent(context, updateServiceSmall.class);
				break;
			case UpdateUtils.MEDIUM:
				updateIntent=new Intent(context, updateServiceMedium.class);
				break;
			default:
				updateIntent=new Intent(context, updateServiceLarge.class);
				break;
		}
		updateIntent.putExtra("id", WidgetId);
		PendingIntent pendingIntent=PendingIntent.getBroadcast(context,Integer.valueOf(WidgetId), updateIntent, 0);
		
		if(alarmManager != null){
			alarmManager.cancel(pendingIntent);
		}else{
			alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		}
		alarmManager.set(AlarmManager.ELAPSED_REALTIME, 
			SystemClock.elapsedRealtime()+period, pendingIntent);
	}
	
	public static void stopAlarmManager(Context context,int WidgetId,int size){
		Intent updateIntent;
		switch(size){
			case UpdateUtils.SMALL:
				updateIntent=new Intent(context, updateServiceSmall.class);
				break;
			case UpdateUtils.MEDIUM:
				updateIntent=new Intent(context, updateServiceMedium.class);
				break;
			default:
				updateIntent=new Intent(context, updateServiceLarge.class);
				break;
		}
		updateIntent.putExtra("id", WidgetId);
		PendingIntent pendingIntent=PendingIntent.getBroadcast(context,WidgetId, updateIntent, 0);
		if(alarmManager != null){
			alarmManager.cancel(pendingIntent);
		}
		
	}
	public static void updateExistsWidgets(Context context,Cursor cur,String planetName) {
		ComponentName thisAppWidgetSmall = new ComponentName(context.getPackageName(),
			WidgetProviderSmall.class.getName());
		int[] appWidgetIdsSmall =
			AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidgetSmall);
		if(appWidgetIdsSmall.length!=0){
			for (int i=0;i<appWidgetIdsSmall.length;i++){
				WidgetModel model=WidgetModel.retrieveModel(context, appWidgetIdsSmall[i]);
				if((model!=null)&&(model.getPlanet().equals(planetName))){
					SetWidgetData(context, model, cur);
					DataUtils.RecalculateTime(context, model, SMALL);
					updateWidget(context,AppWidgetManager.getInstance(context),model,SMALL);
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
				if((model!=null)&&(model.getPlanet().equals(planetName))){
					SetWidgetData(context, model, cur);
					DataUtils.RecalculateTime(context, model, MEDIUM);
					updateWidget(context,AppWidgetManager.getInstance(context),model,MEDIUM);
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
				if((model!=null)&&(model.getPlanet().equals(planetName))){
					SetWidgetData(context, model, cur);
					DataUtils.RecalculateTime(context, model, LARGE);
					updateWidget(context,AppWidgetManager.getInstance(context),model,LARGE);
				}
			}
		}	
	}

	public static boolean FindSimilarWidget(Context context,Cursor cur,int mAppWidgetId,int size){
		cur.moveToFirst();
		int primaryId=-1;
		Log.v("mes", "hrkuk");
		WidgetModel primaryModel=null; 
		String planetName=cur.getString(cur.getColumnIndex(ColumnNames.PLANET_NAME));
		ComponentName thisAppWidgetSmall = new ComponentName(context.getPackageName(),
			WidgetProviderSmall.class.getName());
		int[] appWidgetIdsSmall =
			AppWidgetManager.getInstance(context).getAppWidgetIds(thisAppWidgetSmall);
		if(appWidgetIdsSmall.length!=0){
			for (int i=0;i<appWidgetIdsSmall.length;i++){
				WidgetModel model=WidgetModel.retrieveModel(context, appWidgetIdsSmall[i]);
				if((model!=null)&&(model.getPlanet().equals(planetName))){
					if(primaryId==-1){
						primaryId=appWidgetIdsSmall[i];
						primaryModel=model;
					}else{
						if(appWidgetIdsSmall[i]<primaryId){
							primaryId=appWidgetIdsSmall[i];
							primaryModel=model;
						}
					}
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
				if((model!=null)&&(model.getPlanet().equals(planetName))){
					if(primaryId==-1){
						primaryId=appWidgetIdsMedium[i];
						primaryModel=model;
					}else{
						if(appWidgetIdsMedium[i]<primaryId){
							primaryId=appWidgetIdsMedium[i];
							primaryModel=model;
						}
					}
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
				if((model!=null)&&(model.getPlanet().equals(planetName))){
					if(primaryId==-1){
						primaryId=appWidgetIdsLarge[i];
						primaryModel=model;
					}else{
						if(appWidgetIdsLarge[i]<primaryId){
							primaryId=appWidgetIdsLarge[i];
							primaryModel=model;
						}
					}
				}
			}
		}	
		if(primaryId!=-1){
			CopyExistModel(context,mAppWidgetId,primaryModel,cur,size);
			DataUtils.RecalculateTime(context, primaryModel, size);
			return true;
		}
		return false;
	}
	private static void CopyExistModel(Context context,int mAppWidgetId,WidgetModel model,Cursor cur,int size){
		WidgetModel m=new WidgetModel(mAppWidgetId);
		m.setTimeParams(model.getDaysInYear(),model.getMonthInYear(),model.getDaysInMonth(),
			model.getHoursInDay(),model.getMinutesInHour(),model.getSecsInMinute());
		m.setTemperaturesParams(model.getMaxTemperature(), model.getMinTemperature(), 
			model.getColdestTime(), model.getWarmestTime());
		m.setLaunchingSecond(model.getLaunchingSecond());
		m.setPlanetName(model.getPlanet(), model.getCountry(), model.getCity());
		m.setDataView(model.getDateView());
		m.setCurrentTime(model.getCurrentYear(), model.getCurrentMonth(), model.getCurrentDay(), 
			model.getCurrentHour(), model.getCurrentMinute());
		m.setStartingTime(model.getStartingYear(), model.getStartingDay(), model.getStartingHour(), 
			model.getStartingMinute());
		m.setBirthday(model.getYearOfBirthday(), model.getMonthOfBirthday(),
			model.getDayOfBirthday(),model.getBirthdayAlarm());
		m.setAgeBefore(model.getAgeBefore());
		m.setWeather(model.getWeather());
		m.setIcon(model.getIcon());
		m.SavePreferences(context);
		if(model.getSynchronize().contains(m.iid+"")){
			Log.v("mes", "no");
		}else{
			switch(size){
				case SMALL:
					model.addToSynchronize(m.getId()+"s");
				break;
				case MEDIUM:
					model.addToSynchronize(m.getId()+"m");
				break;
				case LARGE:
					model.addToSynchronize(m.getId()+"l");
				break;
			}
		}
		model.SavePreferences(context);
	}
	public static void Synchonize(Context context,WidgetModel model) {
		String sync=model.getSynchronize();
		String curString="";
		for (int i=0;i<sync.length();i++){
			if(sync.charAt(i)!='/'){
				curString=curString+sync.charAt(i);
			}else{
				int id=Integer.valueOf(curString.substring(0,curString.length()-1));
				String sizeStr=curString.substring(curString.length()-1);
				curString="";
				
				int size=1;
				if(sizeStr.equals("s")){size=SMALL;}
				if(sizeStr.equals("m")){size=MEDIUM;}
				if(sizeStr.equals("l")){size=LARGE;}
				WidgetModel m=WidgetModel.retrieveModel(context, id);
				if(m==null){
					String newSynchro=sync.substring(0,i-(id+"/").length())+sync.substring(i+1);
					model.setSynchronize(newSynchro);
					model.SavePreferences(context);
					Log.v("mes", sync+" "+newSynchro);
					continue;
				}
					m.setTimeParams(model.getDaysInYear(),model.getMonthInYear(),model.getDaysInMonth(),
						model.getHoursInDay(),model.getMinutesInHour(),model.getSecsInMinute());
					m.setTemperaturesParams(model.getMaxTemperature(), model.getMinTemperature(), 
						model.getColdestTime(), model.getWarmestTime());
					m.setLaunchingSecond(model.getLaunchingSecond());
					m.setPlanetName(model.getPlanet(), model.getCountry(), model.getCity());
					m.setDataView(model.getDateView());
					m.setCurrentTime(model.getCurrentYear(), model.getCurrentMonth(), model.getCurrentDay(), 
						model.getCurrentHour(), model.getCurrentMinute());
					m.setStartingTime(model.getStartingYear(), model.getStartingDay(), model.getStartingHour(), 
						model.getStartingMinute());
					m.setBirthday(model.getYearOfBirthday(), model.getMonthOfBirthday(),
						model.getDayOfBirthday(),model.getBirthdayAlarm());
					m.setAgeBefore(model.getAgeBefore());
					m.setWeather(model.getWeather());
					m.setIcon(model.getIcon());
					m.SavePreferences(context);
			}
		}
	}
}
