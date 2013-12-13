package physics.material;

public class Substance {
    public Material m;
    public double w;
    public double V;

    public Substance(Material m, double w){
        this.m = m;
        this.w = w;
    }

    public Substance(Substance s){
        this.m = s.m;
        this.w = s.w;
    }
}

