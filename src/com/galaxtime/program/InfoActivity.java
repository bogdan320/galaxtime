package com.galaxtime.program;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxtime.R;
import com.galaxtime.database.BaseHelper;
import com.galaxtime.database.ColumnNames;
import com.galaxtime.widget.DataUtils;
import com.galaxtime.widget.UpdateUtils;

public class InfoActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.planet_info_widgets);
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		String planetName=this.getIntent().getExtras().getString("planetName");
		Cursor cur=managedQuery(BaseHelper.uri, null,ColumnNames.PLANET_NAME+"=?",
			new String[]{planetName},null);
		cur.moveToFirst();
		int dateView=cur.getInt(cur.getColumnIndex(ColumnNames.DATE_VIEW));
		int startingYear=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_YEAR));
		int startingDay=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_DAY));
		int startingHour=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_HOUR));
		int startingMinute=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_MINUTE));
		float hoursInDay=cur.getFloat(cur.getColumnIndex(ColumnNames.HOURS_IN_DAY));
		int coldestTime=cur.getInt(cur.getColumnIndex(ColumnNames.COLDEST_TIME));
		String weather=cur.getString(cur.getColumnIndex(ColumnNames.WEATHER));
		int iconId=DataUtils.getIconId(cur.getInt(cur.getColumnIndex(ColumnNames.ICON)));
		
		String date=DataUtils.getDate(dateView,startingYear,1,startingDay);
		
		String temperature="";
		if((coldestTime!=-1)&(prefs.getBoolean(getString(R.string.prefs_temperature), true))){
			temperature=DataUtils.getCurrentTemperature(cur);
		}
		
		String timeOfDay="";
		if(prefs.getBoolean(getString(R.string.prefs_timeOfDay), true)){
			timeOfDay=getResources().getString(
				DataUtils.getTimeOfDay((int)Math.floor(hoursInDay),startingHour));
		}
		double age2=DataUtils.getAge(cur);
		String age="";
		if(prefs.getBoolean(getString(R.string.prefs_age), true)){
			age="Мне "+age2+" лет";
		}
		String currentWeather="";
		if(prefs.getBoolean(getString(R.string.prefs_weather), true)){
			currentWeather=DataUtils.getWeather(weather);
		}
		String time=DataUtils.getTime(startingHour,startingMinute);
		
		((TextView)findViewById(R.id.info_widget_small_date)).setText(date);
		((TextView)findViewById(R.id.info_widget_medium_date)).setText(date);
		((ImageView)findViewById(R.id.info_widget_large_date)).setImageBitmap(UpdateUtils.dateBitmap(this, date));
		
		((TextView)findViewById(R.id.info_widget_small_age)).setText(age);
		((TextView)findViewById(R.id.info_widget_medium_age)).setText(age);
		((TextView)findViewById(R.id.info_widget_large_age)).setText(age);
		
		((TextView)findViewById(R.id.info_widget_small_timeOfDay)).setText(timeOfDay);
		((TextView)findViewById(R.id.info_widget_medium_timeOfDay)).setText(timeOfDay);
		((TextView)findViewById(R.id.info_widget_large_timeOfDay)).setText(timeOfDay);
		
		((TextView)findViewById(R.id.info_widget_small_temperature)).setText(temperature);
		((TextView)findViewById(R.id.info_widget_medium_temperature)).setText(temperature);
		((TextView)findViewById(R.id.info_widget_large_temperature)).setText(temperature);
		
		((TextView)findViewById(R.id.info_widget_small_planetName)).setText(planetName);
		((TextView)findViewById(R.id.info_widget_medium_planetName)).setText(planetName);
		((TextView)findViewById(R.id.info_widget_large_planetName)).setText(planetName);
		
		((TextView)findViewById(R.id.info_widget_small_weather)).setText(currentWeather);
		((TextView)findViewById(R.id.info_widget_medium_weather)).setText(currentWeather);
		((TextView)findViewById(R.id.info_widget_large_weather)).setText(currentWeather);
		
		((ImageView)findViewById(R.id.info_widget_small_image)).setImageResource(iconId);
		((ImageView)findViewById(R.id.info_widget_medium_image)).setImageResource(iconId);
		((ImageView)findViewById(R.id.info_widget_large_image)).setImageResource(iconId);
		
		((ImageView)findViewById(R.id.info_widget_small_time)).setImageBitmap(DataUtils.TimeBitmap(this,time,UpdateUtils.SMALL));
		((ImageView)findViewById(R.id.info_widget_medium_time)).setImageBitmap(DataUtils.TimeBitmap(this,time,UpdateUtils.MEDIUM));
		((ImageView)findViewById(R.id.info_widget_large_time)).setImageBitmap(DataUtils.TimeBitmap(this,time,UpdateUtils.LARGE));

		
		/*((Button)findViewById(R.id.info_button_close)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		String planetName=this.getIntent().getExtras().getString("planetName");
		Cursor cur=managedQuery(BaseHelper.uri, null,ColumnNames.PLANET_NAME+"=?",
			new String[]{planetName},null);
		cur.moveToFirst();
		((TextView)findViewById(R.id.info_textView_planet)).setText(planetName);
		((TextView)findViewById(R.id.info_textView_country)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.COUNTRY_NAME)));
		((TextView)findViewById(R.id.info_textView_city)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.CITY_NAME)));
		((TextView)findViewById(R.id.info_textView_EarthDaysInYear)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.EARTH_DAYS_IN_YEAR)));
		if(cur.getString(cur.getColumnIndex(ColumnNames.MONTH_IN_YEAR)).equals("-1")){
		}else{
			((TextView)findViewById(R.id.info_textView_monthInYear)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.MONTH_IN_YEAR)));
			((TextView)findViewById(R.id.info_textView_daysInMonth)).setText(
					cur.getString(cur.getColumnIndex(ColumnNames.DAYS_IN_MONTH)));
		}
		((TextView)findViewById(R.id.info_textView_daysInYear)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.DAYS_IN_YEAR)));
		((TextView)findViewById(R.id.info_textView_hoursInDay)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.HOURS_IN_DAY)));
		((TextView)findViewById(R.id.info_textView_minutesInHour)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.MINUTES_IN_HOUR)));
		((TextView)findViewById(R.id.info_textView_SecsInMinute)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.SEСS_IN_MINUTE)));
		((TextView)findViewById(R.id.info_TextView_startYear)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.STARTING_YEAR)));
		((TextView)findViewById(R.id.info_TextView_startDay)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.STARTING_DAY)));
		((TextView)findViewById(R.id.info_TextView_startHour)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.STARTING_HOUR)));
		((TextView)findViewById(R.id.info_TextView_startMinute)).setText(
			cur.getString(cur.getColumnIndex(ColumnNames.STARTING_MINUTE)));
		if(!cur.getString(cur.getColumnIndex(ColumnNames.COLDEST_TIME)).equals("-1")){
			((TextView)findViewById(R.id.info_textView_maxTemperature)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.MAX_TEMPERATURE)));
			((TextView)findViewById(R.id.info_textView_minTemperature)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.MIN_TEMPERATURE)));
			((TextView)findViewById(R.id.info_textView_warmestTime)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.WARMEST_TIME)));
			((TextView)findViewById(R.id.info_textView_coldestTime)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.COLDEST_TIME)));
		}*/
	}
}
