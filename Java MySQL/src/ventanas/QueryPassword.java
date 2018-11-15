package ventanas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class QueryPassword extends PasswordDialog {
	private static final long serialVersionUID = 1L;

	public QueryPassword(GUI gui){
		super(gui);
		

		okButton.addActionListener(new ListenerDialogQueryButtons());
		cancelButton.addActionListener(new ListenerDialogQueryButtons());
	}
	
	private class ListenerDialogQueryButtons implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String s = e.getActionCommand();
			switch(s){
				case("OK"):{
					String password = "admin";
					String pass = "";
					char[] p = get_password.getPassword();
					for(int i=0;i<p.length;i++){
						pass += p[i];
					}
					if(pass.equals(password))
						gui.queryPasswordChecked();
					else{
						JOptionPane.showMessageDialog(getContentPane(),
                                "Contraseña incorrecta",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				case("Cancel"):{
					dispose();
					break;
				}
			}
		}
	}
}
