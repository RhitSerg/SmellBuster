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

	public void setIsHeader(boolean isHeader) {
		this.isHeader = isHeader;
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		int w = getWidth();
		int h = getHeight();
		float leftFactor = 0.3f;
		float rightFactor = 0.7f;

		GradientPaint[] gp = getGradientPaint(w, leftFactor, rightFactor);

		Paint oldPaint = g2d.getPaint();
		g2d.setPaint(gp[0]);
		g2d.fillRect(0, 0, (int) (leftFactor * w), h);

		g2d.setPaint(gp[1]);
		g2d.fillRect((int) (leftFactor * w), 0, (int) (0.4 * w) + 1, h);

		g2d.setPaint(gp[2]);
		g2d.fillRect((int) (rightFactor * w), 0, (int) (leftFactor * w) + 1, h);

		g2d.setPaint(oldPaint);
		super.paintComponent(g);
	}

	private GradientPaint[] getGradientPaint(int w, float leftFactor,
			float rightFactor) {
		Color current = this.getColorForCell(row, col);

		float leftX = leftFactor * w;
		float rightX = rightFactor * w;

		GradientPaint[] gradients = new GradientPaint[] {
				new GradientPaint(0, 0, current, leftX, 0, current),
				new GradientPaint(leftX, 0, current, rightX, 0, current),
				new GradientPaint(rightX, 0, current, w, 0, current) };

		this.setFontColor(current);
		
		if (this.dataValues[row][col].length() == 0 && !isHeader){
			return gradients;
		}else if (isHeader) {
			this.setForeground(Color.WHITE);
			gradients[0] = new GradientPaint(0, 0, Color.BLACK, leftX, 0,
					Color.BLACK);
			gradients[1] = new GradientPaint(leftX, 0, Color.BLACK, rightX, 0,
					Color.BLACK);
			gradients[2] = new GradientPaint(rightX, 0, Color.BLACK, w, 0,
					Color.BLACK);
			return gradients;
		}

		if (col == 0) {
			Color right = this.getColorForCell(row, col + 1);
			gradients[2] = new GradientPaint(rightX, 0, current, w, 0, right);
		} else if (col == dataValues[0].length - 1) {
			Color left = this.getColorForCell(row, col - 1);
			gradients[0] = new GradientPaint(0, 0, left, leftX, 0, current);
		} else {

			Color left = this.getColorForCell(row, col - 1);
			Color right = this.getColorForCell(row, col + 1);
			String leftCell = this.dataValues[row][col-1];
			if (!current.equals(left) && leftCell.length()==0) {
				gradients[0] = new GradientPaint(0, 0, left, leftX, 0, current);
			}
			if (!current.equals(right)) {
				gradients[2] = new GradientPaint(rightX, 0, current, w, 0,
						right);
			}
		}
		return gradients;
	}

	private Color getColorForCell(int row, int col) {

		String value = this.dataValues[row][col];

		if (value.length() == 0) {
			return Color.LIGHT_GRAY;
		}

		return this.resultTableLogic.getColorForMetricScore(
				this.selectedMetric, value, this.columnNames[col]);
	}

	private void setFontColor(Color currentColor) {
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
