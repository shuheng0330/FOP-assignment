/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;

class changeUsername1 extends JFrame {
     private JTextField oldUsername;
    private JPasswordField password;
    private JTextField newUsername;
    private JButton confirmButton;
    private JButton backButton;
    private UserData user;
    private String loggedInUsername;
    private String loggedInPassword;
    private String loggedInEmail;
    private String loggedInContactno;

    
    public changeUsername1() {
        initComponents();
        setTitle("Change Username");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    public changeUsername1(UserData user) {
        initComponents();
        this.user=user;
        setTitle("Change Password");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    
    private void initComponents() {
        JPanel jPanel1 = new javax.swing.JPanel();
        JPanel jPanel2 = new javax.swing.JPanel();
        JLabel jLabel6 = new javax.swing.JLabel();
        JPanel jPanel3 = new javax.swing.JPanel();
        JLabel jLabel2 = new javax.swing.JLabel();
        JLabel jLabel3 = new javax.swing.JLabel();
         oldUsername = new javax.swing.JTextField();
        JLabel jLabel4 = new javax.swing.JLabel();
         password = new javax.swing.JPasswordField();
        JLabel jLabel5 = new javax.swing.JLabel();
         newUsername = new javax.swing.JTextField();
        JButton Button_confirm = new javax.swing.JButton();
        JButton Button_back = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Change Username");

        jPanel1.setToolTipText("");
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pricetracker/change username.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jLabel6)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 400, 500);

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 1, 36)); // NOI18N
        jLabel2.setText("Change Username");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Old username");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Password");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("New username");

        Button_confirm.setBackground(new java.awt.Color(255,255,255));
        Button_confirm.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Button_confirm.setForeground(new java.awt.Color(255,0,0));
        Button_confirm.setText("Confirm");
        Button_confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                 Button_confirmActionPerformed(evt);
            }
        });

        Button_back.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Button_back.setForeground(new java.awt.Color(255, 102, 102));
        Button_back.setText("Back");
        Button_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                 Button_backActionPerformed(evt);
    
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(oldUsername)
                            .addComponent(password)
                            .addComponent(newUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(Button_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(Button_back, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addGap(31, 31, 31)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oldUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(Button_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Button_back, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(400, 0, 400, 500);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

        public void setUserData(String username, String password, String email, String contactNo) {
        this.loggedInUsername = username;
        this.loggedInPassword = password;
        this.loggedInEmail = email;
        this.loggedInContactno = contactNo;
        }

     private void Button_backActionPerformed(java.awt.event.ActionEvent evt) {                                            
            AccountSettingsPage account = new AccountSettingsPage(user);
            account.setUserData(user.getUsername(), user.getPassword(), user.getEmail(), user.getContactno());
            account.setVisible(true);
            account.pack();
            account.setLocationRelativeTo(null);
            dispose();
    }              
  private void Button_confirmActionPerformed(java.awt.event.ActionEvent evt) {                                               
    String Oldusername, Password, Newusername, selectquery, updatequery, oldname = null;
    int notFound = 0;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javafx-video", "root", "Shuheng0330.");
        Connection connection1=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/combinedcart", "root", "Shuheng0330.");

        if ("".equals(oldUsername.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "Old username is required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if ("".equals(password.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "Password is required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if ("".equals(newUsername.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "New username is required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Oldusername = oldUsername.getText();
            Password = password.getText();
            Newusername = newUsername.getText();

            // Check if the new username already exists
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, Newusername);

            ResultSet checkResult = checkStatement.executeQuery();
            if (checkResult.next()) {
                JOptionPane.showMessageDialog(new JFrame(), "The username already exists! Please use another username.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                selectquery = "SELECT * FROM users WHERE username = ?";
                updatequery = "UPDATE users SET username = ? WHERE username = ? AND password = ?";

                PreparedStatement selectStatement = connection.prepareStatement(selectquery);
                selectStatement.setString(1, Oldusername);
                ResultSet resultset = selectStatement.executeQuery();

                if (resultset.next()) {
                    oldname = resultset.getString("username");
                    String p = resultset.getString("password");
                    notFound = 1;

                    if (notFound == 1 && p.equals(Password) && oldname.equals(Oldusername)) {
//                            String updateItemsQuery = "UPDATE cart SET username = ? WHERE username = ?";
//                            PreparedStatement updateItemsStatement = connection1.prepareStatement(updateItemsQuery);
//                            updateItemsStatement.setString(1, Newusername);
//                            updateItemsStatement.setString(2, Oldusername);
//                            updateItemsStatement.executeUpdate();

                        PreparedStatement updateStatement = connection.prepareStatement(updatequery);
                        updateStatement.setString(1, Newusername);
                        updateStatement.setString(2, Oldusername);
                        updateStatement.setString(3, Password);
                        updateStatement.executeUpdate();
                        JOptionPane.showMessageDialog(new JFrame(), "Username Changed Successfully!");
                        oldUsername.setText(""); 

                    } else if (!oldname.equals(Oldusername)) {
                        JOptionPane.showMessageDialog(new JFrame(), "Invalid old username", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "Invalid password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    } catch (Exception e) {
        System.out.println("Error!" + e.getMessage());
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new changeUsername1().setVisible(true);
        });
    }
}
