/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;

class SignUpForm1 extends JFrame {
    private JButton Button_signup;
    private JPasswordField confirmpassword;
    private JTextField contactno;
    private JTextField email;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPasswordField password;
    private JTextField username;

    public SignUpForm1() {
        setTitle("Sign Up");
        initComponents();
    }

    private void initComponents() {
         jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        confirmpassword = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        contactno = new javax.swing.JTextField();
        Button_signup = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1000, 800));

        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 800));
        jPanel2.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 800));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pricetracker/屏幕截图 2023-12-13 154926.png"))); 
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(230, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(213, 213, 213))
        );

        jPanel2.add(jPanel1);
        jPanel1.setBounds(0, 0, 460, 800);

        jPanel3.setBackground(new java.awt.Color(255, 227, 226));
        jPanel3.setMinimumSize(new java.awt.Dimension(500, 800));
        jPanel3.setPreferredSize(new java.awt.Dimension(500, 800));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); 
        jLabel2.setText("Sign Up");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel3.setText("Username");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel4.setText("Password");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel5.setText("Confirm Password");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel6.setText("Email");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel7.setText("Contact no.");

        contactno.setMinimumSize(new java.awt.Dimension(64, 27));

        Button_signup.setBackground(new java.awt.Color(0, 102, 102));
        Button_signup.setFont(new java.awt.Font("Segoe UI", 0, 14));
        Button_signup.setForeground(new java.awt.Color(255, 255, 255));
        Button_signup.setText("Sign Up");
        Button_signup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Button_signupActionPerformed(evt);
            }
        });
    

        jLabel8.setText("Already have an account?");

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jButton2.setForeground(new java.awt.Color(255, 102, 102));
        jButton2.setText("Log in");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
 
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel7)
                                .addComponent(jLabel5)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(email, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                                .addComponent(confirmpassword)
                                .addComponent(password)
                                .addComponent(username)
                                .addComponent(contactno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel8)
                        .addGap(66, 66, 66)
                        .addComponent(jButton2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(Button_signup, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(158, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confirmpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contactno, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(Button_signup, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jButton2))
                .addContainerGap(210, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3);
        jPanel3.setBounds(460, 0, 540, 800);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>  
    
        private void Button_signupActionPerformed(ActionEvent evt) {
                 String Username, Password, Confirmpassword, Email, Contactno, query;
                 Connection connection = null;
                 Statement statement = null;

             try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javafx-video", "root", "Shuheng0330.");
            statement = connection.createStatement();

            Username = username.getText();
            Password = password.getText();
            Confirmpassword = confirmpassword.getText();
            Email = email.getText();
            Contactno = contactno.getText();

            ResultSet resultSetEmail = statement.executeQuery("SELECT * FROM users WHERE email = '" + Email + "'");
            if (resultSetEmail.next()) {
                JOptionPane.showMessageDialog(new JFrame(), "This Email is already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                ResultSet resultSetContact = statement.executeQuery("SELECT * FROM users WHERE contactno = '" + Contactno + "'");
                if (resultSetContact.next()) {
                    JOptionPane.showMessageDialog(new JFrame(), "This contactno is already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    if ("".equals(Username) || "".equals(Password) || "".equals(Confirmpassword) || "".equals(Email) || "".equals(Contactno)) {
                        JOptionPane.showMessageDialog(new JFrame(), "All the credentials must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (!Password.equals(Confirmpassword)) {
                        JOptionPane.showMessageDialog(new JFrame(), "Please confirm your password correctly!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE username = '" + Username + "'");
                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(new JFrame(), "This username has been used!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            query = "INSERT INTO users(username,password,email,contactno,confirmpassword) VALUES('" + Username + "','" + Password + "','" + Email + "','" + Contactno + "','" + Confirmpassword + "')";
                            statement.execute(query);

                            username.setText("");
                            password.setText("");
                            confirmpassword.setText("");
                            email.setText("");
                            contactno.setText("");
                            JOptionPane.showMessageDialog(null, "New account has been created successfully!");
                        }
                    }
                    resultSetContact.close(); 
                }
            }
            resultSetEmail.close(); 

        } catch (Exception e) {
            System.out.println("Error!" + e.getMessage());
        } finally {
            try {
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
        
            private void jButton2ActionPerformed(ActionEvent evt) {
                 LoginForm1 LoginFrame=new LoginForm1();
                 LoginFrame.setVisible(true);
                 LoginFrame.pack();
                 LoginFrame.setLocationRelativeTo(null);
                 dispose();
    
             }
    
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SignUpForm1().setVisible(true);
                new SignUpForm1().setLocationRelativeTo(null);

            }
        });
    }
}  
        