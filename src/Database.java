import java.util.*;

//this is very long bc it contains an entry for every pokemon and an entry for every paldea mon
public class Database{
    private static final HashMap<String, Pokemon> natDex = new HashMap<>();
    private static final HashMap<String, Move> moveList = new HashMap<>();
    private static final ArrayList<String> itemList = new ArrayList<>();
    private static final ArrayList<String> abilityList = new ArrayList<>();
    private static final HashMap<String, Nature> natureList = new HashMap<>();
    private static final Map<String, Type> typeList = new HashMap<>(); //allows u to sort by type name

    //ughhh repeated code.. u have to do the same thing for every method here cuz the parameters dont match up otherwise-----
    public static String[] getNatDexList(){return HelperMethods.getMapAsList(natDex.keySet());}

    public static String[] getMoveList(){return HelperMethods.getMapAsList(moveList.keySet());}

    public static String[] getNatureList(){return HelperMethods.getMapAsList(natureList.keySet());}

    public static String[] getItemList(){return HelperMethods.arrayListToArray(itemList);}

    public static String[] getAbilityList(){return HelperMethods.arrayListToArray(abilityList);}


    public static Nature getNature(String name){return natureList.get(name);}

    public static Move getMove(String input){return moveList.get(input);}

    public static Pokemon getPokemon(String input){
        Pokemon result=natDex.get(input);


        if(result==null) {
            ErrorPrinter.setDetails(input, false);
            ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ABN_DB_MISSINGNO, null);
            return natDex.get("Gallade");
        }

        boolean secondTypeMalformed = result.types.length>1&&result.types[1]==null;
        if(result.types[0]==null||secondTypeMalformed){
            ErrorPrinter.setDetails(input, false);
            ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ABN_DB_BIRDTYPE, null);
            return natDex.get("Gallade");
        }

        return result;
    }

    //this could technically only be one line but then it would be incredibly unreadable
    public static Type getType(String input){
        Type output = typeList.get(input);

        if(output==null){
            ErrorPrinter.setDetails(input,false);
            ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ABN_DB_TYPE_DNE, null);
            return typeList.get("Normal");
        }else{return output;}
    }


    public static double getMatchups(Type[] types, String attackType) {
        final Type typeOne = types[0];
        double typeTwoMod = 1;
        if(types.length!=1){typeTwoMod = Type.returnOneMatchup(types[1], attackType);}

        return (Type.returnOneMatchup(typeOne, attackType)*typeTwoMod);
    }

    //THANK U SO MUCH FOR DOING ALL THE TEDIOUS ASS FORMATTING LEXI!!! :HEART EMOJI:
    public static void initialize(){
        ErrorPrinter.init();
        Type.init();
        Ability.init();
        Items.init();
        Move.init();
        Nature.init();
    }

    static public class Pokemon {
        public int dexNumber;
        public String name;
        public Type[] types;
        public int[] stats;
        

        Pokemon(int dexNumber, String name, Type[] types, int[] stats){
            this.dexNumber = dexNumber;
            this.name = name;
            this.types = types;
            this.stats = stats;


            natDex.put(name, this);
        }

        public int getStat(Constants.Stats stat){
            return switch(stat){
                case HP -> stats[0];
                case Attack -> stats[1];
                case Defense -> stats[2];
                case Spatk -> stats[3];
                case Spdef -> stats[4];
                case Speed -> stats[5];
            };
        }
    }

    static public class Move{
        String name;
        int baseDamage;
        String type;
        Constants.MOVE_CATS moveCategory;

        private Move(String name, int baseDamage, String type, Constants.MOVE_CATS moveCategory) {
            this.name = name;
            this.baseDamage = baseDamage;
            this.type = type;
            this.moveCategory = moveCategory;

            moveList.put(name, this);
        }

        protected static void init(){
            new Move("Absorb", 20, "Grass", Constants.MOVE_CATS.Special);
            new Move("Accelerock", 40, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Acid", 40, "Poison", Constants.MOVE_CATS.Special);
            new Move("Acid Spray", 40, "Poison", Constants.MOVE_CATS.Special);
            new Move("Acrobatics", 55, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Aerial Ace", 60, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Aeroblast", 100, "Flying", Constants.MOVE_CATS.Special);
            new Move("Air Cutter", 60, "Flying", Constants.MOVE_CATS.Special);
            new Move("Air Slash", 75, "Flying", Constants.MOVE_CATS.Special);
            new Move("Alluring Voice", 80, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Anchor Shot", 80, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Ancient Power", 60, "Rock", Constants.MOVE_CATS.Special);
            new Move("Apple Acid", 80, "Grass", Constants.MOVE_CATS.Special);
            new Move("Aqua Cutter", 70, "Water", Constants.MOVE_CATS.Physical);
            new Move("Aqua Jet", 40, "Water", Constants.MOVE_CATS.Physical);
            new Move("Aqua Step", 80, "Water", Constants.MOVE_CATS.Physical);
            new Move("Aqua Tail", 90, "Water", Constants.MOVE_CATS.Physical);
            new Move("Arm Thrust", 15, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Armor Cannon", 120, "Fire", Constants.MOVE_CATS.Special);
            new Move("Assurance", 60, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Astonish", 30, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Astral Barrage", 120, "Ghost", Constants.MOVE_CATS.Special);
            new Move("Attack Order", 90, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Aura Sphere", 80, "Fighting", Constants.MOVE_CATS.Special);
            new Move("Aura Wheel", 110, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Aurora Beam", 65, "Ice", Constants.MOVE_CATS.Special);
            new Move("Avalanche", 60, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Axe Kick", 120, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Baddy Bad", 80, "Dark", Constants.MOVE_CATS.Special);
            new Move("Barb Barrage", 60, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Barrage", 15, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Beak Blast", 100, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Behemoth Bash", 100, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Behemoth Blade", 100, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Belch", 120, "Poison", Constants.MOVE_CATS.Special);
            new Move("Bind", 15, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Bite", 60, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Bitter Blade", 90, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Bitter Malice", 75, "Ghost", Constants.MOVE_CATS.Special);
            new Move("Blast Burn", 150, "Fire", Constants.MOVE_CATS.Special);
            new Move("Blaze Kick", 85, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Blazing Torque", 80, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Bleakwind Storm", 100, "Flying", Constants.MOVE_CATS.Special);
            new Move("Blizzard", 110, "Ice", Constants.MOVE_CATS.Special);
            new Move("Blood Moon", 140, "Normal", Constants.MOVE_CATS.Special);
            new Move("Blue Flare", 130, "Fire", Constants.MOVE_CATS.Special);
            new Move("Body Press", 80, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Body Slam", 85, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Bolt Beak", 85, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Bolt Strike", 130, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Bone Club", 65, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Bone Rush", 25, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Bonemerang", 50, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Boomburst", 140, "Normal", Constants.MOVE_CATS.Special);
            new Move("Bounce", 85, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Bouncy Bubble", 60, "Water", Constants.MOVE_CATS.Special);
            new Move("Branch Poke", 40, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Brave Bird", 120, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Breaking Swipe", 60, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Brick Break", 75, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Brine", 65, "Water", Constants.MOVE_CATS.Special);
            new Move("Brutal Swing", 60, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Bubble", 40, "Water", Constants.MOVE_CATS.Special);
            new Move("Bubble Beam", 65, "Water", Constants.MOVE_CATS.Special);
            new Move("Bug Bite", 60, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Bug Buzz", 90, "Bug", Constants.MOVE_CATS.Special);
            new Move("Bulldoze", 60, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Bullet Punch", 40, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Bullet Seed", 25, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Burn Up", 130, "Fire", Constants.MOVE_CATS.Special);
            new Move("Burning Jealousy", 70, "Fire", Constants.MOVE_CATS.Special);
            new Move("Buzzy Buzz", 60, "Electric", Constants.MOVE_CATS.Special);
            new Move("Ceaseless Edge", 65, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Charge Beam", 50, "Electric", Constants.MOVE_CATS.Special);
            new Move("Chatter", 65, "Flying", Constants.MOVE_CATS.Special);
            new Move("Chilling Water", 50, "Water", Constants.MOVE_CATS.Special);
            new Move("Chip Away", 70, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Chloroblast", 150, "Grass", Constants.MOVE_CATS.Special);
            new Move("Circle Throw", 60, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Clamp", 35, "Water", Constants.MOVE_CATS.Physical);
            new Move("Clanging Scales", 110, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Clear Smog", 50, "Poison", Constants.MOVE_CATS.Special);
            new Move("Close Combat", 120, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Collision Course", 100, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Combat Torque", 100, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Comet Punch", 18, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Confusion", 50, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Constrict", 10, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Core Enforcer", 100, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Covet", 60, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Crabhammer", 100, "Water", Constants.MOVE_CATS.Physical);
            new Move("Cross Chop", 100, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Cross Poison", 70, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Crunch", 80, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Crush Claw", 75, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Cut", 50, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Dark Pulse", 80, "Dark", Constants.MOVE_CATS.Special);
            new Move("Darkest Lariat", 85, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Dazzling Gleam", 80, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Diamond Storm", 100, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Dig", 80, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Dire Claw", 80, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Disarming Voice", 40, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Discharge", 80, "Electric", Constants.MOVE_CATS.Special);
            new Move("Dive", 80, "Water", Constants.MOVE_CATS.Physical);
            new Move("Dizzy Punch", 70, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Doom Desire", 140, "Steel", Constants.MOVE_CATS.Special);
            new Move("Double Hit", 35, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Double Iron Bash", 60, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Double Kick", 30, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Double Shock", 120, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Double Slap", 15, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Double-Edge", 120, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Draco Meteor", 130, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Dragon Ascent", 120, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Dragon Breath", 60, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Dragon Claw", 80, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Dragon Darts", 50, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Dragon Energy", 150, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Dragon Hammer", 90, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Dragon Pulse", 85, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Dragon Rush", 100, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Dragon Tail", 60, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Drain Punch", 75, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Draining Kiss", 50, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Dream Eater", 100, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Drill Peck", 80, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Drill Run", 80, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Drum Beating", 80, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Dual Chop", 40, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Dual Wingbeat", 40, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Dynamax Cannon", 100, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Dynamic Punch", 100, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Earth Power", 90, "Ground", Constants.MOVE_CATS.Special);
            new Move("Earthquake", 100, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Echoed Voice", 40, "Normal", Constants.MOVE_CATS.Special);
            new Move("Eerie Spell", 80, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Egg Bomb", 100, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Electro Drift", 100, "Electric", Constants.MOVE_CATS.Special);
            new Move("Electro Shot", 130, "Electric", Constants.MOVE_CATS.Special);
            new Move("Electroweb", 55, "Electric", Constants.MOVE_CATS.Special);
            new Move("Ember", 40, "Fire", Constants.MOVE_CATS.Special);
            new Move("Energy Ball", 90, "Grass", Constants.MOVE_CATS.Special);
            new Move("Eruption", 150, "Fire", Constants.MOVE_CATS.Special);
            new Move("Esper Wing", 80, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Eternabeam", 160, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Expanding Force", 80, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Explosion", 250, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Extrasensory", 80, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Extreme Speed", 80, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Facade", 70, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Fairy Wind", 40, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Fake Out", 40, "Normal", Constants.MOVE_CATS.Physical);
            new Move("False Surrender", 80, "Dark", Constants.MOVE_CATS.Physical);
            new Move("False Swipe", 40, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Feint", 30, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Feint Attack", 60, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Fell Stinger", 50, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Fickle Beam", 80, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Fiery Dance", 80, "Fire", Constants.MOVE_CATS.Special);
            new Move("Fiery Wrath", 90, "Dark", Constants.MOVE_CATS.Special);
            new Move("Fire Blast", 110, "Fire", Constants.MOVE_CATS.Special);
            new Move("Fire Fang", 65, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Fire Lash", 80, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Fire Pledge", 80, "Fire", Constants.MOVE_CATS.Special);
            new Move("Fire Punch", 75, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Fire Spin", 35, "Fire", Constants.MOVE_CATS.Special);
            new Move("First Impression", 90, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Fishious Rend", 85, "Water", Constants.MOVE_CATS.Physical);
            new Move("Flame Burst", 70, "Fire", Constants.MOVE_CATS.Special);
            new Move("Flame Charge", 50, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Flame Wheel", 60, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Flamethrower", 90, "Fire", Constants.MOVE_CATS.Special);
            new Move("Flare Blitz", 120, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Flash Cannon", 80, "Steel", Constants.MOVE_CATS.Special);
            new Move("Fleur Cannon", 130, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Flip Turn", 60, "Water", Constants.MOVE_CATS.Physical);
            new Move("Floaty Fall", 90, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Flower Trick", 70, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Fly", 90, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Flying Press", 100, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Focus Blast", 120, "Fighting", Constants.MOVE_CATS.Special);
            new Move("Focus Punch", 150, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Force Palm", 60, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Foul Play", 95, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Freeze Shock", 140, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Freeze-Dry", 70, "Ice", Constants.MOVE_CATS.Special);
            new Move("Freezing Glare", 90, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Freezy Frost", 100, "Ice", Constants.MOVE_CATS.Special);
            new Move("Frenzy Plant", 150, "Grass", Constants.MOVE_CATS.Special);
            new Move("Frost Breath", 60, "Ice", Constants.MOVE_CATS.Special);
            new Move("Fury Attack", 15, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Fury Cutter", 40, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Fury Swipes", 18, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Fusion Bolt", 100, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Fusion Flare", 100, "Fire", Constants.MOVE_CATS.Special);
            new Move("Future Sight", 120, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Gear Grind", 50, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Giga Drain", 75, "Grass", Constants.MOVE_CATS.Special);
            new Move("Giga Impact", 150, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Gigaton Hammer", 160, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Glacial Lance", 120, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Glaciate", 65, "Ice", Constants.MOVE_CATS.Special);
            new Move("Glaive Rush", 120, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Glitzy Glow", 80, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Grass Pledge", 80, "Grass", Constants.MOVE_CATS.Special);
            new Move("Grassy Glide", 55, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Grav Apple", 80, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Gunk Shot", 120, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Gust", 40, "Flying", Constants.MOVE_CATS.Special);
            new Move("Hammer Arm", 100, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Head Charge", 120, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Head Smash", 150, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Headbutt", 70, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Headlong Rush", 120, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Heart Stamp", 60, "Psychic", Constants.MOVE_CATS.Physical);
            new Move("Heat Wave", 95, "Fire", Constants.MOVE_CATS.Special);
            new Move("Hex", 65, "Ghost", Constants.MOVE_CATS.Special);
            new Move("Hidden Power", 60, "Normal", Constants.MOVE_CATS.Special);
            new Move("High Horsepower", 95, "Ground", Constants.MOVE_CATS.Physical);
            new Move("High Jump Kick", 130, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Hold Back", 40, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Horn Attack", 65, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Horn Leech", 75, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Hurricane", 110, "Flying", Constants.MOVE_CATS.Special);
            new Move("Hydro Cannon", 150, "Water", Constants.MOVE_CATS.Special);
            new Move("Hydro Pump", 110, "Water", Constants.MOVE_CATS.Special);
            new Move("Hydro Steam", 80, "Water", Constants.MOVE_CATS.Special);
            new Move("Hyper Beam", 150, "Normal", Constants.MOVE_CATS.Special);
            new Move("Hyper Drill", 100, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Hyper Fang", 80, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Hyper Voice", 90, "Normal", Constants.MOVE_CATS.Special);
            new Move("Hyperspace Fury", 100, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Hyperspace Hole", 80, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Ice Ball", 30, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Ice Beam", 90, "Ice", Constants.MOVE_CATS.Special);
            new Move("Ice Burn", 140, "Ice", Constants.MOVE_CATS.Special);
            new Move("Ice Fang", 65, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Ice Hammer", 100, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Ice Punch", 75, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Ice Shard", 40, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Ice Spinner", 80, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Icicle Crash", 85, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Icicle Spear", 25, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Icy Wind", 55, "Ice", Constants.MOVE_CATS.Special);
            new Move("Incinerate", 60, "Fire", Constants.MOVE_CATS.Special);
            new Move("Infernal Parade", 60, "Ghost", Constants.MOVE_CATS.Special);
            new Move("Inferno", 100, "Fire", Constants.MOVE_CATS.Special);
            new Move("Infestation", 20, "Bug", Constants.MOVE_CATS.Special);
            new Move("Iron Head", 80, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Iron Tail", 100, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Ivy Cudgel", 100, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Jaw Lock", 80, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Jet Punch", 60, "Water", Constants.MOVE_CATS.Physical);
            new Move("Judgment", 100, "Normal", Constants.MOVE_CATS.Special);
            new Move("Jump Kick", 100, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Karate Chop", 50, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Knock Off", 65, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Kowtow Cleave", 85, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Land's Wrath", 90, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Lash Out", 75, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Last Resort", 140, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Last Respects", 50, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Lava Plume", 80, "Fire", Constants.MOVE_CATS.Special);
            new Move("Leaf Blade", 90, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Leaf Storm", 130, "Grass", Constants.MOVE_CATS.Special);
            new Move("Leaf Tornado", 65, "Grass", Constants.MOVE_CATS.Special);
            new Move("Leafage", 40, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Leech Life", 80, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Lick", 30, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Light of Ruin", 140, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Liquidation", 85, "Water", Constants.MOVE_CATS.Physical);
            new Move("Low Sweep", 65, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Lumina Crash", 80, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Lunge", 80, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Luster Purge", 95, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Mach Punch", 40, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Magical Leaf", 60, "Grass", Constants.MOVE_CATS.Special);
            new Move("Magical Torque", 100, "Fairy", Constants.MOVE_CATS.Physical);
            new Move("Magma Storm", 100, "Fire", Constants.MOVE_CATS.Special);
            new Move("Magnet Bomb", 60, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Make It Rain", 120, "Steel", Constants.MOVE_CATS.Special);
            new Move("Malignant Chain", 100, "Poison", Constants.MOVE_CATS.Special);
            new Move("Matcha Gotcha", 80, "Grass", Constants.MOVE_CATS.Special);
            new Move("Mega Drain", 40, "Grass", Constants.MOVE_CATS.Special);
            new Move("Mega Kick", 120, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Mega Punch", 80, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Megahorn", 120, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Metal Claw", 50, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Meteor Assault", 150, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Meteor Beam", 120, "Rock", Constants.MOVE_CATS.Special);
            new Move("Meteor Mash", 90, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Mighty Cleave", 95, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Mind Blown", 150, "Fire", Constants.MOVE_CATS.Special);
            new Move("Mirror Shot", 65, "Steel", Constants.MOVE_CATS.Special);
            new Move("Mist Ball", 95, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Misty Explosion", 100, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Moonblast", 95, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Moongeist Beam", 100, "Ghost", Constants.MOVE_CATS.Special);
            new Move("Mortal Spin", 30, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Mountain Gale", 100, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Mud Bomb", 65, "Ground", Constants.MOVE_CATS.Special);
            new Move("Mud Shot", 55, "Ground", Constants.MOVE_CATS.Special);
            new Move("Mud-Slap", 20, "Ground", Constants.MOVE_CATS.Special);
            new Move("Muddy Water", 90, "Water", Constants.MOVE_CATS.Special);
            new Move("Multi-Attack", 120, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Mystical Fire", 75, "Fire", Constants.MOVE_CATS.Special);
            new Move("Mystical Power", 70, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Needle Arm", 60, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Night Daze", 85, "Dark", Constants.MOVE_CATS.Special);
            new Move("Night Slash", 70, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Noxious Torque", 100, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Nuzzle", 20, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Oblivion Wing", 80, "Flying", Constants.MOVE_CATS.Special);
            new Move("Octazooka", 65, "Water", Constants.MOVE_CATS.Special);
            new Move("Ominous Wind", 60, "Ghost", Constants.MOVE_CATS.Special);
            new Move("Order Up", 80, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Origin Pulse", 110, "Water", Constants.MOVE_CATS.Special);
            new Move("Outrage", 120, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Overdrive", 80, "Electric", Constants.MOVE_CATS.Special);
            new Move("Overheat", 130, "Fire", Constants.MOVE_CATS.Special);
            new Move("Parabolic Charge", 65, "Electric", Constants.MOVE_CATS.Special);
            new Move("Pay Day", 40, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Payback", 50, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Peck", 35, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Petal Blizzard", 90, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Petal Dance", 120, "Grass", Constants.MOVE_CATS.Special);
            new Move("Phantom Force", 90, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Photon Geyser", 100, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Pin Missile", 25, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Plasma Fists", 100, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Play Rough", 90, "Fairy", Constants.MOVE_CATS.Physical);
            new Move("Pluck", 60, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Poison Fang", 50, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Poison Jab", 80, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Poison Sting", 15, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Poison Tail", 50, "Poison", Constants.MOVE_CATS.Physical);
            new Move("Pollen Puff", 90, "Bug", Constants.MOVE_CATS.Special);
            new Move("Poltergeist", 110, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Population Bomb", 20, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Pounce", 50, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Pound", 40, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Powder Snow", 40, "Ice", Constants.MOVE_CATS.Special);
            new Move("Power Gem", 80, "Rock", Constants.MOVE_CATS.Special);
            new Move("Power Trip", 20, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Power Whip", 120, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Power-Up Punch", 40, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Precipice Blades", 120, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Prismatic Laser", 160, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Psybeam", 65, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Psyblade", 80, "Psychic", Constants.MOVE_CATS.Physical);
            new Move("Psychic", 90, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Psychic Fangs", 85, "Psychic", Constants.MOVE_CATS.Physical);
            new Move("Psychic Noise", 75, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Psycho Boost", 140, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Psycho Cut", 70, "Psychic", Constants.MOVE_CATS.Physical);
            new Move("Psyshield Bash", 70, "Psychic", Constants.MOVE_CATS.Physical);
            new Move("Psyshock", 80, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Psystrike", 100, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Pursuit", 40, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Pyro Ball", 120, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Quick Attack", 40, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Rage", 20, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Rage Fist", 50, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Raging Bull", 90, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Raging Fury", 120, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Rapid Spin", 50, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Razor Leaf", 55, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Razor Shell", 75, "Water", Constants.MOVE_CATS.Physical);
            new Move("Razor Wind", 80, "Normal", Constants.MOVE_CATS.Special);
            new Move("Relic Song", 75, "Normal", Constants.MOVE_CATS.Special);
            new Move("Retaliate", 70, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Revelation Dance", 90, "Normal", Constants.MOVE_CATS.Special);
            new Move("Revenge", 60, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Rising Voltage", 70, "Electric", Constants.MOVE_CATS.Special);
            new Move("Roar of Time", 150, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Rock Blast", 25, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Rock Climb", 90, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Rock Slide", 75, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Rock Smash", 40, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Rock Throw", 50, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Rock Tomb", 60, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Rock Wrecker", 150, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Rolling Kick", 60, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Rollout", 30, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Round", 60, "Normal", Constants.MOVE_CATS.Special);
            new Move("Sacred Fire", 100, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Sacred Sword", 90, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Salt Cure", 40, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Sand Tomb", 35, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Sandsear Storm", 100, "Ground", Constants.MOVE_CATS.Special);
            new Move("Sappy Seed", 100, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Scald", 80, "Water", Constants.MOVE_CATS.Special);
            new Move("Scale Shot", 25, "Dragon", Constants.MOVE_CATS.Physical);
            new Move("Scorching Sands", 70, "Ground", Constants.MOVE_CATS.Special);
            new Move("Scratch", 40, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Searing Shot", 100, "Fire", Constants.MOVE_CATS.Special);
            new Move("Secret Power", 70, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Secret Sword", 85, "Fighting", Constants.MOVE_CATS.Special);
            new Move("Seed Bomb", 80, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Seed Flare", 120, "Grass", Constants.MOVE_CATS.Special);
            new Move("Self-Destruct", 200, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Shadow Ball", 80, "Ghost", Constants.MOVE_CATS.Special);
            new Move("Shadow Bone", 85, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Shadow Claw", 70, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Shadow Force", 120, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Shadow Punch", 60, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Shadow Sneak", 40, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Shell Side Arm", 90, "Poison", Constants.MOVE_CATS.Special);
            new Move("Shell Trap", 150, "Fire", Constants.MOVE_CATS.Special);
            new Move("Shock Wave", 60, "Electric", Constants.MOVE_CATS.Special);
            new Move("Signal Beam", 75, "Bug", Constants.MOVE_CATS.Special);
            new Move("Silver Wind", 60, "Bug", Constants.MOVE_CATS.Special);
            new Move("Sizzly Slide", 60, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Skitter Smack", 70, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Skull Bash", 130, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Sky Attack", 140, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Sky Drop", 60, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Sky Uppercut", 85, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Slam", 80, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Slash", 70, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Sludge", 65, "Poison", Constants.MOVE_CATS.Special);
            new Move("Sludge Bomb", 90, "Poison", Constants.MOVE_CATS.Special);
            new Move("Sludge Wave", 95, "Poison", Constants.MOVE_CATS.Special);
            new Move("Smack Down", 50, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Smart Strike", 70, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Smelling Salts", 70, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Smog", 30, "Poison", Constants.MOVE_CATS.Special);
            new Move("Snap Trap", 35, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Snarl", 55, "Dark", Constants.MOVE_CATS.Special);
            new Move("Snipe Shot", 80, "Water", Constants.MOVE_CATS.Special);
            new Move("Snore", 50, "Normal", Constants.MOVE_CATS.Special);
            new Move("Solar Beam", 120, "Grass", Constants.MOVE_CATS.Special);
            new Move("Solar Blade", 125, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Spacial Rend", 100, "Dragon", Constants.MOVE_CATS.Special);
            new Move("Spark", 65, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Sparkling Aria", 90, "Water", Constants.MOVE_CATS.Special);
            new Move("Sparkly Swirl", 120, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Spectral Thief", 90, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Spike Cannon", 20, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Spin Out", 100, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Spirit Break", 75, "Fairy", Constants.MOVE_CATS.Physical);
            new Move("Spirit Shackle", 80, "Ghost", Constants.MOVE_CATS.Physical);
            new Move("Splishy Splash", 90, "Water", Constants.MOVE_CATS.Special);
            new Move("Springtide Storm", 100, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Steam Eruption", 110, "Water", Constants.MOVE_CATS.Special);
            new Move("Steamroller", 65, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Steel Beam", 140, "Steel", Constants.MOVE_CATS.Special);
            new Move("Steel Roller", 130, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Steel Wing", 70, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Stomp", 65, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Stomping Tantrum", 75, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Stone Axe", 65, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Stone Edge", 100, "Rock", Constants.MOVE_CATS.Physical);
            new Move("Stored Power", 20, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Storm Throw", 60, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Strange Steam", 90, "Fairy", Constants.MOVE_CATS.Special);
            new Move("Strength", 80, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Struggle", 50, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Struggle Bug", 50, "Bug", Constants.MOVE_CATS.Special);
            new Move("Submission", 80, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Sucker Punch", 70, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Sunsteel Strike", 100, "Steel", Constants.MOVE_CATS.Physical);
            new Move("Supercell Slam", 100, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Superpower", 120, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Surf", 90, "Water", Constants.MOVE_CATS.Special);
            new Move("Surging Strikes", 25, "Water", Constants.MOVE_CATS.Physical);
            new Move("Swift", 60, "Normal", Constants.MOVE_CATS.Special);
            new Move("Synchronoise", 120, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Syrup Bomb", 60, "Grass", Constants.MOVE_CATS.Special);
            new Move("Tachyon Cutter", 50, "Steel", Constants.MOVE_CATS.Special);
            new Move("Tackle", 40, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Tail Slap", 25, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Take Down", 90, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Techno Blast", 120, "Normal", Constants.MOVE_CATS.Special);
            new Move("Temper Flare", 75, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Tera Blast", 80, "Normal", Constants.MOVE_CATS.Special);
            new Move("Tera Starstorm", 120, "Normal", Constants.MOVE_CATS.Special);
            new Move("Terrain Pulse", 50, "Normal", Constants.MOVE_CATS.Special);
            new Move("Thief", 60, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Thousand Arrows", 90, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Thousand Waves", 90, "Ground", Constants.MOVE_CATS.Physical);
            new Move("Thrash", 120, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Throat Chop", 80, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Thunder", 110, "Electric", Constants.MOVE_CATS.Special);
            new Move("Thunder Cage", 80, "Electric", Constants.MOVE_CATS.Special);
            new Move("Thunder Fang", 65, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Thunder Punch", 75, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Thunder Shock", 40, "Electric", Constants.MOVE_CATS.Special);
            new Move("Thunderbolt", 90, "Electric", Constants.MOVE_CATS.Special);
            new Move("Thunderclap", 70, "Electric", Constants.MOVE_CATS.Special);
            new Move("Thunderous Kick", 90, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Torch Song", 80, "Fire", Constants.MOVE_CATS.Special);
            new Move("Trailblaze", 50, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Tri Attack", 80, "Normal", Constants.MOVE_CATS.Special);
            new Move("Triple Arrows", 90, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Triple Axel", 20, "Ice", Constants.MOVE_CATS.Physical);
            new Move("Triple Dive", 30, "Water", Constants.MOVE_CATS.Physical);
            new Move("Triple Kick", 10, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Trop Kick", 70, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Twin Beam", 40, "Psychic", Constants.MOVE_CATS.Special);
            new Move("Twineedle", 25, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Twister", 40, "Dragon", Constants.MOVE_CATS.Special);
            new Move("U-turn", 70, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Upper Hand", 65, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Uproar", 90, "Normal", Constants.MOVE_CATS.Special);
            new Move("V-create", 180, "Fire", Constants.MOVE_CATS.Physical);
            new Move("Vacuum Wave", 40, "Fighting", Constants.MOVE_CATS.Special);
            new Move("Venoshock", 65, "Poison", Constants.MOVE_CATS.Special);
            new Move("Vine Whip", 45, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Vise Grip", 55, "Normal", Constants.MOVE_CATS.Physical);
            new Move("Vital Throw", 70, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Volt Switch", 70, "Electric", Constants.MOVE_CATS.Special);
            new Move("Volt Tackle", 120, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Wake-Up Slap", 70, "Fighting", Constants.MOVE_CATS.Physical);
            new Move("Water Gun", 40, "Water", Constants.MOVE_CATS.Special);
            new Move("Water Pledge", 80, "Water", Constants.MOVE_CATS.Special);
            new Move("Water Pulse", 60, "Water", Constants.MOVE_CATS.Special);
            new Move("Water Shuriken", 15, "Water", Constants.MOVE_CATS.Special);
            new Move("Water Spout", 150, "Water", Constants.MOVE_CATS.Special);
            new Move("Waterfall", 80, "Water", Constants.MOVE_CATS.Physical);
            new Move("Wave Crash", 120, "Water", Constants.MOVE_CATS.Physical);
            new Move("Weather Ball", 50, "Normal", Constants.MOVE_CATS.Special);
            new Move("Whirlpool", 35, "Water", Constants.MOVE_CATS.Special);
            new Move("Wicked Blow", 75, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Wicked Torque", 80, "Dark", Constants.MOVE_CATS.Physical);
            new Move("Wild Charge", 90, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Wildbolt Storm", 100, "Electric", Constants.MOVE_CATS.Special);
            new Move("Wing Attack", 60, "Flying", Constants.MOVE_CATS.Physical);
            new Move("Wood Hammer", 120, "Grass", Constants.MOVE_CATS.Physical);
            new Move("Wrap", 15, "Normal", Constants.MOVE_CATS.Physical);
            new Move("X-Scissor", 80, "Bug", Constants.MOVE_CATS.Physical);
            new Move("Zap Cannon", 120, "Electric", Constants.MOVE_CATS.Special);
            new Move("Zen Headbutt", 80, "Psychic", Constants.MOVE_CATS.Physical);
            new Move("Zing Zap", 80, "Electric", Constants.MOVE_CATS.Physical);
            new Move("Zippy Zap", 80, "Electric", Constants.MOVE_CATS.Physical);
        }
    }

    public static class Type {
        String name;
        String[] resistances;
        String[] weaknesses;
        String[] immunities;
        private static final String[] typeNames = new String[]{"Normal", "Fighting", "Flying", "Fire", "Grass", "Water", "Electric", "Ground", "Rock", "Dragon", "Poison", "Bug", "Ghost", "Psychic", "Ice", "Steel", "Dark", "Fairy"};
        //also maybe a section about fringe cases such as typeList.get("Grass") types being immune to spore and typeList.get("Ghost") types unable to be trapped

        public Type(String name) {
            this.name = name;
            typeList.put(name, this);
        }


        public static ArrayList[] returnAllMatchups(Type[] defender){
            ArrayList<String> neutral = new ArrayList<>();
            ArrayList<String> superEffective = new ArrayList<>();
            ArrayList<String> notVeryEffective = new ArrayList<>();
            ArrayList<String> immune = new ArrayList<>();

            for(String currentType:typeNames){
                double effectiveness = 1;
            
                for(Type i:defender){effectiveness*=returnOneMatchup(i, currentType);}

                if(effectiveness>1){superEffective.add(currentType);
                }else if(effectiveness<1&&effectiveness!=0){notVeryEffective.add(currentType);
                }else if(effectiveness==0){immune.add(currentType);
                }else{neutral.add(currentType);}
            }

            return new ArrayList[]{neutral,superEffective,notVeryEffective,immune};
        }

        private static double returnOneMatchup(Type defendingType, String attackingType){
            if(defendingType==null){return 1;}

            if(Arrays.asList(defendingType.weaknesses).contains(attackingType)) {return 2;
            }else if(Arrays.asList(defendingType.resistances).contains(attackingType)){return 0.5;
            }try{if(Arrays.asList(defendingType.immunities).contains(attackingType)) {return 0;}}catch(Exception _){}

            return 1;
        }

        public static void init(){
            //type database (inits all types into the array
            for(int i=0; i <=typeNames.length-1; i++){new Type(typeNames[i]);}

            //weaknesses
            typeList.get("Normal").weaknesses = new String[]{"Fighting"};
            typeList.get("Fighting").weaknesses = new String[]{"Flying", "Psychic", "Fairy"};
            typeList.get("Flying").weaknesses = new String[]{"Rock", "Electric", "Ice"};
            typeList.get("Fire").weaknesses = new String[]{"Water", "Ground", "Rock"};
            typeList.get("Grass").weaknesses = new String[]{"Poison", "Flying", "Ice", "Fire", "Bug"};
            typeList.get("Water").weaknesses = new String[]{"Electric", "Grass"};
            typeList.get("Electric").weaknesses = new String[]{"Ground"};
            typeList.get("Ground").weaknesses = new String[]{"Water", "Ice", "Grass"};
            typeList.get("Rock").weaknesses = new String[]{"Fighting", "Water", "Grass", "Ground", "Steel"};
            typeList.get("Dragon").weaknesses = new String[]{"Ice", "Fairy"};
            typeList.get("Poison").weaknesses = new String[]{"Ground", "Psychic"};
            typeList.get("Bug").weaknesses = new String[]{"Flying", "Rock", "Fire"};
            typeList.get("Dark").weaknesses = new String[]{"Fighting", "Fairy", "Bug"};
            typeList.get("Steel").weaknesses = new String[]{"Fighting", "Fire", "Ground"};
            typeList.get("Ghost").weaknesses = new String[]{"Ghost", "Dark"};
            typeList.get("Fairy").weaknesses = new String[]{"Steel", "Poison"};
            typeList.get("Psychic").weaknesses = new String[]{"Dark", "Ghost", "Bug"};
            typeList.get("Ice").weaknesses = new String[]{"Fighting", "Rock", "Fire", "Steel"};

            //resists
            typeList.get("Normal").resistances = new String[]{};
            typeList.get("Fighting").resistances = new String[]{"Rock", "Bug", "Dark"};
            typeList.get("Flying").resistances = new String[]{"Fighting", "Bug", "Grass"};
            typeList.get("Fire").resistances = new String[]{"Bug","Fire","Grass","Steel","Steel","Ice","Fairy"};
            typeList.get("Grass").resistances = new String[]{"Water","Electric","Grass","Ground"};
            typeList.get("Water").resistances = new String[]{"Fire","Water","Ice","Steel"};
            typeList.get("Electric").resistances = new String[]{"Flying","Steel","Electric"};
            typeList.get("Ground").resistances = new String[]{"Poison","Rock"};
            typeList.get("Rock").resistances = new String[]{"Normal","Fire","Poison","Flying"};
            typeList.get("Dragon").resistances = new String[]{"Fire","Water","Grass","Electric"};
            typeList.get("Poison").resistances = new String[]{"Fighting","Poison","Grass","Bug","Fairy"};
            typeList.get("Bug").resistances = new String[]{"Fighting","Ground","Grass"};
            typeList.get("Dark").resistances = new String[]{"Ghost", "Dark"};
            typeList.get("Steel").resistances = new String[]{"Normal","Flying","Rock","Bug","Steel","Grass","Psychic","Ice","Dragon","Fairy"};
            typeList.get("Ghost").resistances = new String[]{"Poison","Bug"};
            typeList.get("Fairy").resistances = new String[]{"Fighting","Bug","Dark"};
            typeList.get("Psychic").resistances = new String[]{"Fighting","Psychic"};
            typeList.get("Ice").resistances = new String[]{"Ice"};

            //immunities
            typeList.get("Normal").immunities = new String[]{"Ghost"};
            typeList.get("Flying").immunities = new String[]{"Ground"};
            typeList.get("Steel").immunities = new String[]{"Poison"};
            typeList.get("Ghost").immunities = new String[]{"Normal", "Fighting"};
            typeList.get("Fairy").immunities = new String[]{"Dragon"};
            }
        }

    public static class Items{
        public static double getItemEffect(String input, Constants.MOVE_CATS category){
            if(input.equals("Choice Band")&&category== Constants.MOVE_CATS.Physical){return 1.5;
            }else if(input.equals("Choice Specs")&&category== Constants.MOVE_CATS.Special){return 1.5;
            }else if(input.equals("Life Orb")){return 1.3;}
            return 1;
        }

        private static void init(){
            itemList.add("No Item/Other");
            itemList.add("Choice Band");
            itemList.add("Choice Specs");
            itemList.add("Choice Scarf"); //use when u make outspeed calc
            itemList.add("Life Orb");
            //itemList.add("Booster Energy [doesn't work rn]");
        }
    }

    public static class Nature{
        private final String name;

        private final double attack;
        private final double defense;
        private final double spatk;
        private final double spdef;
        private final double speed;
        private final double[] statsArray;

        private Nature(String name, double attack, double defense, double spatk, double spdef, double speed){
            this.name = name;
            this.attack = attack;
            this.defense = defense;
            this.spatk = spatk;
            this.spdef = spdef;
            this.speed = speed;

            statsArray = new double[]{attack,defense,spatk,spdef,speed};
            natureList.put(name, this);
        }

        public double getValue(Constants.Stats stat){
            return switch(stat){
                case Attack -> attack;
                case Defense -> defense;
                case Spatk -> spatk;
                case Spdef -> spdef;
                case Speed -> speed;
                default -> 1;
            };
        }

        public double[] getStatsArray(){return statsArray;}

        public String getName(){return this.name;}

        private static void init(){
            new Nature("+Attack",1.1,1,1,1,1);
            new Nature("-Attack",0.9,1,1,1,1);

            new Nature("+Defense",1,1.1,1,1,1);
            new Nature("-Defense",1,0.9,1,1,1);

            new Nature("+SpAtk",1,1,1.1,1,1);
            new Nature("-SpAtk",1,1,0.9,1,1);

            new Nature("+SpDef",1,1,1,1.1,1);
            new Nature("-SpDef",1,1,1,0.9,1);

            new Nature("+Speed",1,1,1,1,1.1);
            new Nature("-Speed",1,1,1,1,0.9);

            new Nature("Neutral",1,1,1,1,1);

        }
    }

    //ughhh do this shit later
    public static class Ability{
        private static void init(){
            abilityList.add("Other Ability");
            abilityList.add("Swift Swim");
            abilityList.add("Chlorophyll");
            abilityList.add("Huge Power");
            abilityList.add("Thick Fat");
            abilityList.add("Hustle");
            abilityList.add("Guts");
            abilityList.add("Marvel Scale");
            abilityList.add("Pure Power");
            abilityList.add("Heatproof");
            abilityList.add("Dry Skin");
            abilityList.add("Iron Fist");
            abilityList.add("Adaptability");
            abilityList.add("Solar Power");
            abilityList.add("Quick Feet");
            abilityList.add("Technician");
            abilityList.add("Tinted Lens");
            abilityList.add("Filter");
            abilityList.add("Scrappy");
            abilityList.add("Solid Rock");
            abilityList.add("Reckless");
            abilityList.add("Flower Gift");
            abilityList.add("Sheer Force");
            abilityList.add("Sand Rush");
            abilityList.add("Analytic");
            abilityList.add("Sap Sipper");
            abilityList.add("Sand Force");
            abilityList.add("Fur Coat");
            abilityList.add("Strong Jaw");
            abilityList.add("Refrigerate");
            abilityList.add("Mega Launcher");
            abilityList.add("Grass Pelt");
            abilityList.add("Tough Claws");
            abilityList.add("Pixilate");
            abilityList.add("Aerilate");
            abilityList.add("Parental Bond");
            abilityList.add("Dark Aura");
            abilityList.add("Fairy Aura");
            abilityList.add("Delta Stream");
            abilityList.add("Steelworker");
            abilityList.add("Slush Rush");
            abilityList.add("Galvanize");
            abilityList.add("Surge Surfer");
            abilityList.add("Fluffy");
            abilityList.add("Shadow Shield");
            abilityList.add("Prism Armor");
            abilityList.add("Neuroforce");
            abilityList.add("Ice Scales");
            abilityList.add("Steely Spirit");
            abilityList.add("Gorilla Tactics");
            abilityList.add("Transistor");
            abilityList.add("Dragon's Maw");
            abilityList.add("Purifying Salt");
            abilityList.add("Well-Baked Body");
            abilityList.add("Wind Rider");
            abilityList.add("Rocky Payload");
            abilityList.add("Protosynthesis");
            abilityList.add("Quark Drive");
            abilityList.add("Vessel of Ruin");
            abilityList.add("Sword of Ruin");
            abilityList.add("Tablets of Ruin");
            abilityList.add("Beads of Ruin");
            abilityList.add("Orichalcum Pulse");
            abilityList.add("Hadron Engine");
            abilityList.add("Sharpness");
            abilityList.add("Mind's Eye");
        }
    }
}