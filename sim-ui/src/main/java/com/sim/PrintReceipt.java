package com.sim;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class PrintReceipt implements Printable {
	int lines;
	ArrayList<String> text1;
//	ArrayList<PrintableLine> plal;
	public PrintReceipt(String text, int lines) {
		this.lines = lines;
		text1 = new ArrayList<String>(lines);
		StringTokenizer st = new StringTokenizer(text, "\n");
		while(st.hasMoreTokens()){
			text1.add(st.nextToken());
		}
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf = job.defaultPage();
		double margin = 1.0;
		Paper paper = new Paper();
		paper.setSize(216.0, (double) (paper.getHeight() + lines*10.0));
		paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight() - margin * 2);
		pf.setPaper(paper);
		pf.setOrientation(PageFormat.PORTRAIT);
		job.setPrintable(this, pf);
		try {
		  job.print();
		} catch (PrinterException ex) {
			System.out.println("Printing Failed Try Again" + ex);
		 }
	} 
	/*public PrintReceipt(ArrayList<PrintableLine> plal) {
		this.plal = plal;
		PrinterJob job = PrinterJob.getPrinterJob();
		PageFormat pf = job.defaultPage();
		double margin = 1.0;
		Paper paper = new Paper();
		paper.setSize(216.0, (double) (paper.getHeight() + plal.size()*10.0));
		paper.setImageableArea(margin, margin, paper.getWidth() - margin * 2, paper.getHeight() - margin * 2);
		pf.setPaper(paper);
		pf.setOrientation(PageFormat.PORTRAIT);
		job.setPrintable(this, pf);
		try {
		  job.print();
		} catch (PrinterException ex) {
			System.out.println("Printing Failed Try Again");
		 }
	} */
	/*@SuppressWarnings("unused")
	public int print(Graphics g, PageFormat pf, int page) {
		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		int i;
		Graphics2D g2d = (Graphics2D) g;
		Line2D.Double line = new Line2D.Double();
		g2d.setColor(Color.black);
		g2d.translate(pf.getImageableX(), pf.getImageableY());
		int y = 15;
		if(this.plal == null) {
			for (String s : text1) {
				g2d.drawString(s, 1, y);
				y += 15;
			}
		} else {
			FontMetrics fm = g2d.getFontMetrics();
			for (PrintableLine pl : plal) {
				if(pl.isSingleLine()){
					g2d.drawString(pl.getText1(), 1, y);
				} else {
					int x = (int) (pf.getImageableWidth() - fm.stringWidth(pl.getText2()) - 5);
					//while(fm.stringWidth(pl.getText1()) <= x) {
					//	pl.setText1(pl.getText1().substring(0, pl.getText1().length() - 2));
					//}
					if (fm.stringWidth(pl.getText1()) - 5 > x && pl.getText1().length() > 20) {
						g2d.drawString(pl.getText1().substring(0,20), 1, y);
						y += 15;
						g2d.drawString(pl.getText1().substring(20,pl.getText1().length()), 1, y);
					} else {
						g2d.drawString(pl.getText1(), 1, y);
					}
					g2d.drawString(pl.getText2(), x, y);
				}
				y += 15;
			}
		}
		return PAGE_EXISTS;
	}*/
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	 public static void main(String[] args) {
		 try {
			 
			 PrintReceipt pr = new PrintReceipt("Hakim /n saifee/n", 2);
			 System.out.println("Done");
		 }catch (Exception e) {
			 System.out.println(e);
		 }
	}
}