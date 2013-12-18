package iface;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import world.Block;

public class Input {
	Interface iface;
    int orderMode;

	public Input (Interface I){
		iface = I;
        orderMode = 0;
	}

	public void poll(long deltaT) {
		int[] pos = iface.camera.resolveMousePosition(Mouse.getEventX(), Mouse.getEventY(), iface.current_layer);
		Block where = iface.world.getBlock(pos[0], pos[1], iface.current_layer);
		if (where != null) {
			iface.cursor.reposition(where.x, where.y, where.z);
		} else {
			iface.cursor.reposition(pos[0], pos[1], iface.current_layer);
			iface.cursor.disable();
		}

		while (Mouse.next()){
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					if (where != null) {
						iface.player.order.clear();
                        switch (orderMode){
                            case 0: iface.player.placeMoveOrder(where); break;
                            case 1: iface.player.placeDigOrder(where); break;
						    case 2: iface.player.placeBuildOrder(where, iface.world.material[0]); break;
                        }
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
                if (Keyboard.getEventCharacter() == '1') {
                    System.out.println("MOVE order mode engaged");
                    orderMode = 0;
                }
                if (Keyboard.getEventCharacter() == '2') {
                    System.out.println("DIG order mode engaged");
                    orderMode = 1;
                }
	            if (Keyboard.getEventCharacter() == '3') {
                    System.out.println("BUILD order mode engaged");
                    orderMode = 2;
                }
            }
		}
	}
}
