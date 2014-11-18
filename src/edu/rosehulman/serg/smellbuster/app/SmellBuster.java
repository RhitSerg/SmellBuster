package edu.rosehulman.serg.smellbuster.app;

import javax.swing.UIManager;
import javax.swing.WindowConstants;
import edu.rosehulman.serg.smellbuster.gui.VersionInputScreen;

public class SmellBuster  {

	public static void main(String[] args) throws Exception {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		VersionInputScreen frame = new VersionInputScreen();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}
}
