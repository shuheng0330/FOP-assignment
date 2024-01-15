package pricetracker;
import javax.swing.*;
import java.sql.*;


public class AccountSettingsPage extends JFrame {
    private String loggedInUsername;
    private String loggedInPassword;
    private String loggedInEmail;
    private String loggedInContactno;
    private UserData user;

    private JPanel jPanel1;
    private JPanel leftPanel;
    private JLabel jLabel1;
    private JPanel rightPanel;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JTextField jTextField1;
    private JLabel usernameLabel;
    private JLabel jLabel5;
    private JLabel passwordLabel;
    private JLabel jLabel7;
    private JLabel emailLabel;
    private JLabel jLabel9;
    private JLabel contactNoLabel;
    private JButton Button_changeUsername;
    private JButton Button_changePassword;
    private JButton Button_back;
    private JButton Button_removeAccount; 

    public AccountSettingsPage() {
        setTitle("Account Settings");
        initComponents();
    }
    public AccountSettingsPage(UserData user) {
        setTitle("Account Settings ");
        this.user=user;
        initComponents();
    }

    private void initComponents() {
        Button_removeAccount=new JButton();
        jPanel1 = new JPanel();
        leftPanel = new JPanel();
        jLabel1 = new JLabel();
        rightPanel = new JPanel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jTextField1 = new JTextField();
        usernameLabel = new JLabel();
        jLabel5 = new JLabel();
        passwordLabel = new JLabel();
        jLabel7 = new JLabel();
        emailLabel = new JLabel();
        jLabel9 = new JLabel();
        contactNoLabel = new JLabel();
        Button_changeUsername = new JButton();
        Button_changePassword = new JButton();
        Button_back = new JButton();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        leftPanel.setBackground(new java.awt.Color(255, 255, 255));
        leftPanel.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pricetracker/photo1703927582.jpeg"))); // NOI18N

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(jLabel1)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        jPanel1.add(leftPanel);
        leftPanel.setBounds(0, 0, 400, 510);

        rightPanel.setBackground(new java.awt.Color(204, 204, 255));
        rightPanel.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); 
        jLabel2.setText("Account Settings");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); 
        jLabel3.setText("Username");

        jTextField1.setText("jTextField1");

        usernameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        usernameLabel.setText("john");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); 
        jLabel5.setText("Password");

        passwordLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        passwordLabel.setText("123");

        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); 
        jLabel7.setText("Email");

        emailLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        emailLabel.setText("gmail.com");

        jLabel9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); 
        jLabel9.setText("Contact no");

        contactNoLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        contactNoLabel.setText("12345");

        Button_changeUsername.setBackground(new java.awt.Color(153, 204, 255));
        Button_changeUsername.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        Button_changeUsername.setText("Change username");
        Button_changeUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_changeUsernameActionPerformed(evt);
            }
        });

        Button_changePassword.setBackground(new java.awt.Color(153, 204, 255));
        Button_changePassword.setFont(new java.awt.Font("Segoe UI", 0, 14));
        Button_changePassword.setText("Change password");
        Button_changePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_changePasswordActionPerformed(evt);
            }
        });

        Button_back.setBackground(new java.awt.Color(153, 204, 255));
        Button_back.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        Button_back.setText("Back");
        Button_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_backActionPerformed(evt);
            }
        });

        Button_removeAccount.setBackground(new java.awt.Color(153, 204, 255));
        Button_removeAccount.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        Button_removeAccount.setText("Remove account");
        Button_removeAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_removeAccountActionPerformed(evt);
            }
        });        

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(Button_changeUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Button_changePassword)
                .addGap(48, 48, 48))
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(passwordLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(usernameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(emailLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(contactNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 19, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(Button_back, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(Button_removeAccount)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(usernameLabel))
                .addGap(16, 16, 16)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(passwordLabel)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(emailLabel)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contactNoLabel)
                .addGap(18, 18, 18)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Button_changeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Button_changePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Button_back, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Button_removeAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel1.add(rightPanel);
        rightPanel.setBounds(400, 0, 400, 500);

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

        // Display user information on the Account Settings page
        usernameLabel.setText(username);
        passwordLabel.setText(password);
        emailLabel.setText(email);
        contactNoLabel.setText(contactNo);
    }  

    private void Button_backActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
//            CSVImporter importer = new CSVImporter();
//            importer.setUserData(user.getUsername());
//            importer.setExtendedState(JFrame.MAXIMIZED_BOTH);
//            importer.setVisible(true);
//            importer.displayMainMenu();
            
    }

    private void Button_changeUsernameActionPerformed(java.awt.event.ActionEvent evt) {
                changeUsername1 changeUsername = new changeUsername1(user);
                changeUsername.setVisible(true);
                changeUsername.pack();
                changeUsername.setLocationRelativeTo(null);
                dispose();
    }

    private void Button_changePasswordActionPerformed(java.awt.event.ActionEvent evt) {
               changePassword1 changePassword = new changePassword1(user);
               changePassword.setVisible(true);
               changePassword.pack();
               changePassword.setLocationRelativeTo(null);
               dispose();
    }
    private void Button_removeAccountActionPerformed(java.awt.event.ActionEvent evt) {
           int confirmDelete = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete your account?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
           if (confirmDelete == JOptionPane.YES_OPTION) {
            deleteUserDataFromDatabase(user.getUsername()); 
        }
            }

    private void deleteUserDataFromDatabase(String username) {
        String url = "jdbc:mysql://127.0.0.1:3306/javafx-video";
        String usernamedb = "root";
        String password = "Shuheng0330."; 

        try (Connection connection = DriverManager.getConnection(url, usernamedb, password)) {
            String query = "DELETE FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Account deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete account.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
            
      try (Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/combinedcart" , "root", "Shuheng0330.")) {  
            String query = "DELETE FROM cart WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            int rowsAffected = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        LoginForm1 LoginFrame=new LoginForm1();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null);
        dispose();
    }
            

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new AccountSettingsPage().setVisible(true);
        });
    }
}





