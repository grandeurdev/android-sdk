package com.example.grandeurcloud_android_sdk.cookies;

import android.content.Context;
import android.app.Application;
import android.content.Context;

public class ContextHelper extends Application
{
    private static ContextHelper instance = null;

    private ContextHelper()
    {
        instance = this;
    }

    public static Context getInstance()
    {
        if (null == instance)
        {
            instance = new ContextHelper();
        }

        return instance;
    }
}