package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.UserManagement;

@SuppressWarnings("serial")
public class LoginScreen extends JPanel{
	
	// User interface objects
	private JButton btn_register = new JButton("Registreer");
	
	private JLabel lbl_action = new JLabel("Inloggen");
	
	private JLabel lbl_username = new JLabel("Gebruikersnaam");
	private JTextField input_username = new JTextField();
	private JLabel lbl_password = new JLabel("Wachtwoord");
	private JPasswordField input_password = new JPasswordField();
	private JLabel lbl_password_again = new JLabel("Wachtwoord");
	private JPasswordField input_password_again = new JPasswordField();
	private JButton btn_logIn = new JButton("Log in");
	
	// The action to perform when hitting the submit button
	private enum Action{
		REGISTRATE,
		LOGIN
	}
	private Action action = Action.LOGIN;
	
	public LoginScreen(){
		this.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent fe){
				initScreen();
			}
		});
		
		/* User interface */
		this.setLayout(null);
		
		btn_register.setSize(100, 25);
		btn_register.setLocation(Program.windowSize.width - btn_register.getWidth() - 8, 8);
		btn_register.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				// Switch between the login and registrate function 
				if(action == Action.LOGIN){
					action = Action.REGISTRATE;
					btn_register.setText("Login");
					lbl_action.setText("Registreren");
					btn_logIn.setText("Registreer");
					
					lbl_password_again.setVisible(true);
					input_password_again.setVisible(true);
				}else{
					action = Action.LOGIN;
					btn_register.setText("Registreer");
					lbl_action.setText("Inloggen");
					btn_logIn.setText("Log in");
					
					lbl_password_again.setVisible(false);
					input_password_again.setVisible(false);
				}
			}
		});
		this.add(btn_register);
		
		lbl_action.setSize(300, 50);
		lbl_action.setLocation((Program.windowSize.width * 1 / 2) - lbl_action.getWidth(), Program.windowSize.height * 1 / 10);
		lbl_action.setFont(new Font("arial", Font.BOLD, 40));
		this.add(lbl_action);
		
		lbl_username.setSize(200, 25);
		lbl_username.setLocation((Program.windowSize.width * 1 / 2) - lbl_username.getWidth(), Program.windowSize.height * 3 / 10);
		this.add(lbl_username);
		
		input_username.setSize(200, 25);
		input_username.setLocation((Program.windowSize.width * 1 / 2), Program.windowSize.height * 3 / 10);
		this.add(input_username);
		
		lbl_password.setSize(200, 25);
		lbl_password.setLocation((Program.windowSize.width * 1 / 2) - lbl_username.getWidth(), Program.windowSize.height * 4 / 10);
		this.add(lbl_password);
		
		input_password.setSize(200, 25);
		input_password.setLocation((Program.windowSize.width * 1 / 2), Program.windowSize.height * 4 / 10);
		this.add(input_password);

		lbl_password_again.setSize(200, 25);
		lbl_password_again.setLocation((Program.windowSize.width * 1 / 2) - lbl_username.getWidth(), Program.windowSize.height * 5 / 10);
		lbl_password_again.setVisible(false);
		this.add(lbl_password_again);
		
		input_password_again.setSize(200, 25);
		input_password_again.setLocation((Program.windowSize.width * 1 / 2), Program.windowSize.height * 5 / 10);
		input_password_again.setVisible(false);
		this.add(input_password_again);
		
		btn_logIn.setSize(200, 25);
		btn_logIn.setLocation((Program.windowSize.width * 1 / 2), Program.windowSize.height * 6 / 10);
		btn_logIn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				// If the user requested a login
				if(action == Action.LOGIN){
					// Try to log in
					Program.loggedInUser = UserManagement.userLogin(input_username.getText(), new String(input_password.getPassword()));
					if(Program.loggedInUser != null){
						Program.setFeedback("Gebruiker ingelogd", Color.green);
					}else{
						input_password.setText("");
						Program.setFeedback("Wachtwoord was onjuist", Color.red);
						return;
					}
				// If the user requested a registration
				}else if(action == Action.REGISTRATE){
					// First check if both password inputs are equal
					if(!equalCharArrays(input_password.getPassword(), input_password_again.getPassword())){
						Program.setFeedback("Wachtwoorden komen niet overeen", Color.red);
						return;
					}
					
					// Try to registrate
					Program.loggedInUser = UserManagement.addUser(input_username.getText(), new String(input_password.getPassword()));
					if(Program.loggedInUser != null){
						Program.setFeedback("Gebruiker aangemaakt en ingelogd", Color.green);
					}else{
						Program.setFeedback("Gebruiker niet aangemaakt; gebruikersnaam bestaat al", Color.red);
						return;
					}
				}
				Program.switchToPanel(PreGameScreen.class);
			}
		});
		this.add(btn_logIn);
		
	}
	
	private boolean equalCharArrays(char[] array1, char[] array2){
		if(array1.length == array2.length){
			for(int i = 0; i < array1.length; i++){
				if(array1[i] != array2[i]){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public void initScreen(){
		
	}
	
}
