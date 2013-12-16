package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import java.lang.Math;

public class Camera {
	float x, y, z;
	float targetAngleZ;
	float currentAngleZ;
	final float cameraAngleSpeed = (float)Math.toRadians(5.0f);

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
		forceUpdate();
	}

	public void repositionAbsolute(float new_x, float new_y, float new_z) {
		x += new_x;
		y += new_y;
		z += new_z;
	}

	public void update() {
		if (currentAngleZ == targetAngleZ)
			return;
		forceUpdate();
	}

	public void forceUpdate() {
		if (Math.abs(currentAngleZ - targetAngleZ) < cameraAngleSpeed) {
			currentAngleZ = targetAngleZ;
		} else if (currentAngleZ < targetAngleZ) {
			currentAngleZ += cameraAngleSpeed;
		} else {
			currentAngleZ -= cameraAngleSpeed;
		}
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GLU.gluLookAt(x+142f*(float)Math.cos(currentAngleZ), y+142f*(float)Math.sin(currentAngleZ), z+100.4f, x, y, z, 0.0f, 0.0f, 1.0f);
	}
}
