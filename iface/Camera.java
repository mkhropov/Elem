package iface;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.BufferUtils;
import java.lang.Math;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import world.*;

public class Camera {
	float x, y, z;
	float targetAngleZ;
	float currentAngleZ;
	final float cameraAngleSpeed = (float)Math.toRadians(90.0f);

	public Camera (float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.targetAngleZ = (float)Math.toRadians(45.0f);
		this.currentAngleZ = (float)Math.toRadians(45.0f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(10.0f, 4.0f/3.0f, 1.0f, 1000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GLU.gluLookAt(x+142f*(float)Math.cos(currentAngleZ), y+142f*(float)Math.sin(currentAngleZ), z+100.4f, x, y, z, 0.0f, 0.0f, 1.0f);
	}

	public void rotateLeft() {
		targetAngleZ -= (float)Math.toRadians(90.0f);
	}

	public void rotateRight() {
		targetAngleZ += (float)Math.toRadians(90.0f);
	}

	public void repositionDelta(float dx, float dy, float dz) {
		x += dx;
		y += dy;
		z += dz;
		forceUpdate(0);
	}

	public void repositionAbsolute(float new_x, float new_y, float new_z) {
		x += new_x;
		y += new_y;
		z += new_z;
	}

	public int[] resolveMousePosition (int x, int y, int current_layer) {
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		FloatBuffer p0 = BufferUtils.createFloatBuffer(3);
		FloatBuffer p1 = BufferUtils.createFloatBuffer(3);
		float winX, winY, winZ;
		double X0, Y0, Z0;
		double X1, Y1, Z1;

		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

		GLU.gluUnProject( (float)x, (float)y, 0.0f, modelview, projection, viewport, p0);
		GLU.gluUnProject( (float)x, (float)y, 1.0f, modelview, projection, viewport, p1);

		int[] res = new int[2];
		res[0] = (int)Math.floor(p0.get(0) - (p0.get(2)-current_layer-1)*(p1.get(0)-p0.get(0))/(p1.get(2)-p0.get(2)));
		res[1] = (int)Math.floor(p0.get(1) - (p0.get(2)-current_layer-1)*(p1.get(1)-p0.get(1))/(p1.get(2)-p0.get(2)));

		return res;
	}

	public void update(long deltaT) {
		if (currentAngleZ == targetAngleZ)
			return;
		forceUpdate(deltaT);
	}

	public void forceUpdate(long deltaT) {
		if (Math.abs(currentAngleZ - targetAngleZ) < ((cameraAngleSpeed*deltaT)/1000)) {
			currentAngleZ = targetAngleZ;
		} else if (currentAngleZ < targetAngleZ) {
			currentAngleZ += ((cameraAngleSpeed*deltaT)/1000);
		} else {
			currentAngleZ -= ((cameraAngleSpeed*deltaT)/1000);
		}
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GLU.gluLookAt(x+142f*(float)Math.cos(currentAngleZ), y+142f*(float)Math.sin(currentAngleZ), z+100.4f, x, y, z, 0.0f, 0.0f, 1.0f);
	}
}
