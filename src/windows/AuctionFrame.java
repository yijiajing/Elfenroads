package windows;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import commands.IncreaseBidCommand;
import commands.PassTurnCommand;
import domain.Player;
import domain.TransportationCounter;
import gamemanager.GameManager;
import gamescreen.GameScreen;
import networking.CommunicationsManager;
import domain.CounterUnit;
import enums.CounterType;
import networking.GameState;

/**
 *
 * @author philb
 */
public class AuctionFrame extends javax.swing.JFrame {

    private javax.swing.GroupLayout jPanel1Layout;
    private SequentialGroup sequentHor;
    private ParallelGroup vertLayout;


    private ArrayList<CounterUnit> listCounters = new ArrayList<CounterUnit>();
    private final Logger LOGGER = Logger.getLogger("Auction Frame");
    private int currentBid = 0;
    private Player highestBidPlayer = null;
    private boolean localPlayerHasPassed = false;

    /**
     * Creates new form AuctionFrame
     */
    public AuctionFrame() {
        initComponents();
        //ArrayList<TransportationCounter> listCounters = new ArrayList<TransportationCounter>(); 
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PassButton = new javax.swing.JButton();
        EnterButton = new javax.swing.JButton();
        IncreaseLabel = new javax.swing.JLabel();
        CurrentBidPanel = new javax.swing.JLabel();
        TextInput = new javax.swing.JTextField();
        CurrentBidOutput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        displayText = new JTextField();

        displayText.setText("");
        displayText.setFont(new java.awt.Font("Segoe UI", 0, 14));
        displayText.setEditable(false);
        displayText.setBorder(null);

        //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PassButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PassButton.setText("PASS");
        PassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PassButtonActionPerformed(evt);
            }
        });

        EnterButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        EnterButton.setText("ENTER");
        EnterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnterButtonActionPerformed(evt);
            }
        });

        IncreaseLabel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        IncreaseLabel.setText("Increase bid by:");

        CurrentBidPanel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        CurrentBidPanel.setText("Current bid:");

        TextInput.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        CurrentBidOutput.setEditable(false);
        CurrentBidOutput.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        CurrentBidOutput.setBorder(null);
        CurrentBidOutput.setText("0");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("gold coins");

        jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        sequentHor = jPanel1Layout.createSequentialGroup();
        vertLayout = jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);


        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("gold coins");

        jScrollPane1.setBorder(null);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        //sequentHor = jPanel1Layout.createSequentialGroup();
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19))
                /*.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                .addContainerGap(271, Short.MAX_VALUE))*/
        );
        //vertLayout = jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING))
                                /*.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))*/
                                .addGap(0, 24, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(IncreaseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(CurrentBidPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(displayText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(TextInput, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel2))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(CurrentBidOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel1)))

                                .addContainerGap(41, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(PassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(EnterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))
            /*.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(displayText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                )*/

        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                //.addGap(24, 24, 24)
                                .addContainerGap()
                                .addComponent(displayText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(CurrentBidPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(CurrentBidOutput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(IncreaseLabel)
                                        .addComponent(TextInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2))
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(PassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(EnterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(44, 44, 44))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void EnterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnterButtonActionPerformed

        if (!GameManager.getInstance().isLocalPlayerTurn()) {
            displayMessage("You can only make a bid when it is your turn!");
            return;
        }
        if (localPlayerHasPassed) {
            displayMessage("You cannot make further bids for this item as you have already passed once");
            return;
        }

        try {
            int increaseAmount = Integer.parseInt(TextInput.getText());

            if (currentBid + increaseAmount > GameManager.getInstance().getThisPlayer().getGoldCoins()) {
                displayMessage("You do not have enough gold coins to bid. Please lower your bid increase or pass the turn.");
                return;
            }

            CommunicationsManager coms = GameManager.getInstance().getComs();
            IncreaseBidCommand bid = new IncreaseBidCommand(increaseAmount);
            bid.execute(); // update bid status locally
            coms.sendGameCommandToAllPlayers(bid);
            displayMessage("Waiting for other players to bid...");
            GameManager.getInstance().endTurn();
        } catch (NumberFormatException e) {
            displayMessage("You must enter a valid number!");
            LOGGER.warning("String cannot be converted to integer.");
        } catch (IOException e) {
            LOGGER.severe("There was a problem sending the IncreaseBidCommand to all players.");
        }
    }//GEN-LAST:event_EnterButtonActionPerformed

    private void PassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PassButtonActionPerformed
        if (!GameManager.getInstance().isLocalPlayerTurn()) {
            displayMessage("You cannot end someone else's turn!");
            return;
        }

        localPlayerHasPassed = true;
        PassTurnCommand command = new PassTurnCommand();
        command.execute();
        try {
            GameManager.getInstance().getComs().sendGameCommandToAllPlayers(command);
            GameManager.getInstance().endTurn();
            displayMessage("You passed your turn. Waiting for other players to bid...");
        } catch (IOException e) {
            LOGGER.severe("There was a problem sending the PassTurnCommand to all players.");
            e.printStackTrace();
        }
        //IncreaseLabel.setVisible(false);
        //EnterButton.setVisible(false);
        //PassButton.setVisible(false);
        //TextInput.setVisible(false);
        //jLabel2.setVisible(false);
        //for (int i=0;i<50000;i++){
        //    for (int j = 0;j<30000;j++){
        //        for (int z = 0; z<100000;z++){
        //
        //        }

        //    }
        //}
        //IncreaseLabel.setVisible(true);
        //EnterButton.setVisible(true);
        //PassButton.setVisible(true);
        //TextInput.setVisible(true);
    }//GEN-LAST:event_PassButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AuctionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AuctionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AuctionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AuctionFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AuctionFrame().setVisible(true);
            }
        });
    }

    /**
     * Add a counter to the display of the auction window
     * ************* Important the size of the counter should be 70x70 **********
     * @param counterUnit
     */
    public void addCounter(CounterUnit counterUnit){
        LOGGER.info("Adding a " + counterUnit.getType() + " to auction frame, existing counters: " + listCounters);
        listCounters.add(counterUnit);
        addCounterUIComponent(counterUnit);
    }

    private void addCounterUIComponent(CounterUnit counterUnit) {
        JLabel icon = counterUnit.getDisplay();
        icon.setSize(70, 70);

        icon.setVisible(true);
        //icon.setBorder(BorderFactory.createBevelBorder(1));
        javax.swing.JPanel cardJPanel = new javax.swing.JPanel();
        //javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(cardJPanel);
        //cardJPanel.setLayout(jPanel2Layout);
        cardJPanel.setBorder(BorderFactory.createBevelBorder(1));

        cardJPanel.add(icon);
        cardJPanel.setVisible(true);
        sequentHor.addComponent(cardJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                //.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
                .addGap(75, 75,75);
        vertLayout.addComponent(cardJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE);


        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(sequentHor
                        )

        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(vertLayout)
                                .addGap(0, 24, Short.MAX_VALUE))
        );
        jPanel1.setLayout(jPanel1Layout);
        jPanel1.setVisible(true);
        jScrollPane1.setViewportView(jPanel1);
        jScrollPane1.setVisible(true);
    }

    public CounterUnit removeFirstCounter() {
        assert listCounters.size() > 0;
        CounterUnit counter = listCounters.remove(0);
        jPanel1 = new javax.swing.JPanel();
//
//        jScrollPane1.setViewportView(jPanel1);
//
//        this.repaint();
//        jPanel1.repaint();
        for (CounterUnit cu : listCounters){
            addCounterUIComponent(cu);
        }
        LOGGER.info("Removed the first counter " + counter);
        return counter;
    }

    public void setAuctionWindowCurr(){
        IncreaseLabel.setVisible(true);
        EnterButton.setVisible(true);
        PassButton.setVisible(true);
        TextInput.setVisible(true);
        jLabel2.setVisible(true);
    }

    public void setAuctionWindowNotCurr(){
        IncreaseLabel.setVisible(false);
        EnterButton.setVisible(false);
        PassButton.setVisible(false);
        TextInput.setVisible(false);
        jLabel2.setVisible(false);
    }

    public void resetBidStatus(){
        currentBid = 0;
        CurrentBidOutput.setText("");
        highestBidPlayer = null;
        localPlayerHasPassed = false;
        GameState.instance().clearPassedPlayerCount();
    }

    public void setAuction(int num){

        //CurrentBidPanel.setText(this.);

    }

    public void increaseCurrentBid(int increaseAmount) {
        currentBid += increaseAmount;
        CurrentBidOutput.setText(Integer.toString(currentBid));
    }

    public Player getHighestBidPlayer() {
        return highestBidPlayer;
    }

    public void setHighestBidPlayer(String highestBidPlayerName) {
        this.highestBidPlayer = GameState.instance().getPlayerByName(highestBidPlayerName);
    }

    public int getCurrentBid() {
        return currentBid;
    }

    public int getNumCountersInAuction() {
        return listCounters.size();
    }

    public void displayMessage(String message){
        displayText.setText(message);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CurrentBidOutput;
    private javax.swing.JLabel CurrentBidPanel;
    private javax.swing.JButton EnterButton;
    private javax.swing.JLabel IncreaseLabel;
    private javax.swing.JButton PassButton;
    private javax.swing.JTextField TextInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private JTextField displayText;
    //private SequentialGroup sequentHor;
    //private ParallelGroup vertLayout;
    // End of variables declaration//GEN-END:variables
}
