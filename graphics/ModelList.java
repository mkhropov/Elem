package graphics;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import com.google.gson.Gson;

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
		Gson gson = new Gson();
		Model m;
		Renderer r = Renderer.getInstance();
		list = new ArrayList<>();
		File modelFolder = new File("res/models/");
		for (File f: modelFolder.listFiles())
			if (f.isFile() && f.getName().matches(".*\\.json")) {
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(f));
				} catch (java.io.FileNotFoundException e) {
					return;
				}
				String s, text = "";
				try {
					while ((s = br.readLine()) != null)
						text += s+"\n";
				} catch (java.io.IOException e) {
					return;
				}
				/* FIXME br.close()? */
				m = gson.fromJson(text, Model.class);
				ModelLoader.getInstance().load(m);
				m.prepare();
				add(m);
			}
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
