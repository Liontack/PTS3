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
import model.server.UserManagement;

@SuppressWarnings("serial")
public class LoginScreen extends JPanel{
	
	// User interface objects
	private JButton btn_register = new JButton("Registreer");
	private JLabel lbl_username = new JLabel("Gebruikersnaam");
	private JTextField input_username = new JTextField();
	private JLabel lbl_password = new JLabel("Wachtwoord");
	private JPasswordField input_password = new JPasswordField();
	private JButton btn_logIn = new JButton("Log in");

	private JLabel lbl_fail_login = new JLabel("Wachtwoord was onjuist.");
	
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
				Program.switchToPanel(RegistrationScreen.class);
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
				Program.loggedInUser = UserManagement.userLogin(input_username.getText(), new String(input_password.getPassword()));
				if(Program.loggedInUser != null){
					Program.switchToPanel(PreGameScreen.class);
				}else{
					lbl_fail_login.setVisible(true);
					input_password.setText("");
				}
			}
		});
		this.add(btn_logIn);
		
		lbl_fail_login.setSize(200, 25);
		lbl_fail_login.setLocation((Program.windowSize.width * 1 / 2), Program.windowSize.height * 6 / 10);
		lbl_fail_login.setForeground(Color.red);
		this.add(lbl_fail_login);
		
	}
	
	public void initScreen(){
		lbl_fail_login.setVisible(false);
	}
	
}
