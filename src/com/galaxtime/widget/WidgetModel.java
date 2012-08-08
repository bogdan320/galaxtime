package com.galaxtime.widget;


import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class WidgetModel extends APrefWidgetModel{

	private String Planet;
	private String DaysInYear;
	private String MonthInYear;
	private String HoursInDay;
	private String MinutesInHour;
	private String SecsInMinute;
	private String StartingYear;
	private String StartingDay;
	//private String StartingMonth;
	private String StartingHour;
	private String StartingMinute;
	private String DateView;
	private final String S_PLANET="planet";
	private final String S_DAYS="days";
	private final String S_MONTH="month";
	private final String S_HOURS="hour";
	private final String S_MINUTE="minute";
	private final String S_SECS="seconds";
	private final String S_STARTINGYEAR="startyear";
	//private final String S_STARTINGMONTH="startmonth";
	private final String S_STARTINGDAY="startday";
	private final String S_STARTINGHOUR="starthour";
	private final String S_STARTINGMINUTE="startminute";
	private final String S_DATEVIEW="dateview";
	
	public WidgetModel(int InstanceId) {
		super(InstanceId);
	}
	public WidgetModel(int InstanceId, String planet,String daysInYear, String monthInYear, 
			String hoursInDay, String minutesInHour,String secsInMinute){
		super(InstanceId);
		Planet=planet;
		DaysInYear=daysInYear;
		MonthInYear=monthInYear;
		HoursInDay=hoursInDay;
		MinutesInHour=minutesInHour;
		SecsInMinute=secsInMinute;
	}
	public void setPlanet(String txt){
		Planet=txt;
	}
	public void setDataView(String dateView){
		DateView=dateView;
	}
	
	public void setStartingTime(String year,String day,String hour,String minute){
		StartingYear=year;
		//StartingMonth=month;
		StartingDay=day;
		StartingHour=hour;
		StartingMinute=minute;
	}
	public String getPlanet(){
		return Planet;
	}
	public String getDaysInYear(){
		return DaysInYear;
	}
	public String getMonthInYear(){
		return MonthInYear;
	}
	public String getHoursInDay(){
		return HoursInDay;
	}
	public String getMinutesInHour(){
		return MinutesInHour;
	}
	public String getSecsInMinute(){
		return SecsInMinute;
	}
	public String getStartingYear(){
		return StartingYear;
	}
	//public String getStartingMonth(){return StartingMonth;}
	public String getStartingDay(){
		return StartingDay;
	}
	public String getStartingHour(){
		return StartingHour;
	}
	public String getStartingMinute(){
		return StartingMinute;
	}
	public String getDateView(){
		return DateView;
	}
	
	public static final String WIDGET_PROVIDER_STRING_NAME="com.example.widgetnew.WidgetProvider";

	public void init() {}

	public String getPrefName() {
		return WIDGET_PROVIDER_STRING_NAME;
	}

	public Map<String, String> getPrefsToSave() {
		Map<String,String> map=new HashMap<String,String>();
		map.put(S_PLANET, Planet);
		map.put(S_DAYS, DaysInYear);
		map.put(S_MONTH, MonthInYear);
		map.put(S_HOURS, HoursInDay);
		map.put(S_MINUTE, MinutesInHour);
		map.put(S_SECS, SecsInMinute);
		map.put(S_STARTINGYEAR, StartingYear);
		//map.put(S_STARTINGMONTH, StartingMonth);
		map.put(S_STARTINGDAY, StartingDay);
		map.put(S_STARTINGHOUR, StartingHour);
		map.put(S_STARTINGMINUTE, StartingMinute);
		map.put(S_DATEVIEW, DateView);
		return map;
	}
	
	public void setValueForPref(String key, String value) {
		if(key.equals(S_PLANET))Planet=value;
		if(key.equals(S_DAYS))DaysInYear=value;
		if(key.equals(S_MONTH))MonthInYear=value;
		if(key.equals(S_HOURS))HoursInDay=value;
		if(key.equals(S_MINUTE))MinutesInHour=value;
		if(key.equals(S_SECS))SecsInMinute=value;
		if(key.equals(S_STARTINGYEAR))StartingYear=value;
		if(key.equals(S_STARTINGDAY))StartingDay=value;
		//if(key.equals(S_STARTINGMONTH))StartingMonth=value;
		if(key.equals(S_STARTINGHOUR))StartingHour=value;
		if(key.equals(S_STARTINGMINUTE))StartingMinute=value;
		if(key.equals(S_DATEVIEW))DateView=value;
		return;
	}

	public static WidgetModel retrieveModel(Context context, int widgetId){
		WidgetModel wm=new WidgetModel(widgetId);
		boolean found=wm.retrievePref(context);
		return found?wm:null;
	}
	
	public static void clearAllPreferences(Context context){
		APrefWidgetModel.clearAllPreferences(context,WIDGET_PROVIDER_STRING_NAME);
	}
}
