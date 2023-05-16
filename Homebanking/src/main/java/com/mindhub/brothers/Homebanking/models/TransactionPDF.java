package com.mindhub.brothers.Homebanking.models;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.print.Doc;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionPDF {
    private List<Transaction> listTransactions;
    private Account account;
    private static final String LOGO_PATH= "C:\\Users\\Familia Laguna\\Desktop\\Homebanking\\Homebanking\\src\\main\\resources\\static\\web\\img\\logoBlack.png";
    public TransactionPDF(List<Transaction> listTransactions, Account account){
        this.listTransactions = listTransactions;
        this.account = account;
    }

    private  void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(4);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(BaseColor.BLACK);

        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Transaction Type", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);
    }

    private void writeTableData ( PdfPTable table){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Transaction transaction: listTransactions){
            table.addCell(String.format(String.valueOf(transaction.getAmount())));
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getType()));
            table.addCell(transaction.getDate().format(formatter));
        }
    }

    public void usePDFExport(HttpServletResponse response) throws DocumentException, IOException{
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(BaseColor.BLUE);
        Font font2 = FontFactory.getFont(FontFactory.HELVETICA);
        font2.setColor(BaseColor.BLACK);
        Font font3 = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
        font3.setColor(BaseColor.BLACK);

        Image logo = Image.getInstance(LOGO_PATH);
        logo.scaleToFit(50, 50);
        logo.setAlignment(Image.ALIGN_LEFT);
        doc.add(new Paragraph("HOPENED BANK", font3));
        doc.add(logo);

        Paragraph p = new Paragraph("YOUR ACCOUNT STATUS", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        doc.add(p);

        Paragraph accountInfo = new Paragraph("Account Information", font2);
        accountInfo.setAlignment(Paragraph.ALIGN_LEFT);
        doc.add(accountInfo);

        doc.add(new Paragraph("Number: "+account.getNumber(),font2));
        doc.add(new Paragraph("Balance: "+account.getBalance(),font2));

        Paragraph p2 = new Paragraph("TRANSACTIONS", font3);
        p2.setAlignment(Paragraph.ALIGN_CENTER);
        doc.add(p2);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        doc.add(table);
        doc.close();
    }
}
