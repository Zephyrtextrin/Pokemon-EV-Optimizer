/* specifications[]
0 = IV
1 = EV
2 = nature
3 = level
*/
public class Tasks{
    //finds how fast you need to be base-stats wise to outspeed something, includes boosts and evs
    public static void findBaseStatsToOutspeed(){
        Pokedex.initialize(); //loads the pokedex

        int baseSpeed = Miscellaneous.selectPokemon()[5];

        double[] specifications = Miscellaneous.getSpecifications();

        final int stat = Miscellaneous.Calculators.statCalculation(baseSpeed, (int)specifications[0], (int)specifications[1],specifications[2], (int)specifications[3]);
        System.out.println("\n[STAT]: "+stat);

        TaskOutput.baseStatFinder((int)specifications[3],stat,baseSpeed);
    }

    public static void findEVsToOutspeed() {
        System.out.println("This input is for YOUR Pokemon.");
        int yourBaseSpeed = Miscellaneous.selectPokemon()[5];

        System.out.println("This is for the OPPONENT's Pokemon.");
        int targetBaseSpeed = Miscellaneous.selectPokemon()[5];
        double[] specifications = Miscellaneous.getSpecifications();
        final int targetStat = Miscellaneous.Calculators.statCalculation(targetBaseSpeed, (int)specifications[0], (int)specifications[1], specifications[2], (int)specifications[3]);

        //PLACEHOLDER ADD TEXT TO IT LATER neutral nature no boosts
        System.out.println(Miscellaneous.Calculators.findLeastEVs(31, yourBaseSpeed, 1.0, (int)specifications[3], targetStat, 0));
        //PLACEHOLDER ADD TEXT TO IT LATER pos nature no boosts
        System.out.println(Miscellaneous.Calculators.findLeastEVs(31, yourBaseSpeed, 1.1, (int)specifications[3], targetStat, 0));
        //PLACEHOLDER ADD TEXT TO IT LATER neutral nature 1 boost
        System.out.println(Miscellaneous.Calculators.findLeastEVs(31, yourBaseSpeed, 1.0, (int)specifications[3], targetStat, 1));
        //PLACEHOLDER ADD TEXT TO IT LATER pos nature 1 boost
        System.out.println(Miscellaneous.Calculators.findLeastEVs(31, yourBaseSpeed, 1.1, (int)specifications[3], targetStat, 1));
    }
}
