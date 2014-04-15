package graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;

public class Sun {
	public void update() {
		float light_ambient[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		float light_diffuse[] = { 0.7f, 0.7f, 0.7f, 1.0f };
		float light_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float light_position[] = { -15.0f, 5.0f, 10.0f, 0.0f };

		ByteBuffer temp = ByteBuffer.allocateDirect(4*4);
		temp.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = temp.asFloatBuffer();
		buffer.put(light_ambient); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, buffer);
		buffer.clear();
		buffer.put(light_diffuse); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, buffer);
		buffer.clear();
		buffer.put(light_specular); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, buffer);
		buffer.clear();
		buffer.put(light_position); buffer.flip();
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, buffer);
	}
}
