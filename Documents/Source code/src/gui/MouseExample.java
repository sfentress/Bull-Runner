package gui;

/**
 *
 * @author Sam Fentress
 * @version 0.01
 */

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class MouseExample extends JFrame {
    
    /** Creates a new instance of MouseExample */
    public MouseExample() {
        showFrame();
        hideMouse();
    }
    
    private void showFrame(){
        
        
        //   GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //   GraphicsDevice device = env.getScreenDevices()[0];   //devices is an array of monitors
        
        //   DisplayMode oldMode = device.getDisplayMode();
        
        //   this.setSize(new Dimension(oldMode.getWidth(), oldMode.getHeight()));
        //   device.setFullScreenWindow(this);
        
        this.setSize(100,100);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void hideMouse(){
     /*   BufferedImage image = getGraphicsConfiguration().createCompatibleImage(1, 1, Transparency.BITMASK);
        Graphics2D g = image.createGraphics();
        g.setBackground(new Color(0,0,0,0));
        g.clearRect(0,0,1,1);
        
        Cursor invisibleCursor = getToolkit().createCustomCursor(
                image, new Point(0,0), "Invisible");
        this.setCursor(invisibleCursor);
     //*/   
        
        // ***** Neither this:
        
        ImageIcon emptyIcon = new ImageIcon(new byte[0]);
        Cursor invisibleCursor = getToolkit().createCustomCursor(
                emptyIcon.getImage(), new Point(0,0), "Invisible");
        this.setCursor(invisibleCursor);
         // */
        // ***** nor this:
        /*
        Toolkit tk = Toolkit.getDefaultToolkit();
        Cursor invisCursor = tk.createCustomCursor(tk.createImage(""),new Point(),null);
        this.setCursor(invisCursor);
         */
        
        // ***** works
        
    }
    
    public static void main(String[] args) {
        MouseExample m = new MouseExample();
    }
    
}
