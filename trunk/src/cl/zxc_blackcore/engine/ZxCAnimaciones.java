package cl.zxc_blackcore.engine;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

public class ZxCAnimaciones {
	private final String vertexShaderCode = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 vPosition;" + "void main() {"
			+ "gl_Position = vPosition * uMVPMatrix;" + "}";

	private final String fragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "gl_FragColor = vColor;" + "}";

	private final FloatBuffer vertexBuffer;
	private final ShortBuffer drawListBuffer;
	private final int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;

	long loopStart = 0;
	long loopEnd = 0;
	long loopRunTime = 0;
	int i = 0;
	int maxAnim = 3;
	int nroAnim = 0;
	int nroVertex = 12;
	int facesLength;

	static final int COORDS_PER_VERTEX = 3;
	private final int vertexStride = COORDS_PER_VERTEX * 4;
	float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

	public ZxCAnimaciones(String fileName, int nroAnim) {
		ZxCCargarOBJ cargarOBJ = new ZxCCargarOBJ();
		cargarOBJ.objLoader(fileName);
		
		System.out.println("ZXC.FCOUNT "+cargarOBJ.fCount);
		System.out.println("ZXC.VCOUNT "+cargarOBJ.vCount);
		System.out.println("ZXC.VNCOUNT "+cargarOBJ.vnCount);
		System.out.println("ZXC.VTCOUNT "+cargarOBJ.vtCount);

		facesLength = cargarOBJ.fCount;
		
		ByteBuffer bb = ByteBuffer.allocateDirect(cargarOBJ.v.length * 4
				* nroAnim);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();

		vertexBuffer.position(0);
		vertexBuffer.put(cargarOBJ.v);

		ByteBuffer dlb = ByteBuffer.allocateDirect(cargarOBJ.faces.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();

		drawListBuffer.put(cargarOBJ.faces).position(0);;
		
		int vertexShader = ZxCUtils.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = ZxCUtils.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
														// to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
															// shader to program
		GLES20.glLinkProgram(mProgram); // create OpenGL program executables
	}

	public void draw(float[] mvpMatrix) {
		GLES20.glUseProgram(mProgram);

		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

		GLES20.glEnableVertexAttribArray(mPositionHandle);

		// vertexBuffer.position(0);
		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		/*
		 * loopStart = System.currentTimeMillis(); if (loopStart > loopEnd +
		 * 1000) { loopEnd = System.currentTimeMillis(); i = nroAnim *
		 * nroVertex; nroAnim++; if (nroAnim > maxAnim) { i = 0; nroAnim = 1; }
		 * 
		 * }
		 */
		// get handle to fragment shader's vColor member
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

		// Set color for drawing the triangle
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);

		// get handle to shape's transformation matrix
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		ZxCUtils.checkGlError("glGetUniformLocation");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		ZxCUtils.checkGlError("glUniformMatrix4fv");

		// Draw the square
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, facesLength,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
