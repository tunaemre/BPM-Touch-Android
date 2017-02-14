package com.tunaemre.bpmcounter.operator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionOperator
{
	public static int REQUEST_MIC_PERMISSION = 11;

	public boolean isMicrophonePermissionGranded(Context context)
	{
		if (Build.VERSION.SDK_INT < 23) return true;
		
		if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) return true;
		
		return false;
	}
	
	public boolean requestMicrophonePermission(Activity context) {
		if (isMicrophonePermissionGranded(context)) return true;

		ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MIC_PERMISSION);

		return true;
	}
}
