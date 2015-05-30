package view;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	private JLabel lbl_username = new JLabel("Gebruikersnaam");
	private JTextField input_username = new JTextField();
	private JLabel lbl_password = new JLabel("Wachtwoord");
	private JPasswordField input_password = new JPasswordField();
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
		btn_register.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				// Switch between the login and registrate function 
				if(action == Action.LOGIN){
					action = Action.REGISTRATE;
					btn_register.setText("Login");
					btn_logIn.setText("Registreer");
				}else{
					action = Action.LOGIN;
					btn_register.setText("Registreer");
					btn_logIn.setText("Log in");
				}
			}
		});
		this.add(btn_register);
		
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
		
		btn_logIn.setSize(200, 25);
		btn_logIn.setLocation((Program.windowSize.width * 1 / 2), Program.windowSize.height * 5 / 10);
		btn_logIn.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent event){
				// If the user requested a login
				if(action == Action.LOGIN){
					// Try to log in
					Program.loggedInUser = UserManagement.userLogin(input_username.getText(), new String(input_password.getPassword()));
					if(Program.loggedInUser != null){
						Program.setFeedback("Gebruiker ingelogd", Color.green);
					}else{
						input_password.setText("");
						Program.setFeedback("Wachtwoord was onjuist", Color.red);
					}
				// If the user requested a registration
				}else if(action == Action.REGISTRATE){
					// Try to registrate
					Program.loggedInUser = UserManagement.addUser(input_username.getText(), new String(input_password.getPassword()));
					if(Program.loggedInUser != null){
						Program.setFeedback("Gebruiker aangemaakt en ingelogd", Color.green);
					}else{
						Program.setFeedback("Gebruiker niet aangemaakt; gebruikersnaam bestaat al", Color.red);
					}
				}
				Program.switchToPanel(PreGameScreen.class);
			}
		});
		this.add(btn_logIn);
		
	}
	
	public void initScreen(){
		
	}
	
}
