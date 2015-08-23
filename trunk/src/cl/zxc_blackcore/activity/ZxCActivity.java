package cl.zxc_blackcore.activity;

import cl.zxc_blackcore.R;
import cl.zxc_blackcore.engine.ZxCEngine;
import cl.zxc_blackcore.render.ZxCGameRenderer;
import cl.zxc_blackcore.view.ZxCGameView;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.util.Log;
import android.view.Menu;

public class ZxCActivity extends Activity {

	private GLSurfaceView gameView;

	// private GLSurfaceView mGLView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gameView = new ZxCGameView(this);

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager
				.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

		if (supportsEs2) { // Request an OpenGL ES 2.0 compatible context.
			gameView.setEGLContextClientVersion(2);

			// Set the renderer to our demo renderer, defined below.
			gameView.setRenderer(new ZxCGameRenderer());
			Log.d("ZxCActivity", "OGLES.SI");
		} else {
			// This is where you could create an OpenGL ES1.x compatible
			// renderer if you wanted to support both ES 1 and ES2.
			Log.d("ZxCActivity", "OGLES.NO");
			// return;
		}
		setContentView(gameView);
		ZxCEngine.context = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.zxcmain, menu);
		return true;
	}
}