package com.galaxtime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.galaxtime.R;
import com.galaxtime.database.BaseHelper;
import com.galaxtime.database.ColumnNames;

public class WidgetConfigure extends Activity{
	Button button;
	Context context;
	ListView listView;
	int mAppWidgetId;
	final int CONTEXT_EDIT=301;
	final int CONTEXT_DELETE=302;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.edit_main);
		listView=(ListView)findViewById(R.id.list_allPlanets);
		this.registerForContextMenu(listView);
		context=this;
		
		Bundle extras=(getIntent()).getExtras();
		if(extras!=null){
			mAppWidgetId=extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		}
				
		button=(Button)findViewById(R.id.button_newWorld);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(context, CreateNewWorld.class);
				startActivity(intent);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View view, int position,	long id) {
				Cursor cur= managedQuery(BaseHelper.uri, null, BaseHelper._ID+"=?", 
						new String[]{l.getItemIdAtPosition(position)+""}, null);
				
				updateWidgetLocal(cur);
				
				Intent resultValue=new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
				setResult(RESULT_OK,resultValue);
				finish();
			}
		});

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu,View v,ContextMenuInfo menuInfo){
		menu.add(0, CONTEXT_EDIT, 0,R.string.edit );
		menu.add(0, CONTEXT_DELETE, 0, R.string.delete);
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		switch (item.getItemId()){
			case CONTEXT_EDIT:
				Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
				break;
			case CONTEXT_DELETE:
				Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
				//this.getContentResolver().delete(BaseHelper.uri, ColumnNames.PLANET_NAME+"=?",)
				break;
		}
		return super.onContextItemSelected(item);
	}
	
	private void updateWidgetLocal(Cursor cur) {
		cur.moveToFirst();
		String PlanetName=cur.getString(cur.getColumnIndex(ColumnNames.PLANET_NAME));
		String DaysInYear=cur.getString(cur.getColumnIndex(ColumnNames.DAYS_IN_YEAR));
		String MonthInYear=cur.getString(cur.getColumnIndex(ColumnNames.MONTH_IN_YEAR));
		String HoursInDay=cur.getString(cur.getColumnIndex(ColumnNames.HOURS_IN_DAY));
		String MinutesInHour=cur.getString(cur.getColumnIndex(ColumnNames.MINUTES_IN_HOUR));
		String SecsInMinute=cur.getString(cur.getColumnIndex(ColumnNames.SEСS_IN_MINUTE));
		String StartingYear=cur.getString(cur.getColumnIndex(ColumnNames.STARTING_YEAR));
		//String StartingMonth=cur.getString(cur.getColumnIndex(ColumnNames.STARTING_MONTH));
		String StartingDay=cur.getString(cur.getColumnIndex(ColumnNames.STARTING_DAY));
		String StartingHour=cur.getString(cur.getColumnIndex(ColumnNames.STARTING_HOUR));
		String StartingMinute=cur.getString(cur.getColumnIndex(ColumnNames.STARTING_MINUTE));
		String DateView=cur.getString(cur.getColumnIndex(ColumnNames.DATE_VIEW));
		WidgetModel m=new WidgetModel(mAppWidgetId,PlanetName,DaysInYear+"",MonthInYear+"",HoursInDay+"",
				MinutesInHour+"",SecsInMinute+"");
		m.setStartingTime(StartingYear+"", StartingDay+"", StartingHour+"", StartingMinute+"");
		m.setDataView(DateView);
		
		updateWidget(this,AppWidgetManager.getInstance(this),m);
		m.SavePreferences(this);
		

		/*long curTime=System.currentTimeMillis();
		long curSecs=(long)curTime/1000;
		int curYear=(int)(curSecs/(60*60*24*(365.2425)));
		int curDay=(int)(curSecs/(60*60*24)-curYear*365-11);
		int curHour=(int)(curSecs/(60*60)-curYear*365*24-curDay*24-24*11);
		int curMin=(int)(curSecs/60-curYear*365*24*60-24*11*60-curDay*24*60-curHour*60);
		int curSec=(int)(curSecs-curYear*365*24*60*60-24*11*60*60-curDay*24*60*60-curHour*60*60-curMin*60);*/
		
	}
	
	public static void updateWidget(Context content, AppWidgetManager appWidgetMgr,WidgetModel m){
		int StartingYear=Integer.valueOf(m.getStartingYear());
		int StartingDay=Integer.valueOf(m.getStartingDay());
		int StartingHour=Integer.valueOf(m.getStartingHour());
		int StartingMinute=Integer.valueOf(m.getStartingMinute());
		int DateView=Integer.valueOf(m.getDateView());
		int DaysInYear=Integer.valueOf(m.getDaysInYear());
		int MonthInYear=Integer.valueOf(m.getMonthInYear());
		int HoursInDay=Integer.valueOf(m.getHoursInDay());
		float MinutesInHour=Float.valueOf(m.getMinutesInHour());
		float SecsInMinute=Float.valueOf(m.getSecsInMinute());
		
		long curSeconds=TimeUtils.currentSeconds(StartingYear, StartingDay, StartingHour, StartingMinute);
		String[] dateAndTime=TimeUtils.getDateAndTime(DateView, DaysInYear,MonthInYear,HoursInDay,
				MinutesInHour,SecsInMinute,curSeconds);
		String date=dateAndTime[0];
		String time=dateAndTime[1];
		
		RemoteViews views=new RemoteViews(content.getPackageName(), R.layout.widget_configure_small);
		views.setTextViewText(R.id.widget_small_planetName,m.getPlanet());
		views.setTextViewText(R.id.widget_small_date,date);
		views.setTextViewText(R.id.widget_small_time,time);
		appWidgetMgr.updateAppWidget(m.iid, views);
	}

	@Override
	public void onResume(){
		super.onResume();
		SimpleCursorAdapter adapter;
		Cursor cursor=this.managedQuery(BaseHelper.uri, null, null, null, null);
		adapter=new SimpleCursorAdapter(this, R.layout.list_item, cursor, 
				new String[]{ColumnNames.PLANET_NAME}, new int[]{R.id.text_listItemPlanet});
		listView.setAdapter(adapter);
	}
	
}
