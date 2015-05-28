package com.telekurye.utils;

import android.app.Application;

/**
 * Created by sefagurel on 24.3.2015.
 */
public class App extends Application {

	public static MainThreadBus	bus	= null;

	@Override
	public void onCreate() {
		super.onCreate();
		bus = new MainThreadBus();
	}
}
