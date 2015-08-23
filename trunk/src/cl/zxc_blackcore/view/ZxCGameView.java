package cl.zxc_blackcore.view;

import cl.zxc_blackcore.render.ZxCGameRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView;

public class ZxCGameView extends GLSurfaceView {
	private GLSurfaceView.Renderer renderer;

	public ZxCGameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		renderer = new ZxCGameRenderer();
		
		setEGLContextClientVersion(2);
		this.setRenderer(renderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
	}
}
