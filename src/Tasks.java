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
        final int baseSpeed = target.speed;

        double[] specifications = Miscellaneous.getSpecifications();

        final int stat = Miscellaneous.Calculators.statCalculation(baseSpeed, (int)specifications[0], (int)specifications[1],specifications[2], (int)specifications[3]);
        System.out.println("\n[STAT]: "+stat);

        TaskOutput.baseStatFinder((int)specifications[3],stat,(int)specifications[1],baseSpeed, target.name.toUpperCase());
    }

    public static void findEVsToOutspeed() {
        System.out.println("This input is for YOUR Pokemon.");
        final Pokemon you = Miscellaneous.selectPokemon();
        final int yourBaseSpeed = you.speed;

        System.out.println("This is for the OPPONENT's Pokemon.");
        final Pokemon target = Miscellaneous.selectPokemon();
        final int baseSpeed = target.speed;
        System.out.println("This is for the OPPONENT's Pokemon."); //print again cause it can be kinda easy to forget
        double[] specifications = Miscellaneous.getSpecifications();
        final int targetStat = Miscellaneous.Calculators.statCalculation(baseSpeed, (int)specifications[0], (int)specifications[1], specifications[2], (int)specifications[3]);

        TaskOutput.EVFinder(yourBaseSpeed,(int)specifications[3],targetStat, (int)specifications[1],specifications[2], target.name.toUpperCase(), you.name.toUpperCase());
    }
}
