
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class TownPanel extends JPanel {

    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private GameScreen gameScreen;
    private ElfBootPanel elfBootPanel;

    public TownPanel(String pName, int x, int y, int pWidth, int pHeight, GameScreen pGameScreen) {
        this.name = pName;
        this.x = x;
        this.y = y;
        this.width = pWidth;
        this.height = pHeight;
        this.gameScreen = pGameScreen;
        this.elfBootPanel = new ElfBootPanel(this, x, y+height, gameScreen.getWidth()*72/1440, gameScreen.getHeight()*48/900);

        this.setBounds(this.x, this.y, this.width, this.height);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        this.addMouseListener(new MouseAdapter()
        {
            // TODO: this all needs to be changed to allow for 6 elf boots

            @Override
            public void mouseClicked(MouseEvent e)
            {
                GameScreen gameScreen = TownPanel.this.gameScreen;
                JPanel elfBoot1_panelSpot = gameScreen.getElfBoot1().getCurSpotInPanel();
                JPanel elfBoot2_panelSpot = gameScreen.getElfBoot2().getCurSpotInPanel();

                if (gameScreen.getElfBoot1().getSelected() && gameScreen.getMyTurn())
                {
                    elfBoot1_panelSpot.remove(gameScreen.getElfBoot1().getImage());
                    gameScreen.getElfBoot1().getCurPanel().setSpotAvailability(elfBoot1_panelSpot, true);
                    update(elfBoot1_panelSpot);

                    gameScreen.getElfBoot1().setCurPanelAndSpot(elfBootPanel);
                    JPanel newPanelSpot = gameScreen.getElfBoot1().getCurSpotInPanel(); // new spot

                    newPanelSpot.add(gameScreen.getElfBoot1().getImage());
                    update(newPanelSpot);

                    gameScreen.getElfBoot1().setSelected(false);

                    gameScreen.reverseTurn();

                    try
                    {
                        gameScreen.sendGameState(newPanelSpot);
                    }

                    catch (IOException problem)
                    {
                        // do nothing. we are screwed
                        System.out.println("We ran into an IOException when trying to send the game state over to the other player.");
                        problem.printStackTrace();
                    }
                }

                else if (gameScreen.getElfBoot2().getSelected() && gameScreen.getMyTurn())
                {
                    elfBoot2_panelSpot.remove(gameScreen.getElfBoot2().getImage());
                    gameScreen.getElfBoot2().getCurPanel().setSpotAvailability(elfBoot2_panelSpot, true);
                    update(elfBoot2_panelSpot);

                    gameScreen.getElfBoot2().setCurPanelAndSpot(elfBootPanel);
                    JPanel newPanelSpot = gameScreen.getElfBoot2().getCurSpotInPanel(); // new spot

                    newPanelSpot.add(gameScreen.getElfBoot2().getImage());
                    update(newPanelSpot);

                    gameScreen.getElfBoot2().setSelected(false);

                    gameScreen.reverseTurn();

                    try
                    {
                        gameScreen.sendGameState(newPanelSpot);
                    }

                    catch (IOException problem)
                    {
                        // do nothing. we are screwed
                        System.out.println("We ran into an IOException when trying to send the game state over to the other player.");
                        problem.printStackTrace();
                    }


                }
            }
        });
    }

    public ElfBootPanel getElfBootPanel() { return this.elfBootPanel; }

    public void update(JPanel panel)
    {
        panel.repaint();
        panel.revalidate();
    }
}

