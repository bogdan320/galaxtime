package com.galaxtime.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.galaxtime.R;
import com.galaxtime.database.BaseHelper;
import com.galaxtime.database.ColumnNames;

public class CreateNewWorld extends Activity implements DialogInterface.OnClickListener{
	private Button butCreateWorld;
	private Context context;
	private EditText monthInYear;
	private RadioGroup dateView,dateType;
	private RadioButton DDxYY,DD_YY,DD_MM_YY,MM_DD_YY,DDxMMxYY;
	private EditText weatherChange;
	private View view;
	private int type;
	private final int WEATHER=324;
	private String PlanetName;
	private CheckBox weather1;
	private CheckBox weather2;
	private CheckBox weather3;
	private CheckBox weather4;
	private CheckBox weather5;
	private CheckBox weather6;
	private CheckBox weather7;
	private CheckBox weather8;
	private CheckBox weather9;
	private EditText maxTemperature;
	private EditText minTemperature;
	ProgressDialog progrDial;

	private TableRadioButtonGroup iconGroup;
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt("changedItem", iconGroup.getCheckedRadioButtonId());
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		iconGroup.setCheckedRadioButton(findViewById(savedInstanceState.getInt("changedItem")));
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.create_new_world);
		type=this.getIntent().getExtras().getInt("type");
		context=this;
		dateView=(RadioGroup)findViewById(R.id.new_RadioGroup_dateView);
		DDxYY=(RadioButton)findViewById(R.id.new_RadioBut_DDxYY);
		DD_YY=(RadioButton)findViewById(R.id.new_RadioBut_DD_YY);
		DD_MM_YY=(RadioButton)findViewById(R.id.new_RadioBut_DD_MM_YY);
		MM_DD_YY=(RadioButton)findViewById(R.id.new_RadioBut_MM_DD_YY);
		DDxMMxYY=(RadioButton)findViewById(R.id.new_RadioBut_DDxMMxYY);
		monthInYear=(EditText)findViewById(R.id.new_editText_monthInYear);
		dateType=(RadioGroup)findViewById(R.id.new_RadioGroup_dateType);
		maxTemperature=(EditText)findViewById(R.id.new_editText_maxTemp);
		minTemperature=(EditText)findViewById(R.id.new_editText_minTemp);
		iconGroup=new TableRadioButtonGroup(this);

		View.OnLongClickListener checkBoxClickListener=new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				view=v;
				showDialog(WEATHER);
				weatherChange.setText(((CheckBox)view).getText().toString());
				return true;
			}
		};

		View.OnKeyListener mantissLength=new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(!(keyCode==KeyEvent.KEYCODE_DEL)){
					mantissChecker(v);
				}
				return false;
			}
		};
		OnFocusChangeListener disabledFieldCounter=new OnFocusChangeListener(){
			@Override
			public void onFocusChange(View v, boolean arg1) {
				String monthInYear=((EditText)findViewById(R.id.new_editText_monthInYear)).getText().toString();
				String daysInYear=((EditText)findViewById(R.id.new_editText_daysInYear)).getText().toString();
				EditText DaysInMonth=(EditText)findViewById(R.id.new_editText_daysInMonth);
				boolean withMonth=((RadioButton)findViewById(R.id.new_RadioBut_daysAndMonthAndYear)).isChecked();
				if((withMonth)&(!monthInYear.equals(""))&(!daysInYear.equals(""))){
					float MonthInYear=Float.valueOf(monthInYear);
					float DaysInYear=Float.valueOf(daysInYear);
					float dayssInMonth=DaysInYear/MonthInYear;
					DaysInMonth.setEnabled(true);
					DaysInMonth.setText(dayssInMonth+"");
					mantissChecker(DaysInMonth);
					DaysInMonth.setEnabled(false);
				}
				String earthDaysInYear=((EditText)findViewById(R.id.new_editText_EarthDaysInYear)).getText().toString();
				String hoursInDay=((EditText)findViewById(R.id.new_EditText_hoursInDay)).getText().toString();
				String minutesInHour=((EditText)findViewById(R.id.new_editText_minutesInHour)).getText().toString();
				EditText SecsInMinute=(EditText)findViewById(R.id.new_editText_SecsInMinute);
				if((!earthDaysInYear.equals(""))&(!daysInYear.equals(""))&(!hoursInDay.equals(""))&(!minutesInHour.equals(""))){
					double secsInMinute=DataUtils.getSecondsInMinute(Double.valueOf(earthDaysInYear),Float.valueOf(daysInYear),
						Float.valueOf(hoursInDay),Float.valueOf(minutesInHour) );
					SecsInMinute.setEnabled(true);
					SecsInMinute.setText(secsInMinute+"");
					mantissChecker(SecsInMinute);
					SecsInMinute.setEnabled(false);
				}
			}
		};
		
		((EditText)findViewById(R.id.new_editText_daysInYear)).setOnKeyListener(mantissLength);
		((EditText)findViewById(R.id.new_editText_EarthDaysInYear)).setOnKeyListener(mantissLength);
		((EditText)findViewById(R.id.new_editText_monthInYear)).setOnKeyListener(mantissLength);
		((EditText)findViewById(R.id.new_EditText_hoursInDay)).setOnKeyListener(mantissLength);
		((EditText)findViewById(R.id.new_editText_minutesInHour)).setOnKeyListener(mantissLength);
		((EditText)findViewById(R.id.new_editText_monthInYear)).setOnFocusChangeListener(disabledFieldCounter);
		((EditText)findViewById(R.id.new_editText_daysInYear)).setOnFocusChangeListener(disabledFieldCounter);
		((EditText)findViewById(R.id.new_editText_EarthDaysInYear)).setOnFocusChangeListener(disabledFieldCounter);
		((EditText)findViewById(R.id.new_EditText_hoursInDay)).setOnFocusChangeListener(disabledFieldCounter);
		((EditText)findViewById(R.id.new_editText_minutesInHour)).setOnFocusChangeListener(disabledFieldCounter);
		maxTemperature.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(maxTemperature.getText().toString().equals(getResources().getString(R.string.max))){
					maxTemperature.setText("");
				}
				return false;
			}
		});
		minTemperature.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(minTemperature.getText().toString().equals(getResources().getString(R.string.min))){
					minTemperature.setText("");
				}
				return false;
			}
		});

		weather1=(CheckBox)findViewById(R.id.new_CheckBox_weather1);
		weather2=(CheckBox)findViewById(R.id.new_CheckBox_weather2);
		weather3=(CheckBox)findViewById(R.id.new_CheckBox_weather3);
		weather4=(CheckBox)findViewById(R.id.new_CheckBox_weather4);
		weather5=(CheckBox)findViewById(R.id.new_CheckBox_weather5);
		weather6=(CheckBox)findViewById(R.id.new_CheckBox_weather6);
		weather7=(CheckBox)findViewById(R.id.new_CheckBox_weather7);
		weather8=(CheckBox)findViewById(R.id.new_CheckBox_weather8);
		weather9=(CheckBox)findViewById(R.id.new_CheckBox_weather9);
		weather1.setOnLongClickListener(checkBoxClickListener);
		weather2.setOnLongClickListener(checkBoxClickListener);
		weather3.setOnLongClickListener(checkBoxClickListener);
		weather4.setOnLongClickListener(checkBoxClickListener);
		weather5.setOnLongClickListener(checkBoxClickListener);
		weather6.setOnLongClickListener(checkBoxClickListener);
		weather7.setOnLongClickListener(checkBoxClickListener);
		weather8.setOnLongClickListener(checkBoxClickListener);
		weather9.setOnLongClickListener(checkBoxClickListener);
		if(type==0){
			noMonth();
			iconGroup.setCheckedRadioButton((RadioButton)findViewById(R.id.new_RadioBut_planet1));
		}
		if(type==1){
			PlanetName=this.getIntent().getExtras().getString("planetName");
			Cursor cur=managedQuery(BaseHelper.uri, null,ColumnNames.PLANET_NAME+"=?",new String[]{PlanetName},null);
			cur.moveToFirst();
			((EditText)findViewById(R.id.new_editText_planet)).setText(PlanetName);
			((EditText)findViewById(R.id.new_editText_country)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.COUNTRY_NAME)));
			((EditText)findViewById(R.id.new_editText_city)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.CITY_NAME)));
			((EditText)findViewById(R.id.new_editText_EarthDaysInYear)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.EARTH_DAYS_IN_YEAR)));
			if(cur.getString(cur.getColumnIndex(ColumnNames.MONTH_IN_YEAR)).equals("-1")){
				noMonth();
			}else{
				withMonth();
				((EditText)findViewById(R.id.new_editText_monthInYear)).setText(
					cur.getString(cur.getColumnIndex(ColumnNames.MONTH_IN_YEAR)));
				((EditText)findViewById(R.id.new_editText_daysInMonth)).setText(
						cur.getString(cur.getColumnIndex(ColumnNames.DAYS_IN_MONTH)));
			}
			((EditText)findViewById(R.id.new_editText_daysInYear)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.DAYS_IN_YEAR)));
			((EditText)findViewById(R.id.new_EditText_hoursInDay)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.HOURS_IN_DAY)));
			((EditText)findViewById(R.id.new_editText_minutesInHour)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.MINUTES_IN_HOUR)));
			((EditText)findViewById(R.id.new_editText_SecsInMinute)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.SEСS_IN_MINUTE)));
			((EditText)findViewById(R.id.new_editText_startYear)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.STARTING_YEAR)));
			((EditText)findViewById(R.id.new_editText_startDay)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.STARTING_DAY)));
			((EditText)findViewById(R.id.new_editText_startHour)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.STARTING_HOUR)));
			((EditText)findViewById(R.id.new_editText_startMinute)).setText(
				cur.getString(cur.getColumnIndex(ColumnNames.STARTING_MINUTE)));
			
			String MinTemperatureString=cur.getString(cur.getColumnIndex(ColumnNames.MIN_TEMPERATURE));
			String MaxTemperatureString=cur.getString(cur.getColumnIndex(ColumnNames.MAX_TEMPERATURE));
			int MinTemperature=Integer.valueOf(MinTemperatureString.substring(0,MinTemperatureString.length()-1));
			int MaxTemperature=Integer.valueOf(MaxTemperatureString.substring(0,MaxTemperatureString.length()-1));
			String tempType=MinTemperatureString.substring(MinTemperatureString.length()-1);
			if(tempType.equals("C")){
				((RadioButton)findViewById(R.id.new_RadioBut_celsij)).setChecked(true);
			}
			if(tempType.equals("K")){
				((RadioButton)findViewById(R.id.new_RadioBut_kelvin)).setChecked(true);
			}
			if(tempType.equals("F")){
				((RadioButton)findViewById(R.id.new_RadioBut_farengeit)).setChecked(true);
			}
			if(MinTemperature<0){
				((RadioButton)findViewById(R.id.new_RadioBut_minMinus)).setChecked(true);
			}else{
				((RadioButton)findViewById(R.id.new_RadioBut_minPlus)).setChecked(true);
			}
			if(MaxTemperature<0){
				((RadioButton)findViewById(R.id.new_RadioBut_maxMinus)).setChecked(true);
			}else{
				((RadioButton)findViewById(R.id.new_RadioBut_maxPlus)).setChecked(true);
			}
			if(!cur.getString(cur.getColumnIndex(ColumnNames.WARMEST_TIME)).equals("-1")){
				((EditText)findViewById(R.id.new_editText_minTemp)).setText(""+Math.abs(MinTemperature));
				((EditText)findViewById(R.id.new_editText_maxTemp)).setText(""+Math.abs(MaxTemperature));
				((EditText)findViewById(R.id.new_editText_warmestTime)).setText(
					cur.getString(cur.getColumnIndex(ColumnNames.WARMEST_TIME)));
				((EditText)findViewById(R.id.new_editText_coldestTime)).setText(
					cur.getString(cur.getColumnIndex(ColumnNames.COLDEST_TIME)));
			}
			((DatePicker)findViewById(R.id.new_datePicker_birthday)).init(
				Integer.valueOf(cur.getString(cur.getColumnIndex(ColumnNames.BIRTHDAY_YEAR))),
				Integer.valueOf(cur.getString(cur.getColumnIndex(ColumnNames.BIRTHDAY_MONTH)))-1,
				Integer.valueOf(cur.getString(cur.getColumnIndex(ColumnNames.BIRTHDAY_DAY))), null);
			if(cur.getInt(cur.getColumnIndex(ColumnNames.BIRTHDAY_ALARM))==1){
				((CheckBox)findViewById(R.id.new_CheckBox_birthdayAlarm)).setChecked(true);
			}else{
				((CheckBox)findViewById(R.id.new_CheckBox_birthdayAlarm)).setChecked(false);
			}
			
			iconGroup.setCheckedRadioButton(findViewById(cur.getInt(cur.getColumnIndex(ColumnNames.ICON))));
			String allWeather=cur.getString(cur.getColumnIndex(ColumnNames.WEATHER_ALL));
			String checkWeather=cur.getString(cur.getColumnIndex(ColumnNames.WEATHER_CHECKED));
			String tempWeather="";
			int k=1;
			for(int i=0;i<allWeather.length();i++){
				if(allWeather.charAt(i)=='/'){
					switch(k){
						case 1:	weather1.setText(tempWeather);break;
						case 2:	weather2.setText(tempWeather);break;
						case 3: weather3.setText(tempWeather);break;
						case 4:	weather4.setText(tempWeather);break;
						case 5:	weather5.setText(tempWeather);break;
						case 6:	weather6.setText(tempWeather);break;
						case 7:	weather7.setText(tempWeather);break;
						case 8:	weather8.setText(tempWeather);break;
						case 9:	weather9.setText(tempWeather);break;
						default:break;
					}
					tempWeather="";
					k=k+1;
				}else{
					tempWeather=tempWeather+allWeather.charAt(i);
				}
			}
			tempWeather="";
			for(int i=0;i<checkWeather.length();i++){
				if(checkWeather.charAt(i)=='/'){
					switch(Integer.valueOf(tempWeather)){
						case 1:weather1.setChecked(true);break;
						case 2:weather2.setChecked(true);break;
						case 3:weather3.setChecked(true);break;
						case 4:	weather4.setChecked(true);break;
						case 5:	weather5.setChecked(true);break;
						case 6:	weather6.setChecked(true);break;
						case 7:	weather7.setChecked(true);break;
						case 8:	weather8.setChecked(true);break;
						case 9:	weather9.setChecked(true);break;
						default:break;
					}
					tempWeather="";
					k=k+1;
				}else{
					tempWeather=tempWeather+checkWeather.charAt(i);
				}
			}
		}
		dateType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
					case (R.id.new_RadioBut_daysAndYear):	
						noMonth();
						break;
					case (R.id.new_RadioBut_daysAndMonthAndYear):
						withMonth();
						break;
				}
			}
		});
		butCreateWorld=(Button)findViewById(R.id.new_button_SaveWorld);
		butCreateWorld.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean withMonth=((RadioButton)findViewById(R.id.new_RadioBut_daysAndMonthAndYear)).isChecked();
				final String newPlanetName=((EditText)findViewById(R.id.new_editText_planet)).getText().toString();
				String countryName=((EditText)findViewById(R.id.new_editText_country)).getText().toString();
				String cityName=((EditText)findViewById(R.id.new_editText_city)).getText().toString();
				int dateView=((RadioGroup)findViewById(R.id.new_RadioGroup_dateView)).getCheckedRadioButtonId();
				String startingYear=((EditText)findViewById(R.id.new_editText_startYear)).getText().toString();
				String startingDay=((EditText)findViewById(R.id.new_editText_startDay)).getText().toString();
				String startingHour=((EditText)findViewById(R.id.new_editText_startHour)).getText().toString();
				String startingMinute=((EditText)findViewById(R.id.new_editText_startMinute)).getText().toString();
				String earthDaysInYear=((EditText)findViewById(R.id.new_editText_EarthDaysInYear)).getText().toString();
				String daysInMonth=((EditText)findViewById(R.id.new_editText_daysInMonth)).getText().toString();
				String daysInYear=((EditText)findViewById(R.id.new_editText_daysInYear)).getText().toString();
				String monthInYear=((EditText)findViewById(R.id.new_editText_monthInYear)).getText().toString();
				String hoursInDay=((EditText)findViewById(R.id.new_EditText_hoursInDay)).getText().toString();
				String minutesInHour=((EditText)findViewById(R.id.new_editText_minutesInHour)).getText().toString();
				String secsInMinute=((EditText)findViewById(R.id.new_editText_SecsInMinute)).getText().toString();
				String maxTemp=((EditText)findViewById(R.id.new_editText_maxTemp)).getText().toString();
				String minTemp=((EditText)findViewById(R.id.new_editText_minTemp)).getText().toString();
				String warmestTime=((EditText)findViewById(R.id.new_editText_warmestTime)).getText().toString();
				String coldestTime=((EditText)findViewById(R.id.new_editText_coldestTime)).getText().toString();
				String birthdayDay=((DatePicker)findViewById(R.id.new_datePicker_birthday)).getDayOfMonth()+"";
				String birthdayMonth=(((DatePicker)findViewById(R.id.new_datePicker_birthday)).getMonth()+1)+"";
				String birthdayYear=((DatePicker)findViewById(R.id.new_datePicker_birthday)).getYear()+"";
				int birthdayAlarm;
				int iconIndex=iconGroup.getCheckedRadioButtonId();
				
				if(((CheckBox)findViewById(R.id.new_CheckBox_birthdayAlarm)).isChecked()){
					birthdayAlarm=1;
				}else{
					birthdayAlarm=0;
				}
				
				if(newPlanetName.equals("")){
					Toast.makeText(context, "Введите название планеты.", Toast.LENGTH_SHORT).show();
					return;
				}
				String weatherChecked="";
				String weatherAll="";
				String weather="";
				if(weather1.isChecked()){
					weatherChecked=weatherChecked+"1/";
					weather=weather+weather1.getText()+"/";
				}		
				if(weather2.isChecked()){
					weatherChecked=weatherChecked+"2/";
					weather=weather+weather2.getText()+"/";
				}
				if(weather3.isChecked()){
					weatherChecked=weatherChecked+"3/";
					weather=weather+weather3.getText()+"/";
				}
				if(weather4.isChecked()){
					weatherChecked=weatherChecked+"4/";
					weather=weather+weather4.getText()+"/";
				}
				if(weather5.isChecked()){
					weatherChecked=weatherChecked+"5/";
					weather=weather+weather5.getText()+"/";
				}
				if(weather6.isChecked()){
					weatherChecked=weatherChecked+"6/";
					weather=weather+weather6.getText()+"/";
				}
				if(weather7.isChecked()){
					weatherChecked=weatherChecked+"7/";
					weather=weather+weather7.getText()+"/";
				}
				if(weather8.isChecked()){
					weatherChecked=weatherChecked+"8/";
					weather=weather+weather8.getText()+"/";
				}
				if(weather9.isChecked()){
					weatherChecked=weatherChecked+"9/";
					weather=weather+weather9.getText()+"/";
				}
				
				weatherAll=weather1.getText()+"/"+weather2.getText()+"/"+weather3.getText()+"/"+
					weather4.getText()+"/"+weather5.getText()+"/"+weather6.getText()+"/"+
					weather7.getText()+"/"+weather8.getText()+"/"+weather9.getText()+"/";
				try{
					
					if((withMonth)&&(Float.valueOf(monthInYear)<1)){
						Toast.makeText(context, "Количество месяцев в году вашей планеты должно быть больше 1", Toast.LENGTH_SHORT).show();
						return;
					}
					if((withMonth)&&(Float.valueOf(daysInMonth)<1)){
						Toast.makeText(context, "Количество суток в месяце вашей планеты должно быть больше 1", Toast.LENGTH_SHORT).show();
						return;
					}
					if(Float.valueOf(daysInYear)<1){
						Toast.makeText(context, "Количество суток в году вашей планеты должно быть больше 1", Toast.LENGTH_SHORT).show();
						return;
					}
					if(Float.valueOf(hoursInDay)<1){
						Toast.makeText(context, "Количество часов в сутках вашей планеты должно быть больше 1", Toast.LENGTH_SHORT).show();
						return;
					}
					if(Float.valueOf(minutesInHour)<1){
						Toast.makeText(context, "Количество минут в часе вашей планеты должно быть больше 1", Toast.LENGTH_SHORT).show();
						return;
					}
					if(Float.valueOf(secsInMinute)<1){
						Toast.makeText(context, "Количество секунд в минуте вашей планеты должно быть больше 1", Toast.LENGTH_SHORT).show();
						return;
					}
					
					
					if(earthDaysInYear.equals("")){
						Toast.makeText(context, "Введите количество земных дней в году вашей планеты.", Toast.LENGTH_SHORT).show();
						return;
					}
					if(daysInYear.equals("")){
						Toast.makeText(context, "Введите количество дней в году.", Toast.LENGTH_SHORT).show();
						return;
					}
					
					if((monthInYear.equals(""))&&(withMonth)){
						Toast.makeText(context, "Введите количество месяцев в году.", Toast.LENGTH_SHORT).show();
						return;
					}
					Log.v("mes", "1x");
					
					if(hoursInDay.equals("")){
						Toast.makeText(context, "Введите количество часов в дне.", Toast.LENGTH_SHORT).show();
						return;
					}
					
					if(minutesInHour.equals("")){
						Toast.makeText(context, "Введите количество минут в часе.", Toast.LENGTH_SHORT).show();
						return;
					}
					if((!startingDay.equals(""))&&(Integer.valueOf(startingDay)>Float.valueOf(daysInYear))){
						Toast.makeText(context, "Начальный день должен быть меньше," +
							" чем количество дней в году.", Toast.LENGTH_SHORT).show();
						return;
					}
					if((!startingDay.equals(""))&&(Integer.valueOf(startingDay)==0)){
						Toast.makeText(context, "Начальный день должен быть как " +
							"минимум 1.", Toast.LENGTH_SHORT).show();
						return;
					}
					if((!startingYear.equals(""))&&(Integer.valueOf(startingYear)==0)){
						Toast.makeText(context, "Начальный год должен быть как " +
							"минимум 1.", Toast.LENGTH_SHORT).show();
						return;
					}
					if((!startingHour.equals(""))&&(Integer.valueOf(startingHour)>Float.valueOf(hoursInDay))){
						Toast.makeText(context, "Начальный час должен быть меньше," +
							" чем количество часов в сутках.", Toast.LENGTH_SHORT).show();
						return;
					}
					Log.v("mes", "2x");
					if((!startingMinute.equals(""))&&(Integer.valueOf(startingDay)>Float.valueOf(daysInYear))){
						Toast.makeText(context, "Начальная минута должна быть меньше," +
							" чем количество минут в часе.", Toast.LENGTH_SHORT).show();
						return;
					}
					
					if(maxTemp.equals(getResources().getString(R.string.max))){
						maxTemp="";
					}
					if(minTemp.equals(getResources().getString(R.string.min))){
						minTemp="";
					}
					if((maxTemp.equals(""))&(minTemp.equals(""))){
						coldestTime="-1"; warmestTime="-1";
						if(((RadioButton)findViewById(R.id.new_RadioBut_farengeit)).isChecked()){
							maxTemp="-1F";minTemp="-1F";
						}
						if(((RadioButton)findViewById(R.id.new_RadioBut_celsij)).isChecked()){
							maxTemp="-1C";minTemp="-1C";
						}
						if(((RadioButton)findViewById(R.id.new_RadioBut_kelvin)).isChecked()){
							maxTemp="-1K";minTemp="-1K";
						}
					}else{
						Log.v("mes", "3x");
						if(coldestTime.equals("")){
							coldestTime=((int)(Float.valueOf(hoursInDay)/6))+"";
						}
						if(warmestTime.equals("")){
							warmestTime=((int)(Float.valueOf(hoursInDay)/2))+"";
						}
						if((maxTemp.equals(""))&(!minTemp.equals(""))){
							maxTemp=minTemp;
							if(((RadioButton)findViewById(R.id.new_RadioBut_minMinus)).isChecked()){
								((RadioButton)findViewById(R.id.new_RadioBut_maxMinus)).setChecked(true);
							}else{
								((RadioButton)findViewById(R.id.new_RadioBut_maxPlus)).setChecked(true);
							}
						}
						Log.v("mes", "4x");
						if((!maxTemp.equals(""))&(minTemp.equals(""))){
							minTemp=maxTemp;
							if(((RadioButton)findViewById(R.id.new_RadioBut_maxMinus)).isChecked()){
								((RadioButton)findViewById(R.id.new_RadioBut_minMinus)).setChecked(true);
							}else{
								((RadioButton)findViewById(R.id.new_RadioBut_minPlus)).setChecked(true);
							}
						}
						int tMaxTemp=Integer.valueOf(maxTemp),tMinTemp=Integer.valueOf(minTemp);
						if(((RadioButton)findViewById(R.id.new_RadioBut_maxMinus)).isChecked()){
							tMaxTemp=(-1)*tMaxTemp;
							maxTemp="-"+maxTemp;
						}
						if(((RadioButton)findViewById(R.id.new_RadioBut_minMinus)).isChecked()){
							tMinTemp=(-1)*tMinTemp;
							minTemp="-"+minTemp;
						}
						if(((RadioButton)findViewById(R.id.new_RadioBut_farengeit)).isChecked()){
							tMaxTemp=(int)((tMaxTemp-32)/1.8)+273;
							tMinTemp=(int)((tMinTemp-32)/1.8)+273;
							minTemp=minTemp+"F";
							maxTemp=maxTemp+"F";
						}
						if(((RadioButton)findViewById(R.id.new_RadioBut_celsij)).isChecked()){
							tMaxTemp=(tMaxTemp+273);
							tMinTemp=(tMinTemp+273);
							minTemp=minTemp+"C";
							maxTemp=maxTemp+"C";
						}
						if(((RadioButton)findViewById(R.id.new_RadioBut_kelvin)).isChecked()){
							minTemp=minTemp+"K";
							maxTemp=maxTemp+"K";
						}
						if((tMaxTemp<tMinTemp)|(tMaxTemp<0)|(tMinTemp<0)){
							Toast.makeText(context, "Некорректные температурные данные", Toast.LENGTH_SHORT).show();
							return;
						}
						Log.v("mes", "5x");
					}
				}catch(NumberFormatException e){
					Toast.makeText(context, "Неверные данные", Toast.LENGTH_SHORT).show();
					return;
				}
				
				ContentValues values=new ContentValues();
				values.put(ColumnNames.PLANET_NAME,newPlanetName);	
				values.put(ColumnNames.COUNTRY_NAME,countryName);
				values.put(ColumnNames.CITY_NAME,cityName);
				values.put(ColumnNames.EARTH_DAYS_IN_YEAR, earthDaysInYear);
				if(withMonth){
					values.put(ColumnNames.MONTH_IN_YEAR, monthInYear);
					values.put(ColumnNames.DAYS_IN_MONTH, daysInMonth);
				}else{
					values.put(ColumnNames.MONTH_IN_YEAR, -1);
					values.put(ColumnNames.DAYS_IN_MONTH, -1);
				}
				values.put(ColumnNames.DAYS_IN_YEAR, daysInYear);
				values.put(ColumnNames.HOURS_IN_DAY, hoursInDay);
				values.put(ColumnNames.MINUTES_IN_HOUR, minutesInHour);
				values.put(ColumnNames.SEСS_IN_MINUTE, secsInMinute);
				values.put(ColumnNames.DATE_VIEW, dateView);
				values.put(ColumnNames.STARTING_YEAR, startingYear);
				values.put(ColumnNames.STARTING_DAY, startingDay);
				values.put(ColumnNames.STARTING_HOUR, startingHour);
				values.put(ColumnNames.STARTING_MINUTE, startingMinute);
				values.put(ColumnNames.COLDEST_TIME, coldestTime);
				values.put(ColumnNames.WARMEST_TIME, warmestTime);
				values.put(ColumnNames.MIN_TEMPERATURE, minTemp);
				values.put(ColumnNames.MAX_TEMPERATURE, maxTemp);
				values.put(ColumnNames.BIRTHDAY_DAY, birthdayDay);
				values.put(ColumnNames.BIRTHDAY_MONTH, birthdayMonth);
				values.put(ColumnNames.BIRTHDAY_YEAR, birthdayYear);
				values.put(ColumnNames.BIRTHDAY_ALARM, birthdayAlarm);
				values.put(ColumnNames.WEATHER_ALL, weatherAll);
				values.put(ColumnNames.WEATHER_CHECKED, weatherChecked);
				values.put(ColumnNames.WEATHER, weather);
				values.put(ColumnNames.ICON, iconIndex);
				try{
					if(type==1){
						context.getContentResolver().update(BaseHelper.uri, values, 
							ColumnNames.PLANET_NAME+"=?",new String[]{PlanetName});
						progrDial=ProgressDialog.show(context, "Сохранение...", "Сохранение данных");
						Thread thrd=new Thread(){
							@Override
							public void run(){
								Cursor cur=managedQuery(BaseHelper.uri, null, ColumnNames.PLANET_NAME+"=?", 
									new String[]{newPlanetName}, null);
								UpdateUtils.updateExistsWidgets(context,cur,PlanetName);
								uiCallback.sendEmptyMessage(0);
								finish();
							}
						};
						thrd.start();
					}
					if(type==0){
						context.getContentResolver().insert(BaseHelper.uri, values);
						finish();
					}
				}catch(SQLException e){
					Toast.makeText(context, "Введите уникальное имя планеты.", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
		
	}
	private Handler uiCallback=new Handler(){
		@Override
		public void handleMessage(Message msg){
			progrDial.dismiss();
		}
	};
	
	protected void mantissChecker(View v) {
		String text=((EditText)v).getText().toString();
		int i; 
		for(i=0;i<text.length();i++){
			if(text.charAt(i)=='.'){
				i=i+1;
				break;
			}
		}
		if(text.substring(i, text.length()).length()>4){
			((EditText)v).setText(text.substring(0, i+4));
		}
	}

	private void noMonth(){
		monthInYear.setEnabled(false);
		//monthInYear.setFocusable(false);
		monthInYear.setFocusableInTouchMode(false);
		DDxYY.setEnabled(true);
		DD_YY.setEnabled(true);
		DDxYY.setTextColor(Color.WHITE);
		DD_YY.setTextColor(Color.WHITE);
		DD_MM_YY.setEnabled(false);
		MM_DD_YY.setEnabled(false);
		DDxMMxYY.setEnabled(false);
		DD_MM_YY.setTextColor(Color.GRAY);
		MM_DD_YY.setTextColor(Color.GRAY);
		DDxMMxYY.setTextColor(Color.GRAY);
		((RadioButton)findViewById(R.id.new_RadioBut_daysAndYear)).setChecked(true);
		dateView.check(R.id.new_RadioBut_DD_YY);
	}
	
	private void withMonth(){
		monthInYear.setFocusableInTouchMode(true);
		//monthInYear.setFocusable(true);
		monthInYear.setEnabled(true);
		DDxYY.setEnabled(false);
		DD_YY.setEnabled(false);
		DDxYY.setTextColor(Color.GRAY);
		DD_YY.setTextColor(Color.GRAY);
		DD_MM_YY.setEnabled(true);
		MM_DD_YY.setEnabled(true);
		DDxMMxYY.setEnabled(true);
		DD_MM_YY.setTextColor(Color.WHITE);
		MM_DD_YY.setTextColor(Color.WHITE);
		DDxMMxYY.setTextColor(Color.WHITE);
		((EditText)findViewById(R.id.new_editText_SecsInMinute)).setFocusable(true);
		((RadioButton)findViewById(R.id.new_RadioBut_daysAndMonthAndYear)).setChecked(true);
		dateView.check(R.id.new_RadioBut_DD_MM_YY);
	}
	
	@Override
    public Dialog onCreateDialog(int id){
		AlertDialog.Builder builder = null;
    	switch(id){
    		case WEATHER:
    			LayoutInflater inflater=this.getLayoutInflater();
    			View layout=inflater.inflate(R.layout.dialog_weather, null);
    			builder=null;
    			builder=new AlertDialog.Builder(this);
    			builder.setView(layout);
    			weatherChange=(EditText)layout.findViewById(R.id.weatherDialog_weather);
    			builder.setPositiveButton(getResources().getString(R.string.change),this);
    			builder.setNegativeButton(getResources().getString(R.string.not_change),new OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.cancel();
					}
				});
    			break;
    	}
    	return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		((CheckBox)view).setText(weatherChange.getText());
		weatherChange.setText("");
	}
	
}
