import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class AttackOptimizer{
    //make the opponent a different panel and u a different panel cause im TIRED of resizing shit we are a layout manager household now
    public static void initUI() throws IOException{
        final int BOUNDS = 5; //how far each image should be padded
        final int SIZE = 500;

        final JFrame frame = new JFrame();
        frame.setSize(SIZE,SIZE);
        frame.setVisible(true);

        final JPanel panel = new JPanel();
        panel.setBounds(0,0,panel.getWidth(),panel.getHeight());
        panel.setLayout(null);
        frame.add(panel);


        //gallade is placeholder rn i dont have the files for everyone else

        //image display for your pokemon
        final File yourIMGFile = new File("src/assets/gallade.png"); //the opponentsPokemonIMGFile itself. (just the data the image isnt actually read)
        final ImageIcon yourIcon = new ImageIcon(ImageIO.read(yourIMGFile)); //ImageIO actually reads the data from the image and then that data is set to an icon
        //the label that actually displays it
        final JLabel youDisplayLabel = new JLabel(yourIcon);
        youDisplayLabel.setBounds(BOUNDS,BOUNDS,96,96);
        panel.add(youDisplayLabel);

        //image display for your pokemon
        final File opponentIMGFile = new File("src/assets/gallade.png"); //the opponentsPokemonIMGFile itself. (just the data the image isnt actually read)
        final ImageIcon opponentIcon = new ImageIcon(ImageIO.read(opponentIMGFile)); //ImageIO actually reads the data from the image and then that data is set to an icon
        //the label that actually displays it
        final JLabel opponentDisplayLabel = new JLabel(opponentIcon);
        opponentDisplayLabel.setBounds(SIZE-150,BOUNDS,96,96);
        panel.add(opponentDisplayLabel);
    }
}
