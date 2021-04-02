package MainPackage;


import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import NSE_Package.AnalyzeFrame;
import NSE_Package.AnalyzeOIFrame;
import NSE_Package.UploadFrame;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainMenuFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	MainMenuFrame self;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			} catch(Exception e) {
				e.printStackTrace();
			}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenuFrame frame = new MainMenuFrame();
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
	public MainMenuFrame() {
		super("Main Menu");
		self = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(200, 244, 247));
		
		JLabel lblNewLabel = new JLabel("Menu options");
		lblNewLabel.setFont(new Font("Calibri", Font.BOLD, 18));
		lblNewLabel.setBounds(351, 91, 127, 56);
		contentPane.add(lblNewLabel);
		
		JButton btnUpload = new JButton("Upload");
		btnUpload.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				UploadFrame f = new UploadFrame(self);
				self.setVisible(false);
			}
		});
		btnUpload.setBounds(328, 149, 150, 31);
		contentPane.add(btnUpload);
		
		JButton btnAnalyaze = new JButton("Analyze GL");
		btnAnalyaze.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAnalyaze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				AnalyzeFrame f = new AnalyzeFrame(self);
				self.setVisible(false);
			}
		});
		btnAnalyaze.setBounds(328, 192, 150, 31);
		contentPane.add(btnAnalyaze);
		
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(328, 321, 150, 31);
		contentPane.add(btnExit);
		
		JButton btnAnalyzeOpenint = new JButton("Analyze OInt");
		btnAnalyzeOpenint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				AnalyzeOIFrame f = new AnalyzeOIFrame(self);
				self.setVisible(false);
			}
		});
		btnAnalyzeOpenint.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAnalyzeOpenint.setBounds(328, 235, 150, 31);
		contentPane.add(btnAnalyzeOpenint);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDelete.setBounds(328, 278, 150, 31);
		contentPane.add(btnDelete);
	}
}
