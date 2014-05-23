package utils;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class JSONList <E extends Initializable & Named> implements Initializable{
	final private ArrayList<E> list;
	final private E sample;
	final private Gson gson;
	boolean initialized;
	private int defaultId;

	public JSONList(E sample){
		list = new ArrayList<>();
		this.sample = sample;
		gson = new Gson();
		initialized = true;
		defaultId = 0;
	}

	private void loadSingle(File f){
		initialized = false;
		if (f.isFile() && f.getName().matches(".*\\.json")) {
			//System.out.println("Loading "+f.getName());
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(f));
			} catch (java.io.FileNotFoundException e) {
				System.out.println("ERROR: Trying to read missing json file "+f.getName());
				return;
			}
			try {
				String s, text = "";
				while ((s = br.readLine()) != null)
					text += s+"\n";
				add((E)gson.fromJson(text, sample.getClass()));
			} catch (java.io.IOException e) {
				System.out.println("ERROR: Error while reading "+f.getName());
			} finally {
			   try {
				   br.close();
				} catch (java.io.IOException e) {
					System.out.println("ERROR: Error closing file "+f.getName());
					return;
				}	
			}
		}
	}

	public void loadSingle(String FileName){
		File f = new File(FileName);
		loadSingle(f);
	}

	public void load(String FolderName){
		File jsonFolder = new File(FolderName);
		for (File f: jsonFolder.listFiles()){
			this.loadSingle(f);
		}
	}

	@Override
	public void initialize(){
		if (initialized) return;
		for (int i=0; i<list.size(); i++){
			list.get(i).initialize();
		}
	}

	public boolean contains(String name){
		for (int i=0; i<list.size(); i++){
			if (list.get(i).getName().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}

	public boolean contains(E item){
		return contains(item.getName());
	}

	public int getId(String name) {
		int res = defaultId;
		for (int i=0; i<list.size(); i++){
			if (list.get(i).getName().equalsIgnoreCase(name)){
				res = i;
			}
		}
		return res;
	}

	public final void add(E item){
		if (item.getName()==null || item.getName().isEmpty()){
			System.out.println("ERROR trying to add unnamed object");
			return;
		}
		if (contains(item)) {
			System.out.println("WARNING: Trying to add duplicate object : "+item.getName());
			return;
		}
		list.add(item);
	}

	public E get(int id){
		if (id<0 || id>list.size()) {
			System.out.println("WARNING: ID out of bounds : "+id);
		}
		return list.get(id);
	}

	public E get(String name){
		return get(getId(name));
	}

	public void setDefault(int Id){
		defaultId = Id;
	}

	public void setDefault(String name) {
		if (!contains(name)) {
			System.out.println("WARNING: Trying to set default list item that is not on the list : "+name);
			return;
		}
		defaultId = getId(name);
	}

	public void setDefault(E item) {
		if (!contains(item)) add(item);
		setDefault(item.getName());
	}
}
