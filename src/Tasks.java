/* specifications[]
0 = IV
1 = EV
2 = nature
3 = level
*/
public class Tasks{
    //finds how fast you need to be base-stats wise to outspeed something, includes boosts and evs
    public static void findBaseStatsToOutspeed{
        Pokedex.initialize(); //loads the pokedex

        int baseSpeed = Miscellaneous.selectPokemon()[5];

        double[] specifications = Miscellaneous.getSpecifications();

        final int stat = Miscellaneous.Calculators.statCalculation(baseSpeed,specifications[0],specifications[1],specifications[2],specifications[3]);
        System.out.println("\n[STAT]: "+stat);

        TaskOutput.baseStatFinder(level,stat,baseSpeed);
    }

    public static void find EVsToOutspeed(){
        System.out.println("This input is for YOUR Pokemon.");
        int yourBaseSpeed = Miscellaneous.getSelectedPokemon()[5];

        System.out.println("This is for the OPPONENT's Pokemon.")
        int targetBaseSpeed = Miscellaneous.getSelectedPokemon()[5];
        double specifications = Miscellaneous.getSpecifications();
        final int targetStat = Miscellaneous.Calculators.statCalculation(targetBaseSpeed,specifications[0],specifications[1],specifications[2],specifications[3]);

        //PLACEHOLDER ADD TEXT TO IT LATER neutral nature no boosts
        System.out.println(Miscellaneous.Calculators.findLeastEVs(31,yourBaseSpeed,1,specifications[3],targetStat,0);
        //PLACEHOLDER ADD TEXT TO IT LATER pos nature no boosts
        System.out.println(Miscellaneous.Calculators.findLeastEVs(31,yourBaseSpeed,1.1,specifications[3],targetStat,0);
        //PLACEHOLDER ADD TEXT TO IT LATER neutral nature 1 boost
        System.out.println(Miscellaneous.Calculators.findLeastEVs(31,yourBaseSpeed,1,specifications[3],targetStat,1);
        //PLACEHOLDER ADD TEXT TO IT LATER pos nature 1 boost
        System.out.println(Miscellaneous.Calculators.findLeastEVs(31,yourBaseSpeed,1.1,specifications[3],targetStat,1);
}
