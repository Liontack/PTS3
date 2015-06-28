package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ConnectServerScreen extends JPanel{
	
	// User interface objects
	private JLabel lbl_ipAddress = new JLabel("Voer het ip-adres van de server in");
	private JTextField input_ipAddress = new JTextField();
	private JButton btn_connect = new JButton("Maak connectie");
	
	public ConnectServerScreen(){
		this.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent fe){
				initScreen();
			}
		});
		
		/* User interface */
		this.setLayout(null);

		lbl_ipAddress.setSize(Program.windowSize.width / 4, 30);
		lbl_ipAddress.setLocation((Program.windowSize.width - lbl_ipAddress.getWidth()) / 2, Program.windowSize.height * 2 / 10);
		this.add(lbl_ipAddress);
		
		input_ipAddress.setSize(Program.windowSize.width / 2, 30);
		input_ipAddress.setLocation((Program.windowSize.width - input_ipAddress.getWidth()) / 2, Program.windowSize.height * 3 / 10);
		this.add(input_ipAddress);
		
		btn_connect.setSize(250, 30);
		btn_connect.setLocation((Program.windowSize.width - btn_connect.getWidth()) / 2 , Program.windowSize.height * 4 / 10);
		btn_connect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				// Try to connect
				String ipAddress = input_ipAddress.getText();
				Program.connectToServer(ipAddress);
				
				try{
					Thread.sleep(400);
				}catch(InterruptedException exception){}
				
				// Go back to the start screen
				Program.switchToPanel(StartScreen.class);
			}
		});
		this.add(btn_connect);
	}
	
	public void initScreen(){
		// Clear input
		this.input_ipAddress.setText("");
	}
	
}
