/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pricetracker;

class UserData {
    private String Username;
    private String Password;
    private String Email;
    private String Contactno;

    public UserData(String Username, String Password,String Email,String Contactno) {
        this.Username=Username;
        this.Password=Password;
        this.Email=Email;
        this.Contactno=Contactno;
    }

    // Getters for user data
    public String getUsername() {
        return Username;
    }
    public String getPassword() {
        return Password;
    }  
    
    public String getEmail() {
        return Email;
    }
    public String getContactno() {
        return Contactno;
    }

}
