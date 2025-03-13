import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

public class Main{
    public static void main(String[] args) throws ParserConfigurationException {
        Database.initialize(); //loads the pokedex
        initReader();
        Constants.Attribute.init();
        final int SIZE = 200;

        final JFrame frame = new JFrame();
        frame.setSize(SIZE*3, SIZE);
        frame.setLayout(new GridLayout());
        frame.setVisible(true);

        final JButton ev = new JButton("EV Optimizer");
        frame.add(ev);

        final JButton db = new JButton("Pokemon Database");
        frame.add(db);

        db.addActionListener(_-> PokemonDatabaseUI.initUI());

        ev.addActionListener(_-> {
            try{EVCalculatorUI.initUI();
            }catch(IOException e) {throw new RuntimeException(e);}
        });

        frame.repaint();
        frame.revalidate();

        initReader();
    }

    
    private static void initReader() throws ParserConfigurationException {
        final File databaseFile = Constants.DatabaseXML;
        try {
            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); //this method allows you to make document builders
            final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //this method parses xml files
            final Document database = dBuilder.parse(databaseFile); //this is the parsed xml file
            database.getDocumentElement().normalize();

            initAllPokemon(database);
        }catch(IOException|SAXException e){throw new RuntimeException(e);}
    }
    
    private static void initAllPokemon(Document database){
        final NodeList allPKMN = database.getElementsByTagName("pokemon");

        for(int temp = 0; temp<allPKMN.getLength(); temp++){
            final Element currentPKMN = (Element)allPKMN.item(temp);

            final String name = getItem("name",currentPKMN);

            Database.Type[] typeList;

            try{typeList = new Database.Type[]{Database.getType(getItem("type1",currentPKMN)),Database.getType(getItem("type2",currentPKMN))};}
            catch (NullPointerException e){typeList=new Database.Type[]{Database.getType(getItem("type1",currentPKMN))};}

            final int[] stats = new int[6];
            if(Constants.DEBUG_DB_MODE){System.out.println("\n---[DEBUG: INITIALIZING STATS]---\nPokemon name: "+name+"\n");}
            for(int i=0; i<Constants.STATS.length; i++){
                String currentStat = Constants.STATS[i];
                if(currentStat.equalsIgnoreCase("Special Attack")){currentStat = "Spatk";}
                else if(currentStat.equalsIgnoreCase("Special Defense")){currentStat = "Spdef";}

                if(Constants.DEBUG_DB_MODE){System.out.println("CURRENT STAT: "+currentStat+"\nVALUE: "+Integer.parseInt(getItem(currentStat,currentPKMN))+"\n");}

                stats[i] = Integer.parseInt(getItem(currentStat,currentPKMN));
            }
            if(Constants.DEBUG_DB_MODE){
                System.out.println("\n---[END.]---\n");
            }

            final int dex = Integer.parseInt(currentPKMN.getAttribute("id"));
            final Database.Pokemon finalPokemon = new Database.Pokemon(dex,name,typeList,stats);
            
            if(Constants.DEBUG_DB_MODE){
                System.out.printf("\n---[DEBUG: POKEMON INITIALIZED]---\nPokemon %s has been initialized.\n\nType 1: %s\nType 2: %s\n\nHP: %s\nAttack: %s\nDefense: %s\nSpecial Attack: %s\nSpecial Defense: %s\nSpeed: %s\n\n---[END.]---\n", finalPokemon.name,finalPokemon.types[0].name, finalPokemon.types[1].name, finalPokemon.stats[0],finalPokemon.stats[1],finalPokemon.stats[2],finalPokemon.stats[3],finalPokemon.stats[4],finalPokemon.stats[5]);
                System.out.println("GET HP VIA METHOD: "+finalPokemon.getStat(Constants.Stats.HP));
            }

        }
    }

    private static String getItem(String item, Element element){return element.getElementsByTagName(item.toLowerCase()).item(0).getTextContent();}

    /*private static void initAllDatabase.Types(Document Database){
        
    }*/
        
}


