package edu.rosehulman.serg.smellbuster.app;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;

import edu.rosehulman.serg.smellbuster.gui.VersionInputScreen;

public class SmellBuster  {

	public static void main(String[] args) throws Exception {

		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
//			
//		    UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
//
//		    UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
//		    
//		    UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
//		    
//		    UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
//		    
//		    UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
//		    
//		    UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
		} catch (Exception e) {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		

		VersionInputScreen frame = new VersionInputScreen();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}
}
