/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ThreadManagerDialog.java
 *
 * Created on Jun 22, 2011, 7:29:06 PM
 */
package org.ut.biolab.medsavant.view.thread;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.ut.biolab.medsavant.controller.ThreadController;
import org.ut.biolab.medsavant.view.util.ViewUtil;

/**
 *
 * @author mfiume
 */
public class ThreadManagerDialog extends javax.swing.JDialog implements WindowListener {

    private JPanel progressContainer;
    private JLabel updateLabel;
    private Thread updateThread;

    /** Creates new form ThreadManagerDialog */
    public ThreadManagerDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setTitle("Threads");
        this.addWindowListener(this);
        initComponents();
        initGUI();
        updateRunningThreads();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ThreadManagerDialog dialog = new ThreadManagerDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private void updateRunningThreads() {

        
                List<Thread> runningThreads = ThreadController.getInstance().getRunningThreads();

                updateLabel.setText("Updated: " + (new Date()).toLocaleString());

                progressContainer.removeAll();

                if (runningThreads.isEmpty()) {
                    progressContainer.add(new JLabel("No threads currently running."));
                } else {
                    for (Thread t : runningThreads) {
                        progressContainer.add(new ThreadProgressIndicator("name", t));
                    }
                }

                //progressContainer.add(Box.createVerticalGlue());
                progressContainer.repaint();

    }

    private void initGUI() {

        this.setMinimumSize(new Dimension(500, 300));
        this.setPreferredSize(new Dimension(500, 300));
        this.setLayout(new BorderLayout());

        JPanel p = ViewUtil.getBannerPanel();

        JButton refreshButton = new JButton("Refresh");


        updateLabel = new JLabel();

        p.add(updateLabel);

        p.add(Box.createHorizontalGlue());

        p.add(refreshButton);


        this.add(p, BorderLayout.NORTH);

        progressContainer = new JPanel();
        progressContainer.setBorder(ViewUtil.getMediumBorder());
        progressContainer.setLayout(new BoxLayout(progressContainer, BoxLayout.Y_AXIS));

        this.add(progressContainer, BorderLayout.CENTER);

        refreshButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateRunningThreads();
            }
        });

    }

    public void windowOpened(WindowEvent e) {
        Runnable r = new Runnable() {

            public void run() {
                int delay = 5000;   // delay for 5 sec.
                int period = delay;  // repeat every sec.
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                updateRunningThreads();
                            }
                        });
                    }
                }, delay, period);
            }
        };

        ThreadController.getInstance().runInThread(r);
        //updateThread = new Thread(r);
        //updateThread.start();
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
        if (updateThread != null && updateThread.isAlive()) {
            updateThread.interrupt();
        }
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}