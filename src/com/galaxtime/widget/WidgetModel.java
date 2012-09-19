package com.galaxtime.widget;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class WidgetModel extends APrefWidgetModel{

	private int WidgetId;
	private String Planet;
	private String Country;
	private String City;
	private float DaysInYear;
	private float DaysInMonth;
	private float MonthInYear;
	private float HoursInDay;
	private float MinutesInHour;
	private float SecsInMinute;
	private int CurrentYear;
	private int CurrentMonth;
	private int CurrentDay;
	private int CurrentHour;
	private int CurrentMinute;
	private int StartingYear;
	private int StartingDay;
	private int StartingHour;
	private int StartingMinute;
	private int DateView;
	private String maxTemp;
	private String minTemp;
	private int warmestTime;
	private int coldestTime;
	private int birthdayMonth;
	private int birthdayYear;
	private int birthdayDay;
	private int birthdayAlarm;
	private double ageBefore;
	private String weather;
	private int icon;
	private long launchingSecond;
	private String synchronize;
		
	private final String S_WIDGETID="widget_id";
	private final String S_PLANET="planet";
	private final String S_COUNTRY="country";
	private final String S_CITY="city";
	private final String S_DAYSINYEAR="daysinyear";
	private final String S_DAYSINMONTH="daysinmonth";
	private final String S_MONTHINYEAR="monthinyear";
	private final String S_MONTH="month";
	private final String S_HOURS="hour";
	private final String S_MINUTE="minute";
	private final String S_SECS="seconds";
	private final String S_CURRENTYEAR="currentyear";
	private final String S_CURRENTMONTH="currentmonth";
	private final String S_CURRENTDAY="currentday";
	private final String S_CURRENTHOUR="currenthour";
	private final String S_CURRENTMINUTE="currentminute";
	private final String S_STARTINGYEAR="startingyear";
	private final String S_STARTINGDAY="startingday";
	private final String S_STARTINGHOUR="startinghour";
	private final String S_STARTINGMINUTE="startingminute";
	private final String S_DATEVIEW="dateview";
	private final String S_MAXTEMP="maxtemp";
	private final String S_MINTEMP="mintemp";
	private final String S_WARMESTTIME="warmesttime";
	private final String S_COLDESTTIME="coldesttime";
	private final String S_BIRTHDAYDAY="birthdayday";
	private final String S_BIRTHDAYMONTH="birthdaymonth";
	private final String S_BIRTHDAYYEAR="birthdayyear";
	private final String S_BIRTHDAYALARM="birthdayalarm";
	private final String S_AGEBEFORE="agebefore";
	private final String S_WEATHER="weather";
	private final String S_ICON="icon";
	private final String S_LAUNCHINGSECOND="launchingsecond";
	private final String S_SYNCHRONIZE="synchronize";
	
	public WidgetModel(int InstanceId) {
		super(InstanceId);
		WidgetId=InstanceId;
		synchronize="";
	}
	public void setTimeParams(float daysInYear,float monthInYear,float daysInMonth,
			float hoursInDay, float minutesInHour,float secsInMinute){
		DaysInYear=daysInYear;
		MonthInYear=monthInYear;
		DaysInMonth=daysInMonth;
		HoursInDay=hoursInDay;
		MinutesInHour=minutesInHour;
		SecsInMinute=secsInMinute;
	}
	public void setTemperaturesParams(String maxTemperature, String minTemperature,
			int coldestTime,int warmestTime){
		maxTemp=maxTemperature;
		minTemp=minTemperature;
		this.coldestTime=coldestTime;
		this.warmestTime=warmestTime;
	}
	
	public void setLaunchingSecond(long sec){
		launchingSecond=sec;
	}
	
	public void setPlanetName(String planet,String country,String city){
		Planet=planet;
		Country=country;
		City=city;
	}
	public void setDataView(int dateView){
		DateView=dateView;
	}
	
	public void setCurrentTime(int year,int month,int day,int hour,int minute){
		CurrentYear=year;
		CurrentMonth=month;
		CurrentDay=day;
		CurrentHour=hour;
		CurrentMinute=minute;
	}
	
	public void setStartingTime(int year,int day,int hour,int minute){
		StartingYear=year;
		StartingDay=day;
		StartingHour=hour;
		StartingMinute=minute;
	}
	
	public void setBirthday(int year,int month,int day,int alarm){
		birthdayDay=day;
		birthdayMonth=month;
		birthdayYear=year;
		birthdayAlarm=alarm;
	}
	public void setAgeBefore(double age){
		ageBefore=age;
	}
	
	public void setWeather(String weather){
		this.weather=weather;
	}

	public void setIcon(int iconButtonId){
		this.icon=iconButtonId;
	}
	public void setSynchronize(String synchro){
		synchronize=synchro;
	}
	public void addToSynchronize(String id){
		synchronize=synchronize+id+"/";
	}
	public void clearSynchronize(){
		synchronize="";
	}
	
	public String getPlanet(){
		return Planet;
	}
	public String getCountry(){
		return Country;
	}
	public String getCity(){
		return City;
	}
	public float getDaysInYear(){
		return DaysInYear;
	}
	public float getDaysInMonth(){
		return DaysInMonth;
	}
	public float getMonthInYear(){
		return MonthInYear;
	}
	public float getHoursInDay(){
		return HoursInDay;
	}
	public float getMinutesInHour(){
		return MinutesInHour;
	}
	public float getSecsInMinute(){
		return SecsInMinute;
	}
	public int getCurrentYear(){
		return CurrentYear;
	}
	public int getCurrentMonth() {
		return CurrentMonth;
	}
	public int getCurrentDay(){
		return CurrentDay;
	}
	public int getCurrentHour(){
		return CurrentHour;
	}
	public int getCurrentMinute(){
		return CurrentMinute;
	}
	public int getStartingYear() {
		return StartingYear;
	}
	public int getStartingDay(){
		return StartingDay;
	}
	public int getStartingHour(){
		return StartingHour;
	}
	public int getStartingMinute(){
		return StartingMinute;
	}
	public int getDateView(){
		return DateView;
	}
	public String getMaxTemperature(){
		return maxTemp;
	}
	public String getMinTemperature(){
		return minTemp;
	}
	public int getWarmestTime(){
		return warmestTime;
	}
	public int getColdestTime(){
		return coldestTime;
	}
	public int getDayOfBirthday(){
		return birthdayDay;
	}
	public int getMonthOfBirthday(){
		return birthdayMonth;
	}
	public int getYearOfBirthday(){
		return birthdayYear;
	}
	public int getBirthdayAlarm(){
		return birthdayAlarm;
	}
	public String getWeather(){
		return weather;
	}
	public int getIcon(){
		return icon;
	}
	public double getAgeBefore() {
		return ageBefore;
	}
	public int getId() {
		return WidgetId;
	}
	public long getLaunchingSecond(){
		return launchingSecond;
	}
	public String getSynchronize(){
		return synchronize;
	}
	
	public static final String WIDGET_PROVIDER_STRING_NAME="com.example.widgetnew.WidgetProvider";

	public void init() {}

	public String getPrefName() {
		return WIDGET_PROVIDER_STRING_NAME;
	}

	public Map<String, String> getPrefsToSave() {
		Map<String,String> map=new HashMap<String,String>();
		map.put(S_WIDGETID, String.valueOf(WidgetId));
		map.put(S_PLANET, Planet);
		map.put(S_COUNTRY, Country);
		map.put(S_CITY, City);
		map.put(S_DAYSINYEAR, String.valueOf(DaysInYear));
		map.put(S_DAYSINMONTH, String.valueOf(DaysInMonth));
		map.put(S_MONTHINYEAR, String.valueOf(MonthInYear));
		map.put(S_MONTH, String.valueOf(MonthInYear));
		map.put(S_HOURS, String.valueOf(HoursInDay));
		map.put(S_MINUTE, String.valueOf(MinutesInHour));
		map.put(S_SECS, String.valueOf(SecsInMinute));
		map.put(S_CURRENTYEAR, String.valueOf(CurrentYear));
		map.put(S_CURRENTMONTH, String.valueOf(CurrentMonth));
		map.put(S_CURRENTDAY, String.valueOf(CurrentDay));
		map.put(S_CURRENTHOUR, String.valueOf(CurrentHour));
		map.put(S_CURRENTMINUTE, String.valueOf(CurrentMinute));
		map.put(S_STARTINGYEAR, String.valueOf(StartingYear));
		map.put(S_STARTINGDAY, String.valueOf(StartingDay));
		map.put(S_STARTINGHOUR, String.valueOf(StartingHour));
		map.put(S_STARTINGMINUTE, String.valueOf(StartingMinute));
		map.put(S_DATEVIEW, String.valueOf(DateView));
		map.put(S_MAXTEMP, String.valueOf(maxTemp));
		map.put(S_MINTEMP, String.valueOf(minTemp));
		map.put(S_COLDESTTIME, String.valueOf(coldestTime));
		map.put(S_WARMESTTIME, String.valueOf(warmestTime));
		map.put(S_BIRTHDAYDAY, String.valueOf(birthdayDay));
		map.put(S_BIRTHDAYMONTH, String.valueOf(birthdayMonth));
		map.put(S_BIRTHDAYYEAR, String.valueOf(birthdayYear));
		map.put(S_BIRTHDAYALARM, String.valueOf(birthdayAlarm));
		map.put(S_AGEBEFORE, String.valueOf(ageBefore));
		map.put(S_WEATHER, weather);
		map.put(S_ICON, String.valueOf(icon));
		map.put(S_LAUNCHINGSECOND, String.valueOf(launchingSecond));
		map.put(S_SYNCHRONIZE, String.valueOf(synchronize));
		return map;
	}
	
	public void setValueForPref(String key, String value) {

		if(key.equals(getStoredKeyForFieldName(S_WIDGETID)))WidgetId=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_PLANET)))Planet=value;
		if(key.equals(getStoredKeyForFieldName(S_COUNTRY)))Country=value;
		if(key.equals(getStoredKeyForFieldName(S_CITY)))City=value;
		if(key.equals(getStoredKeyForFieldName(S_MONTHINYEAR)))MonthInYear=Float.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_DAYSINYEAR)))DaysInYear=Float.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_DAYSINMONTH)))DaysInMonth=Float.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_MONTH)))MonthInYear=Float.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_HOURS)))HoursInDay=Float.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_MINUTE)))MinutesInHour=Float.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_SECS)))SecsInMinute=Float.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_STARTINGYEAR)))StartingYear=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_STARTINGDAY)))StartingDay=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_STARTINGHOUR)))StartingHour=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_STARTINGMINUTE)))StartingMinute=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_DATEVIEW)))DateView=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_CURRENTYEAR)))CurrentYear=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_CURRENTMONTH)))CurrentMonth=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_CURRENTDAY)))CurrentDay=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_CURRENTHOUR)))CurrentHour=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_CURRENTMINUTE)))CurrentMinute=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_MAXTEMP)))maxTemp=value;
		if(key.equals(getStoredKeyForFieldName(S_MINTEMP)))minTemp=value;
		if(key.equals(getStoredKeyForFieldName(S_COLDESTTIME)))coldestTime=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_WARMESTTIME)))warmestTime=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_BIRTHDAYDAY)))birthdayDay=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_BIRTHDAYMONTH)))birthdayMonth=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_BIRTHDAYYEAR)))birthdayYear=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_BIRTHDAYALARM)))birthdayAlarm=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_AGEBEFORE)))ageBefore=Double.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_WEATHER)))weather=value;
		if(key.equals(getStoredKeyForFieldName(S_ICON)))icon=Integer.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_LAUNCHINGSECOND)))launchingSecond=Long.valueOf(value);
		if(key.equals(getStoredKeyForFieldName(S_SYNCHRONIZE)))synchronize=value;
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
	@Override
	public void removePrefs(Context context) {
		Map<String, String> keyValuePairs = getPrefsToSave();
		if (keyValuePairs == null) {
			return;
		}
		SharedPreferences.Editor prefs = context.getSharedPreferences(getPrefName(), 0).edit();
		for (String key : keyValuePairs.keySet()) {
			removePref(prefs, key);
		}
		prefs.commit();
	}
	private void removePref(SharedPreferences.Editor prefs, String key) {
		String newkey = getStoredKeyForFieldName(key);
		prefs.remove(newkey);
	}
}
