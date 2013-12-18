package iface;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import world.Block;

public class Input {
	Interface iface;

	public Input (Interface I){
		iface = I;
	}

	public void poll(long deltaT) {
		Block where = iface.camera.resolveClick(Mouse.getEventX(), Mouse.getEventY(), iface.current_layer, iface.world);
		if (where != null) {
			iface.cursor.reposition(where.x, where.y, where.z);
		} else {
			iface.cursor.hide();
		}

		while (Mouse.next()){
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					if (where != null) {
						iface.player.order.clear();
						iface.player.placeMoveOrder(where);
						System.out.println("Click at "+where.x+" "+where.y+" "+where.z);
					} else {
						System.out.println("Click at void");
					}
				}
			}
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				// Key pressed
				if (Keyboard.getEventCharacter() == 'z') {
					if (iface.current_layer>0) {
						 iface.current_layer--;
						 iface.camera.repositionDelta(0.0f, 0.0f, -1.0f);
					}
				}
				if (Keyboard.getEventCharacter() == 'x') {
					if (iface.current_layer<iface.world.zsize-1) {
						iface.current_layer++;
						iface.camera.repositionDelta(0.0f, 0.0f, 1.0f);
					}
				}
				if (Keyboard.getEventCharacter() == 'q') {
					iface.camera.rotateLeft();
				}
				if (Keyboard.getEventCharacter() == 'e') {
					iface.camera.rotateRight();
				}
			}
		}
	}
}
