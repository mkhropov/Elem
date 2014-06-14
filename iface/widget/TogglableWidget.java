package iface.widget;

import java.util.ArrayList;

public class TogglableWidget extends Widget {

	public ArrayList<Widget> slave;

	public boolean active;

	public TogglableWidget() {
		super();

		this.active = false;
		this.slave = new ArrayList<>();
	}

	public void addSlave(Widget w) {
		slave.add(w);
	}

	public void setActive(boolean active) {
		this.active = active;
		for (Widget w: slave) {
			w.setVisible(active);
		}
	}
}
