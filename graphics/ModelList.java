package graphics;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import com.google.gson.Gson;

import utils.JSONList;

public class ModelList extends JSONList<Model>{
	static ModelList instance = null;
	
	private ModelList(Model sample){
		super(sample);
	}
	
	public static ModelList getInstance() {
		if (instance == null){
			instance = new ModelList(new Model());
			instance.load("res/models/");
			instance.initialize();
			instance.setDefault("cube");
		}
		return instance;
	}
}
