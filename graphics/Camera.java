package graphics;

import org.lwjgl.opengl.GL11;

public class Camera {
	double x, y, z;
	double targetAngleZ;
	double currentAngleZ;
	final double cameraAngleSpeed = 5.0;

	public Camera (double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.targetAngleZ = 45;
		this.currentAngleZ = 45;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(20, -20, 15f, -15f, 20, -20);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glRotated(135, 1.0, 0.0, 0.0);
		GL11.glRotated(45, 0.0, 0.0, 1.0);
		GL11.glTranslated(-x, -y, -z);
	}

	public void rotateLeft() {
		targetAngleZ -= 90;
	}

	public void rotateRight() {
		targetAngleZ += 90;
	}

	public void repositionDelta(double dx, double dy, double dz) {
		x += dx;
		y += dy;
		z += dz;
		forceUpdate();
	}

	public void repositionAbsolute(double new_x, double new_y, double new_z) {
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
		GL11.glRotated(135, 1.0, 0.0, 0.0);
		GL11.glRotated(currentAngleZ, 0.0, 0.0, 1.0);
		GL11.glTranslated(-x, -y, -z);
	}
}
