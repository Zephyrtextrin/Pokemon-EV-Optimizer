import java.io.IOException;

public class Main{
    public static void main(String[] args) throws IOException{
        Database.initialize(); //loads the pokedex
        AttackOptimizer.initUI();

        /*to do
        make pokemon have types
        items
        make calc to see how much defense u need to tank smth
        make calc to see how much speed u need to outspeed
         */
    }
}
