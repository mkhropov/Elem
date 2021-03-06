package iface;

import creature.Elem;
import graphics.Texture;
import graphics.Model;
import graphics.Renderer;
import core.Data;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL20;
import player.Zone;
import world.Block;
import world.World;
import iface.widget.Widget;
import java.util.ArrayList;
import creature.Creature;


public class Input {
	Interface iface;
	int startX, startY;
	int endX, endY;
	Model model;
	Texture gs;
	int hue_uniform;
	boolean draw = false;

	public Input (Interface I){
		Renderer r = Renderer.getInstance();
		hue_uniform = GL20.glGetUniformLocation(
		            r.shaders[Renderer.SHADER_GHOST], "hue");
		iface = I;
		model = Data.Models.get("cube");
		gs = Data.Textures.get("selection");
	}

	public void poll(long deltaT) {
		int x = Mouse.getEventX();
		int y = Mouse.getEventY();
		int[] pos = iface.camera.resolvePixel(x, y, iface.current_layer);

		boolean onMenu = false;
		for (Widget w: Interface.menu) {
			if (w.hover(x, 600-y)) {
				onMenu = true;
			}
		}

		iface.cursor.reposition(pos[0], pos[1], iface.current_layer, x, y);
		if (onMenu)
			iface.cursor.state = Cursor.STATE_IFACE;
		endX = iface.cursor.x;
		endY = iface.cursor.y;

		while (Mouse.next()){
			if (Mouse.getEventButton() == 0)
				if (iface.cursor.state==Cursor.STATE_IFACE) {
					if (Mouse.getEventButtonState()) {
						for (Widget w: Interface.menu) {
							w.onPress(x, 600-y);
						}
					} else {
						for (Widget w: Interface.menu) {
							w.onRelease(x, 600-y);
						}
					}
				} else {
					if (Mouse.getEventButtonState()) { //button pressed
						startX = endX;
						startY = endY;
						if (Interface.getCommandMode() != Interface.COMMAND_MODE_SELECT)
							draw = true;
					} else { //button released
						Zone z = null;
						if (Interface.getCommandMode() == Interface.COMMAND_MODE_ZONE) {
							z = new Zone(Data.Zones.get(Interface.getZoneType()));
						}
						if (Interface.getCommandMode() == Interface.COMMAND_MODE_SELECT) {
							ArrayList<Creature> c = World.getInstance().getCreature(endX, endY,
								iface.current_layer);
							if (c != null && c.size() != 0) {
								Widget tmp = null;
								for (Widget w: Interface.menu)
									if (w instanceof StatsElem || w instanceof StatsBlock) {
										tmp = w;
										break;
									}
								if (tmp != null)
									Interface.menu.remove(tmp);
								Interface.menu.add(new StatsElem(0, 0, (Elem) c.get(0)));
							} else {
								Widget tmp = null;
								for (Widget w: Interface.menu)
									if (w instanceof StatsBlock || w instanceof StatsElem) {
										tmp = w;
										break;
									}
								if (tmp != null)
									Interface.menu.remove(tmp);
								Interface.menu.add(new StatsBlock(0, 0,
									World.getInstance().getBlock(endX, endY,iface.current_layer)));
							}
						}
						draw = false;
						int i, j;
						i = startX;
						do {
							j = startY;
							do {
								if (iface.canPlaceCommand(i, j, iface.current_layer)){
									switch (Interface.getCommandMode()){
									case Interface.COMMAND_MODE_SPAWN:
										iface.player.spawnCreature(
											new Elem(World.getInstance().getBlock(i, j, iface.current_layer)));
										break;
									case Interface.COMMAND_MODE_DIG:
										if (Interface.getDigForm()==World.FORM_BLOCK){
											Interface.setDigForm(World.FORM_FLOOR);
											iface.player.placeDigOrder(
												World.getInstance().getBlock(i, j, iface.current_layer-1));
											Interface.setDigForm(World.FORM_BLOCK);
										}
										iface.player.placeDigOrder(
											World.getInstance().getBlock(i, j, iface.current_layer));
										break;
									case Interface.COMMAND_MODE_BUILD:
										iface.player.placeBuildOrder(
											World.getInstance().getBlock(i, j, iface.current_layer),
											Interface.getBuildMaterial());
										break;
									case Interface.COMMAND_MODE_ZONE:
										z.add(World.getInstance().getBlock(i, j, iface.current_layer));
										break;
									case Interface.COMMAND_MODE_CANCEL:
										iface.player.cancelOrders(
											World.getInstance().getBlock(i, j, iface.current_layer));
										break;
									}
								}
								j+=Math.signum(endY-startY);
							} while(j != endY+Math.signum(endY - startY));
							i+=Math.signum(endX-startX);
						} while(i != endX+Math.signum(endX-startX));
						if (Interface.getCommandMode() == Interface.COMMAND_MODE_ZONE) {
							iface.player.zones.add(z);
						}
					}
				}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			iface.camera.repositionDelta(0.f, -iface.camera.horizontalSpeed*deltaT/1000, 0.f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			iface.camera.repositionDelta(iface.camera.horizontalSpeed*deltaT/1000, 0.f, 0.f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			iface.camera.repositionDelta(0.f, iface.camera.horizontalSpeed*deltaT/1000, 0.f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			iface.camera.repositionDelta(-iface.camera.horizontalSpeed*deltaT/1000, 0.f, 0.f);
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				// Key pressed
				switch(Keyboard.getEventKey()){
				case Keyboard.KEY_Z:
					if (iface.current_layer>0) {
						 iface.current_layer--;
						 iface.camera.repositionDelta(0.0f, 0.0f, -1.0f);
					};
					break;
				case Keyboard.KEY_X:
					if (iface.current_layer<iface.world.zsize-1) {
						iface.current_layer++;
						iface.camera.repositionDelta(0.0f, 0.0f, 1.0f);
					};
					break;
				case Keyboard.KEY_Q:
					iface.camera.rotateLeft();
					break;
				case Keyboard.KEY_E:
					iface.camera.rotateRight();
					break;
				case Keyboard.KEY_M:
					if ((iface.viewMode&Renderer.VIEW_MODE_MASK) != Renderer.VIEW_MODE_FULL) {
						iface.viewMode = Renderer.VIEW_MODE_FULL | (iface.viewMode&(~Renderer.VIEW_MODE_MASK));
					} else {
						iface.viewMode = Renderer.VIEW_MODE_FOW | (iface.viewMode&(~Renderer.VIEW_MODE_MASK));
					}
					Renderer.getInstance().reset();
					break;
                case Keyboard.KEY_1:
					Interface.setCommandMode(Interface.COMMAND_MODE_SPAWN);
					break;
                case Keyboard.KEY_2:
					Interface.setCommandMode(Interface.COMMAND_MODE_DIG);
					break;
	            case Keyboard.KEY_3:
					Interface.setCommandMode(Interface.COMMAND_MODE_BUILD);
					break;
	            case Keyboard.KEY_4:
					Interface.setCommandMode(Interface.COMMAND_MODE_ZONE);
					break;
	            case Keyboard.KEY_5:
					Interface.setCommandMode(Interface.COMMAND_MODE_CANCEL);
					break;
				case Keyboard.KEY_H:
					iface.viewMode |= Renderer.VIEW_MODE_NOBLOCK;
					break;
				case Keyboard.KEY_SPACE:
					iface.viewMode |= Renderer.VIEW_MODE_FLAT;
					break;
				default:
					break;
            }
		} else {
			// Key released
			switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_SPACE:
					iface.viewMode = iface.viewMode & ~Renderer.VIEW_MODE_FLAT;
					break;
				case Keyboard.KEY_H:
					iface.viewMode = iface.viewMode & ~Renderer.VIEW_MODE_NOBLOCK;
					break;
				case Keyboard.KEY_F3:
					iface.debug=!iface.debug;
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
		if (Interface.getCommandMode() == Interface.COMMAND_MODE_ZONE) {
			model = Data.Models.get("floor");
		} else {
			model = Data.Models.get("cube");
		}
		GL20.glUseProgram(Renderer.getInstance().shaders[Renderer.SHADER_GHOST]);
		GL20.glUniform4f(hue_uniform, 1f, 0f, 0f, 0.6f);
		int i, j;
		i = startX;
		do {
			j = startY;
			do {
				if (iface.canPlaceCommand(i, j, iface.current_layer)){
					GL20.glUniform4f(hue_uniform, 0f, 1f, 0f, 0.6f);
				} else {
					GL20.glUniform4f(hue_uniform, 1f, 0f, 0f, 0.6f);
				}
				model.draw(i, j, iface.current_layer, 0.f, gs);
				j += Math.signum(endY - startY);
			} while (j != endY+Math.signum(endY - startY));
			i += Math.signum(endX - startX);
		} while (i != endX+Math.signum(endX - startX));
		GL20.glUseProgram(Renderer.getInstance().shaders[Renderer.SHADER_NONE]);
	}
}
