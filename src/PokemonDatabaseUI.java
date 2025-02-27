import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PokemonDatabaseUI extends Database{
    //!!!!! fuck it bro just do the ui first and then make it not bad
    static final HashMap<String, Component> ComponentMap = new HashMap<>();

    public static void initUI(){

        final String[] natDex = HelperMethods.arrayListToArray(getNatDexAsArrayList());
        final String[] effectiveness = {"Type", "Neutral","Super Effective","Resisted"};

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

        //most definitely an easier way to do this. i just dont care rn
        ((JLabel)ComponentMap.get("HP")).setText("HP: "+pokemon.baseHP);
        ((JLabel)ComponentMap.get("Attack")).setText("Attack: "+pokemon.baseAttack);
        ((JLabel)ComponentMap.get("Defense")).setText("Defense: "+pokemon.baseDefense);
        ((JLabel)ComponentMap.get("Special Attack")).setText("Special Attack: "+pokemon.baseSpatk);
        ((JLabel)ComponentMap.get("Special Defense")).setText("Special Defense: "+pokemon.baseSpdef);
        ((JLabel)ComponentMap.get("Speed")).setText("Speed: "+pokemon.baseSpeed);

        ((JLabel)ComponentMap.get("Type")).setText("Type: "+typeConstruct(pokemon.types));

        ArrayList<String>[] typeMatchups = Type.returnAllMatchups(pokemon.types); //index 0 is neutral index 1 is super index 2 is not effective index 3 is immune

        ((JLabel)ComponentMap.get("Neutral")).setText("Neutral: "+typeMatchups[0]);
        ((JLabel)ComponentMap.get("Super Effective")).setText("Super Effective: "+typeMatchups[1]);
        ((JLabel)ComponentMap.get("Not Very Effective")).setText("Resisted: "+typeMatchups[2]);
        ((JLabel)ComponentMap.get("Immune")).setText("Immune: "+typeMatchups[3]);

    }

    private static String typeConstruct(Type[] types){
        String output = "["+types[0].name;
        if(types.length>1){output += ", "+types[1].name;}

        return output+"]";
    }
}
