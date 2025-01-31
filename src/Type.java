import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Type {
    private static final String[] typeNames = new String[]{"Normal", "Fighting", "Flying", "Fire", "Grass", "Water", "Electric", "Ground", "Rock", "Dragon", "Poison", "Bug", "Ghost", "Psychic", "Ice", "Steel", "Dark", "Fairy"};
    private static final Map<String, Type> typeMap = new HashMap<>(); //allows u to sort by type name
    String name;
    String[] resistances;
    String[] weaknesses;
    String[] immunities;
    //also maybe a section about fringe cases such as grass types being immune to spore and ghost types unable to be trapped

    public Type(String name) {
        this.name = name;
        typeMap.put(name, this);
    }

    //this could technically only be one line but then it would be incredibly unreadable
    public static Type getType(String input) {
        input = input.toUpperCase().charAt(0) + input.substring(1); //makes first letter capital
        return typeMap.get(input);
    }


    public static double getMatchups(Type[] types, String attackType){
        final Type typeOne = types[0];
        final Type typeTwo = types[1];
        double typeTwoMod = 1;
        if(typeTwo!=null){typeTwoMod = returnMatchupModifier(typeTwo, attackType);}

        return (returnMatchupModifier(typeOne,attackType)*typeTwoMod);
    }

    private static double returnMatchupModifier(Type defendingType, String attackingType){
        if(Arrays.asList(defendingType.weaknesses).contains(attackingType)){return 2;
        }else if(Arrays.asList(defendingType.resistances).contains(attackingType)){return 0.5;
        }else if(Arrays.asList(defendingType.immunities).contains(attackingType)){return 0;}

        return 1;

    }
    //type database (inits all types into the array
    public Type(){
        for(int i = 0; i<=typeNames.length-1;i++){new Type(typeNames[i]);}

        //weaknesses
        typeMap.get("Normal").weaknesses = new String[]{"Fighting"};
        typeMap.get("Fighting").weaknesses = new String[]{"Flying","Psychic","Fairy"};
        typeMap.get("Flying").weaknesses = new String[]{"Rock","Electric","Ice"};
        typeMap.get("Fire").weaknesses = new String[]{"Water","Ground","Rock"};
        typeMap.get("Grass").weaknesses = new String[]{"Poison","Flying","Ice","Fire","Bug"};
        typeMap.get("Water").weaknesses = new String[]{"Electric", "Grass"};
        typeMap.get("Electric").weaknesses = new String[]{"Ground"};
        typeMap.get("Ground").weaknesses = new String[]{"Water", "Ice","Grass"};
        typeMap.get("Rock").weaknesses = new String[]{"Fighting","Water","Grass","Ground","Steel"};
        typeMap.get("Dragon").weaknesses = new String[]{"Ice","Fairy"};
        typeMap.get("Poison").weaknesses = new String[]{"Ground","Psychic"};
        typeMap.get("Bug").weaknesses = new String[]{"Flying","Rock","Fire"};
        typeMap.get("Dark").weaknesses = new String[]{"Fighting","Fairy","Bug"};
        typeMap.get("Steel").weaknesses = new String[]{"Fighting","Fire","Ground"};
        typeMap.get("Ghost").weaknesses = new String[]{"Ghost","Dark"};
        typeMap.get("Fairy").weaknesses = new String[]{"Steel","Poison"};
        typeMap.get("Psychic").weaknesses = new String[]{"Dark","Ghost","Bug"};
        typeMap.get("Ice").weaknesses = new String[]{"Fighting","Rock","Fire","Steel"};

        //resists
        typeMap.get("Normal").resistances = new String[]{};
        typeMap.get("Fighting").resistances = new String[]{"Rock","Bug","Dark"};
        typeMap.get("Flying").resistances = new String[]{"Fighting","Bug","Grass"};
        typeMap.get("Fire").resistances = new String[]{"Fighting"};
        typeMap.get("Grass").resistances = new String[]{"Fighting"};
        typeMap.get("Water").resistances = new String[]{"Fighting"};
        typeMap.get("Electric").resistances = new String[]{"Fighting"};
        typeMap.get("Ground").resistances = new String[]{"Fighting"};
        typeMap.get("Rock").resistances = new String[]{"Fighting"};
        typeMap.get("Dragon").resistances = new String[]{"Fighting"};
        typeMap.get("Poison").resistances = new String[]{"Fighting"};
        typeMap.get("Bug").resistances = new String[]{"Fighting"};
        typeMap.get("Dark").resistances = new String[]{"Fighting"};
        typeMap.get("Steel").resistances = new String[]{"Fighting"};
        typeMap.get("Ghost").resistances = new String[]{"Fighting"};
        typeMap.get("Fairy").resistances = new String[]{"Fighting"};
        typeMap.get("Psychic").resistances = new String[]{"Fighting"};
        typeMap.get("Ice").resistances = new String[]{"Fighting"};

        //immunities
        typeMap.get("Normal").immunities = new String[]{"Ghost"};
        typeMap.get("Fighting").immunities = new String[]{"Ghost"};
        typeMap.get("Flying").immunities = new String[]{"Ground"};
        typeMap.get("Steel").immunities = new String[]{"Poison"};
        typeMap.get("Ghost").immunities = new String[]{"Normal","Fighting"};
        typeMap.get("Fairy").immunities = new String[]{"Dragon"};

    }
}
