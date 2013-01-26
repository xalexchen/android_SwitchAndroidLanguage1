package lab.sodino.language;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Locale;

import dalvik.system.DexFile;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class LanguageAct extends Activity{
	private LinearLayout linearLayout;
	private Button btnChinese;
	private Button btnEnglish;
	private BtnListener btnListener;
	private int[] colors = { 0xffff0000, 0xffffA500, 0xffffff00, 0xff00ff00, 0xff0000ff, 0xff9B30FF };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("ANDROID_LAB", "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initViews();
		btnListener = new BtnListener();
		btnChinese = (Button) findViewById(R.id.btnChinese);
		btnChinese.setOnClickListener(btnListener);
		btnEnglish = (Button) findViewById(R.id.btnEnglish);
		btnEnglish.setOnClickListener(btnListener);
	}

	private void initViews() {
		LinearLayout.LayoutParams layParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		String[] arr = getResources().getStringArray(R.array.colors);
		for (int i = 0; i < colors.length; i++) {
			TextView txt = new TextView(this);
			txt.setText(arr[i]);
			txt.setBackgroundColor(colors[i]);
			txt.setTextColor(~colors[i] | 0xff000000);
			linearLayout.addView(txt, layParams);
		}
	}

	class BtnListener implements Button.OnClickListener {
		public void onClick(View view) {
			if (view == btnChinese) {
				updateLanguage(Locale.SIMPLIFIED_CHINESE);
			} else if (view == btnEnglish) {
				updateLanguage(Locale.ENGLISH);
			}
		}
	}

	private void updateLanguage(Locale locale) {
		Log.d("ANDROID_LAB", locale.toString());
		try {
			Object objIActMag, objActMagNative;
			Class clzIActMag = Class.forName("android.app.IActivityManager");
			Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
			Method getDefault = clzActMagNative.getDeclaredMethod("getDefault");
			// IActivityManager iActMag = ActivityManagerNative.getDefault();
			objIActMag = getDefault.invoke(clzActMagNative);
			// Configuration config = iActMag.getConfiguration();
			Method getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
			Configuration config = (Configuration) getConfiguration.invoke(objIActMag);
			config.locale = locale;
			Class[] clzParams = { Configuration.class };
			Method updateConfiguration = clzIActMag.getDeclaredMethod(
					"updateConfiguration", clzParams);
			updateConfiguration.invoke(objIActMag, config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}