import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.*;

public class AttackOptimizer{
    public static void initUI() throws IOException {
        final JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setVisible(true);

        final JPanel panel = new JPanel();
        panel.setBounds(0,0,panel.getWidth(),panel.getHeight());
        panel.setLayout(null);
        frame.add(panel);


        final File file = new File("assets/gallade.png"); //the file itself. (just the data the image isnt actually read)
        final ImageIcon icon = new ImageIcon(ImageIO.read(file)); //ImageIO actually reads the data from the image and then that data is set to an icon
        final JLabel test = new JLabel();
        test.setIcon(icon);
        test.setBounds(50,50,50,50);
        panel.add(test);
    }
}
