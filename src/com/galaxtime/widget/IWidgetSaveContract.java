package com.galaxtime.widget;


import java.util.Map;

public interface IWidgetSaveContract{
	public void init();
	public void setValueForPref(String key,String value);
	public String getPrefName();
	public Map<String,String> getPrefsToSave();
}
