/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package os.assistant;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Panel;

/**
 *
 * @author Daniel
 */
public class Overlay extends Panel {

    private Graphics gfx;
    private int diff;
    private String skill = "";
    private int total;
    public Overlay() {
        super();
        this.setLayout(null);
        this.setBackground(new Color(0,0,0, 128));
        this.setLocation(0, 0);
        this.setSize(120,45);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.GREEN);
        g.drawString("+" + diff + " " + skill + " XP", 5, 15);
        g.drawString("Total: " + total + " XP", 5, 25);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g); //To change body of generated methods, choose Tools | Templates.
     
    }

    @Override
    public void paintAll(Graphics g) {
        super.paintAll(g); //To change body of generated methods, choose Tools | Templates.

    }

    public void setXPDropValue(int diff) {

        this.diff = diff;
        repaint();

    }

    public void setXPDropSkill(String name) {
        this.skill = name;
        invalidate();
    }
    
    public void setTotalXP(int total) {
        this.total = total;
    }
}
