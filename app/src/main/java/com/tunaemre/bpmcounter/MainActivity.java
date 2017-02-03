package com.tunaemre.bpmcounter;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tunaemre.bpmcounter.views.ExtendedActivity;

public class MainActivity extends AppCompatActivity
{
	private TextView txtBmp = null;
	private TextView txtCount = null;
	
	private long lastTouch = 0;
	private List<Long> touches;
	private int count = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		getSupportActionBar().setTitle("Touch Now!");
		
		prepareActivity();
	}

	protected void prepareActivity()
	{
		txtBmp = (TextView) findViewById(R.id.txtBpm);
		txtCount = (TextView) findViewById(R.id.txtCount);
		
		touches = new Vector<Long>();
		
		((Button) findViewById(R.id.btnReset)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View paramView)
			{
				txtBmp.setText("-");
				txtCount.setText("0");
				
				count = 0;
				touches = null;
				
				touches = new Vector<Long>();
				
				lastTouch = 0;
			}
		});
		
		((ImageView) findViewById(R.id.imgTouchPad)).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		((ImageView) findViewById(R.id.imgTouchPad)).setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN: {
					saveTouch(new Date().getTime());
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
	
	private void saveTouch(long time)
	{
		if (lastTouch == 0)
		{
			lastTouch = time;
			return;
		}
		
		if (count < 4)
		{
			touches.add(time - lastTouch);
			count++;
			
			lastTouch = time;
			
			updateStats();
			return;
		}

		if (count < 10)
		{
			touches.add(time - lastTouch);
			count++;
			
			lastTouch = time;
			
			updateStats();
			return;
		}

		touches.add((count) % 10, time - lastTouch);
		count++;
		
		lastTouch = time;
		
		updateStats();
	}
	
	private void updateStats()
	{
		if (count < 4)
		{
			txtCount.setText(String.valueOf(count));
			return;
		}
		
		float avarageTiming = 0f;
		
		for (int i = 0; i < touches.size(); i++)
		{
			avarageTiming += touches.get(i);
		}
		
		avarageTiming = avarageTiming / touches.size();
		
		float bpmFloat = (1000f / avarageTiming) * 60f;
		
		int bpmInteger = Math.round(bpmFloat);
		
		txtCount.setText(String.valueOf(count));
		
		txtBmp.setText(String.valueOf(bpmInteger));
	}

}
