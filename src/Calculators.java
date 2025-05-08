public class Calculators extends Database {
    //calculation for all stats (excluding HP)
    //more info: https://bulbapedia.bulbagarden.net/wiki/Stat#Generation_III_onward
    //must be cast to double to prevent floating point error but has to be cast back to int because all pokemon calculations *always* round down
    public static int statCalculation(int baseStat, int IV, int EV, double nature, int level, int boostCount){
        EV/=4; //divides ev by 4 because ev is divided by 4 in stat calcs
        int stat = -1;
        try{stat=(int)((int)((((double)((2*baseStat+IV+EV)*level)/100)+5)*nature)*getBoostModifier(boostCount));
        }catch(Exception e){
            Printer.errorHandler(Printer.ERROR_CODE.ERR_CC_STAT_CALCULATION_ERROR, e);}

        //if(Constants.DEBUG_CALC_MODE&&EV==0){System.out.printf("\n---[DEBUG: STAT CALCULATION]---\nCALCED STAT: %d\nIV: %d\nEV: %d\nLEVEL: %d\nBOOST MODIFIER: %f\n---[END.]---\n\n",stat,IV,EV,level,getBoostModifier(boostCount));}
        return stat;
    }

    //finds what stat boost you need to be at to be faster than a given stat
    public static int findLeastSpeedEVs(EVCalculatorUI.CurrentPokemon you, int targetStat, int boostCount){

        for(int EV = 0; EV<=252; EV++){
            final int stat = statCalculation(you.getStat(Constants.Stats.Speed),31,EV,you.getNatureMultiplier(Constants.STATS[5]),you.getInt(Constants.Stats.HP, Constants.Attributes.level),boostCount);
            if(Constants.DEBUG_DAMAGE_MODE){System.out.println("\nOPP SPEED STAT: "+targetStat+"\nYOUR SPEED STAT: "+stat+"\nYOUR SPEED EV: "+EV+"\nOPP SPPEED BOOST: "+HelperMethods.getComponentValue("Right-Side Speed Boost", true)+"\nOPP SPPED BOOST MODIFIER: "+getBoostModifier(Double.parseDouble(HelperMethods.getComponentValue("Right-Side Speed Boost", true))));}
            if(stat>targetStat){return EV;}
        }
        return -1;
    }

    //finds what stat boost you need to ohko
    public static int[] findLeastHPEVs(EVCalculatorUI.CurrentPokemon opponentMon, EVCalculatorUI.CurrentPokemon targetMon, Move move, String weather, boolean spread){
        int oppAttackStat = opponentMon.getInt(Constants.Stats.Attack, Constants.Attributes.stats);
        int targetBaseHP = targetMon.getStat(Constants.Stats.HP);
        int targetDefStat = targetMon.getInt(Constants.Stats.Defense, Constants.Attributes.stats);

        if(move.moveCategory== Constants.MOVE_CATS.Special){
            targetDefStat = targetMon.getInt(Constants.Stats.Spdef, Constants.Attributes.stats);
            oppAttackStat = opponentMon.getInt(Constants.Stats.Spatk, Constants.Attributes.stats);
        }
        final int[] EVRolls = {-1,-1,-1};

        int index = 0;
        final int damage = damageCalc(targetMon,opponentMon,move,spread,weather);

        for(double currentRoll:Constants.ROLLS){
            if(Constants.DEBUG_DAMAGE_MODE){System.out.println();}
            for (int EV = 0; EV <= 252; EV += 4){//ev goes up by 4 bc the stat only goes up every 4 evs
                final int targetCalcedHPStat = calcHP(EV, targetMon.getInt(Constants.Stats.HP, Constants.Attributes.level), targetBaseHP);

                if((damage*currentRoll)<targetCalcedHPStat){
                    if(Constants.DEBUG_DAMAGE_MODE){System.out.printf("\n[RESULT FOUND!]\nyour baseHP stat: %d\nyour defense stat: %d\nEV: %d\nyour calculated HP stat: %d\nopp attack stat %d\nopp attack ev: %d\ndamage: %d\nroll:%f\n--------------------------------------------\n[END].\n", targetBaseHP, targetDefStat,EV, targetCalcedHPStat, oppAttackStat, opponentMon.getInt(Constants.Stats.Attack, Constants.Attributes.EV),damage,currentRoll);}
                    EVRolls[index] = EV;
                    break;
                }
            }
            index++;
        }
        return EVRolls;
    }

    //"why do you cast to int so much?" everything rounds down. all calculations *always* round down.
    private static int damageCalc(EVCalculatorUI.CurrentPokemon attacker, EVCalculatorUI.CurrentPokemon defender,Move move, boolean spread, String weather){
        try {
            double rawDamage = getRawDamage(attacker, defender, move); //damage before other factors such as stab, weather, abilities, etc

            final double finalDamage = other(attacker, defender, rawDamage, move, spread, weather); //other factors such as stab/weather

            if(Constants.DEBUG_DAMAGE_MODE){System.out.println("\n---[DEBUG: DAMAGE CALCULATION]---\n\nmove bp: "+move.baseDamage+"\nmove category: "+move.moveCategory+"\nraw damage: "+rawDamage+"\nfinal damage: " + finalDamage+"\n\n------[END DAMAGE CALC]------\n");}

            return (int)finalDamage;
        }catch(Exception e){
            Printer.errorHandler(Printer.ERROR_CODE.ERR_CC_DAMAGE_CALCULATION_ERROR, e);
            return -1;
        }
    }

    //rawDamage is the damage calc before any situational modifiers. more info here: https://bulbapedia.bulbagarden.net/wiki/Damage#Generation_V_onward
    private static int getRawDamage(EVCalculatorUI.CurrentPokemon attacker, EVCalculatorUI.CurrentPokemon defender, Move move){
        double attackStat = attacker.getInt(Constants.Stats.Attack, Constants.Attributes.stats);
        double defenseStat = defender.getInt(Constants.Stats.Defense, Constants.Attributes.stats);

        if(move.moveCategory==Constants.MOVE_CATS.Special){
            attackStat = attacker.getInt(Constants.Stats.Spatk, Constants.Attributes.stats);
            defenseStat = defender.getInt(Constants.Stats.Spdef, Constants.Attributes.stats);
        }

        final int attackerLevel = (int)(((double)(2* attacker.getInt(null, Constants.Attributes.level))/5)+2); //this is done here to decrease verbosity of the rawDamage calc and make it more readable and testable
        final double AD = attackStat/defenseStat; //attack divided by defense. this is done in a seperate variable to decrease verbosity.
        final int stepOne = (int)(attackerLevel*move.baseDamage*AD);

        if(Constants.DEBUG_CALC_MODE){
            Printer.debug("RAW DAMAGE CALC",new String[]{"ATTACKER LEVEL","AD","STEP ONE","STEP TWO"},new double[]{attackerLevel,AD, stepOne, ((double)stepOne/50)});
        };

        return(stepOne/50)+2;
    }

    //get stat boost modifier
    private static double getBoostModifier(double boostCount){
        if(boostCount<=-1){return 2/(2+Math.abs(boostCount));
        }else{return (boostCount+2)/2;}
    }

    //finds what stat boost you need to ohko
    public static int[] findLeastAttackEVs(EVCalculatorUI.CurrentPokemon you, EVCalculatorUI.CurrentPokemon opp, Move move, String weather, boolean spread){
        //sets up which attacking stat to use
        Constants.Stats statToChange = Constants.Stats.Attack;
        int baseStat = you.getStat(Constants.Stats.Attack);
        int defenderStat = opp.getInt(Constants.Stats.Defense, Constants.Attributes.stats);

        if(move.moveCategory==Constants.MOVE_CATS.Special){
            baseStat = you.getStat(Constants.Stats.Spatk);
            defenderStat = opp.getInt(Constants.Stats.Spdef, Constants.Attributes.stats);
            statToChange = Constants.Stats.Spatk;
        }

        int index = 0;
        final int[] EVrolls = {-1,-1,-1,-1};

        for(double currentRoll:Constants.ROLLS){
            if(Constants.DEBUG_DAMAGE_MODE){Printer.debug("DISPLAYING ALL STATS BEFORE DAMAGE CALC",new String[]{"your pokemon","your attack","your special attack","your base atk","opponent's mon","opponent hp","opp defense","roll","move name","category"},new String[]{you.name, String.valueOf(you.getInt(Constants.Stats.Attack,Constants.Attributes.stats)), String.valueOf(you.getInt(Constants.Stats.Spatk,Constants.Attributes.stats)), String.valueOf(baseStat),opp.name, String.valueOf(opp.getInt(Constants.Stats.HP, Constants.Attributes.stats)), String.valueOf(defenderStat), String.valueOf(currentRoll),move.name, String.valueOf(move.moveCategory)});}
            for (int EV = 0; EV <= 252; EV += 4){//ev goes up by 4 bc the stat only goes up every 4 evs
                you.setEVs(statToChange, EV);
                you.recalcStats();


                final int damage = (int)(damageCalc(you,opp,move,spread,weather) * currentRoll);

                //checks if EV = 0 so that way it only runs once and doesnt nuke the log with a million trillion messages
                if(damage >= opp.getInt(Constants.Stats.HP, Constants.Attributes.stats)){
                    EVrolls[index] = EV;
                    if(Constants.DEBUG_DAMAGE_MODE){Printer.debug("[OHKO FOUND!]","Further damage calculations for this roll will be terminated.");}
                    if(EV==0){ //if the current roll's EV is 0, make all future rolls 0 too and end program early
                        for(; index<EVrolls.length; index++){EVrolls[index] = 0;}
                        if(Constants.DEBUG_DAMAGE_MODE){
                            Printer.debug("OHKO WITH NO EVS!","All additional roll calculations will be terminated and set to zero.");
                        }
                        return EVrolls;
                    }
                    break;
                }
            }
            index++;
        }
        return EVrolls;
    }

    public static int calcHP(double EV, double level, double baseHP){return (int)((int)(((2*baseHP+31+(EV/4))*level)/100)+level+10);}

    //other factors
    private static double other(EVCalculatorUI.CurrentPokemon attacker, EVCalculatorUI.CurrentPokemon defender, double total, Move move, boolean spread, String weather){
        total*=getMatchups(defender.types,move.type);
        total*=STAB(attacker,move);
        total*= Items.getItemEffect(attacker.getString(Constants.Attributes.item), move.moveCategory);
        total*=getWeatherMultiplier(move, defender.types, weather);
        if(spread){total*=0.75;}

        if(Constants.DEBUG_DAMAGE_MODE){Printer.debug("OTHER MULTIPLIERS",new String[]{"type","stab","item","weather"}),new double[]{getMatchups(defender.types,move.type),STAB(attacker,move),Items.getItemEffect(attacker.getString(Constants.Attributes.item)),}+"\nItem: "+,move.moveCategory)+"\nWeather: "+getWeatherMultiplier(move, defender.types, weather));}

        return total;
    }

    private static double STAB(EVCalculatorUI.CurrentPokemon you, Move move){
            if(containsType(you.types, getType(move.type))){
                if(you.getString(Constants.Attributes.ability).equals("Adaptability")){return 2;}
                else{return 1.5;}
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
