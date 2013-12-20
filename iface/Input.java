package iface;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import world.Block;
import player.Order;
import creature.SmartWalkingElem;

public class Input {
	Interface iface;
    int orderMode;

	public Input (Interface I){
		iface = I;
        orderMode = Order.ORDER_MOVE;
	}

	public void poll(long deltaT) {
		int[] pos = iface.camera.resolvePixel(Mouse.getEventX(), Mouse.getEventY(), iface.current_layer);
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
		//				iface.player.order.clear();
                        switch (orderMode){
                            case Order.ORDER_MOVE: iface.player.placeMoveOrder(where); break;
                            case Order.ORDER_DIG: iface.player.placeDigOrder(where); break;
						    case Order.ORDER_BUILD: iface.player.placeBuildOrder(where, iface.world.material[0]); break;
                        }
						System.out.println("Click at "+where.x+" "+where.y+" "+where.z);
					} else {
						System.out.println("Click at void");
					}
				}
			}
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState())
				// Key pressed
				switch(Keyboard.getEventCharacter()){
				case 'z':
					if (iface.current_layer>0) {
						 iface.current_layer--;
						 iface.camera.repositionDelta(0.0f, 0.0f, -1.0f);
					};
					break;
				case 'x':
					if (iface.current_layer<iface.world.zsize-1) {
						iface.current_layer++;
						iface.camera.repositionDelta(0.0f, 0.0f, 1.0f);
					};
					break;
				case 'q':
					iface.camera.rotateLeft();
					break;
				case 'e':
					iface.camera.rotateRight();
					break;
				case 's':
					if (where != null)
						iface.player.spawnCreature(new SmartWalkingElem(iface.world, where));
					break;
                case '1':
                    System.out.println("MOVE order mode engaged");
                    orderMode = Order.ORDER_MOVE;
					break;
                case '2':
                    System.out.println("DIG order mode engaged");
                    orderMode = Order.ORDER_DIG;
					break;
	            case '3':
                    System.out.println("BUILD order mode engaged");
                    orderMode = Order.ORDER_BUILD;
					break;
				default:
					break;
            }
		}
	}
}
