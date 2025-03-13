import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class FileReader{
    public static void initReader(){
        final File databaseFile = Constants.DatabaseXML;
        try {

            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); //this method allows you to make document builders
            final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //this method parses xml files
            final Document database = dBuilder.parse(databaseFile); //this is the parsed xml file
            database.getDocumentElement().normalize();


            if (Constants.DEBUG_DB_MODE){debugPrint(database);}

            NodeList allPKMN = database.getElementsByTagName("pokemon");

            for(int temp = 0; temp<allPKMN.getLength(); temp++) {

                final Element currentPKMN = (Element) allPKMN.item(temp);

                final int stats[] = new int[6];
                for(int i=0; i<Constants.STATS.length; i++){
                    if(Constants.DEBUG_DB_MODE){System.out.println("CURRENT STAT: "+Constants.STATS[i]);}
                    stats[i] = Integer.parseInt(getItem(Constants.STATS[i].toLowerCase(),currentPKMN));
                }
                
                final Database.Type[] typeList = new Database.Type[]{Database.getType(getItem("type1",currentPKMN)),Database.getType(getItem("type2",currentPKMN))};
                final String name = getItem("name",currentPKMN);
                final String dex = getItem("id",currentPKMN);

                new Database.Pokemon(Integer.parseInt(dex),name,typeList,stats);
            }

        }catch(Exception e){e.printStackTrace();}
    }

private static String getItem(String item, Element element){return element.getElementsByTagName(item.toLowerCase()).item(0).getTextContent();}
    private static void debugPrint(Document file){


        if(Constants.DEBUG_DB_MODE) {
            System.out.println("Root element :" + file.getDocumentElement().getNodeName());
            System.out.println("----------------------------");
        }

        NodeList nList = file.getElementsByTagName("pokemon");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println("id : "
                        + eElement.getAttribute("id"));
                System.out.println("name : "
                        + eElement.getElementsByTagName("name")
                        .item(0).getTextContent());
                System.out.println("type1 : "
                        + eElement.getElementsByTagName("type1")
                        .item(0).getTextContent());
                System.out.println("type2 : "
                        + eElement.getElementsByTagName("type2")
                        .item(0).getTextContent());

                for(String currentStat:Constants.STATS){
                    System.out.println(currentStat+": "+eElement.getAttribute(currentStat.toLowerCase()));
                }
            }
        }
    }
}
