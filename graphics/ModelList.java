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
		Model m;
		Renderer r = Renderer.getInstance();
		list = new ArrayList<>();

		m = ModelLoader.getInstance().load("res/wisp.obj", "elem");
		m.prepare();
		m.s0 = .5f; m.s1 = .5f; m.s2 = .5f;
		m.a0 = .5f; m.a1 = .5f; m.a2 = .5f;
		add(m);

		m = ModelLoader.getInstance().load("res/box.obj", "boulder");
		m.prepare();
		m.s0 = .2f; m.s1 = .2f; m.s2 = .2f;
		m.a0 = .5f; m.a1 = .5f; m.a2 = .2f;
		add(m);

		m = ModelLoader.getInstance().load("res/box.obj", "cursor");
		m.prepare();
		m.s0 = .6f; m.s1 = .6f; m.s2 = .6f;
		m.a0 = .55f; m.a1 = .55f; m.a2 = .55f;
		add(m);

		m = ModelLoader.getInstance().load("res/box.obj", "cube");
		m.prepare();
		m.s0 = .501f; m.s1 = .501f; m.s2 = .501f;
		m.a0 = .5005f; m.a1 = .5005f; m.a2 = .5005f;
		add(m);

		m = ModelLoader.getInstance().load("res/box.obj", "block");
		m.prepare();
		m.s0 = .5f; m.s1 = .5f; m.s2 = .5f;
		m.a0 = .5f; m.a1 = .5f; m.a2 = .5f;
		add(m);

		m = ModelLoader.getInstance().load("res/box.obj", "floor");
		m.prepare();
		m.s0 = .5f; m.s1 = .5f; m.s2 = .305f;
		m.a0 = .5f; m.a1 = .5f; m.a2 = -.1f;
		add(m);
	}

	public int findId(String name) {
		int res = 0;
		for (int i=0; i<list.size(); i++){
			if (list.get(i).name.equalsIgnoreCase(name)){
				res = i;
			}
		}
		return res;
	}

	public final void add(Model m){
		list.add(m);
	}

	public Model get(int id){
		return list.get(id);
	}

	public Model get(String name){
		return list.get(findId(name));
	}
}
