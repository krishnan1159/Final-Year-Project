import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FrontEnd extends JFrame
{
	JPanel panel;
	JButton trainData,identify;
	public FrontEnd()
	{
		super("Spam Detection On Foursquare");
		panel     = new JPanel();
		setSize(600,600);
		
		trainData = new JButton("Train Sample Data");
		identify  = new JButton("Identify Spam Users");
		
		/* Action Listeners */
		trainData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent ae) 
			{
				System.out.println("Train Data Selected");
				TrainData td=null;
				try {
					td = new TrainData();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				td.setSize(600, 600);
				td.setVisible(true);
				td.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
		
		identify.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* Load Train Data */
			}
		});
		getContentPane().add(trainData);
		panel.add(trainData);
		panel.add(identify);
		getContentPane().add(panel);
		pack();
		//panel.setSize(500, 500);
	}
	
	public static void main(String[] args)
	{
		FrontEnd fe = new FrontEnd();
		fe.setVisible(true);
		fe.setSize(600,600);
		fe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
