package iface;

import iface.widget.*;
import creature.Elem;

public class StatsElem extends Frame {

	public StatsElem(int X, int Y, Elem e) {
		super();

		VBox vb = new VBox();
		this.add(vb);
		Padding p;
		HBox hb;

		Frame f = new Frame();
		f.add(new Label("Void elemental"));
		vb.add(f);

		f = new Frame();
		vb.add(f);
		vb = new VBox();
		f.add(vb);
		hb = new HBox();
		hb.add(new Label("X: "));
		hb.add(new TrackingLabel(e.p, "x", 5));
		vb.add(hb);

		hb = new HBox();
		hb.add(new Label("Y: "));
		hb.add(new TrackingLabel(e.p, "y", 5));
		vb.add(hb);

		hb = new HBox();
		hb.add(new Label("Z: "));
		hb.add(new TrackingLabel(e.p, "z", 5));
		vb.add(hb);

		crop();
		compile(X, Y, minX, minY);
	}
}
