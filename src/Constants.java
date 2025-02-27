public class Constants{
    public static final boolean DEBUG_CALC_MODE = true;
    public static final boolean DEBUG_UI_MODE = false;
    public static final boolean DEBUG_DISABLE_OUTPUT = false;

    public static final String[] BOOSTS = {"+0","-6","-5","-4","-3","-2","-1","+1","+2","+3","+4","+5","+6"};
    public static final String[] STATS = {"HP","Attack","Defense","Special Attack","Special Defense","Speed"};

    public static final double LOWEST_ROLL = 0.85;
    public static final double MEDIAN_ROLL = 0.92;
    public static final double HIGHEST_ROLL = 1;
    public static final double[] ROLLS = {LOWEST_ROLL,MEDIAN_ROLL,HIGHEST_ROLL};

    public static boolean isSpriteErrorPrinted = false;

    public enum MOVE_CATS{
        Physical,
        Special,
    }

    public static final String[] CAPABILITY_LIST = {"OHKO","Tank","Outspeed"};

    public static final String[] WEATHER_LIST = {"No Weather","Sun","Rain","Sand","Snow"};


}
