package edu.rosehulman.serg.smellbuster.gui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import javax.swing.Icon;
import javax.swing.JLabel;

import edu.rosehulman.serg.smellbuster.logic.ResultTableLogic;

public class TableCell extends JLabel {
	private static final long serialVersionUID = 5620866148119992503L;

	private int row;
	private int col;
	private String[][] dataValues;
	private String[] columnNames;
	private int selectedMetric;
	private ResultTableLogic resultTableLogic;
	private boolean isHeader;

	public TableCell() {
		super();
	}

	public TableCell(String text, int row, int col,
			ResultTableLogic resultTableLogic) {
		super(text);
		this.row = row;
		this.col = col;
		this.resultTableLogic = resultTableLogic;
		this.selectedMetric = 0;
		this.isHeader = false;
	}

	public TableCell(String text, Icon icon, int horizontalAlignment, int row,
			int col, ResultTableLogic resultTableLogic) {
		super(text, icon, horizontalAlignment);
		this.row = row;
		this.col = col;
		this.resultTableLogic = resultTableLogic;
		this.selectedMetric = 0;
		this.isHeader = false;
	}

	public void setDataValues(String[][] dataValues) {
		this.dataValues = dataValues;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public void setSelectedMetric(int selectedMetric) {
		this.selectedMetric = selectedMetric;
	}
	
	public void setIsHeader(boolean isHeader){
		this.isHeader = isHeader;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		int w = getWidth();
		int h = getHeight();

		GradientPaint gp = getGradientPaint(w);

		Paint oldPaint = g2d.getPaint();
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
		g2d.setPaint(oldPaint);
		super.paintComponent(g);
	}

	private GradientPaint getGradientPaint(int w) {
		Color current = this.getColorForCell(row, col);
		this.setFontColor(current);
		GradientPaint gp = new GradientPaint(0, 0, current, w, 0, current);

		if (isHeader){
			this.setForeground(Color.WHITE);
			return new GradientPaint(0, 0, Color.BLACK, w, 0, Color.BLACK);
		}
		
		if (col == 0) {
			Color right = this.getColorForCell(row, col + 1);
			gp = new GradientPaint(0, 0, current, w, 0, right);
		} else if (col == dataValues[0].length - 1) {
			gp = new GradientPaint(0, 0, current, w, 0, current);
		} else {
			Color right = this.getColorForCell(row, col + 1);
			if (!current.equals(right)){
				gp = new GradientPaint(0, 0, current, w, 0, right);
			}
		}
		return gp;
	}

	private Color getColorForCell(int row, int col) {

		String value = this.dataValues[row][col];

		if (value.length() == 0) {
			return Color.LIGHT_GRAY;
		}

		return this.resultTableLogic.getColorForMetricScore(
				this.selectedMetric, value, this.columnNames[col]);
	}
	
	private void setFontColor(Color currentColor){
		int r = currentColor.getRed();
		int g = currentColor.getGreen();
		int b = currentColor.getBlue();
		int d = 0;
		double a = 1 - (0.299 * r + 0.587 * g + 0.114 * b) / 255;
		if (a < 0.5)
			d = 0;
		else
			d = 255;
		Color newColor = new Color(d, d, d);
		this.setForeground(newColor);
	}

	// public static void main(String[] args) {
	// JFrame f = new JFrame("Test");
	// JPanel p = (JPanel)f.getContentPane();
	// p.add(new JLabel("Test"), BorderLayout.NORTH);
	// p.add(new
	// TableCell("I am Dharmin! King of the world! Respect my autority!", 0,0),
	// BorderLayout.CENTER);
	// p.add(new JLabel("Bottom"), BorderLayout.SOUTH);
	// f.pack();
	// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// f.setVisible(true);
	// }
}
