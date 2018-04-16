package pandemic.views;

import javax.swing.*;
import java.util.Arrays;

public class GUIHelperUtils {

    public static void setComponentVisibilty(boolean visible, JLabel... components) {
        Arrays.stream(components).forEach(c -> {
            System.out.println("Setting " + c.getName() + " to " + visible);
            c.setVisible(visible);
        });
        GUI.getInstance().revalidate();
        GUI.getInstance().repaint();
    }
}
