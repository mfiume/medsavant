/*
 *    Copyright 2011-2012 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.ut.biolab.medsavant.login;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.ut.biolab.medsavant.MedSavantProgramInformation;
import org.ut.biolab.medsavant.api.Listener;
import org.ut.biolab.medsavant.controller.SettingsController;
import org.ut.biolab.medsavant.util.ClientMiscUtils;
import org.ut.biolab.medsavant.view.manage.AddRemoveDatabaseDialog;
import org.ut.biolab.medsavant.view.images.IconFactory;
import org.ut.biolab.medsavant.view.util.ViewUtil;

/**
 *
 * @author mfiume
 */
public class LoginView extends JPanel implements Listener<LoginEvent> {
    
    private static final Log LOG = LogFactory.getLog(LoginView.class);

    private LoginController controller = LoginController.getInstance();

    private static class SpiralPanel extends JPanel {
        private final Image img;

        public SpiralPanel() {
            img = IconFactory.getInstance().getIcon(IconFactory.StandardIcon.LOGO).getImage();
        }

        @Override
        public void paintComponent(Graphics g) {
            //g.setColor(Color.black);
            //g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(img, this.getWidth()/2-img.getWidth(null)/2, this.getHeight()/2-img.getHeight(null)/2, null);
        }
    }

    /** Creates new form LoginForm */
    public LoginView() {

        initComponents();

        titlePanel.setBackground(ViewUtil.getMenuColor());
        loginButton.setOpaque(false);

        userField.setText(controller.getUserName());
        passwordField.setText(controller.getPassword());

        SettingsController settings = SettingsController.getInstance();
        userField.setText(settings.getUsername());
        if (settings.getRememberPassword()) {
            passwordField.setText(settings.getPassword());
        }

        versionLabel.setText("MedSavant " + MedSavantProgramInformation.getVersion() + " " + MedSavantProgramInformation.getReleaseType());

        statusLabel.setText(" ");
        titlePanel.add(Box.createVerticalGlue(),0);

        spiralPanel.setLayout(new BorderLayout());
        spiralPanel.add(new SpiralPanel(),BorderLayout.CENTER);

        detailsPanel.setVisible(false);

        databaseField.setText(settings.getDBName());
        portField.setText(settings.getServerPort());
        hostField.setText(settings.getServerAddress());

        setOpaque(false);
        setMaximumSize(new Dimension(400, 400));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        titlePanel = new javax.swing.JPanel();
        userField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        spiralPanel = new javax.swing.JPanel();
        versionLabel = new javax.swing.JLabel();
        javax.swing.JLabel userLabel = new javax.swing.JLabel();
        javax.swing.JLabel passwordLabel = new javax.swing.JLabel();
        javax.swing.JToggleButton button_settings = new javax.swing.JToggleButton();
        detailsPanel = new javax.swing.JPanel();
        javax.swing.JLabel hostLabel = new javax.swing.JLabel();
        hostField = new javax.swing.JTextField();
        javax.swing.JLabel portLabel = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        javax.swing.JButton dbCreateButton = new javax.swing.JButton();
        databaseField = new javax.swing.JTextField();
        javax.swing.JLabel databaseLabel = new javax.swing.JLabel();
        javax.swing.JButton dbRemoveButton = new javax.swing.JButton();
        loginButton = new javax.swing.JButton();
        statusLabel = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        titlePanel.setBackground(new java.awt.Color(217, 222, 229));
        titlePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));
        titlePanel.setMaximumSize(new java.awt.Dimension(400, 32767));
        titlePanel.setMinimumSize(new java.awt.Dimension(400, 800));

        userField.setColumns(25);
        userField.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        userField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                userFieldKeyPressed(evt);
            }
        });

        passwordField.setColumns(25);
        passwordField.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        passwordField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordFieldKeyPressed(evt);
            }
        });

        spiralPanel.setPreferredSize(new java.awt.Dimension(150, 150));

        javax.swing.GroupLayout spiralPanelLayout = new javax.swing.GroupLayout(spiralPanel);
        spiralPanel.setLayout(spiralPanelLayout);
        spiralPanelLayout.setHorizontalGroup(
            spiralPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 388, Short.MAX_VALUE)
        );
        spiralPanelLayout.setVerticalGroup(
            spiralPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        versionLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        versionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        versionLabel.setText("version information");

        userLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        userLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userLabel.setText("USERNAME");

        passwordLabel.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        passwordLabel.setText("PASSWORD");

        button_settings.setText("Settings");
        button_settings.putClientProperty("JButton.buttonType", "textured");
        button_settings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_settingsActionPerformed(evt);
            }
        });

        detailsPanel.setBackground(new java.awt.Color(204, 204, 204));
        detailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Connection Settings", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        detailsPanel.setName("Connection Settings"); // NOI18N
        detailsPanel.setOpaque(false);

        hostLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hostLabel.setText("SERVER ADDRESS");

        hostField.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        hostField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        hostField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                hostFieldKeyPressed(evt);
            }
        });

        portLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        portLabel.setText("SERVER PORT");

        portField.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        portField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        portField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                portFieldKeyPressed(evt);
            }
        });

        dbCreateButton.setText("Create Database");
        dbCreateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dbCreateButtonActionPerformed(evt);
            }
        });

        databaseField.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        databaseField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        databaseField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                databaseFieldKeyPressed(evt);
            }
        });

        databaseLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        databaseLabel.setText("DATABASE NAME");

        dbRemoveButton.setText("Remove Database");
        dbRemoveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dbRemoveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(databaseLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(hostLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addComponent(hostField, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addComponent(portField, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addComponent(portLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailsPanelLayout.createSequentialGroup()
                        .addComponent(dbRemoveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dbCreateButton))
                    .addComponent(databaseField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
                .addContainerGap())
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addComponent(hostLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hostField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(databaseLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(databaseField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dbCreateButton)
                    .addComponent(dbRemoveButton)))
        );

        loginButton.setBackground(new java.awt.Color(0, 0, 0));
        loginButton.setText("Login");
        loginButton.putClientProperty("JButton.buttonType", "textured");
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        statusLabel.setText("  ");

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(versionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
            .addComponent(spiralPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titlePanelLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titlePanelLayout.createSequentialGroup()
                        .addComponent(button_settings)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loginButton))))
            .addComponent(detailsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(userLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(passwordLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(passwordField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addComponent(spiralPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(versionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userLabel)
                    .addComponent(passwordLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loginButton)
                    .addComponent(button_settings))
                .addGap(3, 3, 3)
                .addComponent(statusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(detailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(45, 45, 45, 45);
        add(titlePanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void passwordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_passwordFieldKeyPressed

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginButtonActionPerformed
        this.loginUsingEnteredUsernameAndPassword();
    }//GEN-LAST:event_loginButtonActionPerformed

    private void userFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
}//GEN-LAST:event_userFieldKeyPressed

    private void button_settingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_settingsActionPerformed
        detailsPanel.setVisible(!detailsPanel.isVisible());
    }//GEN-LAST:event_button_settingsActionPerformed

    private void hostFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_hostFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_hostFieldKeyPressed

    private void portFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_portFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_portFieldKeyPressed

    private void databaseFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_databaseFieldKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            loginUsingEnteredUsernameAndPassword();
        }
    }//GEN-LAST:event_databaseFieldKeyPressed

    private void dbCreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbCreateButtonActionPerformed
        AddRemoveDatabaseDialog dlg = new AddRemoveDatabaseDialog(hostField.getText(), portField.getText(), databaseField.getText(), userField.getText(), passwordField.getPassword(), false);
        dlg.setVisible(true);
        hostField.setText(dlg.getHost());
        portField.setText(Integer.toString(dlg.getPort()));
        databaseField.setText(dlg.getDatabase());
    }//GEN-LAST:event_dbCreateButtonActionPerformed

    private void dbRemoveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dbRemoveButtonActionPerformed
        new AddRemoveDatabaseDialog(hostField.getText(), portField.getText(), databaseField.getText(), userField.getText(), passwordField.getPassword(), true).setVisible(true);
    }//GEN-LAST:event_dbRemoveButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField databaseField;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JTextField hostField;
    private javax.swing.JButton loginButton;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField portField;
    private javax.swing.JPanel spiralPanel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField userField;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables

    private void loginUsingEnteredUsernameAndPassword() {

        try {
            Integer.parseInt(portField.getText());
        } catch (Exception e) {
            portField.requestFocus();
            return;
        }

        SettingsController settings = SettingsController.getInstance();
        settings.setDBName(databaseField.getText());
        settings.setServerAddress(hostField.getText());
        settings.setServerPort(portField.getText());

        statusLabel.setText("signing in...");
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setForeground(Color.black);
        loginButton.setEnabled(false);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginController.getInstance().login(userField.getText(), passwordField.getText(), databaseField.getText(), hostField.getText(), portField.getText());
            }
        });
        //new Thread(new RunLogin(this,this.field_username.getText(),this.field_password.getText())).start();
    }

    private void notifyOfUnsuccessfulLogin(Exception ex) {

        userField.requestFocus();
        loginButton.setEnabled(true);
        statusLabel.setForeground(Color.RED);

        // Exception may be null if we got here as a result of a failed version check.
        if (ex == null) {
            statusLabel.setText("not logged in");
            statusLabel.setToolTipText(null);
        } else {
            statusLabel.setText("login error");
            LOG.error("Problem contacting server.", ex);
            if (ex instanceof SQLException) {
                if (ex.getMessage().contains("Access denied")) {
                    statusLabel.setText("access denied");
                } else {
                    statusLabel.setText("problem contacting server");
                }
            }
            statusLabel.setToolTipText(ClientMiscUtils.getMessage(ex));
        }
    }

    @Override
    public void handleEvent(LoginEvent event) {
        switch (event.getType()) {
            case LOGGED_IN:
                statusLabel.setText("");
                break;
            case LOGGED_OUT:
                break;
            case LOGIN_FAILED:
                notifyOfUnsuccessfulLogin(event.getException());
                break;
        }
    }
}
