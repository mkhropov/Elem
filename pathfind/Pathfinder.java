package pathfind;

import creature.Action;
import creature.Creature;
import creature.Elem;
import item.Inventory;
import item.ItemReservation;
import java.util.ArrayList;
import java.util.Stack;
import player.Order;
import world.Block;
import world.World;

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

	public void resetDepth(){
		t = 0;
	}

	public int getDepth(){
		return t;
	}

    public void clear(){
        for (int i=0; i<xsize; ++i)
            for (int j=0; j<ysize; ++j)
                for (int k=0; k<zsize; ++k)
                    d[i][j][k] = -1.f;
    }

	public ArrayList<Block> spreadReach(Creature c, ArrayList<Block> start, Condition cond){
		ArrayList<Block> res = new ArrayList<>();
        ArrayList<Block> near;
		Block m, n;
		int i, j, l;
		int tmp = 0;
		boolean found = false;
        nextLayer = start;

		while (!found && tmp < 2) {
			currLayer = nextLayer;
			nextLayer = new ArrayList<>();
			l = currLayer.size();
			for (i = 0; i < l; ++i) {
				m = currLayer.get(i);
				if (cond.suits(m)) {
					res.add(m);
					found = true;
				}
				near = m.nearest();
				for (j = 0; j < near.size(); ++j) {
					n = near.get(j);
					if (n == null) {
						continue;
					}
					if (c.canReach(n, m)) {
						nextLayer.add(n);
					}
				}
			}
			t++; tmp++;
		}
		return res;
	}

	public ArrayList<Block> spreadMove(Creature c, ArrayList<Block> start, Condition cond){
		ArrayList<Block> res = new ArrayList<>();
        ArrayList<Block> near;
        int i, j, l;
        Block m, n;
        float D, Dn, Dt;
        int tmp = 0;
		boolean found = false;
        nextLayer = start;
		for (Block b: nextLayer)
			d[b.x][b.y][b.z] = 0.f;
        while ((!found) && (tmp<100) && (nextLayer.size()>0)){
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
            t++; tmp++;
        }
		return res;
	}


    public Stack<Action> getPath(Creature c, Block b, Condition c1, Condition c2) {
        ArrayList<Block> b1, b2;

		//find c1-point around b
		b2 = new ArrayList<>(); b2.add(b);
		b1 = spreadReach(C, b2, c1);
		if (b1.isEmpty()){
			//System.out.println("Suitable c1-point not found");
			return null;
		} else {
			 //System.out.println(b1.size()+" c1-points found");
		}

		clear();
		//find c2-point around b1
		b2 = spreadMove(c, b1, c2);
		if (b2.isEmpty()){
			//System.out.println("Suitable c2-point not found");
			return null;
		} else {
			//System.out.println(b2.size()+" c2-points found");
		}

		return backtrack(c, b2.get(0));
	}

	public Stack<Action> backtrack(Creature c, Block b){
        int j, t;
		Block m, k, n; m = b;
        ArrayList<Block> near;
		float D = d[b.x][b.y][b.z];
		float Dn;
		Stack<Action> q = new Stack<>();//(int)(D/2.f));
        q.push(new Action(Action.ACTION_MOVE, (double)m.x, (double)m.y, (double)m.z));
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
