package iface;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import world.Block;
import player.Order;
import creature.SmartWalkingElem;
import physics.material.Material;

public class Input {
	Interface iface;
    int orderMode;

	public Input (Interface I){
		iface = I;
        orderMode = Order.ORDER_MOVE;
	}

	public void poll(long deltaT) {
		Button b = null;
		boolean mouseOnInterface = false;
		int x = Mouse.getEventX();
		int y = Mouse.getEventY();
		Block where;
		int[] pos = iface.camera.resolvePixel(x, y, iface.current_layer);
		where = iface.world.getBlock(pos[0], pos[1], iface.current_layer);

		for (int i=0; i<iface.buttons.size(); ++i){
			b = iface.buttons.get(i);
			if (b.isIn(x, 600-y)){
				mouseOnInterface = true;
				where = null;
			}
		}

		if (mouseOnInterface){
			iface.cursor.state = Cursor.STATE_IFACE;
			iface.cursor.reposition(x, y, 0);
		} else if (where != null) {
			iface.cursor.state = Cursor.STATE_ENABLED;
			iface.cursor.reposition(where.x, where.y, where.z);
		} else {
			iface.cursor.state = Cursor.STATE_DISABLED;
			iface.cursor.reposition(pos[0], pos[1], iface.current_layer);
		}

		while (Mouse.next()){
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == 0) {
					for (int i=0; i<iface.buttons.size(); ++i){
						b = iface.buttons.get(i);
						if (b.isIn(x, 600-y))
							b.onPress();
					}
					if (where != null) {
		//				iface.player.order.clear();
                        switch (iface.commandMode){
                            case Command.COMMAND_SPAWN:
								iface.player.spawnCreature(new SmartWalkingElem(iface.world, where));
								break;
                            case Command.COMMAND_DIG:
								iface.player.placeDigOrder(where);
								break;
						    case Command.COMMAND_BUILD:
								iface.player.placeBuildOrder(where, iface.world.material[Material.MATERIAL_MARBLE]);
								break;
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
						iface.player.cast(0, where);
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
