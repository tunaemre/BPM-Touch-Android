package com.tunaemre.bpmcounter;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tunaemre.bpmcounter.app.ExtendedCompatActivity;
import com.tunaemre.bpmcounter.operator.PermissionOperator;
import com.tunaemre.bpmcounter.sound.MicrophoneEvent;
import com.tunaemre.bpmcounter.sound.MicrophoneManager;

import java.util.Date;
import java.util.List;
import java.util.Vector;

public class MainActivity extends ExtendedCompatActivity
{
	private PermissionOperator mPermissionOperator = new PermissionOperator();

	private ExecuteMicrophoneListener mExecuteMicrophoneListener = null;

	private Menu mMenu = null;

	private TextView txtBpm = null;
	private TextView txtCount = null;
	
	private long mLastBeatTiming = 0;
	private List<Long> mBeatTimings;
	private int mBeatCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		getSupportActionBar().setTitle("Touch Now!");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		this.mMenu = menu;

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.menu_main_automode:
				if (mExecuteMicrophoneListener == null)
					openMicrophoneListener();
				else
					closeMicrophoneListener();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if (requestCode == PermissionOperator.REQUEST_MIC_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
			openMicrophoneListener();
	}

	@Override
	protected void onDestroy()
	{
		if(mExecuteMicrophoneListener != null)
		{
			mExecuteMicrophoneListener.Stop();
			mExecuteMicrophoneListener = null;
		}

		super.onDestroy();
	}

	@Override
	protected void prepareActivity()
	{
		txtBpm = (TextView) findViewById(R.id.txtBpm);
		txtCount = (TextView) findViewById(R.id.txtCount);

		mBeatTimings = new Vector<Long>();
		
		findViewById(R.id.btnReset).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View paramView)
			{
			resetStats();
			}
		});
		
		findViewById(R.id.imgTouchPad).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
			}
		});
		
		findViewById(R.id.imgTouchPad).setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN: {
					if (mExecuteMicrophoneListener != null)
						break;
					saveBeat(new Date().getTime());
					break;
				}
				case MotionEvent.ACTION_UP: {
					break;
				}
				case MotionEvent.ACTION_CANCEL: {
					break;
				}
				}
				return false;
			}
		});
	}

	private void saveBeat(long time)
	{
		if (mLastBeatTiming == 0)
		{
			mLastBeatTiming = time;
			return;
		}
		
		if (mBeatCount < 4)
		{
			mBeatTimings.add(time - mLastBeatTiming);
			mBeatCount++;

			mLastBeatTiming = time;
			
			updateStats();
			return;
		}

		if (mBeatCount < 10)
		{
			mBeatTimings.add(time - mLastBeatTiming);
			mBeatCount++;

			mLastBeatTiming = time;
			
			updateStats();
			return;
		}

		mBeatTimings.add((mBeatCount) % 10, time - mLastBeatTiming);
		mBeatCount++;

		mLastBeatTiming = time;
		
		updateStats();
	}

	private boolean checkPermission()
	{
		if (!mPermissionOperator.isMicrophonePermissionGranded(this))
		{
			mPermissionOperator.requestMicrophonePermission(this);
			return false;
		}

		return true;
	}

	private void openMicrophoneListener()
	{
		if (!checkPermission())
			return;

		mMenu.findItem(R.id.menu_main_automode).setTitle("Sound Mode On");
		mMenu.findItem(R.id.menu_main_automode).setChecked(true);

		getSupportActionBar().setTitle("Listening...");

		resetStats();

		findViewById(R.id.imgTouchPad).setEnabled(false);
		findViewById(R.id.imgMicrophone).setVisibility(View.VISIBLE);
		findViewById(R.id.progressMicrophone).setVisibility(View.VISIBLE);

		mExecuteMicrophoneListener = new ExecuteMicrophoneListener();
		mExecuteMicrophoneListener.Run();
	}

	private void closeMicrophoneListener()
	{
		mMenu.findItem(R.id.menu_main_automode).setTitle("Sound Mode Off");
		mMenu.findItem(R.id.menu_main_automode).setChecked(false);

		getSupportActionBar().setTitle("Touch Now!");

		resetStats();

		findViewById(R.id.imgTouchPad).setEnabled(true);
		findViewById(R.id.imgMicrophone).setVisibility(View.GONE);
		findViewById(R.id.progressMicrophone).setVisibility(View.GONE);

		if(mExecuteMicrophoneListener == null) return;
		mExecuteMicrophoneListener.Stop();
		mExecuteMicrophoneListener = null;
	}

	private void updateStats()
	{
		if (mBeatCount < 4)
		{
			txtCount.setText(String.valueOf(mBeatCount));
			return;
		}
		
		float averageTiming = 0f;
		
		for (int i = 0; i < mBeatTimings.size(); i++)
		{
			averageTiming += mBeatTimings.get(i);
		}
		
		averageTiming = averageTiming / mBeatTimings.size();
		
		float bpmFloat = (1000f / averageTiming) * 60f;
		
		int bpmInteger = Math.round(bpmFloat);
		
		txtCount.setText(String.valueOf(mBeatCount));

		txtBpm.setText(String.valueOf(bpmInteger));
	}

	private void resetStats()
	{
		txtBpm.setText("-");
		txtCount.setText("0");

		mBeatCount = 0;
		mBeatTimings = null;

		mBeatTimings = new Vector<Long>();

		mLastBeatTiming = 0;
	}

	private class ExecuteMicrophoneListener
	{
		private AsyncTask<Void, Void, Void> mTask = null;
		private MicrophoneManager mMicrophoneManager = null;

		private void Run()
		{
			mTask = new BaseAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}

		private void Stop()
		{
			mTask.cancel(true);

			if(mMicrophoneManager == null) return;

			mMicrophoneManager.stop();
		}

		private class BaseAsyncTask extends AsyncTask<Void, Void, Void>
		{
			protected Void doInBackground(Void... args)
			{
				mMicrophoneManager = new MicrophoneManager();
				mMicrophoneManager.run(new MicrophoneEvent() {
					@Override
					public void soundMeter(final double dB)
					{
						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								((ProgressBar) findViewById(R.id.progressMicrophone)).setProgress((int) Math.min(100 + dB, 100));
							}
						});
					}

					@Override
					public void onBeat(final double time) {
						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								final Drawable indicatorDrawable = ((ImageView)findViewById(R.id.imgTouchPad)).getDrawable();
								final int[] initialState = indicatorDrawable.getState();
								indicatorDrawable.setState(new int[] {android.R.attr.state_pressed});

								new Handler().postDelayed(new Runnable() {
									@Override
									public void run()
									{
										indicatorDrawable.setState(initialState);
									}
								}, 100);

								saveBeat((long) (time * 1000));
							}
						});
					}
				});

				return null;
			}
		}
	}
}
