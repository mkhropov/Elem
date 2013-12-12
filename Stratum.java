import java.util.Random;

import physics.material.*;

public class Stratum {
    public int x;
    public int y;
    public int[][] width;

    static Random gen;

    Stratum(int x, int y, int width){
        this.x = x;
        this.y = y;
        this.width = new int[x][y];

        if (gen == null)
            gen = new Random(width);

        double scx = x*x/4.d;
        double scy = y*y/4.d;
        double a = gen.nextInt(180)/(360*2*3.14d); //angle
        double ca = Math.cos(a);
        double sa = Math.sin(a);
        // ellipse will be cut when rotated =|
        // and scalings are off
        // whatever
        for (int i=-x/2; i<x/2; ++i)
            for (int j=-y/2; j<y/2; ++j)
                if (((sa*i+ca*j)*(sa*i+ca*j)/scx +
                     (sa*j+ca*i)*(sa*j+ca*i)/scy ) < 1.d)
                    this.width[i+x/2][j+y/2] = width;
    }

    public void drop(Material m, World w, int x, int y){
/*        System.out.printf("stratum of m=(%f %f %f)\n", m.color.R, m.color.G, m.color.B);
        for (int i=0; i<this.x; ++i){
            for (int j=0; j<this.y; ++j)
                System.out.printf("%d ", this.width[i][j]);
            System.out.printf("\n");
        }
        System.out.printf("x=%d, y=%d\n", x, y);*/
        int i0 = (x<0)?(0):(x);
        int i1 = (x+this.x<w.xsize)?(x+this.x):(w.xsize);
        int j0 = (y<0)?(0):(y);
        int j1 = (y+this.y<w.ysize)?(y+this.y):(w.ysize);
//        System.out.printf("%d -- %d   %d -- %d\n", i0, i1, j0, j1);
        for (int i=i0; i<i1; ++i)
            for (int j=j0; j<j1; ++j){
                int k = 0;
                while ((k<w.zsize) && (w.blockArray[i][j][k].m != null))
                    ++k;
                int d = k+this.width[i-x][j-y];
                for (; (k<d && k<w.zsize); k++)
                    w.blockArray[i][j][k].m = new Substance(m, 1.d);
            }
    }
}
