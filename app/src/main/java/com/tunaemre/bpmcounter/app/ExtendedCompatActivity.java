package com.tunaemre.bpmcounter.app;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

public abstract class ExtendedCompatActivity extends AppCompatActivity
{
    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);

        prepareActivity();
    }

    protected abstract void prepareActivity();
}
