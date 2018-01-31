//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.redomar.game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public final class JSplashLabel extends JLabel {
    private static final long serialVersionUID = -774117609679903588L;
    private String m_text = null;
    private Font m_font = null;
    private Color m_color = null;

    public JSplashLabel(URL url, String s, Font f, Color c) {
        ImageIcon icon = new ImageIcon(url);
        if (icon.getImageLoadStatus() != 8) {
            System.err.println("Cannot load splash screen: " + url);
            this.setText("Cannot load splash screen: " + url);
        } else {
            this.setIcon(icon);
            this.m_text = s;
            this.m_font = f;
            this.m_color = c;
            if (this.m_font != null) {
                this.setFont(this.m_font);
            }
        }

    }

    public JSplashLabel(URL url, String s) {
        ImageIcon icon = new ImageIcon(url);
        if (icon.getImageLoadStatus() != 8) {
            System.err.println("Cannot load splash screen: " + url);
            this.setText("Cannot load splash screen: " + url);
        } else {
            this.setIcon(icon);
            this.m_text = s;
            if (this.m_font != null) {
                this.setFont(this.m_font);
            }
        }

    }

    public void paint(Graphics g) {
        super.paint(g);
        if (this.m_text != null) {
            if (this.m_color != null) {
                g.setColor(this.m_color);
            }

            FontMetrics fm = g.getFontMetrics();
            int width = fm.stringWidth(this.m_text) + 20;
            int height = fm.getHeight();
            g.drawString(this.m_text, this.getWidth() - width, this.getHeight() - height);
        }

    }
}
