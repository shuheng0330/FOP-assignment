 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.sql.*;
import javax.swing.*;

class LoginForm1 extends JFrame {
    private JTextField username;
    private JPasswordField password;
    private JButton Button_login;
    private JPanel Left;
    private JPanel Right;
    private JButton jButton2;
    private String loggedInUsername;
    
     public LoginForm1() {
        initComponents();
        setTitle("LOGIN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1000, 800));
        setLayout(null);
    }
//     public LoginForm1(ChartPrice chartPrice) {
//        initComponents();
//        this.chartPrice=chartPrice;
//        setTitle("LOGIN");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setPreferredSize(new java.awt.Dimension(1000, 800));
//        setLayout(null);
//    }

     private void initComponents() {
        JPanel jPanel1 = new javax.swing.JPanel();
        Left = new javax.swing.JPanel();
        JLabel jLabel3 = new javax.swing.JLabel();
        JLabel jLabel4 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        JLabel jLabel5 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        Button_login = new javax.swing.JButton();
        JLabel jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        Right = new javax.swing.JPanel();
        JLabel jLabel1 = new javax.swing.JLabel();
        JLabel jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGIN");
        setPreferredSize(new java.awt.Dimension(1000, 800));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 800));
        jPanel1.setLayout(null);

        Left.setBackground(new java.awt.Color(252, 248, 222));
        Left.setPreferredSize(new java.awt.Dimension(500, 800));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setText("LOGIN");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Username");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Password");

        Button_login.setBackground(new java.awt.Color(0, 102, 102));
        Button_login.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Button_login.setForeground(new java.awt.Color(255, 255, 255));
        Button_login.setText("Login");
        Button_login.addActionListener(new java.awt.event.ActionListener() {
            
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_loginActionPerformed(evt);
            }
        });

        jLabel6.setText("I dont't have an account");

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 51, 51));
        jButton2.setText("Sign Up");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                 jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel4)
                        .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addComponent(password)
                        .addComponent(Button_login, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(LeftLayout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(31, 31, 31)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        LeftLayout.setVerticalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel3)
                .addGap(65, 65, 65)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(27, 27, 27)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(Button_login, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(303, Short.MAX_VALUE))
        );

        jPanel1.add(Left);
        Left.setBounds(0, 0, 500, 800);

        Right.setBackground(new java.awt.Color(255, 255, 255));
        Right.setPreferredSize(new java.awt.Dimension(500, 800));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pricetracker/屏幕截图 2023-12-14 104222.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Poor Richard", 1, 24)); // NOI18N
        jLabel2.setText("Welcome To Price Tracker!");

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addContainerGap(87, Short.MAX_VALUE)
                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(121, 121, 121))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(68, 68, 68))))
        );
        RightLayout.setVerticalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addGap(135, 135, 135)
                .addComponent(jLabel1)
                .addContainerGap(277, Short.MAX_VALUE))
        );

        jPanel1.add(Right);
        Right.setBounds(500, 0, 500, 800);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>      
      private UserData getUserData(String username) {
        UserData userData = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javafx-video", "root", "Shuheng0330.");

            String query = "SELECT * FROM users WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String retrievedUsername = resultSet.getString("Username");
                String retrievedPassword = resultSet.getString("Password");
                String retrievedEmail=resultSet.getString("Email");
                String retrievedContact=resultSet.getString("Contactno");

                // Create a UserData object to hold the retrieved data
                userData = new UserData(retrievedUsername,retrievedPassword,retrievedEmail,retrievedContact);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
             try {
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    } catch (SQLException e) {
        System.out.println("Error closing resources: " + e.getMessage());
    }
        }

        return userData;
    }

         private void Button_loginActionPerformed(java.awt.event.ActionEvent evt) {                                             
                String usernameText = username.getText();
                String passwordText = password.getText();
                String query = "SELECT * FROM users WHERE username= ?";
                int notFound = 0;
                Connection connection = null;
                PreparedStatement statement = null;
                 ResultSet resultset = null;

try { 
    Class.forName("com.mysql.cj.jdbc.Driver");
    connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javafx-video", "root", "Shuheng0330.");
    statement = connection.prepareStatement(query);

    if ("".equals(usernameText) || "".equals(passwordText)) {
        JOptionPane.showMessageDialog(new JFrame(), "All the credentials must be filled", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
        statement.setString(1, usernameText);
        resultset = statement.executeQuery();

        while (resultset.next()) {
            String passDb = resultset.getString("Password");
            String name = resultset.getString("Username");
            notFound = 1;

            if (passwordText.equals(passDb)) {
                UserData user = getUserData(usernameText);
               if (user != null) {
                CSVImporter CSVFrame = new CSVImporter();
                CSVFrame.setUser(name);
                CSVFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                CSVFrame.setVisible(true);
                CSVFrame.displayMainMenu();
                this.dispose();
                
            } else {
                   System.out.println("ERROR!");
               }

            } else {
                JOptionPane.showMessageDialog(new JFrame(), "Incorrect password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (notFound == 0) {
            JOptionPane.showMessageDialog(new JFrame(), "User not found", "Error", JOptionPane.ERROR_MESSAGE);
        }

        password.setText("");
    }
} catch (Exception e) {
    System.out.println("Error! " + e.getMessage());
} finally {
    // Close all resources in the finally block
    try {
        if (resultset != null) {
            resultset.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    } catch (SQLException e) {
        System.out.println("Error closing resources: " + e.getMessage());
    }
}
            } 
        
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
                SignUpForm1 SignUpFrame = new SignUpForm1();
                SignUpFrame.setVisible(true);
                SignUpFrame.pack();
                SignUpFrame.setLocationRelativeTo(null);
                dispose();
        
      }
      public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoginForm1 loginform=new LoginForm1();
                loginform.setVisible(true);
                loginform.setLocationRelativeTo(null);
            }
        });
    }
}
//    private JTextField username;
//    private JPasswordField password;
//    private JButton Button_login;
//    private JPanel Left;
//    private JPanel Right;
//    private JButton jButton2;
//    private String loggedInUsername;
//    private ChartPrice chartPrice;
//    
//     public LoginForm1() {
//        initComponents();
//        setTitle("LOGIN");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setPreferredSize(new java.awt.Dimension(1000, 800));
//        setLayout(null);
//    }
//     public LoginForm1(ChartPrice chartPrice) {
//        initComponents();
//        this.chartPrice=chartPrice;
//        setTitle("LOGIN");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setPreferredSize(new java.awt.Dimension(1000, 800));
//        setLayout(null);
//    }
//
//     private void initComponents() {
//        JPanel jPanel1 = new javax.swing.JPanel();
//        Left = new javax.swing.JPanel();
//        JLabel jLabel3 = new javax.swing.JLabel();
//        JLabel jLabel4 = new javax.swing.JLabel();
//        username = new javax.swing.JTextField();
//        JLabel jLabel5 = new javax.swing.JLabel();
//        password = new javax.swing.JPasswordField();
//        Button_login = new javax.swing.JButton();
//        JLabel jLabel6 = new javax.swing.JLabel();
//        jButton2 = new javax.swing.JButton();
//        Right = new javax.swing.JPanel();
//        JLabel jLabel1 = new javax.swing.JLabel();
//        JLabel jLabel2 = new javax.swing.JLabel();
//
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//        setTitle("LOGIN");
//        setPreferredSize(new java.awt.Dimension(1000, 800));
//
//        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
//        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 800));
//        jPanel1.setLayout(null);
//
//        Left.setBackground(new java.awt.Color(252, 248, 222));
//        Left.setPreferredSize(new java.awt.Dimension(500, 800));
//
//        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
//        jLabel3.setText("LOGIN");
//
//        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
//        jLabel4.setText("Username");
//
//        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
//        jLabel5.setText("Password");
//
//        Button_login.setBackground(new java.awt.Color(0, 102, 102));
//        Button_login.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//        Button_login.setForeground(new java.awt.Color(255, 255, 255));
//        Button_login.setText("Login");
//        Button_login.addActionListener(new java.awt.event.ActionListener() {
//            
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                Button_loginActionPerformed(evt);
//            }
//        });
//
//        jLabel6.setText("I dont't have an account");
//
//        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
//        jButton2.setForeground(new java.awt.Color(255, 51, 51));
//        jButton2.setText("Sign Up");
//        jButton2.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                 jButton2ActionPerformed(evt);
//            }
//        });
//
//        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
//        Left.setLayout(LeftLayout);
//        LeftLayout.setHorizontalGroup(
//            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(LeftLayout.createSequentialGroup()
//                .addGap(26, 26, 26)
//                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                    .addComponent(jLabel3)
//                    .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
//                        .addComponent(jLabel4)
//                        .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
//                        .addComponent(jLabel5)
//                        .addComponent(password)
//                        .addComponent(Button_login, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
//                        .addGroup(LeftLayout.createSequentialGroup()
//                            .addComponent(jLabel6)
//                            .addGap(31, 31, 31)
//                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
//                .addContainerGap(116, Short.MAX_VALUE))
//        );
//        LeftLayout.setVerticalGroup(
//            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(LeftLayout.createSequentialGroup()
//                .addGap(29, 29, 29)
//                .addComponent(jLabel3)
//                .addGap(65, 65, 65)
//                .addComponent(jLabel4)
//                .addGap(18, 18, 18)
//                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addGap(18, 18, 18)
//                .addComponent(jLabel5)
//                .addGap(27, 27, 27)
//                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addGap(53, 53, 53)
//                .addComponent(Button_login, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addGap(51, 51, 51)
//                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                    .addComponent(jLabel6)
//                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
//                .addContainerGap(303, Short.MAX_VALUE))
//        );
//
//        jPanel1.add(Left);
//        Left.setBounds(0, 0, 500, 800);
//
//        Right.setBackground(new java.awt.Color(255, 255, 255));
//        Right.setPreferredSize(new java.awt.Dimension(500, 800));
//
//        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pricetracker/屏幕截图 2023-12-14 104222.png"))); // NOI18N
//
//        jLabel2.setFont(new java.awt.Font("Poor Richard", 1, 24)); // NOI18N
//        jLabel2.setText("Welcome To Price Tracker!");
//
//        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
//        Right.setLayout(RightLayout);
//        RightLayout.setHorizontalGroup(
//            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(RightLayout.createSequentialGroup()
//                .addContainerGap(87, Short.MAX_VALUE)
//                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
//                        .addComponent(jLabel2)
//                        .addGap(121, 121, 121))
//                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
//                        .addComponent(jLabel1)
//                        .addGap(68, 68, 68))))
//        );
//        RightLayout.setVerticalGroup(
//            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
//                .addGap(30, 30, 30)
//                .addComponent(jLabel2)
//                .addGap(135, 135, 135)
//                .addComponent(jLabel1)
//                .addContainerGap(277, Short.MAX_VALUE))
//        );
//
//        jPanel1.add(Right);
//        Right.setBounds(500, 0, 500, 800);
//
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
//        getContentPane().setLayout(layout);
//        layout.setHorizontalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        layout.setVerticalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//
//        pack();
//    }// </editor-fold>      
//      private UserData getUserData(String username) {
//        UserData userData = null;
//        Connection connection = null;
//        PreparedStatement statement = null;
//        ResultSet resultSet = null;
//
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javafx-video", "root", "Shuheng0330.");
//
//            String query = "SELECT * FROM users WHERE username = ?";
//            statement = connection.prepareStatement(query);
//            statement.setString(1, username);
//
//            resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//                String retrievedUsername = resultSet.getString("Username");
//                String retrievedPassword = resultSet.getString("Password");
//                String retrievedEmail=resultSet.getString("Email");
//                String retrievedContact=resultSet.getString("Contactno");
//
//                // Create a UserData object to hold the retrieved data
//                userData = new UserData(retrievedUsername,retrievedPassword,retrievedEmail,retrievedContact);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//             try {
//        if (resultSet != null) {
//            resultSet.close();
//        }
//        if (statement != null) {
//            statement.close();
//        }
//        if (connection != null) {
//            connection.close();
//        }
//    } catch (SQLException e) {
//        System.out.println("Error closing resources: " + e.getMessage());
//    }
//        }
//
//        return userData;
//    }
//
//         private void Button_loginActionPerformed(java.awt.event.ActionEvent evt) {                                             
//                String usernameText = username.getText();
//                String passwordText = password.getText();
//                String query = "SELECT * FROM users WHERE username= ?";
//                int notFound = 0;
//                Connection connection = null;
//                PreparedStatement statement = null;
//                 ResultSet resultset = null;
//
//try { 
//    Class.forName("com.mysql.cj.jdbc.Driver");
//    connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javafx-video", "root", "Shuheng0330.");
//    statement = connection.prepareStatement(query);
//
//    if ("".equals(usernameText) || "".equals(passwordText)) {
//        JOptionPane.showMessageDialog(new JFrame(), "All the credentials must be filled", "Error", JOptionPane.ERROR_MESSAGE);
//    } else {
//        statement.setString(1, usernameText);
//        resultset = statement.executeQuery();
//
//        while (resultset.next()) {
//            String passDb = resultset.getString("Password");
//            String name = resultset.getString("Username");
//            notFound = 1;
//
//            if (passwordText.equals(passDb)) {
//                UserData user = getUserData(usernameText);
//               if (user != null) {
//                CSVImporter CSVFrame = new CSVImporter(chartPrice);
//                CSVFrame.setUser(name);
//                CSVFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//                CSVFrame.setVisible(true);
//                CSVFrame.displayMainMenu();
//                this.dispose();
//                
//            } else {
//                   System.out.println("ERROR!");
//               }
//
//            } else {
//                JOptionPane.showMessageDialog(new JFrame(), "Incorrect password", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//
//        if (notFound == 0) {
//            JOptionPane.showMessageDialog(new JFrame(), "User not found", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//        password.setText("");
//    }
//} catch (Exception e) {
//    System.out.println("Error! " + e.getMessage());
//} finally {
//    // Close all resources in the finally block
//    try {
//        if (resultset != null) {
//            resultset.close();
//        }
//        if (statement != null) {
//            statement.close();
//        }
//        if (connection != null) {
//            connection.close();
//        }
//    } catch (SQLException e) {
//        System.out.println("Error closing resources: " + e.getMessage());
//    }
//}
//            }
//        
//    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
//                SignUpForm1 SignUpFrame = new SignUpForm1();
//                SignUpFrame.setVisible(true);
//                SignUpFrame.pack();
//                SignUpFrame.setLocationRelativeTo(null);
//                dispose();
//        
//      }
//      public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                LoginForm1 loginform=new LoginForm1();
//                loginform.setVisible(true);
//                loginform.setLocationRelativeTo(null);
//            }
//        });
//    }
//
//}
