public class Main {
    public static void Main(String[] args){
        int index = 0;
        String[] options = {"FIND BASE STATS/EVS/BOOST COMBINATIONS TO OUTSPEED A SPECIFIC POKEMON","FIND EVS/BOOSTS/NATURE TO OUTSPEED A POKEMON AT A SPECIFIC BASE STAT","EV OPTIMIZER: HOW MUCH DEFENSE DO YOU NEED TO SURVIVE AN ATTACK?"};
        for(String i:options){
            index++;
            System.out.println(index+"- "+i);
        }
        int selection = InputHelper.getRangedInt("Enter a number.",1,options.length);
        switch(selection){
            case(1)->Tasks.findBaseStatsToOutspeed();
            case(2)->Tasks.findEVsToOutspeed();
        }
    }
}
