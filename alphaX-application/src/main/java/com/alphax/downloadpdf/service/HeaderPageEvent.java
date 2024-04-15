package com.alphax.downloadpdf.service;

import org.apache.commons.lang3.StringUtils;

import com.alphax.common.constants.RestInputConstants;
import com.alphax.vo.mb.ReportHeaderDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author A106744104
 */

public class HeaderPageEvent extends PdfPageEventHelper {

//	private static Font font = FontFactory.getFont(FontFactory.COURIER, 8);
//	private static Font font2 = FontFactory.getFont(FontFactory.COURIER, 10);
	
	private Font font;

	private PdfTemplate t;
	private Image total;
	private ReportHeaderDTO reportDetailList;


	public void setReportHeaderList(ReportHeaderDTO reportList, Font font) {
		this.reportDetailList = reportList;
		this.font= font;
	}


	public void onOpenDocument(PdfWriter writer, Document document) {
		t = writer.getDirectContent().createTemplate(30, 16);
		try {
			total = Image.getInstance(t);
			total.setRole(PdfName.ARTIFACT);
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}


	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {			
			if(writer.getCurrentDocumentSize() > 127) {
				addHeader(writer);
			}
			
		} catch (DocumentException e) {			
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	

	private void addHeader(PdfWriter writer)  throws DocumentException, Exception{

		//main table content
		PdfPTable mainTable = new PdfPTable(1);
		mainTable.setWidthPercentage(100);
		mainTable.getDefaultCell().setBorder(0);
		mainTable.getDefaultCell().setPadding(0);
		mainTable.setTotalWidth(535);
		mainTable.setLockedWidth(true);


		PdfPTable nestedTable1 = new PdfPTable(2);
		int[] columnWidth = {70, 30};

		nestedTable1.setWidthPercentage(100);
		nestedTable1.setWidths(columnWidth);
		nestedTable1.addCell(getCell(reportDetailList.getReportName(), PdfPCell.ALIGN_LEFT));
		nestedTable1.addCell(getCell(RestInputConstants.INVENTORY_DATE +": "+ 
				(reportDetailList.getReportDate() != null ? reportDetailList.getReportDate() : "-"), PdfPCell.ALIGN_RIGHT));

		PdfPTable nestedTable2 = new PdfPTable(4);
		int[] columnWidth1 = {80, 9, 6, 5};
		nestedTable2.setWidthPercentage(100);
		nestedTable2.setWidths(columnWidth1);

		nestedTable2.addCell(getCell(RestInputConstants.INVENTORY_NAME +": "+ reportDetailList.getInventoryListName(), PdfPCell.ALIGN_LEFT));
		nestedTable2.addCell(getCell(RestInputConstants.PAGE_COUNT +": ", PdfPCell.RIGHT));

		// add cell for page number count
		PdfPCell pageCountCell = new PdfPCell(new Phrase(StringUtils.leftPad(String.valueOf(writer.getPageNumber()), 3, "0")+"/", font));
		pageCountCell.setBorder(PdfPCell.NO_BORDER);
		pageCountCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		pageCountCell.setPaddingTop(2);
		pageCountCell.setPaddingBottom(3);
		nestedTable2.addCell(pageCountCell);

		// add placeholder for total page count
		PdfPCell totalPageCount = new PdfPCell(total);
		totalPageCount.setBorder(PdfPCell.NO_BORDER);
		totalPageCount.setPaddingTop(2);
		totalPageCount.setPaddingBottom(3);
		nestedTable2.addCell(totalPageCount);   	    		

		//table 3 for warehouse data
		PdfPTable nestedTable3 = new PdfPTable(2);
		int[] columnWidth2 = {60, 40};

		nestedTable3.setWidthPercentage(100);
		nestedTable3.setWidths(columnWidth2);

		nestedTable3.addCell(getCell(RestInputConstants.WAREHOUSE_NUMBER +": "+ reportDetailList.getWarehouseId().toString() +" - "+reportDetailList.getWarehouseName(), 
				PdfPCell.ALIGN_LEFT));		
		nestedTable3.addCell(getCell(RestInputConstants.ACTIVATE_DATE +": "+ (reportDetailList.getActivationDate() != null ?  
				reportDetailList.getActivationDate() : "-"), PdfPCell.ALIGN_RIGHT));
		nestedTable3.addCell(getCell(RestInputConstants.COMAPNY_NAME +": "+ reportDetailList.getAxCompanyName(), PdfPCell.ALIGN_LEFT));
		nestedTable3.addCell(getCell(" ", PdfPCell.ALIGN_RIGHT));

		//add nested tables to main-table
		mainTable.addCell(nestedTable1);
		mainTable.addCell(nestedTable2);

		PdfPCell lineSepratorCell = new PdfPCell();		
		lineSepratorCell.setBorder(0);
		lineSepratorCell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		lineSepratorCell.setPadding(0f);
		lineSepratorCell.addElement(getLineSeperator());

		mainTable.addCell(lineSepratorCell);
		mainTable.addCell(nestedTable3);
		mainTable.addCell(lineSepratorCell);

		// write page
		PdfContentByte canvas = writer.getDirectContent();
		canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
		mainTable.writeSelectedRows(0, -1, 29, 800, writer.getDirectContent());   
		canvas.endMarkedContentSequence();	
		
	}
	
	
	
	public void onCloseDocument(PdfWriter writer, Document document) {
		
		if(font.getSize() == 10) {
			ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
				new Phrase(StringUtils.leftPad(String.valueOf(writer.getPageNumber()), 3, "0"), font), 1, 6, 0);
		}
		else {
			ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
					new Phrase(StringUtils.leftPad(String.valueOf(writer.getPageNumber()), 3, "0"), font), 1, 8, 0);
		}
	}


	public PdfPCell getCell(String text, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setPaddingLeft(2);
		cell.setPaddingBottom(5);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}


	public CustomDashedLineSeparator getLineSeperator() {

		CustomDashedLineSeparator separator = new CustomDashedLineSeparator();
		separator.setDash(3);
		separator.setGap(3);
		separator.setLineWidth(0.5f);

		return separator;
	}

}
