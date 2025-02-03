//since the output for these tasks contain lots of prints this is in a different class

public class TaskOutput {
    //outputs all possible results to outspeed at certain points (reduces clutter in main method)
    public static void baseStatFinder(int level, int targetStat, int targetEV, double targetNature, String targetName){
        final int[] constants = {level, targetStat};
        makeHeader("BASE STAT","OUTSPEED",targetEV,targetNature,"",targetName);


        System.out.println("-[NO BOOST]-");

        outputBase("[NO EVS, NEUTRAL NATURE]: ",constants,0,1,0);
        outputBase("[NO EVS, POSITIVE NATURE]: ",constants,0,1.1,0);

        System.out.println("\n-[ONE BOOST]-");


        outputBase("[NO EVS, NEUTRAL NATURE]: ",constants,0,1,1);
        outputBase("[NO EVS, POSITIVE NATURE]: ",constants,0,1.1,1);
        outputBase("[252 EVS, NEUTRAL NATURE]: ",constants,252,1,1);
        outputBase("[252 EVS, POSITIVE NATURE]: ",constants,252,1.1,1);


        System.out.println("\n-[TWO BOOSTS]-");

        outputBase("[NO EVS, NEUTRAL NATURE]: ",constants,0,1,2);
        outputBase("[NO EVS, POSITIVE NATURE]: ",constants,0,1.1,2);
        outputBase("[252 EVS, NEUTRAL NATURE]: ",constants,252,1,2);
        outputBase("[252 EVS, POSITIVE NATURE]: ",constants,252,1.1,2);
    }

    public static void EVFinder(int yourBaseSpeed, int level, int targetStat, int targetEV, double targetNature, String targetName, String yourName){
        final int[] constants = {yourBaseSpeed,level,targetStat};
        makeHeader("EVS","OUTSPEED",targetEV,targetNature,yourName,targetName);

        System.out.println("\n-[NO BOOST]-");

        outputEV("[NEUTRAL NATURE] ",constants,1,0);
        outputEV("[POSITIVE NATURE] ",constants,1.1,0);

        System.out.println("\n-[ONE BOOST] -");

        outputEV("[NEUTRAL NATURE] ",constants,1,1);
        outputEV("[POSITIVE NATURE] ",constants,1.1,1);
    }

    private static void outputBase(String header, int[] inputs, int EV, double nature, int boostCount){
        final int value = Miscellaneous.Calculators.findLeastStatBoosted(31, EV, nature, inputs[0], inputs[1], boostCount);

        if(value !=-1) {System.out.println(header + value);
        }else{System.out.println(header + "NOT POSSIBLE");}
    }

    private static void outputEV(String header, int[] inputs, double nature, int boostCount){
        final int value = Miscellaneous.Calculators.findLeastSpeedEVs(31, inputs[0], nature, inputs[1], inputs[2], boostCount);

        if(value !=-1) {System.out.println(header + value);
        }else{System.out.println(header + "NOT POSSIBLE");}
    }

    //allowed values for circumstance var, OUTSPEED, TANK, OHKO
    private static void makeHeader(String desiredValue, String circumstance, int EV, double nature, String yourMonName, String opponentName){
        if(nature==1.1){nature=2;}
        if(!yourMonName.isBlank()){yourMonName=" WITH "+yourMonName;}
        final String natureTitle = switch((int)nature){
            case 2 -> "POSITIVE";
            case 0 -> "NEGATIVE";
            default -> "NEUTRAL";
        };
        final String header = "[%s REQUIRED TO %s %d EV %s NATURE %s%s]\n(All calculations assume 31 IVs)\n";
        System.out.printf(header, desiredValue,circumstance,EV,natureTitle,opponentName,yourMonName);
    }
}