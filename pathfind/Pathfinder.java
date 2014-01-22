package pathfind;
import java.util.ArrayList;
import java.util.Stack;
import world.*;
import creature.*;

public class Pathfinder {
    int xsize;
    int ysize;
    int zsize;
    double[][][] d;
    ArrayList<Block> currLayer;
    ArrayList<Block> nextLayer;
    static double dist[] = new double[]
    { 1., 1., 1., 1., 1., 1.,
      1.414, 1.414, 1.414, 1.414, 1.414, 1.414,
      1.414, 1.414, 1.414, 1.414, 1.414, 1.414,
      1.73, 1.73, 1.73, 1.73,
      1.73, 1.73, 1.73, 1.73
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

        d = new double[xsize][ysize][zsize];
    }

    public void clear(){
        for (int i=0; i<xsize; ++i)
            for (int j=0; j<ysize; ++j)
                for (int k=0; k<zsize; ++k)
                    d[i][j][k] = -1.d;
    }

   /* double dist(Block b1, Block b2){
        return Math.sqrt((b1.x-b2.x)*(b1.x-b2.x)+
                         (b1.y-b2.y)*(b1.y-b2.y)+
                         (b1.z-b2.z)*(b1.z-b2.z));
    }*/

    public Stack<Block> getPath(Creature c, Block b, Condition cond) {
        ArrayList<Block> near;
        int i, j, l, t;
        Block m, n, k = null;
        Stack<Block> q = new Stack<>();
        double D, Dn, Dt;
		boolean found = false;

        clear();
        nextLayer = new ArrayList<>(1);
        nextLayer.add(b);
        d[b.x][b.y][b.z] = 0.d;
        t = 0;

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
                    if (c.canMove(m, n) && ((Dt<0.d)||(Dt > Dn))){
                        d[n.x][n.y][n.z] = Dn;
                        nextLayer.add(n);
                    }
                }
            }
            t++;
        }
        if (k == null) {
            System.out.printf("Path timed out\n");
            return null;
        } else
			 m = k;
        q.push(m);
        D = d[m.x][m.y][m.z];
        while(D>0.5d){
            near = m.nearest();
            k = m;
            for (j=0; j<near.size(); ++j){
                n = near.get(j);
                if(n==null) continue;
                Dn = d[n.x][n.y][n.z];
                if ((Dn>-0.5) && (Dn < D) && c.canMove(n, m)){
                    k = n;
                    D = Dn;
                }
            }
            m = k;
            q.push(m);
            D = d[m.x][m.y][m.z];
        }
        return q;
    }
}
