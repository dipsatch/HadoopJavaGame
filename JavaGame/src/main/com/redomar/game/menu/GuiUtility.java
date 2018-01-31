//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.redomar.game.menu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public class GuiUtility {
    public GuiUtility() {
    }

    public static void centerOnScreen(Window w) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension splashSize = w.getPreferredSize();
        w.setLocation(screenSize.width / 2 - splashSize.width / 2, screenSize.height / 2 - splashSize.height / 2);
    }
}
