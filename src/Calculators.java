public class Calculators extends Pokedex{
        //calculation for all stats (excluding HP)
        //more info: https://bulbapedia.bulbagarden.net/wiki/Stat#Generation_III_onward
        //must be cast to double to prevent floating point error but has to be cast back to int because all pokemon calculations *always* round down
        public static int statCalculation(int baseStat, int IV, int EV, double nature, int level, int boostCount){
            EV/=4; //divides ev by 4 because ev is divided by 4 in stat calcs
            return (int)((int)((((double)((2*baseStat+IV+EV)*level)/100)+5)*nature)*getBoostModifier(boostCount));
        }

        //finds what stat boost you need to be at to be faster than a given stat
        public static int findLeastStatBoosted(int IV, int EV, double nature, int level, int targetStat, int boostCount){
            for(int currentStat = 0; currentStat<255; currentStat++){
                final int stat = (int)(statCalculation(currentStat,IV,EV,nature,level,boostCount)*getBoostModifier(boostCount));
                if(stat>targetStat){return currentStat;}
            }
            return -1;
        }

         //finds what stat boost you need to be at to be faster than a given stat
        public static int findLeastSpeedEVs(int IV, int baseStat, double nature, int level, int targetStat, int boostCount){
            for(int EV = 0; EV<=252; EV++){
                final int stat = statCalculation(baseStat,IV,EV,nature,level,boostCount);
                if(stat>targetStat){return EV;}
            }
            return -1;
        }

        //finds what stat boost you need to ohko
        public static int findLeastAtkEVs(int baseStat, double nature, int level, Move move, int defenderStat, int boostCount, int oppHP){
            System.out.println("opp hp "+oppHP);
            for(int EV = 0; EV<=252; EV++){
                final int stat = statCalculation(baseStat,31,EV,nature,level,boostCount);
                final int damage = (int)damageCalc(level,stat,defenderStat,move);
                if(damage>=oppHP){return EV;}
            }
            return -1;
        }

        public static int calcHP(double EV, double level, double baseHP){return (int)((int)(((2*baseHP+31+(EV/4))*level)/100)+level+10);}

        //bro just make a seperate class that has all the evs and ivs and thr base stats atp
        //THIS IS SHITTY UNOPTIMIZED PLACEHOLDER CODE TO GET THE SYSTEM WORKING. ALL THESE ARE PLACEHOLDERS!!
        private static double damageCalc(double attackerLevel, double attackingMonAttack, double targetDefenseStat, Move move){
            attackerLevel = ((2*attackerLevel)/5)+2;


            //rawDamage is the damage calc before any situational modifiers. more info here: https://bulbapedia.bulbagarden.net/wiki/Damage#Generation_V_onward
            double rawDamage = ((attackerLevel*move.baseDamage*(attackingMonAttack/targetDefenseStat))/50)+2;
            rawDamage*=other(new String[]{"Grass", "Ice"}, new String[]{"Fighting", "Fairy"},Pokedex.getMove("Close Combat"));
            System.out.println("raw damage: "+rawDamage);
            return rawDamage;
        }
        //get stat boost modifier
        private static double getBoostModifier(int boostCount){return (double)(boostCount+2)/2;}


//other factors
private static double other(String[] oppTypes, String[] yourTypes, Move move){
    double total = 1;
    final Type[] oppTypesArray = {Type.getType(oppTypes[0]),Type.getType(oppTypes[1])};
    total*=Pokedex.Type.getMatchups(oppTypesArray,move.type);
    System.out.println("type "+Pokedex.Type.getMatchups(oppTypesArray,move.type));
    total*=STAB(yourTypes,move);
    System.out.println("STAB "+STAB(yourTypes,move));

    return total;
}
private static double STAB(String[] yourTypes, Move move){if(yourTypes[0].equals(move.type)||yourTypes[1].equals(move.type)){return 1.5;}else{return 1;}}

}
