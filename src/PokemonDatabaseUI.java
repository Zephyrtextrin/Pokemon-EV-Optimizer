import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PokemonDatabaseUI extends Database{
    //!!!!! fuck it bro just do the ui first and then make it not bad
    static final HashMap<String, Component> ComponentMap = new HashMap<>();

    public static void initUI() throws IOException {

        final String[] natDex = HelperMethods.arrayListToArray(getNatDexAsArrayList());

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

        JLabel HPLabel = new JLabel("HP: ");
        panel.add(HPLabel);
        ComponentMap.put("HP",HPLabel);

        JLabel atkLabel = new JLabel("Attack: ");
        panel.add(atkLabel);
        ComponentMap.put("Attack",atkLabel);

        JLabel defLabel = new JLabel("Defense: ");
        panel.add(defLabel);
        ComponentMap.put("Defense",defLabel);

        JLabel spatkLabel = new JLabel("Special Attack: ");
        panel.add(spatkLabel);
        ComponentMap.put("Special Attack",spatkLabel);

        JLabel spdefLabel = new JLabel("Special Defense: ");
        panel.add(spdefLabel);
        ComponentMap.put("Special Defense",spdefLabel);

        JLabel speedLabel = new JLabel("Speed: ");
        panel.add(speedLabel);
        ComponentMap.put("Speed",speedLabel);

        JLabel typeLabel = new JLabel("Type: ");
        panel.add(typeLabel);
        ComponentMap.put("Type",typeLabel);

        JLabel neutralLabel = new JLabel("Neutral: ");
        panel.add(neutralLabel);
        ComponentMap.put("Neutral", neutralLabel);

        JLabel superEffectiveLabel = new JLabel("Super Effective: ");
        panel.add(superEffectiveLabel);
        ComponentMap.put("Super Effective", superEffectiveLabel);

        JLabel notEffectiveLabel = new JLabel("Not Very Effective: ");
        panel.add(notEffectiveLabel);
        ComponentMap.put("Not Very Effective", notEffectiveLabel);

        JLabel immuneLabel = new JLabel("Immune: ");
        panel.add(immuneLabel);
        ComponentMap.put("Immune", immuneLabel);

        refresh();
        panel.revalidate();

        pokemon.addActionListener(_ -> {
            try {
                refresh(); //ughhh i hate having to put try catches everywhere cause of the ioExceptionssssss
                panel.repaint();
                panel.revalidate();
            }catch(IOException e){throw new RuntimeException(e);}
        });
    }

    private static void refresh() throws IOException{
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
