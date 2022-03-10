package panel;

import javax.swing.*;

public class MenuPanel {

    JMenu menu, submenu;
    JMenuItem i1, i2, i3, i4, i5;

    MenuPanel(){
        JFrame f = new JFrame("Menu");
        JMenuBar mb = new JMenuBar();
        menu = new JMenu("Menu");
        submenu = new JMenu("Rules");

        i1 = new JMenuItem("Save");
        i2 = new JMenuItem("Load");
        i3 = new JMenuItem("Chat");
        i4 = new JMenuItem("Elfenland");
        i5 = new JMenuItem("Elfengold");

        menu.add(i1);
        menu.add(i2);
        menu.add(i3);
        submenu.add(i4);
        submenu.add(i5);
        menu.add(submenu);
        mb.add(menu);
        f.setJMenuBar(mb);
        f.setSize(300,300);
        f.setLayout(null);
        f.setVisible(true);

    }

/*public static void main(String args[])  {  
    new MenuPanel();  
}} */
    
    
    
    



    

