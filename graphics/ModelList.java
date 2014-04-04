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
		list = new ArrayList<Model>();

		m = ModelLoader.getInstance().load("res/box.obj", "elem");
		m.prepare(r.shaders[Renderer.SHADER_BASIC]);
		m.scale = .3f; m.a0 = .5f; m.a1 = .5f; m.a2 = .3f;
		add(m);

		m = ModelLoader.getInstance().load("res/box.obj", "boulder");
		m.prepare(r.shaders[Renderer.SHADER_BASIC]);
		m.scale = .2f; m.a0 = .5f; m.a1 = .5f; m.a2 = .2f;
		add(m);

		m = ModelLoader.getInstance().load("res/box.obj", "cursor");
		m.prepare(r.shaders[Renderer.SHADER_GHOST]);
		m.scale = .6f; m.a0 = .55f; m.a1 = .55f; m.a2 = .55f;
		add(m);

		m = ModelLoader.getInstance().load("res/box.obj", "cube");
		m.prepare(r.shaders[Renderer.SHADER_GHOST]);
		m.scale = .51f; m.a0 = .505f; m.a1 = .505f; m.a2 = .505f;
		add(m);

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
