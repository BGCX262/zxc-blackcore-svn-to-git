package cl.zxc_blackcore.render;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import cl.zxc_blackcore.engine.Square;
import cl.zxc_blackcore.engine.ZxCEngine;
import cl.zxc_blackcore.engine.ZxCPersonaje;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class ZxCGameRenderer implements GLSurfaceView.Renderer {
	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0;
	private Square   mSquare;
	private ZxCPersonaje personaje;
	
	private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    
 // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;

	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		loopStart = System.currentTimeMillis();
		try {
			if (loopRunTime < ZxCEngine.GAME_THREAD_FPS_SLEEP) {
				Thread.sleep(ZxCEngine.GAME_THREAD_FPS_SLEEP - loopRunTime);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		// gl.glLoadIdentity();
		loopEnd = System.currentTimeMillis();
		loopRunTime = ((loopEnd - loopStart));
		// Draw background color
        
		//GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -10, 0f, 0f, 0f, 0f, 10.0f, 10.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        // Draw square
        mSquare.draw(mMVPMatrix);
        //personaje.draw(mMVPMatrix);
        // Create a rotation for the triangle
//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

        // Draw triangle
        //mTriangle.draw(mMVPMatrix);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		// Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//mTriangle = new Triangle();
        mSquare   = new Square();
        //personaje = new ZxCPersonaje();
	}

}
