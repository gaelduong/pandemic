package pandemic.views;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
/**
 * 
 * Purpose: "dye" the image, change from color A to color B, e.g used when epidemic occurs: map's color changes to green
 * 
 */
//in progress
class DyeImage
{
    public static void main(String args[])
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    new DyeImage();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

    public DyeImage() throws Exception
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*ImageIcon img = new ImageIcon(GUI.class.getResource("/pandemic/resources/blueC.png"));
        Image image = img.getImage();
        JPanel panel = new JPanel(new GridLayout(1,0));
        panel.add(new JLabel(new ImageIcon(image)));
        panel.add(new JLabel(new ImageIcon(dye(image, new Color(255,0,0,128)))));
        panel.add(new JLabel(new ImageIcon(dye(image, new Color(255,0,0,32)))));
        panel.add(new JLabel(new ImageIcon(dye(image, new Color(0,128,0,32)))));
        panel.add(new JLabel(new ImageIcon(dye(image, new Color(0,0,255,32)))));
        f.getContentPane().add(panel);
        f.pack();
        f.setVisible(true);*/
    }


    public static Image dye(Image image, Color color)
    {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        BufferedImage dyed = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dyed.createGraphics();
        g.drawImage(image, 0,0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0, 0,980, 500);
        g.dispose();
        return dyed;
    }

}
