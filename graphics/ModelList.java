package graphics;

import java.util.ArrayList;

public class ModelList {
	private ArrayList<Model> list;

	private static ModelList instance = null;
	public static ModelList getInstance() {
		if (instance == null){
			instance = new ModelList();
		}
		return instance;
	}

	private ModelList() {
		list = new ArrayList<Model>();
		add(new graphics.models.Void());
		add(new graphics.models.Cube());
		add(new graphics.models.Boulder());
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

	public void add(Model m){
		list.add(m);
	}

	public Model get(int id){
		return list.get(id);
	}
}
