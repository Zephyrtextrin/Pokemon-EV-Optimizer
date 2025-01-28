public class Main{
    public static void main(String[] args){
        Pokedex.initialize(); //loads the pokedex
        String name;
        boolean keepGoing = true;
        int[] baseStats;

        //get selected mon
        do{
            name = Miscellaneous.getAlias(Miscellaneous.convertToCamelCase(InputHelper.getString("Enter a Pokemon name."))); //must convert to camel case bc lists r case sensitive also this is a really long line of code but alles ok cause its tryna catch like 15 different errors
            baseStats = Pokedex.getPokemonStats(name);

            if(baseStats[0]!=-1) { //only continues if pokemon is valid
                Miscellaneous.printStats(baseStats, name); //lists stats
                keepGoing = !InputHelper.getYN("Is this the Pokemon you wanted?");
            }else{System.out.println("[INVALID INPUT!]\nThe Pokedex does not recognize that Pokemon.\n[INPUT POKEMON]: "+name+"\nThis Pokemon either does not exist or cases the code to throw an error. You'll have to type a new Pokemon.\nif you believe this to be a mistake, lmk - Alexander");} //the biggest print of all time

            System.out.println();//makes new line break for formatting. u cant append \n after the keepGoing input because it would mess up the user input and make the formatting even worse

        }while(keepGoing);

        //find values for base stat calculation
        final int IV = InputHelper.getRangedInt("Enter Speed IV",0,31);
        final int EV = InputHelper.getRangedInt("Enter Speed EV",0,252)/4; //divided by 4 to make the stat calc more streamlined (since evs r divided by 4 in calcs)
        final String nature = InputHelper.getStringInArray(new String[]{"positive","neutral","negative"},"Enter Nature");
        final double natureMultiplier = Miscellaneous.getNature(nature);
        final int level = InputHelper.getRangedInt("Enter Level",1,100);

        final int stat = Miscellaneous.Calculators.statCalculation(baseStats[5],IV,EV,natureMultiplier,level);
        System.out.println("\n[STAT]: "+stat);

        Miscellaneous.outputAllPossibleResults(IV,EV,nature,level,stat,baseStats[5],name);
    }
}