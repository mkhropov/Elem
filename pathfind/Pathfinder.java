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
    short[][][] d;
    ArrayList<Block> currLayer;
    ArrayList<Block> nextLayer;

    public Pathfinder(World w){
        this.xsize = w.xsize;
        this.ysize = w.ysize;
        this.zsize = w.zsize;
        this.w = w;
        
        d = new short[w.xsize][w.ysize][w.zsize];
    }

    public void clear(){
        for (int i=0; i<xsize; ++i)
            for (int j=0; j<ysize; ++j)
                for (int k=0; k<zsize; ++k)
                    d[i][j][k] = -1;
    }

    

    public Stack<Block> getPath(Creature c, Block b1, Block b2) {
        int i, j, l;
        short D;
        Block m, n;
        ArrayList<Block> near;
        clear();
        nextLayer = new ArrayList<>(1);
        nextLayer.add(b1);
        D = 0;
        d[b1.x][b1.y][b1.z] = D;
        while (!(d[b2.x][b2.y][b2.z] >= 0)){
            D++;
            currLayer = nextLayer;
            nextLayer = new ArrayList<>(currLayer.size());
            l = currLayer.size();
            for (i=0; i<l; ++i){
                m = currLayer.get(i);
                near = m.nearest(w);
                for (j=0; j<near.size(); ++j){
                    n = near.get(j);
                    if (c.canMove(m, n) && (d[n.x][n.y][n.z]==-1)){
                        d[n.x][n.y][n.z] = D;
                        nextLayer.add(n);
                    }
                }
            }
        }
        m = b2;
        D = d[m.x][m.y][m.z];
        Stack<Block> q = new Stack<>();
        q.push(m);
        while(D!=0){
            near = m.nearest(w);
            for (j=0; j<near.size(); ++j){
                m = near.get(j);
                if (d[m.x][m.y][m.z] == (D-1)){
                    q.push(m);
                    D--;
                    j = 100; //totally out of bounds
                }
            }
        }
        return q;
    }
}
