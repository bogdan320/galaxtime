package com.galaxtime.widget;


import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public abstract class APrefWidgetModel implements IWidgetSaveContract{

	public abstract void init();
	public abstract String getPrefName();
	public abstract Map<String, String> getPrefsToSave();

	public int iid;
	
	public APrefWidgetModel(int InstanceId){
		iid=InstanceId;
	}
	
	public void setValueForPref(String key, String value) {
		return;
	}
	
	public void SavePreferences(Context context){
		Map<String,String> keyValuePairs=getPrefsToSave();
		if(keyValuePairs==null){
			return;
		}
		SharedPreferences.Editor prefs=context.getSharedPreferences(getPrefName(), 0).edit();
		for(String key:keyValuePairs.keySet()){
			String value=keyValuePairs.get(key);
			SavePref(prefs,key,value);
		}
		prefs.commit();
	}
	private void SavePref(Editor prefs, String key, String value) {
		String newkey=getStoredKeyForFieldName(key);
		prefs.putString(newkey, value);
	}
	
	protected String getStoredKeyForFieldName(String key) {
		return key+"_"+iid;
	}
	
	public void removePreferences(Context context){
		Map<String,String> keyValuePairs=getPrefsToSave();
		if(keyValuePairs==null){
			return;
		}
		SharedPreferences.Editor prefs=context.getSharedPreferences(getPrefName(), 0).edit();
		for(String key:keyValuePairs.keySet()){
			RemovePref(prefs,key);
		}
		prefs.commit();
	}
	
	private void RemovePref(Editor prefs, String key) {
		String newkey=getStoredKeyForFieldName(key);
		prefs.remove(newkey);
	}
	
	public boolean retrievePref(Context context){
		Map<String,?> prefs=context.getSharedPreferences(getPrefName(),0).getAll();
		boolean isFound=false;
		for(String key:prefs.keySet()){
			if(IsItMyPref(key)==true){
				isFound=true;
				String value=(String)prefs.get(key);
				setValueForPref(key,value);
			}
		}	
		return isFound;
	}
	private boolean IsItMyPref(String key) {
		if(key.indexOf("_"+iid)>0){
			return true;
		}
		return false;
	}
	
	public static void clearAllPreferences(Context context,String prefname){
		SharedPreferences.Editor prefs=context.getSharedPreferences(prefname, 0).edit();
		prefs.clear();
		prefs.commit();
	}
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
