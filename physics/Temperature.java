package physics;

public class Temperature {

    public static int max = 9000000;
    public static int min = 0; //Kelvins

    public int t;

    public Temperature(int t){
        if (t<min)
            this.t = min;
        else if (t>max)
            this.t = max;
        else
            this.t = t;
    }
}


