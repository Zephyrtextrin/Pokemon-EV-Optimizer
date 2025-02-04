import java.util.Objects;

public class Calculators extends Database {
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
    public static int findLeastAtkEVs(int baseStat, double nature, int level, Move move, int defenderStat, int boostCount, int oppHP, Pokemon you, Pokemon opp, String item, String weather){
        System.out.println("opp hp "+oppHP);
        for(int EV = 0; EV<=252; EV++){
            final int stat = statCalculation(baseStat,31,EV,nature,level,boostCount);
            final int damage = (int)damageCalc(level,stat,defenderStat,move, you,opp,item, weather);
            if(damage>=oppHP){return EV;}
        }
        return -1;
    }

    public static int calcHP(double EV, double level, double baseHP){return (int)((int)(((2*baseHP+31+(EV/4))*level)/100)+level+10);}

    //bro just make a seperate class that has all the evs and ivs and thr base stats atp
    //THIS IS SHITTY UNOPTIMIZED PLACEHOLDER CODE TO GET THE SYSTEM WORKING. ALL THESE ARE PLACEHOLDERS!!
    private static double damageCalc(double attackerLevel, double attackingMonAttack, double targetDefenseStat, Move move, Pokemon you, Pokemon opp, String item, String weather){
        attackerLevel=((2*attackerLevel)/5)+2;
        //rawDamage is the damage calc before any situational modifiers. more info here: https://bulbapedia.bulbagarden.net/wiki/Damage#Generation_V_onward
        double rawDamage = ((attackerLevel*move.baseDamage*(attackingMonAttack/targetDefenseStat))/50)+2;
        rawDamage*=other(you.types,opp.types, move, item, weather);
        System.out.println("raw damage: "+rawDamage);
        return rawDamage;
    }

    //get stat boost modifier
    private static double getBoostModifier(int boostCount){
        if(boostCount<=-1){return (double)2/(2+boostCount);
        }else{return (double)(boostCount+2)/2;}
    }


    //other factors
    private static double other(Type[] yourTypes, Type[] oppTypes, Move move, String item, String weather){
        double total = 1;

        total*= Database.Type.getMatchups(oppTypes,move.type);
        System.out.println("type "+ Database.Type.getMatchups(oppTypes,move.type));
        total*=STAB(yourTypes,move);
        total*=Database.Items.getItemEffect(item, move.moveCategory);
        System.out.println("Item: "+Database.Items.getItemEffect(item,move.moveCategory));
        return total;
    }

    private static double STAB(Type[] yourTypes, Move move){
            if(yourTypes[0].equals(Type.getType(move.type))||yourTypes[1].equals(Type.getType(move.type))){
                System.out.println("STAB! 1.5 increase");
                return 1.5;
            }else{
                return 1;
            }
        }

        private static double getWeatherMultiplier(Move move, Type[] opp, String weather){
        if(weather.equals("Sun")&&Objects.equals(move.type, "Fire")){return 1.5;}
        }
}
