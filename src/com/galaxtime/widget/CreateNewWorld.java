package com.galaxtime.widget;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxtime.R;
import com.galaxtime.database.BaseHelper;
import com.galaxtime.database.ColumnNames;

public class CreateNewWorld extends Activity{
	Button butCreateWorld;
	Context context;
	EditText month;
	TextView daysIn;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.create_new_world);
		context=this;
		month=(EditText)findViewById(R.id.new_editText_monthInYear);
		daysIn=(TextView)findViewById(R.id.new_textView_DaysIn);
		RadioGroup dateType=(RadioGroup)findViewById(R.id.new_RadioGroup_dateType);
		dateType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId){
					case (R.id.new_RadioBut_daysAndYear):	
						month.setEnabled(false);
						daysIn.setText(R.string.daysInYear);
						break;
					case (R.id.new_RadioBut_daysAndMonthAndYear):
						month.setEnabled(true);
						daysIn.setText(R.string.daysInMonth);
				}
			}
		});
		butCreateWorld=(Button)findViewById(R.id.button_saveWorld);
		butCreateWorld.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean withMonth=((RadioButton)findViewById(R.id.new_RadioBut_daysAndMonthAndYear)).isChecked();
				String planetName=((EditText)findViewById(R.id.new_editText_planet)).getText().toString();
				String countryName=((EditText)findViewById(R.id.new_editText_country)).getText().toString();
				String cityName=((EditText)findViewById(R.id.new_editText_city)).getText().toString();
				int dateView=((RadioGroup)findViewById(R.id.new_RadioGroup_dateView)).getCheckedRadioButtonId();
				String startingYear=((EditText)findViewById(R.id.new_editText_startYear)).getText().toString();
				String startingMonth=((EditText)findViewById(R.id.new_editText_startMonth)).getText().toString();
				String startingDay=((EditText)findViewById(R.id.new_editText_startDay)).getText().toString();
				String startingHour=((EditText)findViewById(R.id.new_editText_startDay)).getText().toString();
				String startingMinute=((EditText)findViewById(R.id.new_editText_startMinute)).getText().toString();
				String daysInYear=((EditText)findViewById(R.id.new_editText_daysInYear)).getText().toString();
				String monthInYear=((EditText)findViewById(R.id.new_editText_monthInYear)).getText().toString();
				String hoursInDay=((EditText)findViewById(R.id.new_EditText_hoursInDay)).getText().toString();
				String minutesInHour=((EditText)findViewById(R.id.new_editText_minutesInHour)).getText().toString();
				String secsInMinute=((EditText)findViewById(R.id.new_editText_SecsInMinute)).getText().toString();
				
				if(planetName.equals("")){
					Toast.makeText(context, "Введите название планеты.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				try{
					if(!(daysInYear.equals(""))){
						Integer.valueOf(daysInYear);
					}else{
						Toast.makeText(context, "Введите количество дней в году.", Toast.LENGTH_SHORT).show();
						return;
					}
					
					if((!monthInYear.equals(""))&&(withMonth)){
						Integer.valueOf(monthInYear);
					}else if((monthInYear.equals(""))&&(withMonth)){
						Toast.makeText(context, "Введите количество месяцев в году.", Toast.LENGTH_SHORT).show();
						return;
					}
					if(!hoursInDay.equals("")){
						Integer.valueOf(hoursInDay);
					}else{
						Toast.makeText(context, "Введите количество часов в дне.", Toast.LENGTH_SHORT).show();
						return;
					}
					if(!minutesInHour.equals("")){
						Integer.valueOf(minutesInHour);
					}else{
						Toast.makeText(context, "Введите количество минут в часе.", Toast.LENGTH_SHORT).show();
						return;
					}
					if(!secsInMinute.equals("")){
						Integer.valueOf(secsInMinute);
					}else{
						Toast.makeText(context, "Введите количество секунд в минуте.", Toast.LENGTH_SHORT).show();
						return;
					}
					if(!startingYear.equals("")){Integer.valueOf(startingYear);}
					if((!startingMonth.equals(""))&&(withMonth)){Integer.valueOf(startingMonth);}
					if(!startingDay.equals("")){Integer.valueOf(startingDay);}
					if(!startingHour.equals("")){Integer.valueOf(startingHour);}
					if(!startingMinute.equals("")){Integer.valueOf(startingMinute);}
				}catch(NumberFormatException e){
					Toast.makeText(context, "Неверные данные", Toast.LENGTH_SHORT).show();
					return;
				}
				
				ContentValues values=new ContentValues();
				values.put(ColumnNames.PLANET_NAME,planetName);	
				values.put(ColumnNames.COUNTRY_NAME,countryName);
				values.put(ColumnNames.CITY_NAME,cityName);
				if(withMonth){
					values.put(ColumnNames.MONTH_IN_YEAR, monthInYear);
					values.put(ColumnNames.STARTING_MONTH, startingMonth);
				}else{
					values.put(ColumnNames.MONTH_IN_YEAR, -1);
					values.put(ColumnNames.STARTING_MONTH, -1);
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
				context.getContentResolver().insert(BaseHelper.uri, values);
				finish();
			}
		});
	}
	
	
}
