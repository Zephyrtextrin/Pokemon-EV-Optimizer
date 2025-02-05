public class Calculators extends Database {
    //calculation for all stats (excluding HP)
    //more info: https://bulbapedia.bulbagarden.net/wiki/Stat#Generation_III_onward
    //must be cast to double to prevent floating point error but has to be cast back to int because all pokemon calculations *always* round down
    public static int statCalculation(int baseStat, int IV, int EV, double nature, int level, int boostCount){
        EV/=4; //divides ev by 4 because ev is divided by 4 in stat calcs
        int stat = -1;
        try {stat=(int)((int)((((double)((2*baseStat+IV+EV)*level)/100)+5)*nature)*getBoostModifier(boostCount));
        }catch (Exception e){ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ERR_CC_STAT_CALCULATION_ERROR, e);}
        return stat;
    }

    //finds what stat boost you need to be at to be faster than a given stat
    public static int findLeastSpeedStatBoosted(int IV, int EV, double nature, int level, int targetStat, int boostCount){
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
    public static int findLeastAtkEVs(AttackOptimizerUI.CurrentPokemon you, AttackOptimizerUI.CurrentPokemon opp, Move move, String weather, double roll){
        int baseStat = you.base.baseAttack;
        double nature = you.nature.baseAttack;
        int boostCount = you.atkBoost;
        int defenderStat = opp.defStat;

        if(move.moveCategory== Constants.MOVE_CATS.Special){
            baseStat = you.base.baseSpatk;
            nature = you.nature.baseSpatk;
            boostCount = you.spAtkBoost;
            defenderStat = opp.spDefStat;
        }

        for(int EV = 0; EV<=252; EV+=4){//ev goes up by 4 bc the stat only goes up every 4 evs
            final int stat = statCalculation(baseStat,31,EV,nature,you.level,boostCount);
            System.out.println("CURRENT EV: "+EV);
            final int damage = (int)(damageCalc(you.level,stat,defenderStat,move, you.base,opp.base,you.item, weather)*roll);
            if(damage>=opp.HPStat){
                if(Constants.DEBUG_MODE){System.out.printf("\n[RESULT FOUND!]\nbase stat: %d\nlevel: %d\nEV: %d\nyour calculated stat: %d\nopp stat %d\n--------------------------------------------\n[END].\n",baseStat,you.level,EV,stat,defenderStat);}
                return EV;
            }
        }
        if(Constants.DEBUG_MODE){System.out.printf("\n[NO RESULT!]\nbase stat: %d\nlevel: %d\nEV: 252\nyour calculated stat: %d\nopp stat %d\n--------------------------------------------\n[END].\n",baseStat,you.level,statCalculation(baseStat,31,252,nature,you.level,boostCount),defenderStat);}
        return -1;
    }

    public static int calcHP(double EV, double level, double baseHP){return (int)((int)(((2*baseHP+31+(EV/4))*level)/100)+level+10);}

    //"why do you cast to int so much?" everything rounds down. all calculations *always* round down.
    private static double damageCalc(double attackerLevel, double attackingMonAttack, double targetDefenseStat, Move move, Pokemon you, Pokemon opp, String item, String weather){
        try {
            attackerLevel = (((2*attackerLevel)/5)+2); //this is done here to decrease verbosity of the rawDamage calc and make it more readable and testable
            final double AD = attackingMonAttack/targetDefenseStat; //attack divided by defense. this is done in a seperate variable to decrease verbosity.
            System.out.println("STEP ONE: "+attackerLevel*move.baseDamage*AD);
            //rawDamage is the damage calc before any situational modifiers. more info here: https://bulbapedia.bulbagarden.net/wiki/Damage#Generation_V_onward
            double rawDamage = (((attackerLevel*move.baseDamage*AD)/50)+2); //raw damage before other factors are applied. this is kept seperate for debugging purposes

            final double finalDamage = rawDamage*other(you.types, opp.types, move, item, weather); //other factors such as stab/weather

            if(Constants.DEBUG_MODE){System.out.println("attacker stat: "+attackingMonAttack+"\ndefender stat:"+targetDefenseStat+"\nattacker level: "+attackerLevel+"\nmove bp: "+move.baseDamage+"\nmove category: "+move.moveCategory+"\nraw damage: "+rawDamage+"\nfinal damage: " + finalDamage+"\n------[END DAMAGE CALC]------\n");}

            return (int)finalDamage;
        }catch(Exception e){
            ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ERR_CC_DAMAGE_CALCULATION_ERROR, e);
            return -1;
        }
    }

    //get stat boost modifier
    private static double getBoostModifier(int boostCount){
        if(boostCount<=-1){return (double)2/(2+boostCount);
        }else{return (double)(boostCount+2)/2;}
    }


    //other factors
    private static double other(Type[] yourTypes, Type[] oppTypes, Move move, String item, String weather){
        double total = 1;

        total*= getMatchups(oppTypes,move.type);
        total*=STAB(yourTypes,move);
        total*= Items.getItemEffect(item, move.moveCategory);
        total*=getWeatherMultiplier(move, oppTypes, weather);

        if(Constants.DEBUG_MODE){System.out.println("type "+getMatchups(oppTypes,move.type)+"\nSTAB: "+STAB(yourTypes,move)+"\nItem: "+Items.getItemEffect(item,move.moveCategory)+"\nWeather: "+getWeatherMultiplier(move, oppTypes, weather));}

        return total;
    }

    private static double STAB(Type[] yourTypes, Move move){
            if(containsType(yourTypes, getType(move.type))){
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
            
        }else if(weather.equals("Sand")&&containsType(opp, getType("Rock"))&&move.moveCategory==Constants.MOVE_CATS.Special){return 0.5; //rocks get spdef boost

        }else if(weather.equals("Snow")&&containsType(opp, getType("Ice"))&&move.moveCategory==Constants.MOVE_CATS.Physical){return 0.5;}

        return 1;
    }
    
    private static boolean containsType(Type[] type, Type target){  
        Type secondType = type[0];
        if(type.length!=1){secondType=type[1];} //u have to do this because some pokemon arent dual type so u use the first type as a fallback
        return type[0]==target||secondType==target;
    }
}
