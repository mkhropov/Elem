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
		add("marble", 0.5);
		add("granite", 0.5);
		add("red_granite", 0.5);
		add("textures", 0.0);
		add("selection", 0.0);
		add("IconDig", 0.0);
		add("IconBuild", 0.0);
	}

	public int findId(String name) {
		int res = 0;
		for (int i=0; i<list.size(); i++){
			if (list.get(i).name == name){
				res = i;
			}
		}
		return res;
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

	public GraphicalSurface get(String name){
		return list.get(findId(name));
	}
}
