//since the output for these tasks contain lots of prints this is in a different class

public class TaskOutput{
    //outputs all possible results to outspeed at certain points (reduces clutter in main method)
    public static void baseStatFinder(int level, int targetStat, int targetBaseStat){
        System.out.println("\n--[SPEED VALUES NEEDED TO OUTSPEED TARGET]--\n(All calculations assume 31 IVs)");

        
        System.out.println("-[NO BOOST]-");

        //+1 boost no evs neutral
        System.out.println("[NO EVS, NEUTRAL NATURE]: "+Calculators.findLeastStatBoosted(31,0,1,level,targetStat,targetBaseStat,0));
        //+1 boost no evs positive
        System.out.println("[NO EVS, POSITIVE NATURE]: "+Calculators.findLeastStatBoosted(31,0,1.1,level,targetStat,targetBaseStat,0));

        
        System.out.println("\n-[ONE BOOST]-");
        

        //+1 boost no evs neutral
        System.out.println("[NO EVS, NEUTRAL NATURE]: "+Calculators.findLeastStatBoosted(31,0,1,level,targetStat,targetBaseStat,1));
        //+1 boost no evs positive
        System.out.println("[NO EVS, POSITIVE NATURE]: "+Calculators.findLeastStatBoosted(31,0,1.1,level,targetStat,targetBaseStat,1));
        //+1 boost 252 evs neutral
        System.out.println("[252 EVS, NEUTRAL NATURE]: "+Calculators.findLeastStatBoosted(31,252/4,1,level,targetStat,targetBaseStat,1));
        //+1 boost 252 evs positive
        System.out.println("[252 EVS, POSITIVE NATURE]: "+Calculators.findLeastStatBoosted(31,252/4,1.1,level,targetStat,targetBaseStat,1));



        System.out.println("\n-TWO BOOSTS]-");

        //+2 boost no evs neutral
        System.out.println("[NO EVS, NEUTRAL NATURE]: "+Calculators.findLeastStatBoosted(31,0,1,level,targetStat,targetBaseStat,2));
        //+2 boost no evs positive
        System.out.println("[NO EVS, POSITIVE NATURE]: "+Calculators.findLeastStatBoosted(31,0,1.1,level,targetStat,targetBaseStat,2));
        //+2 boost 252 evs neutral
        System.out.println("[252 EVS, NEUTRAL NATURE]: "+Calculators.findLeastStatBoosted(31,252/4,1,level,targetStat,targetBaseStat,2));
        //+2 boost 252 evs positive
        System.out.println("[252 EVS, POSITIVE NATURE]: "+Calculators.findLeastStatBoosted(31,252/4,1.1,level,targetStat,targetBaseStat,2));
  }
}
