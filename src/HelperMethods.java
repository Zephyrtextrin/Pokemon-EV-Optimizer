import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class HelperMethods {

    public static String[] arrayListToArray(ArrayList<String> arrayList) {
        final int size = arrayList.size();
        final String[] array = new String[size];

        for (int i = 0; i < size; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    public static String[] getMapAsList(Set<String> set){
        final ArrayList<String> list = new ArrayList<>(set);
        Collections.sort(list);
        return HelperMethods.arrayListToArray(list);
    }

    public static String[] parseAllNums(double[] list){
        final String[] result = new String[list.length];
        for(int i=0;i<list.length;i++){
            result[i] = String.valueOf(list[i]);
        }
        return result;
    }

    public static String getComponentValue(String name, boolean isEV){
        int max = 255;
        if(name.equals("Left-Side Level")||name.equals("Right-Side Level")){max=100;}

        HashMap<String,Component> ComponentMap = EVCalculatorUI.componentMap;
        if(!isEV){ComponentMap = PokemonDatabaseUI.ComponentMap;}

        final Component component = ComponentMap.get(name);

        String tempString = "";
        if(component==null){
            Printer.setDetails(name,false);
            Printer.errorHandler(Printer.ERROR_CODE.ERR_UI_UNKNOWN_COMPONENT,new NullPointerException());
        }

        assert component != null;
        if(component.getClass()==JComboBox.class){
            try{tempString = Objects.requireNonNull(((JComboBox<String>) component).getSelectedItem()).toString();}catch(Exception e){
                Printer.setDetails(name,false);
                Printer.errorHandler(Printer.ERROR_CODE.ERR_UI_COMPONENT_DATA_DNE, e);
            }
        }else if(component.getClass()==JTextField.class){
            tempString = ((JTextField)component).getText();
            int toInt = 1;
            try{
                toInt = Integer.parseInt(tempString);
                if(toInt>max||toInt<-6){toInt = 1;}
            }catch(Exception _){}
            tempString = Integer.toString(toInt);
        }else{
            System.out.println("error!\nspecified component: "+name+"\nclass: "+component.getClass());
        }
        return tempString;
    }

    public static File getSpriteFile(String name) {
        File file = new File("src/assets/0.png");
        if (!Objects.equals(name, "0")) {
            int dex = Database.getPokemon(name).dexNumber;

            try {
                file = new File("src/assets/" + dex + ".png");
            } catch (Exception e) {
                Printer.setDetails("src/assets/" + dex + ".png", false);
                Printer.errorHandler(Printer.ERROR_CODE.ABN_UI_MALFORMED_IMAGE_FILE, e);
            }
        }
        if (!file.exists()) {
            if (Database.getPokemon(name).dexNumber >= 906&&!Constants.isSpriteErrorPrinted) {
                System.out.println("Nothing is broken. This is entirely a visual glitch.\nAll Paldea Pokemon don't have sprites available for bulk download.\nAlternate forms such as Regionals and Megas have sprites but it needs me to manually change the file name and the dex number in the codebase so its all still a work in progress.\nstay tuned.");
                Constants.isSpriteErrorPrinted = true;
            }else if(Database.getPokemon(name).dexNumber<=906){
                Printer.setDetails(file.toString(), false);
                Printer.errorHandler(Printer.ERROR_CODE.ABN_UI_MALFORMED_IMAGE_FILE, null);
            }
            file = new File("src/assets/0.png");

        }
        return file;
    }
}
