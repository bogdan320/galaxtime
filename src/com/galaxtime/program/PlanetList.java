package com.galaxtime.program;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.galaxtime.R;
import com.galaxtime.database.BaseHelper;
import com.galaxtime.database.ColumnNames;
import com.galaxtime.widget.CreateNewWorld;
import com.galaxtime.widget.UpdateUtils;

public class PlanetList extends Activity{
	private Button button;
	private Context context;
	private ListView listView;
	private Cursor selectedItem;
	int mAppWidgetId;
	private final int CONTEXT_MENU_EDIT=301;
	private final int CONTEXT_MENU_DELETE=302;
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
				Cursor Item=managedQuery(BaseHelper.uri, null, BaseHelper._ID+"=?",
					new String[]{(l.getItemIdAtPosition(position))+""}, null);	  
				Item.moveToFirst();
				String planetName=Item.getString(Item.getColumnIndex(ColumnNames.PLANET_NAME));
				Intent infoValue=new Intent();
				infoValue.setClass(context,InfoActivity.class);
				infoValue.putExtra(PLANET_NAME, planetName);
				startActivity(infoValue);
			}
		});
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu,View v,ContextMenuInfo menuInfo){
		menu.add(0, CONTEXT_MENU_EDIT, 0,R.string.edit );
		menu.add(0, CONTEXT_MENU_DELETE, 0, R.string.delete);
		AdapterContextMenuInfo aMenuInfo = (AdapterContextMenuInfo) menuInfo;
        int position = aMenuInfo.position;
        selectedItem=managedQuery(BaseHelper.uri, null, BaseHelper._ID+"=?",new String[]{(listView.getItemIdAtPosition(position))+""}, null);
        selectedItem.moveToFirst();
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		String planetName=selectedItem.getString(selectedItem.getColumnIndex(ColumnNames.PLANET_NAME));
		switch (item.getItemId()){
			case CONTEXT_MENU_EDIT:
				Intent intent=new Intent();
				intent.setClass(this,CreateNewWorld.class);
				intent.putExtra(TYPE, EDIT);
				intent.putExtra(PLANET_NAME, planetName);
				this.startActivity(intent);
				break;
			case CONTEXT_MENU_DELETE:
				Toast.makeText(context, getResources().getString(R.string.deletedPlanet)+" "+planetName, Toast.LENGTH_LONG).show();
				this.getContentResolver().delete(BaseHelper.uri, ColumnNames.PLANET_NAME+"=?",
					new String[] {planetName});
				break;
		}
		return super.onContextItemSelected(item);
	}
}
