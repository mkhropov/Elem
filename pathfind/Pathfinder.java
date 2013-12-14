package pathfind;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import world.*;
import creature.*;

public class Pathfinder {
    int xsize;
    int ysize;
    int zsize;
    World w;
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

    public Pathfinder(World w){
        this.xsize = w.xsize;
        this.ysize = w.ysize;
        this.zsize = w.zsize;
        this.w = w;
        
        d = new double[w.xsize][w.ysize][w.zsize];
    }

    public void clear(){
        for (int i=0; i<xsize; ++i)
            for (int j=0; j<ysize; ++j)
                for (int k=0; k<zsize; ++k)
                    d[i][j][k] = -1.d;
    }

    double dist(Block b1, Block b2){
        return Math.sqrt((b1.x-b2.x)*(b1.x-b2.x)+
                         (b1.y-b2.y)*(b1.y-b2.y)+
                         (b1.z-b2.z)*(b1.z-b2.z));
    }

    public Stack<Block> getPath(Creature c, Block b1, Block b2) {
        int i, j, l;
        double D, Dn;
        Block m, n, k;
        ArrayList<Block> near;
        clear();
        nextLayer = new ArrayList<>(1);
        nextLayer.add(b1);
        d[b1.x][b1.y][b1.z] = 0.d;
        while (d[b2.x][b2.y][b2.z] < 0.d){
            currLayer = nextLayer;
            nextLayer = new ArrayList<>(currLayer.size());
            l = currLayer.size();
            for (i=0; i<l; ++i){
                m = currLayer.get(i);
                D = d[m.x][m.y][m.z];
                near = m.nearest(w);
                for (j=0; j<near.size(); ++j){
                    n = near.get(j);
                    Dn = D+dist(m, n);
                    if (c.canMove(m, n) && ((d[n.x][n.y][n.z]<0.d)||(d[n.x][n.y][n.z] > Dn))){
                        d[n.x][n.y][n.z] = Dn;
                        nextLayer.add(n);
                    }
                }
            }
        }
        m = b2;
        Stack<Block> q = new Stack<>();
        q.push(m);
        while(dist(m, b1)>0.5d){
            near = m.nearest(w);
            D = d[m.x][m.y][m.z];
            k = m;
            for (j=0; j<near.size(); ++j){
                n = near.get(j);
                Dn = d[n.x][n.y][n.z];
                if ((Dn>-0.5) && (Dn < D)){
                    k = n;
                    D = Dn;
                }
            }
            m = k;
            q.push(m);
        }
        return q;
    }
}
