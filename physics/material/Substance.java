package physics.material;

public class Substance {
    public Material m;
    public int state;
    public double w;
    public double V;

    public Substance(Material m, double w){
        this.m = m;
        this.w = w;
        this.V = w / m.density;
        this.state = m.state;
    }
}

