package iface;

import iface.widget.*;
import creature.Elem;

public class StatsElem extends VBox {

	public StatsElem(int X, int Y, Elem e) {
		super();

		Frame f;
		Padding p;
		HBox hb;
		
		f = new Frame();
		f.add(new Label("Void elemental"));
		this.add(f);
		
		hb = new HBox();
		hb.add(new Label("X: "));
		hb.add(new TrackingLabel(e.p, "x", 5));
		this.add(hb);
		
		hb = new HBox();
		hb.add(new Label("Y: "));
		hb.add(new TrackingLabel(e.p, "y", 5));
		this.add(hb);
		
		hb = new HBox();
		hb.add(new Label("Z: "));
		hb.add(new TrackingLabel(e.p, "z", 5));
		this.add(hb);
		
		crop();
		compile(X, Y, 100, 100);
	}
}
