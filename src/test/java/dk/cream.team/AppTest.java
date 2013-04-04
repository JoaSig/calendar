package dk.cream.team;

//import junit.framework.Test;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Duration;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;


/**
 * Unit test for simple FormatCalendar.
 */
public class AppTest {

    final static String Event = "VEVENT";
    final static String EventTitle = "SUMMARY";
    final static String Note = "DESCRIPTION";

    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    private static Font HeaderFont = new Font(Font.FontFamily.UNDEFINED, 20);
    private static Font SubHeaderFont = new Font(Font.FontFamily.UNDEFINED, 18, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static Font tinyFont = new Font(Font.FontFamily.UNDEFINED, 2);
    private static Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
    private static Font normalBoldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    private String createFileFromSingleEvent() throws IOException, ParserException {
        FileInputStream fin = new FileInputStream("Hjem.ics");
        CalendarBuilder builder = new CalendarBuilder();
        net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);

        StringBuilder stringBuilder = new StringBuilder();

        File file = new File("calendarEvent.txt");

        Component event = calendar.getComponent(Event);
        Property title = event.getProperty(EventTitle);
        Property note = event.getProperty(Note);

        stringBuilder.append("\t\t\t");
        stringBuilder.append(title.getValue()).append("\n");
        stringBuilder.append(note.getValue());

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(stringBuilder.toString().getBytes());
        fileOutputStream.close();

        return stringBuilder.toString();
    }

    private static String getDuration(long millisek) {
        Integer mins = Integer.valueOf("" + millisek) / 1000 / 60;
        Integer totalMins = mins;
        Integer hrs = 0;
        if (mins / 60 >= 1) {
            hrs = mins / 60;
            totalMins = mins - (hrs * 60);
        }

        String stringMins = String.valueOf(totalMins);
        if (stringMins.length() < 2) {
            stringMins = "0".concat(stringMins);
        }

        String formattedMins = String.format("0%d:%s", hrs, stringMins);
        if (hrs >= 10) {
            formattedMins = String.format("%d:%s", hrs, stringMins);
        }
        return formattedMins;
    }

    @Test
    public void testCreatePDF() throws IOException, COSVisitorException, ParserException {

        PDDocument document = null;
        try {
            document = new PDDocument();
            //Every document requires at least one page, so we will add one
            //blank page.
            PDPage blankPage = new PDPage();
            document.addPage(blankPage);

            PDPageContentStream contentStream = new PDPageContentStream(document, blankPage);

            PDFont font = PDType1Font.HELVETICA_BOLD;

            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.moveTextPositionByAmount(100, 700);
            contentStream.drawString(createFileFromSingleEvent());
            contentStream.endText();

//            PDAcroForm form = new PDAcroForm(document);
//            COSDictionary dictionary = new COSDictionary();
//            PDPushButton button = new PDPushButton(form, dictionary);

            contentStream.close();

            document.save("test.pdf");
            document.close();


        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    public static java.util.Date formatStringToDate(String stringDate) {
        DateFormat df = new SimpleDateFormat(DateUtil.DATE_STRING_PATTERN);
        java.util.Date toDate = new java.util.Date();
        try {
            toDate = df.parse(stringDate);
        } catch (ParseException e) {
            return toDate;
        }
        return toDate;
    }

    @Test
    public void readPDF() throws IOException {
//        Document document = new Document();
        PdfReader reader = new PdfReader(new FileInputStream("CALENDAR.pdf"));

        AcroFields acroFields = reader.getAcroFields();
        Map<String, AcroFields.Item> fields = acroFields.getFields();
        for (AcroFields.Item item : fields.values()) {
            if (item.size() != 0) {
//                item.values.get(0).getKeys().iterator().next().setContent("FUCK");
//                        item.getValue(0)
            }
        }

        for (String s : fields.keySet()) {

            if (s.toLowerCase().contains("date".toLowerCase())) {
                AcroFields.Item item = fields.get(s);
                boolean string = item.getValue(0).getKeys().iterator().next().isString();
                AcroFields.Item newItem = new AcroFields.Item();

            }
        }
    }

    @Test
    public void iText() throws IOException, DocumentException, ParserException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Log-bog.pdf"));
        document.open();
        addMetaData(document);
        addTitlePage(document);
        addContent(document);
        document.close();
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.left();
        document.addTitle("Log-bog");
        document.addSubject("Aktivitet");
        document.addAuthor("Mikkel Thura Mathiasen");
        document.addCreator("Mikkel Thura Mathiasen" +
                "    ->MIKKEL<-  -->  • MIKKEL       " +
                "     21       " +
                "" +
                "" +
                "");
    }

    private static Map<String, Element> dateMap = new TreeMap<String, Element>();

    private static void populateMap() throws IOException, ParserException, DocumentException {
        FileInputStream fin = new FileInputStream("Group.ics");
        CalendarBuilder builder = new CalendarBuilder();
        net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);

        ComponentList events = calendar.getComponents(Event);

        Paragraph preface;

        for (Object component : events) {
            VEvent event = (VEvent) component;
            DtStart startDate = event.getStartDate();

            java.util.Date date = startDate.getDate();

            DtEnd endDate = event.getEndDate();

            Duration duration = event.getDuration();

            Property title = event.getSummary();
            Property note = event.getDescription();

            if (dateMap.containsKey(DateUtil.formatDateToString(date, DateUtil.SHORT_TIMELESS_DATE_PATTERN))) {
                preface = (Paragraph) dateMap.get(DateUtil.formatDateToString(date, DateUtil.SHORT_TIMELESS_DATE_PATTERN));
                addEmptyLine(preface, 2);

                preface.add(new Paragraph(String.format("%s - %s (varighed: %s)", getTimeString(date),
                        getTimeString(endDate.getDate()), getDuration(date, endDate, duration)), redFont));

                addEmptyLine(preface, 2);

                preface.add(new Paragraph(title.getValue(), subFont));

                if (note != null) {
                    addEmptyLine(preface, 1);
                    preface.add(new Paragraph(note.getValue(), subFont));
                }

            } else {
                preface = new Paragraph();
                addEmptyLine(preface, 10);

                preface = new Paragraph();

                // add header
                addEmptyLine(preface, 1);
                preface.add(new Paragraph(DateUtil.formatDateToString(date, DateUtil.SHORT_TIMELESS_DATE_PATTERN), catFont));

                preface.add(new Paragraph(String.format("%s - %s (varighed: %s)", getTimeString(date),
                        getTimeString(endDate.getDate()), getDuration(date, endDate, duration)), redFont));

                addEmptyLine(preface, 2);

                preface.add(new Paragraph(title.getValue(), subFont));

                if (note != null) {
                    addEmptyLine(preface, 1);
                    preface.add(new Paragraph(note.getValue(), subFont));
                }

                dateMap.put(DateUtil.formatDateToString(date, DateUtil.SHORT_TIMELESS_DATE_PATTERN), preface);
            }

        }

    }

    private static String getTimeString(Date date) {
        return DateUtil.formatDateToString(date, DateUtil.TIME_ONLY_PATTERN);
    }

    private static void addTitlePage(Document document) throws DocumentException, IOException, ParserException {
        populateMap();
        java.util.List<java.util.Date> dateList = new ArrayList<java.util.Date>();

        for (String dateFormat : dateMap.keySet()) {
            dateList.add(formatStringToDate(dateFormat));
        }

        Collections.sort(dateList);

        for (java.util.Date date : dateList) {
            Paragraph preface = (Paragraph) dateMap.get(DateUtil.formatDateToString(date, DateUtil.SHORT_TIMELESS_DATE_PATTERN));

            document.add(preface);
            // Start a new page
            if (dateList.indexOf(date) % 2 != 0) {
                document.newPage();
            }
        }
    }

    private static String getDuration(java.util.Date date, DtEnd endDate, Duration duration) {
        String dur;
        if (duration == null) {
            long durationInMilliS = endDate.getDate().getTime() - date.getTime();
            dur = getDuration(durationInMilliS);

        } else {
            dur = duration.getValue();
        }
        return dur;
    }

    private void addContent(Document document) throws DocumentException, IOException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // Add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // Add a table
        createTable(subCatPart);

        // Now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // Now add all this to the document
        document.add(catPart);

    }

    private static void createTable(Section subCatPart) throws BadElementException, IOException {
        PdfPTable table = new PdfPTable(3);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.addElement(Image.getInstance("Letter-S-violet-icon.png"));


//        HashMap<PdfName, PdfObject> accessibleAttributes = c1.getAccessibleAttributes();
//        for (PdfName pdfName : accessibleAttributes.keySet()) {
//            boolean dictionary = pdfName.isDictionary();
//        }
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    @Test
    public void writeColoredChunksAndParagraphs() throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));
        document.open();

        Chunk headerChunk = new Chunk("Log-bog", HeaderFont);
        headerChunk.setBackground(BaseColor.LIGHT_GRAY);
        headerChunk.setFont(catFont);
        headerChunk.setBackground(BaseColor.LIGHT_GRAY, 30, 15, 471, 15);

        Paragraph headerParagraph = new Paragraph(headerChunk);

        document.add(headerParagraph);

        PdfContentByte cb = writer.getDirectContent();

        int x = 100;
        int y = 500;

        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cb.saveState();
        cb.beginText();
        cb.moveText(x, y);
        cb.setFontAndSize(bf, 24);
        cb.showText("This");
        cb.endText();
//        cb.addImage(Image.getInstance(readImage()));
        cb.restoreState();

        Chunk textAsChunk = new Chunk("This is Chunk", redFont);
        textAsChunk.setBackground(new BaseColor(202, 202, 202));

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(textAsChunk), x, y, 0);

        document.close();

    }

    private byte[] readImage() throws IOException {
        return FileUtils.readFileToByteArray(new File("Letter-S-violet-icon.png"));
    }

    @Test
    public void writeTableWithBorderColors() throws DocumentException, FileNotFoundException {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("TableCellBorder.pdf"));
            document.open();

            addHeader(document);

            String dateString = "01.03.13";

            // a table with three columns
            PdfPTable table = new PdfPTable(3);

            // add the 'Date' cell with colspan 3 and padding 15
            table.addCell(createDateHeader(dateString));

            // add the black line with colspan of 3 and 0 padding
            table.addCell(createBlackLine());

            // add the 'Start' cell
            table.addCell(createStartCell(dateString));

            // add the 'Stop' cell
            table.addCell(createStopCell(dateString));

            // add the 'Varighed'
            table.addCell(createDurationCell(dateString));

            // add the 'Task'
            table.addCell(createTaskCell(dateString));

            // add the 'Location'
            table.addCell(createLocationCell(dateString));

            table.setComplete(true);

            document.add(table);

        } finally {
            document.close();
        }
    }

    private static PdfPCell createLocationCell(String dateString) {
        Chunk location = new Chunk("Lokation: " + dateString, normalBoldFont);
        PdfPCell cell = new PdfPCell(new Phrase(location));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(1);
        cell.setPaddingRight(5);
        cell.setPaddingTop(30);
        cell.setColspan(3);
        return cell;
    }

    private static PdfPCell createTaskCell(String dateString) {
        Chunk task = new Chunk("Task: " + dateString, normalFont);
        PdfPCell cell = new PdfPCell(new Phrase(task));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(30);
        cell.setPaddingRight(5);
        cell.setPaddingTop(10);
        cell.setColspan(3);
        cell.setRowspan(5);
        return cell;
    }

    private static PdfPCell createEmptyCell() {
        Chunk blackLine = new Chunk(" ", normalFont);
        PdfPCell cell = new PdfPCell(new Phrase(blackLine));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setColspan(3);
        return cell;
    }

    private static PdfPCell createDurationCell(String dateString) {
        Chunk duration = new Chunk("Varighed: " + dateString, normalFont);
        PdfPCell cell = new PdfPCell(new Phrase(duration));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setPadding(10);
        return cell;
    }

    private static PdfPCell createStopCell(String dateString) {
        Chunk stop = new Chunk("Stop: " + dateString, normalFont);
        PdfPCell cell = new PdfPCell(new Phrase(stop));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setPadding(10);
        return cell;
    }

    private static PdfPCell createStartCell(String dateString) {
        PdfPCell cell;
        Chunk start = new Chunk("Start: " + dateString, normalFont);
        cell = new PdfPCell(new Phrase(start));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(30);
        cell.setPaddingRight(5);
        cell.setPaddingTop(10);
        return cell;
    }

    private static PdfPCell createBlackLine() {
        Chunk blackLine = new Chunk(" ", tinyFont);
        blackLine.setLineHeight(new Float(0.001));
        blackLine.setBackground(BaseColor.BLACK, new Float(0.001), new Float(0.001), 420, new Float(0.001));
        PdfPCell cell = new PdfPCell(new Phrase(blackLine));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setColspan(3);
        cell.setPadding(1);
        return cell;
    }

    private static PdfPCell createDateHeader(String dateString) {
        Chunk date = new Chunk("Dato: " + dateString, SubHeaderFont);
        PdfPCell cell = new PdfPCell(new Phrase(date));
        cell.setColspan(3);
        cell.setPaddingBottom(18);
        cell.setPaddingLeft(10);
        cell.setPaddingRight(18);
        cell.setPaddingTop(18);
        cell.setBorderColor(BaseColor.WHITE);
        return cell;
    }

    private static void addHeader(Document document) throws DocumentException {
        Chunk headerChunk = new Chunk("Log-bog", HeaderFont);
        headerChunk.setBackground(BaseColor.LIGHT_GRAY);
        headerChunk.setFont(catFont);
        headerChunk.setBackground(new BaseColor(208, 208, 208), 30, 13, 470, 15);
        Paragraph headerParagraph = new Paragraph(headerChunk);
        document.add(headerParagraph);

        document.add(new Paragraph(Chunk.NEWLINE));
    }
}
