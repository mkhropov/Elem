package pathfind;

import creature.Action;
import creature.Creature;
import creature.Elem;
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

   /* double dist(Block b1, Block b2){
        return Math.sqrt((b1.x-b2.x)*(b1.x-b2.x)+
                         (b1.y-b2.y)*(b1.y-b2.y)+
                         (b1.z-b2.z)*(b1.z-b2.z));
    }*/
	
	public boolean processBuildOrder(Order o){
		Elem e = new Elem();
		Block b = o.b;
		Condition c1 = new ConditionReach(b, e);
		Condition c2 = new ConditionItem(o.itemCondition);
		Stack<Action> path = getPath(e, b, c1, c2);
		if (path==null){
			o.declined = true;
//			System.out.println("Build order "+o+" declined at buildable search");
			return false;
		}

		o.path.add(0, new Action(Action.ACTION_BUILD, b.x, b.y, b.z, o.f, o.d, o.m));
		b = path.remove(0).b;
		o.path.addAll(0, path);
		int n = b.amount(o.itemCondition); //how many it-suitable objects did we find?
		System.out.println("found "+n+" items out of "+o.N);
		o.marked.addAll(b.markItems(o.N, o.itemCondition));
		for (int t=0; t<Math.min(n, o.N); ++t)
			o.path.add(0, new Action(Action.ACTION_TAKE, o.itemCondition));
		while (n < o.N) {
			c1 = new ConditionBeIn(b);
			c2 = new ConditionItem(o.itemCondition);

			path = getPath(e, b, c1, c2);
			if (path==null) {
				o.declined = true;
				o.unmarkAll();
				return false;
			}
			b = path.remove(0).b;
			o.path.addAll(0, path);
			for (int t=0; t<Math.min(b.amount(o.itemCondition), o.N-n); ++t)
				o.path.add(0, new Action(Action.ACTION_TAKE, o.itemCondition));
				n += b.amount(o.itemCondition);
				System.out.println("found "+n+" items out of "+o.N);
				o.marked.addAll(b.markItems(o.N-n, o.itemCondition));
			}

			c1 = new ConditionBeIn(b);
			c2 = new ConditionWorker(o);
			path = getPath(e, b, c1, c2);
			if (path==null){
				o.declined = true;
//				System.out.println("Build order "+o+" declined at worker search");
				o.unmarkAll();
				return false;
			}
			ArrayList<Creature> candidates = World.getInstance().getCreature(path.remove(0).b);
			o.path.addAll(0, path);
			for (Creature c: candidates){
				if (c.capableOf(o)) {
					c.owner.setOrderAssigned(o, c);
						return true;
					}
				}
			return false;
	}
	
	public boolean processDigOrder(Order o) {
		Elem e = new Elem();
		Block b = o.b;
		Condition c1 = new ConditionReach(b, e);
		Condition c2 = new ConditionWorker(o);
		Stack<Action> path = getPath(e, b, c1, c2);
		if (path==null){
			o.declined = true;
//			System.out.println("Dig order "+o+" declined at worker search");
			return false;
		}
		ArrayList<Creature> candidates = World.getInstance().getCreature(path.remove(0).b);
		o.path.add(0, new Action(Action.ACTION_DIG, b.x, b.y, b.z, o.f, o.d));
		o.path.addAll(0, path);
		for (Creature c: candidates){
			if (c.capableOf(o)) {
				c.owner.setOrderAssigned(o, c);
				return true;
			}
		}
		return false;
	}
	
	public boolean processOrder(Order o){
		if (o.taken || o.declined)
			return false;
		o.path.clear();
		switch (o.type) {
		case (Order.ORDER_BUILD):
			return processBuildOrder(o);
		case (Order.ORDER_DIG):
			return processDigOrder(o);
		default:
			return false;
		}
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
