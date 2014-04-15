package graphics.shaders;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

import stereometry.Point;

public class Matrix4 {
	public float[] val;
	public static final int SIZE = 4;

	public Matrix4(){
		val = new float[SIZE*SIZE];
	}

	public Matrix4(Matrix4 m){
		this();
		for (int i=0; i<SIZE*SIZE; ++i)
			this.val[i] = m.val[i];
	}

	public float[] value(){
		return val;
	}

	public FloatBuffer fb(){
		FloatBuffer res = BufferUtils.createFloatBuffer(16);
		res.put(val);
		res.rewind();
		return res;
	}

	public static Matrix4 identity(){
		Matrix4 m = new Matrix4();
		for (int i=0; i<SIZE; ++i)
			m.val[SIZE*i + i] = 1.f;
		return m;
	}

	public static Matrix4 persp(float[] s){
		assert(s.length == SIZE-1);
		Matrix4 m = Matrix4.identity();
		for (int i=0; i<(SIZE-1); ++i)
			m.val[SIZE*i + SIZE-1] = s[i];
		return m;
	}

	public static Matrix4 scale(float[] s){
		assert(s.length == SIZE-1);
		Matrix4 m = Matrix4.identity();
		for (int i=0; i<(SIZE-1); ++i)
			m.val[SIZE*i + i] = s[i];
		return m;
	}

	public static Matrix4 shift(float[] s){
		assert(s.length == SIZE-1);
		Matrix4 m = Matrix4.identity();
		for (int i=0; i<(SIZE-1); ++i)
			m.val[SIZE*(SIZE-1) + i] = s[i];
		return m;
	}

	public static Matrix4 rot(float alpha, int ind){
		assert((ind>=0) && (ind<SIZE-1));
		Matrix4 m = Matrix4.identity();
		m.val[((ind+1)%(SIZE-1))*SIZE+((ind+1)%(SIZE-1))] = (float)Math.cos(alpha);
		m.val[((ind+2)%(SIZE-1))*SIZE+((ind+2)%(SIZE-1))] = (float)Math.cos(alpha);
		m.val[((ind+1)%(SIZE-1))*SIZE+((ind+2)%(SIZE-1))] = (float)Math.sin(alpha);
		m.val[((ind+2)%(SIZE-1))*SIZE+((ind+1)%(SIZE-1))] = (float)-Math.sin(alpha);
		return m;
	}

	public static Matrix4 lookAt(float x, float y, float z, float tx, float ty, float tz){
		Matrix4 m = identity();
		m = m.multR(shift(new float[]{-tx, -ty, -tz}));
		if (!((tx==x)&&(ty==y))){
			m = m.multR(rot((float)(Math.signum(tx-x)*Math.acos((ty-y)/Math.sqrt((tx-x)*(tx-x)+(ty-y)*(ty-y)))), 2));
		}
		m = m.multR(rot((float)(Math.acos((tz-z)/Math.sqrt(((tx-x)*(tx-x)+(ty-y)*(ty-y)+(tz-z)*(tz-z))))), 0));
		return m;
	}

	public Matrix4 multR(Matrix4 m){
		Matrix4 res = new Matrix4();
		for (int i=0; i<SIZE; ++i)
			for (int j=0; j<SIZE; ++j)
				for (int k=0; k<SIZE; ++k)
					res.val[SIZE*i+j] += this.val[SIZE*i+k]*m.val[SIZE*k+j];
		return res;
	}

	public float[] multR(Point p){
		float[] res = new float[3];
		for (int i=0; i<3; ++i)
			res[i] = (float)(p.x*val[SIZE*0+i]+
							 p.y*val[SIZE*1+i]+
							 p.z*val[SIZE*2+i]+
							 val[SIZE*3+i]);
		return res;
	}

	public float[] multR(double x, double y, double z){
		float[] res = new float[3];
		for (int i=0; i<3; ++i)
			res[i] = (float)(x*val[SIZE*0+i]+
							 y*val[SIZE*1+i]+
							 z*val[SIZE*2+i]+
							 val[SIZE*3+i]);
		return res;
	}

	public Matrix4 multL(Matrix4 m){
		return m.multR(this);
	}

	public void print(){
		for (int i=0; i<SIZE; ++i){
			for (int j=0; j<SIZE; ++j)
				System.out.print(val[i*SIZE+j]+" ");
			System.out.print("\n");
		}
		System.out.print("\n");
	}
}
