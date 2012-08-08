package com.galaxtime.widget;

import com.galaxtime.R;

import android.util.Log;

public class TimeUtils {
	
	public static long currentSeconds(int year,int day,int hour,int minute){
		int currentYear=getCurrentYear();
		long sec=0;
		Log.v("mes", year+" "+day+" "+hour+" "+minute);
		if(year<=1970){
			int grYear=GreatYearCounter(year,currentYear);
			sec=(1970-year)*365*24*60*60+grYear*24*60*60;                            //!!Нужно учесть часовой пояс
			sec=sec+(long)(System.currentTimeMillis()/1000);
		}
		if(year>1970){
			int grYear=GreatYearCounter(1970,year);
			sec=(year-1970)*365*24*60*60+(grYear+day)*24*60*60+hour*60*60+minute*60;
			sec=(long)(System.currentTimeMillis()/1000)-sec;
		}
		return sec;
	}
	public static String[] getDateAndTime(int dateView,int daysInYear,int monthInYear,
			int hoursInDay,float minutesInHour,float secsInMinute,long curTime){
		int day,year,month=0,hour,minute;
		Log.v("mes", dateView+" "+daysInYear+" "+monthInYear+" "+hoursInDay+" "+minutesInHour+" "+secsInMinute);

		if(monthInYear==-1){
			Log.v("mes", curTime+"kigu");
			year=(int)(curTime/(daysInYear*hoursInDay*minutesInHour*secsInMinute));
			Log.v("mes", year+"");
			day=(int)(curTime/(secsInMinute*minutesInHour*hoursInDay)-year*daysInYear);
			hour=(int)(curTime/(secsInMinute*minutesInHour)-year*daysInYear*hoursInDay-day*hoursInDay);
			minute=(int)(curTime/secsInMinute-year*daysInYear*hoursInDay*minutesInHour-
					day*hoursInDay*minutesInHour-hour*minutesInHour);
		}else{
			Log.v("mes", "data "+daysInYear+" "+monthInYear+" "+hoursInDay+" "+minutesInHour+" "+secsInMinute);
			year=(int)(curTime/(daysInYear*monthInYear*hoursInDay*minutesInHour*secsInMinute));
			month=(int)((curTime/(secsInMinute*minutesInHour*hoursInDay*daysInYear))-year*monthInYear);
			day=(int)((curTime/(secsInMinute*minutesInHour*hoursInDay))-year*monthInYear*daysInYear-
					month*daysInYear);
			hour=(int)(curTime/(secsInMinute*minutesInHour)-year*monthInYear*daysInYear*hoursInDay-
					month*daysInYear*hoursInDay-day*hoursInDay);
			minute=(int)(curTime/secsInMinute-year*monthInYear*daysInYear*hoursInDay*minutesInHour-
					month*daysInYear*hoursInDay*minutesInHour-day*hoursInDay*minutesInHour-hour*minutesInHour);
		}
		
		if((int)(hour/10)==0){hour=Integer.valueOf("0"+hour);}
		if((int)(minute/10)==0){minute=Integer.valueOf("0"+minute);}
			
		String date;
		switch(dateView){
			case R.id.new_RadioBut_DDxYY:
				date=day+"."+year;
				break;
			case R.id.new_RadioBut_DD_YY:
				date=day+"/"+year;
				break;
			case R.id.new_RadioBut_DD_MM_YY:
				date=day+"/"+month+"/"+year;
				break;
			case R.id.new_RadioBut_DDxMMxYY:
				date=day+"."+month+"."+year;
				break;
			case R.id.new_RadioBut_MM_DD_YY:
				date=month+"/"+day+"/"+year;
				break;
			default:
				date="Ошибка даты";
				break;
		}
		String time=hour+":"+minute;
		Log.v("mes", time+" "+date);
		String[] dateAndTime=new String[]{date,time};
		return dateAndTime;
	}
	
	public static int GreatYearCounter(int startingYear,int currentYear){
		int cnt=0;
		for(int i=startingYear;i<=currentYear;i++){
			if(((i%4==0)&(i%100!=0))|((i%4==0)&(i%100==0)&(i%400==0))){         //!!!Нужно отнять день,если высокостный год и 29 февралявпереди!!!
				cnt=cnt+1;                                                   
			}
		}
		return cnt;
	}
	
	public static int getCurrentYear(){
		return ((int)(System.currentTimeMillis()/(1000*60*60*24*(365.2425)))+1970);
	}
	
}
