package core;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import com.google.gson.Gson;
import graphics.Model;

import utils.JSONList;

public class Data{
	public static JSONList<Model> Models;
	static Data instance = null;
	
	private Data(){
		Models = new JSONList(new Model());
		Models.load("res/models/");
		Models.initialize();
		Models.setDefault("cube");
	}
	
	public static Data getInstance() {
		if (instance == null){
			instance = new Data();
		}
		return instance;
	}
}
