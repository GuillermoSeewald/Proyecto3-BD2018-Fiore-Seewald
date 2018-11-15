package ventanas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.GridLayout;

public abstract class PasswordDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	protected GUI gui;
	protected JTextField get_user_name;
	protected JPasswordField get_password;
	protected JLabel label_user, label_password;
	protected JButton okButton, cancelButton;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel panel_2;

	/**
	 * Create the dialog.
	 */
	public PasswordDialog(GUI gui) {
		setBounds(100, 100, 300, 150);
		this.gui = gui;
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setVisible(false);
        this.setTitle("Ingresar");
        this.setResizable(false);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		createNorthPane();
		createSouthPane();
	}
	
	private void createNorthPane(){
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		panel_2 = new JPanel();
		contentPanel.add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new GridLayout(2, 1, 0, 0));
		
		panel = new JPanel();
		panel_2.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JPanel user_panel = new JPanel();
		panel.add(user_panel);
		
		label_user = new JLabel("User");
		user_panel.add(label_user);
		
		get_user_name = new JTextField();
		user_panel.add(get_user_name);
		get_user_name.setEditable(false);
		get_user_name.setText("admin");
		get_user_name.setColumns(10);
		
		panel_1 = new JPanel();
		panel_2.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		JPanel password_panel = new JPanel();
		panel_1.add(password_panel);
		
		label_password = new JLabel("Password");
		password_panel.add(label_password);
		
		get_password = new JPasswordField();
		get_password.setEchoChar('*');
		password_panel.add(get_password);
		get_password.setColumns(10);
	}
	
	private void createSouthPane(){
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		okButton = new JButton("OK");
		okButton.setBackground(Color.LIGHT_GRAY);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.setFocusable(false);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBackground(Color.LIGHT_GRAY);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.setFocusable(false);
	}
	
	
}
