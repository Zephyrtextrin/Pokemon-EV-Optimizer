public class Main {
    public static void main(String[] args){
        Pokedex.initialize(); //loads the pokedex
        int index = 0;
        String[] options = {
                "EV OPTIMIZER: HOW MUCH ATTACK YOU NEED TO OHKO",
                "EV OPTIMIZER: HOW MUCH DEFENSE YOU NEED TO SURVIVE AN ATTACK",
                "EV OPTIMIZER: HOW MANY EVS TO OUTSPEED?",
                "FIND BASE STAT + STAT BOOST COMBINATION TO OUTSPEED A SPECIFIC POKEMON",
                "FIND MUCH OF THE FORMAT YOU OUTSPEED (In percentage)"};
        for(String i:options){
            index++;
            System.out.println(index+"- "+i);
        }
        int selection = InputHelper.getRangedInt("Enter a number.",1,options.length);
        switch(selection){
            case(3)->Tasks.findEVsToOutspeed();
            case(4)->Tasks.findBaseStatsToOutspeed();
            case(5)->Tasks.outspeedPercentage();
        }
    }
}
