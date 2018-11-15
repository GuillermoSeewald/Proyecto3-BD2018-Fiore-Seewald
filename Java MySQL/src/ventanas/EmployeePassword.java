package ventanas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EmployeePassword extends PasswordDialog {
	private static final long serialVersionUID = 1L;
	private Connection conexionDataBase = null;

	public EmployeePassword(GUI gui){
		super(gui);
		
		this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent evt) {
               disconnectDataBase();
            }
            public void componentShown(ComponentEvent evt) {
               connectDataBase();
            }
         });

		label_user.setText("Numero de legajo");
		get_user_name.setEditable(true);
		get_user_name.setText("");
		get_user_name.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c= arg0.getKeyChar();
				if(! (Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE)))
					arg0.consume();
			}
		});
		okButton.addActionListener(new ListenerDialogQueryButtons());
		cancelButton.addActionListener(new ListenerDialogQueryButtons());
		connectDataBase();
	}
	
	private class ListenerDialogQueryButtons implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String s = e.getActionCommand();
			switch(s){
				case("OK"):{
					boolean igual=false;
					String legajo= get_user_name.getText();
					String password= get_password.getText();
					if(conexionDataBase!=null) {
						try {
							String selection = "SELECT * FROM empleado WHERE legajo=" + legajo + " AND password=md5('" + password + "')";
							Statement statement = conexionDataBase.createStatement();
							ResultSet result = statement.executeQuery(selection);
							
							while(result.next()){
								igual=true;
							}
					        
						} catch (SQLException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
						}
					if(igual)
						gui.EmployeePasswordChecked(legajo);
					else {
						JOptionPane.showMessageDialog(getContentPane(),
                                "Contraseña incorrecta",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
					}
				}
				case("Cancel"):{
					dispose();
					break;
				}
			}
		}
	}
	
	private void connectDataBase(){
        try {
    		String driver ="com.mysql.cj.jdbc.Driver";
        	String servidor = "localhost:3306";
            String baseDatos = "banco";
            String usuario = "empleado";
            String clave = "empleado";
            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
            this.conexionDataBase = DriverManager.getConnection(uriConexion, usuario, clave);
		} catch (SQLException e) {
			String output_message = "Se produjo un error al intentar conectarse a la base de datos.\n" + e.getMessage();
			JOptionPane.showMessageDialog(this, output_message, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void disconnectDataBase(){
		if (this.conexionDataBase != null){
	         try{
	            this.conexionDataBase.close();
	            this.conexionDataBase = null;
	         }
	         catch (SQLException ex){}
	      }
	}
	
}