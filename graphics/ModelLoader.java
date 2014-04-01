package graphics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class ModelLoader {

	private static ModelLoader instance = null;

	public static ModelLoader getInstance(){
		if (instance == null)
			instance = new ModelLoader();
		return instance;
	}

	private ModelLoader(){
	}

	public Model load(String fname, String name)/* throws IOException*/ {
		Model m = new Model(name);
		ArrayList<Float> t_v = new ArrayList<>(0);
		ArrayList<Float> t_t = new ArrayList<>(0);
		ArrayList<Float> t_n = new ArrayList<>(0);
		ArrayList<Integer> i_v = new ArrayList<>(0);
		ArrayList<Integer> i_t = new ArrayList<>(0);
		ArrayList<Integer> i_n = new ArrayList<>(0);
		BufferedReader f;
		try {
			f = new BufferedReader(new FileReader(fname));
		} catch (java.io.FileNotFoundException e) {
			return null;
		}
		String s=""; String[] w;
		while (s != null){
            try {
                s = f.readLine();
            } catch (java.io.IOException e){
                return null;
            }
			if ((s == null)||(s==""))
				continue;
//			else
//				System.out.print(s+"\n");
			s = s.replace('/', ' ');
			s = s.replace("  ", " ");
			w = s.split(" ");
			switch (w[0]){
				case "v":
//					System.out.print(w[1]+w[2]+w[3]+"\n");
					t_v.add(Float.parseFloat(w[1]));
					t_v.add(Float.parseFloat(w[2]));
					t_v.add(Float.parseFloat(w[3]));
					break;
				case "vt":
					t_t.add(Float.parseFloat(w[1]));
					t_t.add(Float.parseFloat(w[2]));
					break;
				case "vn":
					t_n.add(Float.parseFloat(w[1]));
					t_n.add(Float.parseFloat(w[2]));
					t_n.add(Float.parseFloat(w[3]));
					break;
				case "f":
					i_v.add(Integer.parseInt(w[1])-1);
					i_t.add(Integer.parseInt(w[2])-1);
					i_n.add(Integer.parseInt(w[3])-1);
					i_v.add(Integer.parseInt(w[4])-1);
					i_t.add(Integer.parseInt(w[5])-1);
					i_n.add(Integer.parseInt(w[6])-1);
					i_v.add(Integer.parseInt(w[7])-1);
					i_t.add(Integer.parseInt(w[8])-1);
					i_n.add(Integer.parseInt(w[9])-1);
					break;
				default:
					break;
			}
		}
/*		System.out.print(t_v);
		System.out.print(t_t);
		System.out.print(t_n);
		System.out.print(i_v);
		System.out.print(i_t);
		System.out.print(i_n);*/
		ArrayList<Long> lm = new ArrayList<>();
	//	int v_l = t_v.size();
		int t_l = t_t.size()/2;
		int n_l = t_n.size()/3;
		int t = 0;
		Long key;
		int val;
		m.i = BufferUtils.createIntBuffer(i_v.size());
		for (int i=0; i<i_v.size(); ++i){
			key = ((long)i_v.get(i))*t_l*n_l +
				  i_t.get(i)*n_l +
				  i_n.get(i);
			val = lm.indexOf(key);
			if (val < 0){
				lm.add(key);
				val = t++;
			}
			m.i.put(val);
		}
//		System.out.print("\nt_l="+t_l+", n_l="+n_l+"\n"+lm);
		m.v = BufferUtils.createFloatBuffer(3*t);
		m.t = BufferUtils.createFloatBuffer(2*t);
		m.n = BufferUtils.createFloatBuffer(3*t);
		int ind_n, ind_t, ind_v;
		for (int i=0; i<t; i++){
			key = lm.get(i);
			ind_n = (int)((key % (t_l * n_l))%n_l);
			ind_t = (int)((key / n_l) % (t_l));
			ind_v = (int) (key / (t_l * n_l));
//			System.out.print("\n"+key+" = "+ind_v+" "+ind_t+" "+ind_n);
			m.v.put(t_v.get(3*ind_v+0));
			m.v.put(t_v.get(3*ind_v+1));
			m.v.put(t_v.get(3*ind_v+2));
			m.t.put(t_t.get(2*ind_t+0));
			m.t.put(t_t.get(2*ind_t+1));
			m.n.put(t_n.get(3*ind_n+0));
			m.n.put(t_n.get(3*ind_n+1));
			m.n.put(t_n.get(3*ind_n+2));
		}

		m.initialized = true;
		return m;
	}
}
