import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PokemonDatabaseUI extends Database{
    static final HashMap<String, Component> ComponentMap = new HashMap<>();

    public static void initUI(){

        final String[] natDex = HelperMethods.arrayListToArray(getNatDexAsArrayList());
        final String[] effectiveness = {"Type", "Neutral","Super Effective","Resisted","Immune"};

        final int SIZE = 500;
        final JFrame frame = new JFrame();
        frame.setLayout(new GridLayout());
        frame.setSize(SIZE, SIZE);
        frame.setVisible(true);

        JPanel panel = new JPanel(new GridLayout(-1,1));
        frame.add(panel);

        JComboBox<String> pokemon = new JComboBox<>(natDex);
        panel.add(pokemon);
        ComponentMap.put("Pokemon",pokemon);

        for(String currentStat:Constants.STATS){
            final JLabel label = new JLabel(currentStat+": ");
            panel.add(label);
            ComponentMap.put(currentStat,label);
        }

        for(String currentEntry :effectiveness){
            final JLabel label = new JLabel(currentEntry +": ");
            panel.add(label);
            ComponentMap.put(currentEntry,label);
        }

        refresh();
        panel.revalidate();

        pokemon.addActionListener(_ -> {
            refresh(); //ughhh i hate having to put try catches everywhere cause of the ioExceptionssssss
            panel.repaint();
            panel.revalidate();
        });
    }

    private static void refresh(){
        final Pokemon pokemon = getPokemon(HelperMethods.getComponentValue("Pokemon", false));

        //u use a regular forloop here instead of an enhanced one because u have to get the index of both the pokemon's stats array and the array from constants
        for(int i =0;i<Constants.STATS.length;i++) {((JLabel) ComponentMap.get(Constants.STATS[i])).setText(Constants.STATS[i]+": " + pokemon.stats[i]);}

        ((JLabel)ComponentMap.get("Type")).setText("Type: "+typeConstruct(pokemon.types));
        ArrayList<String>[] typeMatchups = Type.returnAllMatchups(pokemon.types); //index 0 is neutral index 1 is super index 2 is not effective index 3 is immune

        ((JLabel)ComponentMap.get("Neutral")).setText("Neutral: "+typeMatchups[0]);
        ((JLabel)ComponentMap.get("Super Effective")).setText("Super Effective: "+typeMatchups[1]);
        ((JLabel)ComponentMap.get("Resisted")).setText("Resisted: "+typeMatchups[2]);
        ((JLabel)ComponentMap.get("Immune")).setText("Immune: "+typeMatchups[3]);

    }

    private static String typeConstruct(Type[] types){
        String output = "["+types[0].name;
        if(types.length>1){output += ", "+types[1].name;}

        return output+"]";
    }
}
