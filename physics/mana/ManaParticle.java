package physics.mana;

import stereometry.*;

import org.lwjgl.opengl.GL11;

import world.Entity;
import world.World;
import iface.Interface;

public class ManaParticle extends Entity {

	Vector speed;
	double ttl; //time to live
	boolean used;
	Point color;
	boolean drawed;

	public static int inUse = 0;

	public ManaParticle(){
		this.used = false;
	}

	public boolean used(){
		return used;
	}

	public void set(Point p, double ttl){
		this.p = new Point(p);
		this.speed = ManaField.getInstance().field[(int)p.x][(int)p.y][(int)p.z];
		this.drawed = World.getInstance().empty((int)p.x, (int)p.y, (int)p.z);
		this.drawed &= ((int)p.z <= Interface.getInstance().current_layer);
		this.ttl = ttl;
		this.used = true;
		ManaParticle.inUse++;
		double hue = Math.sin(p.x+p.y+p.z); hue *= hue;
		this.color = new Point(hue, hue, 1.);
	}

	public void del(){
		this.used = false;
		ManaParticle.inUse--;
	}

	public void iterate(long dT){
//		System.out.printf("P=(%f %f %f), dT=%d\n", p.x, p.y, p.z, dT);
		p.add(speed, dT);
		ttl -= dT;
		if ((ttl<=0) || (!World.getInstance().isIn(p.x, p.y, p.z))){
			del();
		} else {
			speed = ManaField.getInstance().field[(int)p.x][(int)p.y][(int)p.z];
			drawed = World.getInstance().empty((int)p.x, (int)p.y, (int)p.z);
			drawed &= ((int)p.z <= Interface.getInstance().current_layer);
		}
	}


	public void draw(){
		if (!drawed)
			return;
		GL11.glColor3d(color.x, color.y, color.z);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex3d(p.x, p.y, p.z);
		GL11.glEnd();
		/*
		int size = tail.size();
		if (size>1)
			for(int t = size-2; t>=0; --t)
				drawLine(tail.get(t+1), tail.get(t), t/((double)size));*/
	}

}

