package NSE_Package;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import MainPackage.MainMenuFrame;

public class UploadFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JTextField txtStartFile;
    MainMenuFrame parent;
    UploadFrame self;

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
                    UploadFrame frame = new UploadFrame(null);
                    frame.setVisible(true);
                    frame.setSize(600, 600);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public UploadFrame(MainMenuFrame parent) {
    	super("Upload Files");
    	self = this;
    	this.parent = parent;
    	setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(200, 244, 247));

        txtStartFile = new JTextField();
        txtStartFile.setBounds(166, 92, 413, 31);
        contentPane.add(txtStartFile);
        txtStartFile.setColumns(10);

        JLabel lblStartFile = new JLabel("F & O file:");
        lblStartFile.setFont(new Font("Calibri", Font.BOLD, 16));
        lblStartFile.setBounds(94, 91, 80, 39);
        contentPane.add(lblStartFile);

        JButton btnStartfile = new JButton("Choose file");
        btnStartfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                int flag = jfc.showOpenDialog(contentPane);
                if(flag == JFileChooser.APPROVE_OPTION){
                    txtStartFile.setText(jfc.getSelectedFile().getPath());
                }
            }
        });
        btnStartfile.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnStartfile.setBounds(581, 91, 150, 32);
        contentPane.add(btnStartfile);

        JButton btnUpload = new JButton("Upload");
        btnUpload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    CsvToDBInserter.upload(txtStartFile.getText());
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"Error uploading data","Upload",JOptionPane.OK_OPTION);
                    ex.printStackTrace();
                }
            }
        });

        btnUpload.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnUpload.setBounds(317, 135, 150, 31);
        contentPane.add(btnUpload);
   
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               parent.setVisible(true);
               self.dispose();
            }
        });

        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnBack.setBounds(317, 176, 150, 31);
        contentPane.add(btnBack);
        
        JLabel lblUploaded = new JLabel("Uploaded");
        lblUploaded.setForeground(SystemColor.textHighlight);
        lblUploaded.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblUploaded.setBounds(479, 135, 112, 31);
        contentPane.add(lblUploaded);
    }
}
