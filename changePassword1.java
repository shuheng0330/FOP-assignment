/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;

class changePassword1 extends JFrame {
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel oldPasswordLabel;
    private JLabel newPasswordLabel;
    private JTextField username;
    private JPasswordField oldPassword;
    private JPasswordField newPassword;
    private JButton backButton;
    private JButton confirmButton;
    private String loggedInUsername;
    private String loggedInPassword;
    private String loggedInEmail;
    private String loggedInContactno;
    private UserData user;
    
    public changePassword1() {
        initComponents();
        setTitle("Change Password");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    public changePassword1(UserData user) {
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
        username = new javax.swing.JTextField();
        JLabel jLabel4 = new javax.swing.JLabel();
        oldPassword = new javax.swing.JPasswordField();
        JLabel jLabel5 = new javax.swing.JLabel();
        newPassword = new javax.swing.JPasswordField();
        JButton Button_back = new javax.swing.JButton();
        JButton Button_confirm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ChangePassword");

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pricetracker/change password.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 400, 500);

        jPanel3.setBackground(new java.awt.Color(255, 204, 204));

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 1, 36)); // NOI18N
        jLabel2.setText("Change Password");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Username");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Old password");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("New password");

        Button_back.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Button_back.setForeground(new java.awt.Color(255, 102, 102));
        Button_back.setText("Back");
        Button_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               Button_backActionPerformed(evt);
           
            }
        });

        Button_confirm.setBackground(new java.awt.Color(255,255,255));
        Button_confirm.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Button_confirm.setForeground(new java.awt.Color(255,0,0));
        Button_confirm.setText("Confirm");
        Button_confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_confirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(oldPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                            .addComponent(username)
                            .addComponent(newPassword)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(138, 138, 138)
                        .addComponent(Button_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(150, 150, 150)
                        .addComponent(Button_back, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel2)
                .addGap(44, 44, 44)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oldPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(Button_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Button_back, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
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
        
     private void Button_backActionPerformed(java.awt.event.ActionEvent evt){
        AccountSettingsPage account = new AccountSettingsPage(user);
        account.setUserData(user.getUsername(), user.getPassword(), user.getEmail(), user.getContactno());
        account.setVisible(true);
        account.setLocationRelativeTo(null);
        dispose();

     }
    

     private void Button_confirmActionPerformed(java.awt.event.ActionEvent evt) {
         String Username, Oldpassword, Newpassword, selectquery, updatequery, name = null;
    int notFound = 0;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javafx-video", "root", "Shuheng0330.");
        Statement statement = connection.createStatement();

        if ("".equals(username.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "Username is required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if ("".equals(oldPassword.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "Old password is required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if ("".equals(newPassword.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "New password is required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Username = username.getText();
            Oldpassword = oldPassword.getText();
            Newpassword = newPassword.getText();

            selectquery = "SELECT * FROM users WHERE username= ?";
            updatequery = "UPDATE users SET password=? WHERE username=?";

            PreparedStatement selectStatement = connection.prepareStatement(selectquery);
            selectStatement.setString(1, Username);
            ResultSet resultset = selectStatement.executeQuery();

            while (resultset.next()) {
                name = resultset.getString("username");
                String oldp = resultset.getString("password");
                notFound = 1;

                if (notFound == 1 && oldp.equals(Oldpassword)) {
                    PreparedStatement updateStatement = connection.prepareStatement(updatequery);
                    updateStatement.setString(1, Newpassword);
                    updateStatement.setString(2, Username);
                    updateStatement.executeUpdate();
                    JOptionPane.showMessageDialog(new JFrame(), "Password Changed Successfully!");
                    oldPassword.setText(""); // Clearing the old password field after successful change
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Invalid old password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (notFound == 0) {
                JOptionPane.showMessageDialog(new JFrame(), "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception e) {
        System.out.println("Error!" + e.getMessage());
    
}

        
     }
     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
           changePassword1 changePassword = new changePassword1();
            changePassword.setVisible(true);
        });
    }
}
