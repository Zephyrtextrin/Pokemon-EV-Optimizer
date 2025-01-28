public class Main {
    public static void Main(String[] args){
        //make arraylist and do foreach print
        String[] options = {"1 - FIND BASE STATS/EVS/BOOST COMBINATIONS TO OUTSPEED A SPECIFIC POKEMON","2- FIND EVS/BOOSTS/NATURE TO OUTSPEED A POKEMON AT A SPECIFIC BASE STAT","EV OPTIMIZER: HOW MUCH DEFENSE DO YOU NEED TO SURVIVE AN ATTACK?"};
        for(String i:options){System.out.println(i);}
        int selection = InputHelper.getRangedInt("Enter a number.",1,options.length);
        switch(selection){
            case(1)->Tasks.findBaseStatsToOutspeed();
        }
    }
}
