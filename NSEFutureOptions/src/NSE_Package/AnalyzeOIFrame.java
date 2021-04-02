package NSE_Package;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import com.toedter.calendar.JDateChooser;

import MainPackage.MainMenuFrame;

public class AnalyzeOIFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    MainMenuFrame parent;
    AnalyzeOIFrame self;
    private JTextField csvDownloadFile;
    JLabel lblProc;

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
					AnalyzeOIFrame frame = new AnalyzeOIFrame(null);
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
	public AnalyzeOIFrame(MainMenuFrame parent) {
		super("OpenInterest Analyzer");
    	this.parent = parent;
    	this.self = this;
    	setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(400, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(200, 244, 247));
        
        JDateChooser dateChooser1 = new JDateChooser();
        dateChooser1.setDateFormatString("yyyy-MM-dd");
        dateChooser1.setBounds(189, 85, 222, 40);
        contentPane.add(dateChooser1);
        
        JDateChooser dateChooser2 = new JDateChooser();
        dateChooser2.setDateFormatString("yyyy-MM-dd");
        dateChooser2.setBounds(189, 136, 222, 40);
        contentPane.add(dateChooser2);

        JLabel lblStartFile = new JLabel("Start date\r\n:");
        lblStartFile.setFont(new Font("Calibri", Font.BOLD, 16));
        lblStartFile.setBounds(78, 86, 96, 39);
        contentPane.add(lblStartFile);

        JLabel lblEndfile = new JLabel("End date:");
		lblEndfile.setFont(new Font("Calibri", Font.BOLD, 16));
		lblEndfile.setBounds(78, 137, 80, 39);
		contentPane.add(lblEndfile);
           
        JButton btnAnalyze = new JButton("Analyze OpenInterest");
        btnAnalyze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblProc = new JLabel("Analyzing .....................");
                lblProc.setForeground(Color.RED);
                lblProc.setFont(new Font("Tahoma", Font.BOLD, 14));
                lblProc.setBounds(231, 20, 252, 31);
                
                contentPane.add(lblProc);
                
            	lblProc.setVisible(true);
            	
            	dateChooser1.getDate();
            	SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd");
            	String s1=sdf1.format(dateChooser1.getDate());
            	System.out.println(s1);
            	
            	dateChooser2.getDate();
            	SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd");
            	String s2=sdf2.format(dateChooser2.getDate());
            	System.out.println(s2);
                try {
                   	
                	System.out.println("Start");
                    OIAnalyzer.createTables(s1, s2);
                    System.out.println("End");
                    Thread.sleep(5000);
                    
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"Error analyzing data!","createTables",JOptionPane.OK_OPTION);
                    ex.printStackTrace();
                }
                lblProc.setVisible(false);
            }
        });

        btnAnalyze.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnAnalyze.setBounds(189, 186, 202, 31);
        contentPane.add(btnAnalyze);
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               parent.setVisible(true);
               self.dispose();
            }
        });

        btnBack.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnBack.setBounds(189, 363, 150, 31);
        contentPane.add(btnBack);
        
        JButton btnDownload = new JButton("Download");
        btnDownload.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        		String fn = sdf.format(new Date());
        		String oifilename = csvDownloadFile.getText()+"\\OISum"+fn;
        		
        		try {
        			DBToOICSVFile.downloadOISum(oifilename);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error creating output file");
				}
        	}
        });
        btnDownload.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnDownload.setBounds(189, 281, 150, 31);
        contentPane.add(btnDownload);
        
        JLabel lblSaveFileTo = new JLabel("Save file to:");
        lblSaveFileTo.setFont(new Font("Calibri", Font.BOLD, 16));
        lblSaveFileTo.setBounds(78, 234, 96, 39);
        contentPane.add(lblSaveFileTo);
        
        csvDownloadFile = new JTextField();
        csvDownloadFile.setBounds(189, 240, 325, 31);
        contentPane.add(csvDownloadFile);
        csvDownloadFile.setColumns(10);
        
        JButton btnChooseFile = new JButton("Choose folder");
        btnChooseFile.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		String home = System.getProperty("user.home");
        		File file = new File(home); 
        		JFileChooser jfc = new JFileChooser(file);
        		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int flag = jfc.showSaveDialog(contentPane);
                if(flag == JFileChooser.APPROVE_OPTION){
                	csvDownloadFile.setText(jfc.getSelectedFile().getPath());
                }
        	}
        });
        btnChooseFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnChooseFile.setBounds(524, 238, 150, 31);
        contentPane.add(btnChooseFile);
        
        JLabel lblProc_1 = new JLabel("Analyzing..... Please wait!");
        lblProc_1.setForeground(SystemColor.textHighlight);
        lblProc_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblProc_1.setBounds(403, 186, 252, 31);
        contentPane.add(lblProc_1);
        
        JTextPane txtpnEnterPreviousDate = new JTextPane();
        txtpnEnterPreviousDate.setText("Enter previous date to refer to previous close.");
        txtpnEnterPreviousDate.setBackground(SystemColor.info);
        txtpnEnterPreviousDate.setBounds(421, 92, 253, 24);
        contentPane.add(txtpnEnterPreviousDate);
        
        JLabel lblDownloaded = new JLabel("Downloaded");
        lblDownloaded.setForeground(SystemColor.textHighlight);
        lblDownloaded.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblDownloaded.setBounds(220, 320, 112, 31);
        contentPane.add(lblDownloaded);
	}
}