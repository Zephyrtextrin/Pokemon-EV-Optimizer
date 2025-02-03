import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

/* specifications[]
0 = IV
1 = EV
2 = nature
3 = level
*/
public class Tasks extends Pokedex{
    //finds how fast you need to be base-stats wise to outspeed something, includes boosts and evs
    public static void findBaseStatsToOutspeed(){

        final Pokemon target = Miscellaneous.selectPokemon();
        final int baseSpeed = target.baseSpeed;

        double[] specifications = Miscellaneous.getSpecifications();

        final int stat = Miscellaneous.Calculators.statCalculation(baseSpeed, (int)specifications[0], (int)specifications[1],specifications[2], (int)specifications[3],0);
        System.out.println("\n[STAT]: "+stat);

        TaskOutput.baseStatFinder((int)specifications[3],stat,(int)specifications[1],baseSpeed, target.name.toUpperCase());
    }

    public static void findEVsToOutspeed() {
        System.out.println("This input is for YOUR Pokemon.");
        final Pokemon you = Miscellaneous.selectPokemon();
        final int yourBaseSpeed = you.baseSpeed;

        System.out.println("This is for the OPPONENT's Pokemon.");
        final Pokemon target = Miscellaneous.selectPokemon();
        final int baseSpeed = target.baseSpeed;
        System.out.println("This is for the OPPONENT's Pokemon."); //print again cause it can be kinda easy to forget
        double[] specifications = Miscellaneous.getSpecifications();
        final int targetStat = Miscellaneous.Calculators.statCalculation(baseSpeed, (int)specifications[0], (int)specifications[1], specifications[2], (int)specifications[3],0);

        TaskOutput.EVFinder(yourBaseSpeed,(int)specifications[3],targetStat, (int)specifications[1],specifications[2], target.name.toUpperCase(), you.name.toUpperCase());
    }

    public static void outspeedPercentage() {
        //init vars
        final HashMap<String, Pokemon> dex = Pokedex.changeDex();
        final double total = dex.size() * 3;
        double outsped = 0;
        final Pokemon you = Miscellaneous.selectPokemon();

        double[] specifications = Miscellaneous.getSpecifications();
        final int stat = Miscellaneous.Calculators.statCalculation(you.baseSpeed, (int)specifications[0], (int)specifications[1], specifications[2], (int)specifications[3],0);
        System.out.printf("[%s'S SPEED STAT]: %d\n",you.name.toUpperCase(),stat);
        for (Pokemon currentMon : dex.values()) {
            int neutral = Miscellaneous.Calculators.statCalculation(currentMon.baseSpeed, 31, 252, 1, (int)specifications[3],0);
            int positive = Miscellaneous.Calculators.statCalculation(currentMon.baseSpeed, 31, 252, 1.1, (int)specifications[3],0);
            int noInvest = Miscellaneous.Calculators.statCalculation(currentMon.baseSpeed, 31, 0, 1, (int)specifications[3],0);
            if(stat>neutral){outsped++;}
            if(stat>positive){outsped++;}
            if(stat>noInvest){outsped++;}
        }
        System.out.println("\n[HOW MANY POKEMON DOES "+you.name.toUpperCase()+" OUTSPEED?]\n(This includes 252 Positive nature, 252 neutral nature, and zero investment for each Pokemon.)\n");
        System.out.println("DEX TOTAL: "+(int)total);
        System.out.println("OUTSPED: "+(int)outsped);
        System.out.println("PERCENTAGE: "+(outsped/total)*100+"%");
    }

}
