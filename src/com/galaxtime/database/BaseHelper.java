package com.galaxtime.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

public class BaseHelper extends SQLiteOpenHelper implements BaseColumns{

	public static final String AUTHORITY="com.galaxtime.database.Planets";
	public static final String DATABASE_NAME="Planets.db";
	public static final String TABLE_NAME="planets";
	public static final Uri uri=Uri.parse("content://"+AUTHORITY+"/"+TABLE_NAME);
	public static final String CONTENT_TYPE="vnd.android.cursor.dir/vnd.galaxtime";
	public static final String CONTENT_TYPE_ITEM="vnd.android.cursor.item/vnd.galaxtime";
	public static final String DEFAULT_SORT=ColumnNames.PLANET_NAME+" ASC";
	
	public BaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+TABLE_NAME+"(" +
				BaseHelper._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ColumnNames.PLANET_NAME+" TEXT, "+ColumnNames.COUNTRY_NAME+" TEXT, "
				+ColumnNames.CITY_NAME+" TEXT, "+ ColumnNames.DAYS_IN_YEAR+" INTEGER, "
				+ColumnNames.MONTH_IN_YEAR+" INTEGER, "+ColumnNames.DATE_VIEW+" INTEGER, "
				+ColumnNames.HOURS_IN_DAY+" INTEGER, "+ColumnNames.MINUTES_IN_HOUR+" TEXT, "
				+ColumnNames.SEСS_IN_MINUTE+" TEXT, "+ColumnNames.CALL_YEAR+" TEXT, "
				+ColumnNames.CALL_DAY+" TEXT, "+ColumnNames.MAX_TEMPERATURE+" INTEGER, "
				+ColumnNames.MIN_TEMPERATURE+" INTEGER, "+ ColumnNames.COLDEST_TIME+" INTEGER, "
				+ColumnNames.WARMEST_TIME+" INTEGER, "+ColumnNames.PICTURE_INDEX+" INTEGER, "
				+ColumnNames.STARTING_YEAR+" INTEGER, "+ColumnNames.STARTING_MONTH+" INTEGER, "
				+ColumnNames.STARTING_DAY+" INTEGER, "+ColumnNames.STARTING_HOUR+" INTEGER, "
				+ColumnNames.STARTING_MINUTE+" INTEGER, "+ColumnNames.BIRTHDAY+" TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
		onCreate(db);
	}

}
