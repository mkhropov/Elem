package pathfind;
import java.util.ArrayList;
import java.util.Stack;
import world.*;
import creature.*;

public class Pathfinder {
    int xsize;
    int ysize;
    int zsize;
    float[][][] d;
	int t; //search depth
	Creature C; //virtual creature which can go anywhere
    ArrayList<Block> currLayer;
    ArrayList<Block> nextLayer;
    static float dist[] = new float[]
    { 1.f, 1.f, 1.f, 1.f, 1.f, 1.f,
      1.414f, 1.414f, 1.414f, 1.414f, 1.414f, 1.414f,
      1.414f, 1.414f, 1.414f, 1.414f, 1.414f, 1.414f,
      1.73f, 1.73f, 1.73f, 1.73f,
      1.73f, 1.73f, 1.73f, 1.73f
    };

	private static Pathfinder instance = null;

	public static Pathfinder getInstance(){
		if (instance == null)
			instance = new Pathfinder();
		return instance;
	}

    private Pathfinder(){
        this.xsize = World.DEFAULT_XSIZE;
        this.ysize = World.DEFAULT_YSIZE;
        this.zsize = World.DEFAULT_ZSIZE;

        d = new float[xsize][ysize][zsize];
		C = new Creature();
		t = 0;
    }

    public void clear(){
        for (int i=0; i<xsize; ++i)
            for (int j=0; j<ysize; ++j)
                for (int k=0; k<zsize; ++k)
                    d[i][j][k] = -1.f;
		t = 0;
    }

   /* double dist(Block b1, Block b2){
        return Math.sqrt((b1.x-b2.x)*(b1.x-b2.x)+
                         (b1.y-b2.y)*(b1.y-b2.y)+
                         (b1.z-b2.z)*(b1.z-b2.z));
    }*/

	public Block spreadPath(Creature c, Block b, Condition cond){
        ArrayList<Block> near;
        int i, j, l;
        Block m, n, k = null;
        float D, Dn, Dt;
		boolean found = false;
        nextLayer = new ArrayList<>(1);
        nextLayer.add(b);
        d[b.x][b.y][b.z] = 0.f;
        while ((!found) && (t<1000)){
            currLayer = nextLayer;
            nextLayer = new ArrayList<>(currLayer.size());
            l = currLayer.size();
            for (i=0; i<l; ++i){
                m = currLayer.get(i);
				if (cond.suits(m)){
					found = true;
					k = m;
				}
                D = d[m.x][m.y][m.z];
                near = m.nearest();
                for (j=0; j<near.size(); ++j){
                    n = near.get(j);
                    if (n==null) continue;
                    Dn = D+dist[j];
                    Dt = d[n.x][n.y][n.z];
                    if (c.canMove(m, n) && ((Dt<0.f)||(Dt > Dn))){
                        d[n.x][n.y][n.z] = Dn;
                        nextLayer.add(n);
                    }
                }
            }
            t++;
        }
		if (found)
			return k;
		else
			return null;
	}


    public Stack<Action> getPath(Creature c, Block b, Condition c1, Condition c2) {
        Block b1, b2 = null;

        clear();
		//find c1-point around b
		b1 = spreadPath(C, b, c1);
		if (b1 == null){
			System.out.println("Suitable c1-point not found");
			return null;
		} else {
			 System.out.println("c1-point ("+b1.x+","+b1.y+","+b1.z+" found");
		}

		clear();
		//find c2-point around b1
		b2 = spreadPath(c, b1, c2);
		if (b2 == null){
			System.out.println("Suitable c2-point not found");
			return null;
		} else {
			System.out.println("c2-point ("+b2.x+","+b2.y+","+b2.z+" found");
		}

		return backtrack(c, b2);
	}

	public Stack<Action> backtrack(Creature c, Block b){
        int i, j, l, t;
		Block m, k, n; m = b;
        ArrayList<Block> near;
		boolean found = false;
		float D = d[b.x][b.y][b.z];
		float Dn;
		Stack<Action> q = new Stack<>();//(int)(D/2.f));
        q.push(new Action(Action.ACTION_MOVE, m.x, m.y, m.z));
        while(D>0.5f){
            near = m.nearest();
            k = m;
            for (j=0; j<near.size(); ++j){
                n = near.get(j);
                if(n==null) continue;
                Dn = d[n.x][n.y][n.z];
                if ((Dn>-0.5f) && (Dn < D) && c.canMove(n, m)){
                    k = n;
                    D = Dn;
                }
            }
            m = k;
            q.push(new Action(Action.ACTION_MOVE, m.x, m.y, m.z));
            D = d[m.x][m.y][m.z];
        }
        return q;
    }
}
