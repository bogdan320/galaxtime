package com.galaxtime.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class BaseProvider extends ContentProvider{

	private static UriMatcher mUriMatcher;
	private static final int MATCHER_COLLECTION=100;
	private static final int MATCHER_ITEM=101;
	private BaseHelper base;
	static{
		mUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(BaseHelper.AUTHORITY, BaseHelper.TABLE_NAME, MATCHER_COLLECTION);
		mUriMatcher.addURI(BaseHelper.AUTHORITY, BaseHelper.TABLE_NAME+"/#", MATCHER_ITEM);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db;
		db=base.getWritableDatabase();
		int row=0;
		switch(mUriMatcher.match(uri)){
			case BaseProvider.MATCHER_COLLECTION:
				row=db.delete(BaseHelper.TABLE_NAME, selection, selectionArgs);
				this.getContext().getContentResolver().notifyChange(uri, null);
				break;
			case BaseProvider.MATCHER_ITEM:
				String num=uri.getPathSegments().get(1);
				row=db.delete(BaseHelper.TABLE_NAME, BaseHelper._ID+"="+num+
					(!TextUtils.isEmpty(selection)?" AND ("+selection+")":""), selectionArgs);
				this.getContext().getContentResolver().notifyChange(uri,null);
				break;
		}
		return row;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if(mUriMatcher.match(uri)!=MATCHER_COLLECTION){
			throw new SQLException("Invalid uri "+uri);
		}
		SQLiteDatabase db;
		db=base.getWritableDatabase();
		if(values.get(ColumnNames.COLDEST_TIME).equals(""))
			values.put(ColumnNames.COLDEST_TIME, "-1");
		if(values.get(ColumnNames.WARMEST_TIME).equals(""))
			values.put(ColumnNames.WARMEST_TIME, "-1");
		if(values.get(ColumnNames.STARTING_YEAR).equals(""))
			values.put(ColumnNames.STARTING_YEAR, "1");
		if(values.get(ColumnNames.STARTING_DAY).equals(""))
			values.put(ColumnNames.STARTING_DAY, "1");
		if(values.get(ColumnNames.STARTING_HOUR).equals(""))
			values.put(ColumnNames.STARTING_HOUR, "0");
		if(values.get(ColumnNames.STARTING_MINUTE).equals(""))
			values.put(ColumnNames.STARTING_MINUTE, "0");
		long row=db.insert(BaseHelper.TABLE_NAME, null, values);
		if(row>0){
			Uri insertedUri=ContentUris.withAppendedId(BaseHelper.uri, row);
			this.getContext().getContentResolver().notifyChange(insertedUri, null);
			return insertedUri;
		}
		throw new SQLException("Failed to insert data with "+uri);
	}

	@Override
	public boolean onCreate() {
		base=new BaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, 
			String sortOrder) {
		
		SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
		qb.setTables(BaseHelper.TABLE_NAME);
		if(mUriMatcher.match(uri)==MATCHER_ITEM){
			qb.appendWhere(BaseHelper._ID+"="+uri.getPathSegments().get(1));
		}
		if(sortOrder==null){
			sortOrder=BaseHelper.DEFAULT_SORT;
		}
		SQLiteDatabase db;
		db=base.getReadableDatabase();
		Cursor cur=db.query(BaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
		cur.setNotificationUri(getContext().getContentResolver(), uri);
		return cur;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db;
		db=base.getWritableDatabase();
		switch(mUriMatcher.match(uri)){
			case MATCHER_COLLECTION:
				return db.update(BaseHelper.TABLE_NAME, values, where, whereArgs);
			case MATCHER_ITEM:
				String row=uri.getPathSegments().get(1);
				return db.update(BaseHelper.TABLE_NAME, values, BaseHelper._ID+"="+row+
					(TextUtils.isEmpty(where)?"":" AND ("+where+")"), whereArgs);
		}
		throw new SQLException("Invalid uri "+uri);
	}

}
