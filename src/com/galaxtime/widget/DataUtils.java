package com.galaxtime.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.galaxtime.R;
import com.galaxtime.database.ColumnNames;

public class DataUtils {

	/**
	 * Returns how many great years exists between startingYear and currentYear.
	 */
	public static int GreatYearCounter(int startingYear,int currentYear){
		int cnt=0;
		for(int i=startingYear;i<=currentYear;i++){
			if(((i%4==0)&(i%100!=0))|((i%4==0)&(i%100==0)&(i%400==0))){         //!!!Нужно отнять день,если высокостный год и 29 февралявпереди!!!
				cnt=cnt+1;                                                   
			}
		}
		return cnt;
	}
	/**
	 * Returns current year.
	 */
	public static int getCurrentEarthYear(){
		return ((int)(System.currentTimeMillis()/(1000*60*60*24*(365.2425)))+1970);
	}
	/**
	 * Function computes current tempearture by data about 
	 * time of planet from WidgetModel.
	 * @param model WidgetModel, which contains current time, warmest and coldest 
	 * time, maximum and minimum temperature.
	 * @return Current temperature.
	 */
	public static String getCurrentTemperature(WidgetModel model) {
		String MaxTemperature=model.getMaxTemperature()+"";
		String MinTemperature=model.getMinTemperature()+"";
		int warmestTime=model.getWarmestTime();
		int coldestTime=model.getColdestTime();
		float hoursInDay=model.getHoursInDay();
		float minutesInHour=model.getMinutesInHour();
		int currentHour=model.getCurrentHour();
		int currentMinute=model.getCurrentMinute();
		
		return getCurrentTemperature(MaxTemperature,MinTemperature,warmestTime,coldestTime,hoursInDay,
			minutesInHour,currentHour,currentMinute);
	}
	
	
	/**
	 * Function computes current tempearture by data about 
	 * time of planet from WidgetModel.
	 * @param model WidgetModel, which contains current time, warmest and coldest 
	 * time, maximum and minimum temperature.
	 * @return Current temperature.
	 */
	public static String getCurrentTemperature(Cursor cur) {
		String MaxTemperature=cur.getString(cur.getColumnIndex(ColumnNames.MAX_TEMPERATURE));
		String MinTemperature=cur.getString(cur.getColumnIndex(ColumnNames.MIN_TEMPERATURE));
		int warmestTime=cur.getInt(cur.getColumnIndex(ColumnNames.WARMEST_TIME));
		int coldestTime=cur.getInt(cur.getColumnIndex(ColumnNames.COLDEST_TIME));;
		float hoursInDay=cur.getFloat(cur.getColumnIndex(ColumnNames.HOURS_IN_DAY));;
		float minutesInHour=cur.getFloat(cur.getColumnIndex(ColumnNames.MINUTES_IN_HOUR));;
		int currentHour=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_HOUR));
		int currentMinute=cur.getInt(cur.getColumnIndex(ColumnNames.STARTING_MINUTE));;
		
		return getCurrentTemperature(MaxTemperature,MinTemperature,warmestTime,coldestTime,hoursInDay,
			minutesInHour,currentHour,currentMinute);
	}
	
	private static String getCurrentTemperature(String MaxTemperature,
			String MinTemperature, int warmestTime, int coldestTime,
			float hoursInDay, float minutesInHour, int currentHour,
			int currentMinute) {
		int time1,time2;
		int currentTemperature=0;
		int maxTemperature=Integer.valueOf(MaxTemperature.substring(0, MaxTemperature.length()-1));
		int minTemperature=Integer.valueOf(MinTemperature.substring(0, MinTemperature.length()-1));
		if(warmestTime>coldestTime){
			time1=coldestTime;time2=warmestTime;
		}else{
			time2=coldestTime;time1=warmestTime;
		}
		if((currentHour<time2)&(currentHour>=time1)){
			float dt=maxTemperature-minTemperature;
			float difTemp1=dt/(Math.abs(warmestTime-coldestTime)*minutesInHour);
			if(time2==warmestTime){
				currentTemperature=(int)(minTemperature+
						difTemp1*(currentMinute+(currentHour-coldestTime)*minutesInHour));
			}else{
				currentTemperature=(int)(maxTemperature-
						difTemp1*(currentMinute+(currentHour-warmestTime)*minutesInHour));
			}
		}else{
			float dt=maxTemperature-minTemperature;
			float difTemp2=dt/(Math.abs(hoursInDay-warmestTime+coldestTime)*minutesInHour);
			if(time2==warmestTime){
				if(currentHour>=time2){
					currentTemperature=(int)(maxTemperature-difTemp2*(currentMinute+(currentHour-warmestTime)*minutesInHour));
				}else{
					currentTemperature=(int)(maxTemperature-difTemp2*minutesInHour*((hoursInDay-warmestTime)+currentHour)-difTemp2*currentMinute);
				}
			}else{
				if(currentHour>=time2){
					currentTemperature=(int)(minTemperature+difTemp2*(currentMinute+(currentHour-coldestTime)*minutesInHour));
				}else{
					currentTemperature=(int)(minTemperature+difTemp2*minutesInHour*((hoursInDay-coldestTime)+currentHour)+difTemp2*currentMinute);
				}
			}
		}
		return currentTemperature+" "+MinTemperature.substring(MinTemperature.length()-1);		
	}
	
	public static String getWeather(String weather) {
		String currentWeather="";
		int k=0;
		for(int i=0;i<weather.length();i++){
			if(weather.charAt(i)=='/'){
				k=k+1;
			}
		}
		if(k==0)return "";
		
		k=(int)(k*Math.random())+1;
		for(int i=0;i<weather.length();i++){
			if(weather.charAt(i)=='/'){
				k=k-1;
				if(k==0)break;
				currentWeather="";
			}else{
				currentWeather=currentWeather+weather.charAt(i);
			}
		}
		return currentWeather;
	}
	
	/**
	 * Returns id of drawable resources, which according to selected RadioButton.
	 */
	public static int getIconId(int iconButtonId) {
		int iconId;
		switch(iconButtonId){
			case R.id.new_RadioBut_planet1:
				iconId=R.drawable.plan_1;
				break;
			case R.id.new_RadioBut_planet2:
				iconId=R.drawable.plan_2;
				break;
			case R.id.new_RadioBut_planet3:
				iconId=R.drawable.plan_3;
				break;
			case R.id.new_RadioBut_planet4:
				iconId=R.drawable.plan_4;
				break;
			case R.id.new_RadioBut_planet5:
				iconId=R.drawable.plan_5;
				break;
			case R.id.new_RadioBut_planet6:
				iconId=R.drawable.plan_6;
				break;
			case R.id.new_RadioBut_planet7:
				iconId=R.drawable.plan_7;
				break;
			case R.id.new_RadioBut_planet8:
				iconId=R.drawable.plan_8;
				break;
			case R.id.new_RadioBut_planet9:
				iconId=R.drawable.plan_9;
				break;
			case R.id.new_RadioBut_planet10:
				iconId=R.drawable.plan_10;
				break;
			case R.id.new_RadioBut_planet11:
				iconId=R.drawable.plan_11;
				break;
			default:
				iconId=R.drawable.plan_12;
				break;
		}
		return iconId;
	}
	
	/**
	 * Defines time of day. If it now first fifth of the day, then night, seconds - morning,
	 * third and fourth - day, fifth - evening.
	 * @param hoursInDay How many hours in day.
	 * @param currentHour current hour on your planet.
	 * @return Time of day
	 */
	public static int getTimeOfDay(int hoursInDay, int currentHour) {
		int timeOfDay=1;
		int partOfDay=Integer.valueOf(hoursInDay/5);
		if(currentHour<partOfDay)
			timeOfDay=R.string.night;
		if((currentHour>=partOfDay)&(currentHour<(2*partOfDay)))
			timeOfDay=R.string.morning;
		if((currentHour>=(2*partOfDay))&(currentHour<(4*partOfDay)))
			timeOfDay=R.string.day;
		if(currentHour>=(4*partOfDay))
			timeOfDay=R.string.evening;
		return timeOfDay;
	}
	public static double getSecondsInMinute(double earthDaysInYear,float daysInYear,
		float hoursInDay, float minutesInHour) {
		double secsInMinute=(earthDaysInYear*24*60*60)/
			(daysInYear*hoursInDay*minutesInHour);
		return (float)secsInMinute;
	}

	/**
	 * This function gives data of time and date from Widget model, additions one minute 
	 * and writes new data to Widget model again.
	 * @param model Widget model, where making change of data. 
	 * @param size Size of widget
	 */
	public static void RecalculateTime(Context context,WidgetModel model,int size) {
		float secsInMinute=model.getSecsInMinute();
		float minutesInHour=model.getMinutesInHour();
		float hoursInDay=model.getHoursInDay();
		float daysInMonth=model.getDaysInMonth();
		float monthInYear=model.getMonthInYear();
		float daysInYear=model.getDaysInYear();
		int startingYear=model.getStartingYear();
		int startingDay=model.getStartingDay();
		int startingHour=model.getStartingHour();
		int startingMinute=model.getStartingMinute();
		double minutesLeft=(System.currentTimeMillis()-model.getLaunchingSecond())/(1000*secsInMinute);
		minutesLeft= (minutesLeft+startingMinute+startingHour*minutesInHour+
			(startingDay-1)*hoursInDay*minutesInHour+(startingYear-1)*daysInYear*hoursInDay*minutesInHour);
		long period=(long)(secsInMinute*333);
		//if(period<1000){period=period*10;}
		int year=1,month=1,day=1,hour=1,minute=1;
		if(monthInYear==-1){
			year=(int)(minutesLeft/(daysInYear*hoursInDay*minutesInHour));
			day=(int)((minutesLeft/(minutesInHour*hoursInDay))-(year*daysInYear));
			hour=(int)((minutesLeft/minutesInHour)-(year*daysInYear*hoursInDay)-
				(day*hoursInDay));
			minute=(int)(minutesLeft-(minutesInHour*year*daysInYear*hoursInDay)-
				(minutesInHour*day*hoursInDay)-(minutesInHour*hour));
			year=year+1;
			day=day+1;
		}else{
			year=(int)(minutesLeft/(minutesInHour*hoursInDay*daysInYear));
			month=(int)((minutesLeft/(minutesInHour*hoursInDay*daysInMonth))-
				(year*monthInYear));
			day=(int)((minutesLeft/(minutesInHour*hoursInDay))-
				(year*daysInYear)-(month*daysInMonth));
			hour=(int)((minutesLeft/(minutesInHour))-(year*daysInYear*hoursInDay)-
				(month*daysInMonth*hoursInDay)-(day*hoursInDay));
			minute=(int)(minutesLeft-(minutesInHour*year*daysInYear*hoursInDay)-
				(month*daysInMonth*hoursInDay*minutesInHour)-(minutesInHour*day*hoursInDay)-
				(minutesInHour*hour));
			year=year+1;
			day=day+1;
			month=month+1;
		}
		model.setCurrentTime(year, month, day, hour, minute);
		model.SavePreferences(context);
		UpdateUtils.startAlarmManager(context, model.getId(),period, size);
		UpdateUtils.updateWidget(context, AppWidgetManager.getInstance(context), model, size);
	}	
	
	/**
	 * Transform current data to string according to dateView rule.
	 */
	public static String getDate(int dateView,int currentYear, int currentMonth,int currentDay) {
		String date;
		switch(dateView){
			case R.id.new_RadioBut_DDxYY:
				date=currentDay+"."+currentYear;
				break;
			case R.id.new_RadioBut_DD_YY:
				date=currentDay+"/"+currentYear;
				break;
			case R.id.new_RadioBut_DD_MM_YY:
				date=currentDay+"/"+currentMonth+"/"+currentYear;
				break;
			case R.id.new_RadioBut_DDxMMxYY:
				date=currentDay+"."+currentMonth+"."+currentYear;
				break;
			case R.id.new_RadioBut_MM_DD_YY:
				date=currentMonth+"/"+currentDay+"/"+currentYear;
				break;
			default:
				date="Ошибка даты";
				break;
		}
		return date;
	}

	/**
	 * Transform current time to string.
	 */
	public static String getTime(int hour, int minute) {
		String time=(((""+hour).length()<2)?"0"+hour:hour)+":"+(((""+(long)minute).length()<2)?"0"+(long)minute:(long)minute);
		return time;
	}
	
	/**
	 * This function computes age.
	 * @param model Widget model, where situated data.
	 */
	public static double getAge(WidgetModel model) {
		int yearOfBirthday=Integer.valueOf(model.getYearOfBirthday());
		int monthOfBirthday=Integer.valueOf(model.getMonthOfBirthday());
		int dayOfBirthday=Integer.valueOf(model.getDayOfBirthday()); 
		float daysInYear=Float.valueOf(model.getDaysInYear());
		float daysInMonth=Float.valueOf(model.getDaysInMonth());
		float monthInYear=Float.valueOf(model.getMonthInYear());
		float hoursInDay=Float.valueOf(model.getHoursInDay());
		float minutesInHour=Float.valueOf(model.getMinutesInHour());
		float secsInMinute=Float.valueOf(model.getSecsInMinute());
		return getAge(yearOfBirthday,monthOfBirthday,dayOfBirthday,daysInYear,monthInYear,
			daysInMonth,hoursInDay,	minutesInHour,secsInMinute);
	}
	/**
	 * This function computes age.
	 * @param cur Cursor, where situated data.
	 */
	public static double getAge(Cursor cur) {
		int yearOfBirthday=cur.getInt(cur.getColumnIndex(ColumnNames.BIRTHDAY_YEAR));
		int monthOfBirthday=cur.getInt(cur.getColumnIndex(ColumnNames.BIRTHDAY_MONTH));
		int dayOfBirthday=cur.getInt(cur.getColumnIndex(ColumnNames.BIRTHDAY_DAY));
		float daysInYear=cur.getFloat(cur.getColumnIndex(ColumnNames.DAYS_IN_YEAR));
		float monthInYear=cur.getFloat(cur.getColumnIndex(ColumnNames.MONTH_IN_YEAR));
		float daysInMonth=cur.getFloat(cur.getColumnIndex(ColumnNames.DAYS_IN_MONTH));
		float hoursInDay=cur.getFloat(cur.getColumnIndex(ColumnNames.HOURS_IN_DAY));
		float minutesInHour=cur.getFloat(cur.getColumnIndex(ColumnNames.MINUTES_IN_HOUR));
		float secsInMinute=cur.getFloat(cur.getColumnIndex(ColumnNames.SEСS_IN_MINUTE));
		return getAge(yearOfBirthday,monthOfBirthday,dayOfBirthday,daysInYear,monthInYear,
			daysInMonth,hoursInDay,	minutesInHour,secsInMinute);
		
	}
	private static double getAge(int yearOfBirthday, int monthOfBirthday,
			int dayOfBirthday, float daysInYear, float monthInYear,
			float daysInMonth, float hoursInDay, float minutesInHour,
			float secsInMinute) {
		
		int x=(int) Math.floor(monthOfBirthday/2);  //Учет того, что в месяце не всегда 30 дней
		if(monthOfBirthday>2){x=x-2;}
		if(monthOfBirthday>=9){x=x+1;}
		if((monthOfBirthday==10)||(monthOfBirthday==12)){x=x-1;}
		int day=dayOfBirthday+(monthOfBirthday-1)*30+x;
		float secondsOfBirthday=currentSeconds(yearOfBirthday,day,0,0);
		double age;
		if(monthInYear==-1){
			age=(double)(secondsOfBirthday/(daysInYear*hoursInDay*minutesInHour*secsInMinute));
		}else{
			age=(double)(secondsOfBirthday/(monthInYear*daysInMonth*hoursInDay*minutesInHour*secsInMinute));
		}
		age=(Math.floor(age*100))/100;
		if(age<0)age=0;
		return age;
	}
	/**
	 * Returns the number of seconds elapsed since time in params.
	 */
	public static long currentSeconds(int year,int day,int hour,int minute){
		int currentYear=getCurrentEarthYear();
		long lYear=(long)year,lDay=(long)day,lHour=(long)hour,lMinute=(long)minute;
		long sec=0;
		if(year<=1970){
			long grYear=GreatYearCounter(year,currentYear);
			long x=(1970-lYear)*365*24*60*60-lDay*24*60*60-lHour*60*60-lMinute*60;
			sec=x+grYear*24*60*60;                           
			sec=sec+(long)(System.currentTimeMillis()/1000);
		}
		if(year>1970){
			long grYear=GreatYearCounter(1970,year);
			sec=(year-1970)*365*24*60*60+(grYear+day)*24*60*60+hour*60*60+minute*60;
			sec=(long)(System.currentTimeMillis()/1000)-sec;
		}
		return sec;
	}
	public static Bitmap TimeBitmap(Context context,String text,int size) {
		int textSize=40;
		float textScale=1;
		int width=300;
		int len=text.length();
		switch (size){
			case UpdateUtils.LARGE:
				width=200;
				textSize=80;
		 		textScale=(float)0.7-(float)((len-5)*0.13);
		 		break;
		 	case UpdateUtils.MEDIUM:
		 		textSize=80;
		 		textScale=(float)1.4-(float)((len-5)*0.3);
		 		width=250;
		 		break;
		 	case UpdateUtils.SMALL:
		 		textSize=55;
		 		textScale=(float)1.4-(float)((len-5)*0.2);
		 		width=250;
		 		break;
		 }
		 if(textScale<0.1){
	 		textScale=Float.valueOf("0.1");
	 	 }
	     Bitmap myBitmap = Bitmap.createBitmap(width, textSize, Bitmap.Config.ARGB_4444);
	     Canvas myCanvas = new Canvas(myBitmap);
	     Paint paint = new Paint();
	     Typeface clock = Typeface.createFromAsset(context.getAssets(),"fonts/digital.ttf");
	     paint.setAntiAlias(true);
	     paint.setSubpixelText(true);
	     paint.setTypeface(clock);
	     paint.setTextScaleX(textScale);
	     paint.setStyle(Paint.Style.FILL);
	     paint.setColor(Color.RED);
	     paint.setTextSize(textSize);
	     myCanvas.drawText(text, 0, textSize, paint);
	     return myBitmap;
	 }
}
