package com.galaxtime.widget.widgetMedium;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.galaxtime.R;
import com.galaxtime.database.BaseHelper;
import com.galaxtime.database.ColumnNames;
import com.galaxtime.widget.CreateNewWorld;
import com.galaxtime.widget.UpdateUtils;

public class WidgetConfigureMedium extends Activity{
	private Button button;
	private Context context;
	private ListView listView;
	private Cursor selectedItem;
	public static float period;
	int mAppWidgetId;
	private final int CONTEXT_EDIT=301;
	private final int CONTEXT_DELETE=302;
	protected final static String TYPE="type";
	protected final static String PLANET_NAME="planetName";
	protected final static int EDIT=1;
	protected final static int CREATE=0;

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		UpdateUtils.CreateMenu(context,menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		UpdateUtils.SelectMenu(context,item);
		return true;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.edit_main);
		listView=(ListView)findViewById(R.id.list_allPlanets);
		this.registerForContextMenu(listView);
		context=this;
		
		SimpleCursorAdapter adapter=null;
		Cursor cursor=this.managedQuery(BaseHelper.uri, null, null, null, null);
		adapter=new SimpleCursorAdapter(this, R.layout.list_item, cursor, 
				new String[]{ColumnNames.PLANET_NAME}, new int[]{R.id.text_listItemPlanet});
		listView.setAdapter(adapter);
		
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
				intent.putExtra(TYPE, CREATE);
				startActivity(intent);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View view, int position,	long id) {
				Cursor cur= managedQuery(BaseHelper.uri, null, BaseHelper._ID+"=?", 
					new String[]{l.getItemIdAtPosition(position)+""}, null);
				if(!UpdateUtils.FindSimilarWidget(context,cur,mAppWidgetId,UpdateUtils.MEDIUM)){
					UpdateUtils.updateWidgetLocal(context,cur,mAppWidgetId,UpdateUtils.MEDIUM);
				}
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
		AdapterContextMenuInfo aMenuInfo = (AdapterContextMenuInfo) menuInfo;
        int position = aMenuInfo.position;
        selectedItem=managedQuery(BaseHelper.uri, null, BaseHelper._ID+"=?",new String[]{(listView.getItemIdAtPosition(position))+""}, null);
        selectedItem.moveToFirst();
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		String planetName=selectedItem.getString(selectedItem.getColumnIndex(ColumnNames.PLANET_NAME));
		switch (item.getItemId()){
			case CONTEXT_EDIT:
				Intent intent=new Intent();
				intent.setClass(this,CreateNewWorld.class);
				intent.putExtra(TYPE, EDIT);
				intent.putExtra(PLANET_NAME, planetName);
				this.startActivity(intent);
				break;
			case CONTEXT_DELETE:
				Toast.makeText(context, getResources().getString(R.string.deletedPlanet)+" "+planetName, Toast.LENGTH_LONG).show();
				this.getContentResolver().delete(BaseHelper.uri, ColumnNames.PLANET_NAME+"=?",
					new String[] {planetName});
				break;
		}
		return super.onContextItemSelected(item);
	}


}
