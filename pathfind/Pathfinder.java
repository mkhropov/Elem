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

	public ArrayList<Block> spreadPath(Creature c, ArrayList<Block> b, Condition cond){
		ArrayList<Block> res = new ArrayList<>();
        ArrayList<Block> near;
        int i, j, l;
        Block m, n;
        float D, Dn, Dt;
		boolean found = false;
        nextLayer = b;
		for (Block t: nextLayer)
			d[t.x][t.y][t.z] = 0.f;
        while ((!found) && (t<1000) && (nextLayer.size()>0)){
            currLayer = nextLayer;
            nextLayer = new ArrayList<>();
            l = currLayer.size();
            for (i=0; i<l; ++i){
                m = currLayer.get(i);
				if (cond.suits(m)){
					found = true;
					res.add(m);
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
		return res;
	}


    public Stack<Action> getPath(Creature c, Block b, Condition c1, Condition c2) {
        ArrayList<Block> b1, b2;

        clear();
		//find c1-point around b
		b2 = new ArrayList<>(); b2.add(b);
		b1 = spreadPath(C, b2, c1);
		if (b1.isEmpty()){
			System.out.println("Suitable c1-point not found");
			return null;
		} else {
			 System.out.println(b1.size()+" c1-points found");
		}

		clear();
		//find c2-point around b1
		b2 = spreadPath(c, b1, c2);
		if (b2.isEmpty()){
			System.out.println("Suitable c2-point not found");
			return null;
		} else {
			System.out.println(b2.size()+" c2-points found");
		}

		return backtrack(c, b2.get(0));
	}

	public Stack<Action> backtrack(Creature c, Block b){
        int i, j, l, t;
		Block m, k, n; m = b;
        ArrayList<Block> near;
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
