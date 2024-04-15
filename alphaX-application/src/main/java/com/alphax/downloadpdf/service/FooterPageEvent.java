package com.alphax.downloadpdf.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author A106744104
 */

public class FooterPageEvent extends PdfPageEventHelper {
	
	private static Font font = FontFactory.getFont(FontFactory.COURIER, 10);
	
	
	@Override
    public void onEndPage(PdfWriter writer, Document document) {
		if(writer.getCurrentDocumentSize() > 127) {
			addFooter(writer);
		}
    }
	

	private void addFooter(PdfWriter writer){
		
		PdfPTable footer = new PdfPTable(1);
		footer.setTotalWidth(527);
		footer.setLockedWidth(true);
		footer.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		 
		Paragraph p2 = new Paragraph();		
		leaveEmptyLine(p2, 2);
		p2.setAlignment(Element.ALIGN_LEFT);
		
		footer.addCell("");
		footer.addCell(new Paragraph(
				"Unterschrift 1  ____________________        Unterschrift 2  ____________________", font));

		// write page
		footer.writeSelectedRows(0, -1, 40, 50, writer.getDirectContent());

	}
	
	
	/**
	 * This method is used to create empty lines in PDF
	 * 
	 * @param paragraph
	 * @param number
	 */
	private static void leaveEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

}
