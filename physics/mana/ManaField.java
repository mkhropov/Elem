package physics.mana;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import stereometry.*;
import world.*;
import java.util.Random;

public class ManaField {
	public ArrayList<ManaSource> sources;
	public ArrayList<ManaDrain> drains;
	public int xsize, ysize, zsize;
	public Vector[][][] field;
	public ManaParticle[] particles;
	public final static int MF_PARTICLES = 10000;
	Random gen;

	private static ManaField instance = null;

	public static ManaField getInstance(){
		if (instance == null)
			instance = new ManaField();
		return instance;
	}

	private ManaField(){
		this.xsize = World.DEFAULT_XSIZE;
		this.ysize = World.DEFAULT_YSIZE;
		this.zsize = World.DEFAULT_ZSIZE;
		this.field = new Vector[xsize][ysize][zsize];
		this.sources = new ArrayList<>(0);
		this.drains = new ArrayList<>(0);
		this.particles = new ManaParticle[MF_PARTICLES];
		for (int i=0; i<MF_PARTICLES; ++i)
			particles[i] = new ManaParticle();
		gen = new Random();
		this.recalc();
	}

	public void recalc(){
		Vector v = new Vector(0., 0., 0.);
		Point p;
		int S = sources.size();
		System.out.printf("S = %d\n", S);
		for (int i=0; i<xsize; i++)
			for (int j=0; j<ysize; j++)
				for (int k=0; k<zsize; k++){
					p = new Point(i, j, k);
					v.toZero();
					for (int t=0; t<S; t++)
						v.add(sources.get(t).getField(p), 1.);
//					v.normalize();
					v.scale(.2);
					field[i][j][k] = new Vector(v);
//					System.out.printf("%f %f %f ~ %f %f %f\n", p.x, p.y, p.z, v.x, v.y, v.z);
				}
	}

	public void addSource(ManaSource s){
		sources.add(s);
		recalc();
	}

	public void addDrain(ManaDrain d){
		drains.add(d);
		for (int t=0; t<sources.size(); ++t)
			sources.get(t).deplete(new Point(d));
		recalc();
	}

	public void iterate(long dt){
//		System.out.printf("Iter ");
		for (ManaParticle p : particles)
			if (!p.used()){
//				System.out.printf("- not used ");
				if (ManaParticle.inUse < MF_PARTICLES)
					p.set(new Point(gen.nextInt(xsize)+gen.nextDouble(),
									gen.nextInt(ysize)+gen.nextDouble(),
									gen.nextInt(zsize)+gen.nextDouble()), 3000);
			} else
				p.iterate(dt);
	}

	public void draw(){
//		GL11.glEnable(GL11.COLOR_MATERIAL);
		GL11.glPointSize(2);
		for (ManaParticle p : particles)
			if (p.used())
				p.draw();
	}
}
