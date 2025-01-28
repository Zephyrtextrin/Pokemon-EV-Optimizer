public class OutspeedBaseStats {
    public static void main(String[] args){
        Pokedex.initialize(); //loads the pokedex

        int baseSpeed = Miscellaneous.getSelectedPokemon()[5];

        //find values for base stat calculation
        final int IV = InputHelper.getRangedInt("Enter Speed IV",0,31);
        final int EV = InputHelper.getRangedInt("Enter Speed EV",0,252)/4; //divided by 4 to make the stat calc more streamlined (since evs r divided by 4 in calcs)
        final String nature = InputHelper.getStringInArray(new String[]{"positive","neutral","negative"},"Enter Nature");
        final double natureMultiplier = Miscellaneous.getNature(nature);
        final int level = InputHelper.getRangedInt("Enter Level",1,100);

        final int stat = Miscellaneous.Calculators.statCalculation(baseSpeed,IV,EV,natureMultiplier,level);
        System.out.println("\n[STAT]: "+stat);

        Miscellaneous.outputAllPossibleResults(level,stat,baseSpeed);
    }
}