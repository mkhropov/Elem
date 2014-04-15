package iface;

import java.util.ArrayList;
import java.util.Iterator;

public class SelectorMenu extends Menu {
	private int state=-1;
	private ArrayList<Button> buttons;
	private ArrayList<Integer> options;

	public void addButton(Button b, int option){
		buttons.add(b);
		options.add(option);
		if (state < 0) {
			state = option;
			b.active = true;
		}
	}

	public SelectorMenu(Element p){
		super(p);
		buttons = new ArrayList<Button>();
		options = new ArrayList<Integer>();
	}

	public int getState(){
		return state;
	}
	
	public ButtonFace getButtonFace(){
		return buttons.get(options.indexOf(state)).face;
	}

	public void setState(int newState){
		buttons.get(options.indexOf(state)).active = false;
		state = newState;
		buttons.get(options.indexOf(state)).active = true;
	}

	void _draw(){
		Iterator<Button> i = buttons.iterator();
		while(i.hasNext()) {
			i.next().draw();
		}
	}

	public void click(int x, int y, int mButton) {
		Iterator<Button> i = buttons.iterator();
		Button b;
		while(i.hasNext()) {
			b = i.next();
			if (b.hover(x, y)) {
				b.onClick(mButton);
				if (mButton == 0) {
					buttons.get(options.indexOf(state)).active = false;
					state = options.get(buttons.indexOf(b));
					b.active = true;
				}
			}
		}
	}

	public boolean hover(int x, int y){
		Iterator<Button> i = buttons.iterator();
		boolean res = false;
		while (i.hasNext()) {
			if (i.next().hover(x, y)) {
				res = true;
			}
		}
		return res;
	}
}
