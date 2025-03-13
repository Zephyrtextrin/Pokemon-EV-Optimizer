import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class FileReader{
    public static String getAtt(){
        final File databaseFile = Constants.DatabaseXML;
        try {

            final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance(); //this method allows you to make document builders
            final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder(); //this method parses xml files
            final Document database = dBuilder.parse(databaseFile); //this is the parsed xml file
            database.getDocumentElement().normalize();


            if (Constants.DEBUG_DB_MODE) {
                debugPrint(database);
            }

            NodeList allPKMN = database.getElementsByTagName("pokemon");

            for(int temp = 0; temp<allPKMN.getLength(); temp++) {

                Element currentPKMN = (Element) allPKMN.item(temp);

                int stats[] = new int[6];

                for(int i=0; i<Constants.STATS.length; i++){
                    System.out.println("CURRENT STAT: "+);
                    stats[i] = Integer.parseInt(currentPKMN.getElementsByTagName(Constants.STATS[i].toLowerCase()).item(0).getTextContent());

                }

            }

        }catch(Exception e){e.printStackTrace();}
        return "-1";
    }

    public static void debugPrint(Document file){


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
