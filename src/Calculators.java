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

    //"why do you cast to int so much?" everything rounds down. all calculations *always* round down.
    private static double damageCalc(double attackerLevel, double attackingMonAttack, double targetDefenseStat, Move move, Pokemon you, Pokemon opp, String item, String weather){
        attackerLevel=(int)(((2*attackerLevel)/5)+2);
        //rawDamage is the damage calc before any situational modifiers. more info here: https://bulbapedia.bulbagarden.net/wiki/Damage#Generation_V_onward
        double rawDamage = (int)(((attackerLevel*move.baseDamage*(attackingMonAttack/targetDefenseStat))/50)+2);
        rawDamage*=other(you.types,opp.types, move, item, weather);
        System.out.println("raw damage: "+rawDamage);
        return (int)rawDamage;
    }

    //get stat boost modifier
    private static double getBoostModifier(int boostCount){
        if(boostCount<=-1){return (double)2/(2+boostCount);
        }else{return (double)(boostCount+2)/2;}
    }


    //other factors
    private static double other(Type[] yourTypes, Type[] oppTypes, Move move, String item, String weather){
        double total = 1;

        total*= Type.getMatchups(oppTypes,move.type);
        System.out.println("type "+ Type.getMatchups(oppTypes,move.type));
        
        total*=STAB(yourTypes,move);
        System.out.println("STAB: "+STAB(yourTypes,move));
        
        total*= Items.getItemEffect(item, move.moveCategory);
        System.out.println("Item: "+ Items.getItemEffect(item,move.moveCategory));
        
        total*=getWeatherMultiplier(move, oppTypes, weather);
        System.out.println("Weather: "+getWeatherMultiplier(move, oppTypes, weather));
        
        return total;
    }

    private static double STAB(Type[] yourTypes, Move move){
            if(containsType(yourTypes, Type.getType(move.type))){
                return 1.5;
            }else{
                return 1;
            }
        }

    //all of this is shit code i made on the editor on the github website and its basically the equivalent of using google docs as an ide
    //so this code is proly shit and bad so fix it all later 
    private static double getWeatherMultiplier(Move move, Type[] opp, String weather){
        if(weather.equals("Sun")){
            return switch(move.type){
                case "Fire" -> 1.5;
                case "Water" -> 0.5;
                default -> 1;
            };

        }else if(weather.equals("Rain")){
            return switch(move.type){
                case "Water" -> 1.5;
                case "Fire" -> 0.5;
                default -> 1;
            };
            
        }else if(weather.equals("Sand")&&containsType(opp, Type.getType("Rock"))&&move.moveCategory==Constants.MOVE_CATS.Special){return 0.5; //rocks get spdef boost

        }else if(weather.equals("Snow")&&containsType(opp, Type.getType("Ice"))&&move.moveCategory==Constants.MOVE_CATS.Physical){return 0.5;}

        return 1;
    }
    
    private static boolean containsType(Type[] type, Type target){  
        Type secondType = type[0];
        if(type[1]!=null){secondType=type[1];} //u have to do this because some pokemon arent dual type so u use the first type as a fallback
        return type[0]==target||secondType==target;
    }
}
