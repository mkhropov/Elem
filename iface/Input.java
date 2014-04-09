package iface;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import world.Block;
import creature.SmartWalkingElem;
import graphics.*;
import org.lwjgl.opengl.GL20;
import physics.material.Material;
import world.World;

public class Input {
	Interface iface;
	int startX, startY;
	int endX, endY;
	Model m;
	GraphicalSurface gs;
	int channel_uniform;
	boolean draw = false;

	public Input (Interface I){
		Renderer r = Renderer.getInstance();
		channel_uniform = GL20.glGetUniformLocation(
		            r.shaders[Renderer.SHADER_GHOST], "channel");
		iface = I;
		int mid = ModelList.getInstance().findId("cube");
		m = ModelList.getInstance().get(mid);
		int gsid = GSList.getInstance().findId("IconDig");
		gs = GSList.getInstance().get(gsid);
	}

	public void poll(long deltaT) {
		Button b = null;
		int x = Mouse.getEventX();
		int y = Mouse.getEventY();
		Block where;
		int[] pos = iface.camera.resolvePixel(x, y, iface.current_layer);
		where = iface.world.getBlock(pos[0], pos[1], iface.current_layer);

		for (int i=0; i<iface.buttons.size(); ++i){
			b = iface.buttons.get(i);
			if (b.isIn(x, 600-y)){
				where = null;
			}
		}

		if (where != null) {
			iface.cursor.state = Cursor.STATE_ENABLED;
			iface.cursor.reposition(where.x, where.y, where.z, x, y);
		} else {
			iface.cursor.state = Cursor.STATE_DISABLED;
			iface.cursor.reposition(pos[0], pos[1], iface.current_layer, x, y);
		}
		endX = iface.cursor.x;
		endY = iface.cursor.y;

		while (Mouse.next()){
			if (Mouse.getEventButton() == 0) {
				if (Mouse.getEventButtonState()) {
					if (where != null){
						startX = endX;
						startY = endY;
						draw = true;
					}
				} else { //button released
					for (int i=0; i<iface.buttons.size(); ++i){
						b = iface.buttons.get(i);
						if (b.isIn(x, 600-y))
							b.onPress();
					}
					if (where != null) {
						draw = false;
		//				iface.player.order.clear();
						int i, j;
						i = startX;
						do {
							j = startY;
							do {
								switch (iface.commandMode){
								case Command.COMMAND_SPAWN:
									iface.player.spawnCreature(
											new SmartWalkingElem(
													World.getInstance().getBlock(i, j, iface.current_layer)));
									break;
								case Command.COMMAND_DIG:
									iface.player.placeDigOrder(
											World.getInstance().getBlock(i, j, iface.current_layer));
									break;
								case Command.COMMAND_BUILD:
									iface.player.placeBuildOrder(
											World.getInstance().getBlock(i, j, iface.current_layer),
											Material.MATERIAL_MARBLE);
									break;
								}
								j+=Math.signum(where.y-startY);
							} while(j != where.y+Math.signum(endY - startY));
							i+=Math.signum(where.x-startX);
						} while(i != where.x+Math.signum(where.x-startX));
	//					System.out.println("Click at "+where.x+" "+where.y+" "+where.z);
					}// else {
		//				System.out.println("Click at void");
	//				}
				}
			}
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
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
				case 'w':
					iface.camera.repositionDelta(0.f, -1.f, 0.f);
					break;
				case 'a':
					iface.camera.repositionDelta(1.f, 0.f, 0.f);
					break;
				case 's':
					iface.camera.repositionDelta(0.f, 1.f, 0.f);
					break;
				case 'd':
					iface.camera.repositionDelta(-1.f, 0.f, 0.f);
					break;
				case 'q':
					iface.camera.rotateLeft();
					break;
				case 'e':
					iface.camera.rotateRight();
					break;
				case 'b':
					if (where != null)
						iface.player.cast(0, where);
					break;
				case 'n':
					if (where != null)
						iface.player.cast(1, where);
					break;
				case 'm':
					Renderer.getInstance().draw_mana =
						 !Renderer.getInstance().draw_mana;
					break;
                case '1':
					(new Command(Command.COMMAND_SPAWN)).execute();
					break;
                case '2':
					(new Command(Command.COMMAND_DIG)).execute();
					break;
	            case '3':
					(new Command(Command.COMMAND_BUILD)).execute();
					break;
				case ' ':
					iface.viewMode |= Renderer.VIEW_MODE_FLAT;
					break;
				default:
					break;
            }
		} else {
			// Key released
			switch(Keyboard.getEventKey()){
				case Keyboard.KEY_SPACE:
					iface.viewMode = iface.viewMode & ~Renderer.VIEW_MODE_FLAT;
					break;
				default:
					break;
            }
		}
		}
	}

	public void draw(){
		if (! draw)
			return;
		GL20.glUseProgram(Renderer.getInstance().shaders[Renderer.SHADER_GHOST]);
		GL20.glUniform1i(channel_uniform, 2);
		int i, j;
		i = startX;
		do {
			j = startY;
			do {
				m.draw(i, j, iface.current_layer, 0.f, gs);
				j += Math.signum(endY - startY);
			} while (j != endY+Math.signum(endY - startY));
			i += Math.signum(endX - startX);
		} while (i != endX+Math.signum(endX - startX));
		GL20.glUseProgram(Renderer.getInstance().shaders[Renderer.SHADER_NONE]);
	}
}
