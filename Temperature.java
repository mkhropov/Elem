class Temperature {
    
    public static int max = 9000000;
    public static int min = 0; //Kelvins

    public int t;

    Temperature(int t){
        if (t<this.min)
            this.t = this.min;
        else if (t>this.max)
            this.t = this.max;
        else
            this.t = t;
    }
}


