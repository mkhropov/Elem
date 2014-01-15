package graphics;

import java.util.ArrayList;

public class GSList {
	private ArrayList<GraphicalSurface> list;

	private static GSList instance = null;
	public static GSList getInstance() {
		if (instance == null){
			instance = new GSList();
		}
		return instance;
	}

	private GSList() {
		list = new ArrayList<GraphicalSurface>();
		add("void", 0.5);
		add("stone", 0.5);
		add("earth", 0.5);
	}

	public void add(String Name, double Rand) {
		this.add(new GraphicalSurface(Name, Rand));
	}

	public void add(GraphicalSurface gs){
		list.add(gs);
	}

	public GraphicalSurface get(int id){
		return list.get(id);
	}
}
