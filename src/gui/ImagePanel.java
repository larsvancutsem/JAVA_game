package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Sachi
 */
public class ImagePanel extends JPanel{

    private BufferedImage image;

    public ImagePanel(String fileName) {
       try {                
          image = ImageIO.read(new File(fileName));
       } catch (IOException ex) {
            System.out.println("Error: " + ex);
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // see javadoc for more info on the parameters 
    }

}
