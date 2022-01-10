package domain;

import panel.ElfBootPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ElfBoot extends JLabel {

    private String filepathToRepo = ".";
    private String colour;
    private int width;
    private int height;
    private boolean selected = false;
    private ElfBootPanel curPanel;
    private JPanel curSpotInPanel;
    private int curSpotNumInPanel;
    private JLabel bootImage;


    public ElfBoot(String pColour, int pWidth, int pHeight, ElfBootPanel pCurPanel, int pCurSpot) {

        this.colour = pColour;
        this.width = pWidth;
        this.height = pHeight;
        this.curPanel = pCurPanel;
        this.curSpotNumInPanel = pCurSpot;
        this.curSpotInPanel = pCurPanel.getSpotByNumber(curSpotNumInPanel);

        ImageIcon bootIcon = new ImageIcon(filepathToRepo + "/assets/boppels-and-boots/boppel-" + this.colour + ".png");
        Image bootImage = bootIcon.getImage();
        Image bootResized = bootImage.getScaledInstance(width*15/1440, height*15/900,  java.awt.Image.SCALE_SMOOTH);
        this.bootImage = new JLabel(new ImageIcon(bootResized));

        super.setIcon(bootIcon);

        this.bootImage.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // this toggles the elfBootSelected to determine if it is possible to move a boot
                ElfBoot.this.setSelected(true);
            }
        });
    }

    public boolean getSelected() { return this.selected; }

    public void setSelected(boolean pSelected) {
        this.selected = pSelected;
    }

    public ElfBootPanel getCurPanel() { return this.curPanel; }

    public void setCurPanelAndSpot(ElfBootPanel pCurPanel) {
        this.curPanel = pCurPanel;
        this.curSpotInPanel = curPanel.fillFirstAvailableSpot();
    }

    public JPanel getCurSpotInPanel() { return this.curSpotInPanel; }

    public int getCurSpotNumInPanel() { return this.curSpotNumInPanel; }

    public JLabel getImage() { return this.bootImage; }
}
