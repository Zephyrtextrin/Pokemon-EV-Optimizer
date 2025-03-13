import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main{
    public static void main(String[] args){
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

    
    private static void initReader(){
        final File databaseFile = Constants.DatabaseXML;
        try {
            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); //this method allows you to make document builders
            final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //this method parses xml files
            final Document database = dBuilder.parse(databaseFile); //this is the parsed xml file
            database.getDocumentElement().normalize();

            initAllPokemon(database);
        }catch{throw Exeception e;}
    }
    
    private static void initAllPokemon(Document database){
        final NodeList allPKMN = database.getElementsByTagName("pokemon");

        for(int temp = 0; temp<allPKMN.getLength(); temp++){
                
            final Element currentPKMN = (Element)allPKMN.item(temp);

            final String name = getItem("name",currentPKMN);
            final Type[] typelist = new Type[]{getItem("type1",currentPKMN),getItem("type2",currentPKMN)}

            final int stats[] = new int[6];
            for(int i=0; i<Constants.STATS.length; i++){
                if(Constants.DEBUG_DB_MODE){System.out.println("CURRENT STAT: "+Constants.STATS[i])};
                stats[i] = Integer.parseInt(getItem(currentPKMN, Constants.Stats[i]);
            }
                
            new Pokemon(name,typeList,stats);
            
            if(Constants.DEBUG_DB_MODE){
                System.out.printf(
                    "\n---[DEBUG: POKEMON INITIALIZED]---\n
                Pokemon %s has been initialized.
                    \nID: %d
                    \nType 1: %s
                    \nType 2: %s
                    \nHP: %d
                    \nAttack: %d
                    \nDefense: %d
                    \nSpecial Attack: %d
                    \nSpecial Defense: %d
                    \nSpeed: %d 
                    \n---[END.]---\n",
                    name, getItem(CurrentPKMN,"type1"), getItem(CurrentPKMN,"type2"),
                    getItem(CurrentPKMN,"hp"),getItem(CurrentPKMN,"attack"),getItem(CurrentPKMN,"defense")
                    getItem(CurrentPKMN,"spatk"),getItem(CurrentPKMN,"spdef"),getItem(CurrentPKMN,"speed")
                    );
            }
        }
    }

    /*private static void initAllTypes(Document Database){
        
    }*/
        
}


