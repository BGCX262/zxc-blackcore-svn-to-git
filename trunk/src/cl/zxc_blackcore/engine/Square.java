package cl.zxc_blackcore.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;
import android.util.Log;

public class Square {
	long loopStart = 0;
	long loopEnd = 0;
	long loopRunTime = 0;
	int i = 0;
	int maxAnim = 3;
	int nroAnim = 0;
	int nroVertex = 12;
	private final String vertexShaderCode =
	// This matrix member variable provides a hook to manipulate
	// the coordinates of the objects that use this vertex shader
	"uniform mat4 uMVPMatrix;" +

	"attribute vec4 vPosition;" + "void main() {" +
	// the matrix must be included as a modifier of gl_Position
			"  gl_Position = vPosition * uMVPMatrix;" + "}";

	private final String fragmentShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "  gl_FragColor = vColor;" + "}";

	private final FloatBuffer vertexBuffer;
	private final ShortBuffer drawListBuffer;
	private final int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;

	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	static float squareCoords[] = { -0.5f, 0.5f, 0.0f, // top left
			-0.5f, -0.5f, 0.0f, // bottom left
			0.5f, -0.5f, 0.0f, // bottom right
			0.5f, 0.5f, 0.0f }; // top right
	static float squareCoords1[] = { -0.4f, 0.4f, 0.0f, // top left
			-0.4f, -0.4f, 0.0f, // bottom left
			0.4f, -0.4f, 0.0f, // bottom right
			0.4f, 0.4f, 0.0f }; // top right

	static float squareCoords2[] = { -0.3f, 0.3f, 0.0f, // top left
			-0.3f, -0.3f, 0.0f, // bottom left
			0.3f, -0.3f, 0.0f, // bottom right
			0.3f, 0.3f, 0.0f }; // top right

	private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw

	// vertices

	private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per
															// vertex

	// Set color with red, green, blue and alpha (opacity) values
	float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };
	
	int asd;

	public Square() {
		ZxCCargarOBJ cargarOBJ = new ZxCCargarOBJ();
		cargarOBJ.objLoader("werewolfRigging_000001.obj");
		
		System.out.println("ZXC.fCount"+cargarOBJ.fCount);
		System.out.println("ZXC.vCount"+cargarOBJ.vCount);
		
		asd = cargarOBJ.fCount;
		//asd= cargarOBJ.faces.length;
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 4 bytes per float)
				cargarOBJ.v.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();

		vertexBuffer.position(0);
		vertexBuffer.put(cargarOBJ.v);
		//vertexBuffer.put(squareCoords1);
		//vertexBuffer.put(squareCoords);

		// initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(
		// (# of coordinate values * 2 bytes per short)
				cargarOBJ.faces.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();

		//drawListBuffer
		drawListBuffer.put(cargarOBJ.faces).position(0);

		// prepare shaders and OpenGL program
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

		// Add program to OpenGL environment
		GLES20.glUseProgram(mProgram);

		/*
		 * final int buffers[] = new int[3]; GLES20.glGenBuffers(1, buffers, 0);
		 * GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
		 * 
		 * GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, squareCoords.length * 4 *
		 * 3, vertexBuffer.position(2), GLES20.GL_STATIC_DRAW);
		 */

		// get handle to vertex shader's vPosition member
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		/*
		 * while (vertexBuffer.hasRemaining())
		 * System.out.println(vertexBuffer.position() + " -> " +
		 * vertexBuffer.get());
		 */

		vertexBuffer.position(0);
		// Prepare the triangle coordinate data
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

		/*loopStart = System.currentTimeMillis();
		if (loopStart > loopEnd + 1000) {
			loopEnd = System.currentTimeMillis();
			i = nroAnim * nroVertex;
			nroAnim++;
			if (nroAnim > maxAnim) {
				i = 0;
				nroAnim = 1;
			}

		}*/
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
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, asd,
				GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}

}
