package ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JButton;
import javax.swing.JDialog;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private QueryWindow queryWindow;
	private ATMWindow ATMWindow;
	private LoanWindow loanWindow;
	private JDialog queryPasswordDialog, pinPasswordDialog, employeePasswordDialog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(900, 500);
        this.setTitle("Java y MySQL");
        contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setBounds(200, 40, 900, 500);
		setContentPane(contentPane);
		
		queryWindow = new QueryWindow();
		queryWindow.setVisible(false);
		ATMWindow = new ATMWindow();
		ATMWindow.setVisible(false);
		loanWindow = new LoanWindow();
		loanWindow.setVisible(false);
		queryPasswordDialog = new QueryPassword(this);
		queryPasswordDialog.setVisible(false);
		pinPasswordDialog = new PINPassword(this);
		pinPasswordDialog.setVisible(false);
		employeePasswordDialog = new EmployeePassword(this);
		employeePasswordDialog.setVisible(false);
		
		contentPane.add(ATMWindow);
		contentPane.add(queryWindow);
		contentPane.add(loanWindow);
		

		String[] commands = {"Query" , "ATM", "Prestamos"};
		MenuButtonListener listener = new MenuButtonListener();
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		for(int i=0;i<commands.length;i++){
			JButton button = new JButton(commands[i]);
			button.setFocusable(false);
			button.setActionCommand(commands[i]);
			button.addActionListener(listener);
			button.setBackground(Color.LIGHT_GRAY);
			menuBar.add(button);
		}
        
	}
	
	/*private void setLoanPane(){
		try {
			loanWindow.setMaximum(true);
		} catch (PropertyVetoException e) {
		}
		loanWindow.setVisible(true);
		queryPasswordDialog.setVisible(false);
		pinPasswordDialog.setVisible(false);
		queryWindow.setVisible(false);
		ATMWindow.setVisible(false);
	}*/
	
	private void showQueryPasswordDialog(){
		queryPasswordDialog = new QueryPassword(this);
		centrarDialogo(queryPasswordDialog);
		queryPasswordDialog.setVisible(true);
		pinPasswordDialog.setVisible(false);
		employeePasswordDialog.setVisible(false);
	}
	
	public void queryPasswordChecked(){
		try {
			queryWindow.setMaximum(true);
		} catch (PropertyVetoException e) {
		}
		queryWindow.setMostrar(true);
		queryPasswordDialog.setVisible(false);
		pinPasswordDialog.setVisible(false);
		ATMWindow.setMostrar(false);
		loanWindow.setVisible(false);
		employeePasswordDialog.setVisible(false);
	}
	
	public void showPINPasswordDialog(){
		pinPasswordDialog = new PINPassword(this);
		centrarDialogo(pinPasswordDialog);
		pinPasswordDialog.setVisible(true);
		queryPasswordDialog.setVisible(false);
		employeePasswordDialog.setVisible(false);
	}
	
	public void PINPasswordChecked(String tarjeta){
		try {
			ATMWindow.setMaximum(true);
		} catch (PropertyVetoException e) {
		}
		ATMWindow.pasarTarjeta(tarjeta);
		ATMWindow.setMostrar(true);
		queryPasswordDialog.setVisible(false);
		pinPasswordDialog.setVisible(false);
		employeePasswordDialog.setVisible(false);
		queryWindow.setMostrar(false);
		loanWindow.setVisible(false);
	}
		
		public void showEmployeePasswordDialog(){
			employeePasswordDialog = new EmployeePassword(this);
			centrarDialogo(employeePasswordDialog);
			employeePasswordDialog.setVisible(true);
			queryPasswordDialog.setVisible(false);
			pinPasswordDialog.setVisible(false);
		}
		
		public void EmployeePasswordChecked(String legajo) {
			try {
				loanWindow.setMaximum(true);
			} catch (PropertyVetoException e) {
			}
			loanWindow.pasarLegajo(legajo);
			loanWindow.setMostrar(true);
			ATMWindow.setMostrar(false);
			queryPasswordDialog.setVisible(false);
			pinPasswordDialog.setVisible(false);
			queryWindow.setMostrar(false);
			employeePasswordDialog.setVisible(false);
			
		}
	
	private class MenuButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String s=e.getActionCommand();
			switch(s){
				case("Query"):{
					if(!queryWindow.isVisible())
						showQueryPasswordDialog();
					break;
				}
				case("ATM"):{
					if(!ATMWindow.isVisible())
						showPINPasswordDialog();
					break;
				}
				case("Prestamos"):{
					if(!loanWindow.isVisible())
						showEmployeePasswordDialog();
					break;
				}
			}
		}
	}
	
	private void centrarDialogo(javax.swing.JDialog p_dialogo){
	     p_dialogo.setLocationRelativeTo(this);
	     p_dialogo.setLocationByPlatform(false);
	     int desplzX = (int) ((this.getSize().getWidth() / 2.0) - (p_dialogo.getSize().getWidth() / 2.0));
	     int desplzY = (int) ((this.getSize().getHeight() / 2.0) - (p_dialogo.getSize().getHeight() / 2.0));
	     p_dialogo.setLocation(new Point((int) this.getLocationOnScreen().getX() + desplzX,
	                                     (int) this.getLocationOnScreen().getY() + desplzY));
	     }
}
