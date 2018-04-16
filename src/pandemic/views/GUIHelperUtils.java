package pandemic.views;

import javax.swing.*;
import java.util.Arrays;

public class GUIHelperUtils {

    public static void setComponentVisibilty(boolean visible, JComponent... components) {
        Arrays.stream(components).forEach(c -> c.setVisible(visible));
    }
}
