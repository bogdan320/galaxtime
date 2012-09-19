package com.galaxtime.widget;

import java.util.Locale;

import com.galaxtime.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class InstructionActivity extends Activity{
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_instruction_activity);
		TextView t2 = (TextView) findViewById(R.id.instruction_text);
		t2.setMovementMethod(LinkMovementMethod.getInstance());
		Configuration sysConfig = getResources().getConfiguration();
		Locale locale = sysConfig.locale;
		Locale.setDefault(locale);
	}
}
