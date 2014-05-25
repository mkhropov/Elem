package utils;

import java.util.Date;

public class Random extends java.util.Random {
	
	private static Random instance = null;
	
	private Random() {
		super((new Date()).getTime());
	}
	
	public static Random getInstance() {
		if (instance ==null) {
			instance = new Random();
		}
		return instance;
	}
	
	public double avg(double val[]) {
		return val[0]+nextDouble()*(val[1]-val[0]);
	}
	
	public double avg(int val[]) {
		return val[0]+nextDouble()*(val[1]-val[0]);
	}

	public int getWeighted(double w[]){
		int N = w.length;
		assert(N > 0);
		int i;
		double S = 0.;
		for (i=0; i<N; ++i)
			S += w[i];
		double t = nextDouble()*S;
		i = 0; S = 0.;
		while (S < t && i < N)
			S += w[i++];
		assert(i < N);
		return i;
	}
}