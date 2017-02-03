package com.tunaemre.bpmcounter.views;

import com.tunaemre.bpmcounter.R;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("InlinedApi")
public abstract class ExtendedActionBarActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= 21)
		{
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.setStatusBarColor(getResources().getColor(R.color.App_PrimaryDark_Color));
			window.setNavigationBarColor(getResources().getColor(R.color.App_PrimaryDark_Color));
		}
		
		prepareActivity();
	}
	
	protected abstract void prepareActivity();

}