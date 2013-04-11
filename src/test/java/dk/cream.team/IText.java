package dk.cream.team;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseField;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.Location;
import org.junit.Test;

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
import java.util.Set;
import java.util.TreeMap;

/**
 * Date: 04/04/13
 */
public class IText {

    public static Map<String, Element> dateMap = new TreeMap<String, Element>();

    public static Font catFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    public static Font HeaderFont = new Font(Font.FontFamily.UNDEFINED, 20);
    public static Font SubHeaderFont = new Font(Font.FontFamily.UNDEFINED, 18, Font.BOLD);
    public static Font subFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    public static Font tinyFont = new Font(Font.FontFamily.UNDEFINED, 2);
    public static Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
    public static Font normalBoldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    public static Paragraph NewLine = new Paragraph(Chunk.NEWLINE);

    final static String Event = "VEVENT";

    @Test
    public void testListAndTextFormatting() throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Formatting.pdf"));
        document.open();

        String text = "*Lumma* Þetta gerði ég hjá lummu:" +
                ">Bjó um<" +
                ">Tók til<" +
                ">Eldaði<" +
                "Stundum fór ég líka í garðinn.";

        Chunk chunk = new Chunk(text, HeaderFont);
        Chunk chunky = new Chunk(new DottedLineSeparator());

        Paragraph paragraph = new Paragraph("Here We go !!");
        paragraph.add(TextFormattingUtil.printAllZapfAndSymbolList());

        paragraph.add(NewLine);
        paragraph.add(chunk);
        paragraph.add(chunky);

        document.add(paragraph);

        document.close();

    }

//    @Test
//    public void testCreateRoundTable(Locale locale, int year) throws FileNotFoundException, DocumentException {
//        Document document = new Document(PageSize.A4.rotate());
//        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("")); document.open();
//        PdfPTable table;
//        Calendar calendar;
//        PdfContentByte canvas = writer.getDirectContent(); for (int month = 0; month < 12; month++) {
//            calendar = new GregorianCalendar(year, month, 1); drawImageAndText(canvas, calendar);
//            table = new PdfPTable(7); table.setTableEvent(tableBackground); table.setTotalWidth(504);
//            table.setLockedWidth(true); table.getDefaultCell().setBorder(PdfPCell.NO_BORDER); table.getDefaultCell().setCellEvent(lightRoundRectangle); table.addCell(getDateHeaderCell(calendar, locale));
////            Sets table event
////            B Specifies cell default: white
////                border, rounded corners
//                int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//                int day = 1;
//                int position = 2;
//                while (position != calendar.get(Calendar.DAY_OF_WEEK)) {
//                    position = (position % 7) + 1;
//                    table.addCell("");
//                }
//                while (day <= daysInMonth) {
//                    calendar = new GregorianCalendar(year, month, day++);
//                    table.addCell(getDayCell(calendar, locale));
////                    Formats month cell C
////                    Events for basic building blocks 133
//                }
//                table.completeRow();
//                table.writeSelectedRows(
//                        0, -1, 169, table.getTotalHeight() + 20, canvas);
//                document.newPage();
//        }
//        document.close();
//    }
//
//    public PdfPCell getDateHeaderCell(Calendar calendar, Locale locale) {
//        PdfPCell cell = new PdfPCell();
//        cell.setColspan(7);
//        cell.setBorder(PdfPCell.NO_BORDER);
//        cell.setUseDescender(true);
//        Paragraph p = new Paragraph(
//                String.format(locale, "%1$tB %1$tY", calendar), boldDarkGray);
//        p.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(p);
//        return cell;
//    }
//
//    public PdfPCell getDayCell(Calendar calendar, Locale locale) {
//        PdfPCell cell = new PdfPCell();
//        cell.setCellEvent(cellBackground);
//        if (isSunday(calendar) || isSpecialDay(calendar))
//            cell.setCellEvent(darkRoundRectangle);
//        cell.setPadding(3);
//        cell.setBorder(PdfPCell.NO_BORDER);
//        Chunk chunk = new Chunk(
////                D Formats day cells
////                ??E Formats
////                special day cells
//        String.format(locale, "%1$ta", calendar), small);
//        chunk.setTextRise(8);
//        Paragraph p = new Paragraph(chunk);
//        p.add(new Chunk(new VerticalPositionMark()));
//        p.add(new Chunk(String.format(locale, "%1$te", calendar), normal));
//        cell.addElement(p);
//        return cell;
//    }

    @Test
    public void testButton() throws DocumentException, IOException {
        Document document = new Document(PageSize.A5, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("button2.pdf"));
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        Image img = Image.getInstance("image.png");
//        PdfReader reader = new PdfReader("CALENDAR.pdf");
//        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("advButton.pdf"));
        PushbuttonField button = new PushbuttonField(writer, new Rectangle(200, 200, 300, 300), "Button1");
        button.setText("Open");
        button.setFontSize(12);
        button.setImage(img);
        button.setLayout(PushbuttonField.LAYOUT_ICON_TOP_LABEL_BOTTOM);
        button.setBackgroundColor(BaseColor.LIGHT_GRAY);
        button.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
        button.setBorderColor(BaseColor.DARK_GRAY);
        button.setBorderWidth(5);
        button.setVisibility(BaseField.VISIBLE);
        PdfAnnotation saveAsButton = button.getField();
//        PdfAction action = new PdfAction("YEAH.pdf", 1);
//        action.put(PdfName.NEWWINDOW, PdfBoolean.PDFTRUE);
//        saveAsButton.setAction(action);
        saveAsButton.setAction(PdfAction.javaScript("this.app.alert('Hi);", writer));
        writer.addAnnotation(saveAsButton);
        cb.moveTo(15, 30);

//        Utilities.readFileToString("buttons.js")

        PushbuttonField button2 = new PushbuttonField(writer, new Rectangle(100, 100, 200, 200), "Button2");
        button2.setText("This");
        button2.setFontSize(12);
        button2.setImage(img);
        button2.setLayout(PushbuttonField.LAYOUT_ICON_TOP_LABEL_BOTTOM);
        button2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        button2.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
        button2.setBorderColor(BaseColor.DARK_GRAY);
        button2.setBorderWidth(1);
//        PdfFormField ff = button.getField();
//        PdfAction ac = PdfAction.createSubmitForm("http://www.submit-site.com", null, 0);
        PdfAnnotation saveAsButton2 = button2.getField();
        saveAsButton2.setAction(PdfAction.createHide("Button1", false));
//        PdfAction ac = PdfAction.createSubmitForm("YEAH.pdf", null, 0);
//        ff.setAction(ac);
//        ff.setAction(PdfAction.javaScript("app.openDoc('YEAH.pdf')", stamper.getWriter()));
//        writer.addAnnotation(ff);
        writer.addAnnotation(saveAsButton);
        writer.addAnnotation(saveAsButton2);

//        stamper.close();
//        reader.close();
        document.newPage();

        PdfContentByte cbs = writer.getDirectContent();
        PushbuttonField saveAs = new PushbuttonField(writer, new Rectangle(636, 10, 716, 30), "Save");
        saveAs.setBorderColor(BaseColor.BLACK);
        saveAs.setText("Save");
        saveAs.setTextColor(BaseColor.RED);
        saveAs.setLayout(PushbuttonField.LAYOUT_LABEL_ONLY);
        saveAs.setRotation(90);
        PdfAnnotation but = saveAs.getField();
        but.setAction(PdfAction.gotoLocalPage("", true));
        writer.addAnnotation(but);

        document.close();

    }

    /**
     * Manipulates a PDF file src with the file dest as result
     * //     * @param src the original PDF
     * //     * @param dest the resulting PDF
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void testOtherButton() throws IOException, DocumentException {
        // Create a reader
        PdfReader reader = new PdfReader("CALENDAR.pdf");
        int n = reader.getNumberOfPages();
        // Create a stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("Crap.pdf"));
        // Create pushbutton 1
        PushbuttonField saveAs = new PushbuttonField(stamper.getWriter(), new Rectangle(636, 10, 716, 30), "Save");
        saveAs.setBorderColor(BaseColor.BLACK);
        saveAs.setText("Save");
        saveAs.setTextColor(BaseColor.RED);
        saveAs.setLayout(PushbuttonField.LAYOUT_LABEL_ONLY);
        saveAs.setRotation(90);
        PdfAnnotation saveAsButton = saveAs.getField();
        saveAsButton.setAction(PdfAction.gotoLocalPage("YEAH.pdf", true));
//        saveAsButton.setAction(PdfAction.javaScript("app.execMenuItem('Open')", stamper.getWriter()));
        // Create pushbutton 2
        PushbuttonField mail = new PushbuttonField(stamper.getWriter(), new Rectangle(736, 10, 816, 30), "Mail");
        mail.setBorderColor(BaseColor.BLACK);
        mail.setText("Mail");
        mail.setTextColor(BaseColor.RED);
        mail.setLayout(PushbuttonField.LAYOUT_LABEL_ONLY);
        mail.setRotation(90);
        PdfAnnotation mailButton = mail.getField();
        mailButton.setAction(PdfAction.javaScript(
                "app.execMenuItem('AcroSendMail:SendMail')", stamper.getWriter()));
        // Add the annotations to every page of the document
        for (int page = 1; page <= n; page++) {
            stamper.addAnnotation(saveAsButton, page);
            stamper.addAnnotation(mailButton, page);
        }
        // Close the stamper
        stamper.close();
        reader.close();
    }

    @Test
    public void testNamedActions() {
        Font symbol = new Font(Font.FontFamily.SYMBOL, 20);
        PdfPTable table = new PdfPTable(4);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        Chunk first = new Chunk(String.valueOf((char) 220), symbol);
        first.setAction(new PdfAction(PdfAction.FIRSTPAGE));
        table.addCell(new Phrase(first));
        Chunk previous = new Chunk(String.valueOf((char) 172), symbol);
        previous.setAction(new PdfAction(PdfAction.PREVPAGE));
        table.addCell(new Phrase(previous));
        Chunk next = new Chunk(String.valueOf((char) 174), symbol);
        next.setAction(new PdfAction(PdfAction.NEXTPAGE));
//        Jumps to first page
//        Jumps to previous page
//        Jumps to next page
        table.addCell(new Phrase(next));
        Chunk last = new Chunk(String.valueOf((char) 222), symbol);
        last.setAction(new PdfAction(PdfAction.LASTPAGE));
        table.addCell(new Phrase(last));
        table.setTotalWidth(120);
    }

    @Test
    public void testOutline() throws IOException {
//        PdfOutline pdfOutline = new PdfOutline();
        PdfReader reader = new PdfReader("form.pdf");

        AcroFields form = reader.getAcroFields();
        Set<String> fields = form.getFields().keySet();
        for (String key : fields) {
            System.out.println(key + ": ");
            switch (form.getFieldType(key)) {
                case AcroFields.FIELD_TYPE_CHECKBOX:
                    System.out.println("Checkbox");
                    break;
                case AcroFields.FIELD_TYPE_COMBO:
                    System.out.println("Combobox");
                    break;
                case AcroFields.FIELD_TYPE_LIST:
                    System.out.println("List");
                    break;
                case AcroFields.FIELD_TYPE_NONE:
                    System.out.println("None");
                    break;
                case AcroFields.FIELD_TYPE_PUSHBUTTON:
                    System.out.println("Pushbutton");
                    break;
                case AcroFields.FIELD_TYPE_RADIOBUTTON:
                    System.out.println("Radiobutton");
                    break;
                case AcroFields.FIELD_TYPE_SIGNATURE:
                    System.out.println("Signature");
                    break;
                case AcroFields.FIELD_TYPE_TEXT:
                    System.out.println("Text");
                    break;
                default:
                    System.out.println("?");
            }
        }
//        Map<String, AcroFields.Item> fields = acroFields.getFields();
//        for (AcroFields.Item item : fields.values()) {
//            if (item.size() != 0) {
//                item.values.get(0).getKeys().iterator().next().setContent("FUCK");
//                        item.getValue(0)
//            }
//        }
    }

    @Test
    public void iText() throws IOException, DocumentException, ParserException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("Log-bog.pdf"));
        document.open();
        addMetaData(document);
        addHeader(document);
        addBody(document);
        document.close();
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe Reader under File -> Properties
    public static void addMetaData(Document document) {
        document.left();
        document.addTitle("Log-bog");
        document.addSubject("Aktivitet");
        document.addAuthor("Mikkel Thura Mathiasen");
        document.addCreator("Mikkel Thura Mathiasen" +
                "    ->MIKKEL<-  -->  ? MIKKEL       " +
                "     21       " +
                "" +
                "" +
                "");
    }

    public static void addHeader(Document document) throws DocumentException {
        Chunk header = new Chunk("Log-bog", CustomCalendar.boldDarkGray);
        header.setBackground(new BaseColor(208, 208, 208), 50, 10, 690, 10);
        Phrase phrase = new Phrase(header);
        Paragraph headerParagraph = new Paragraph(phrase);
        headerParagraph.setSpacingBefore(5);
        headerParagraph.setExtraParagraphSpace(0);
        headerParagraph.setFirstLineIndent(5);
        headerParagraph.setKeepTogether(true);
        headerParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(headerParagraph);
    }

    public static void newLine(Document document) throws DocumentException {
        document.add(NewLine);
    }

    public static PdfPCell newLine() throws DocumentException {
        PdfPCell cell = new PdfPCell(new Phrase(Chunk.NEWLINE));
        cell.setColspan(3);
        cell.setBorderColor(BaseColor.WHITE);
        return cell;
    }

    public static Font NormalSymbolFont = new Font(Font.FontFamily.SYMBOL, 16);

    public static void addBody(Document document) throws DocumentException, ParserException, IOException {
        populateTableMap();

        java.util.List<java.util.Date> dateList = new ArrayList<Date>();

        for (String dateFormat : dateMap.keySet()) {
            dateList.add(DateUtil.formatStringToDate(dateFormat));
        }

        Collections.sort(dateList);

        for (java.util.Date date : dateList) {
            document.add(dateMap.get(DateUtil.formatDateToString(date, DateUtil.SHORT_TIMELESS_DATE_PATTERN)));
            // Start a new page after every other event
//            if (dateList.indexOf(date) % 2 != 0) {
            document.newPage();
//            }
        }

    }

    public static void populateTableMap() throws IOException, ParserException, DocumentException {
        FileInputStream fin = new FileInputStream("Group.ics");
        CalendarBuilder builder = new CalendarBuilder();
        net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);

        ComponentList events = calendar.getComponents(Event);

        for (Object component : events) {
            VEvent event = (VEvent) component;
            DtStart startDateTime = event.getStartDate();
            java.util.Date startDate = startDateTime.getDate();

            DtEnd endDate = event.getEndDate();
            java.util.Date stopDate = endDate.getDate();

            Duration duration = event.getDuration();

            Property title = event.getSummary();
            Property note = event.getDescription();
            Location location = event.getLocation();

            // if the date already exists, get it's table and add the new cells
            if (dateMap.containsKey(DateUtil.formatDateToString(startDate, DateUtil.SHORT_TIMELESS_DATE_PATTERN))) {
                PdfPTable table = (PdfPTable) dateMap.get(DateUtil.formatDateToString(startDate, DateUtil.SHORT_TIMELESS_DATE_PATTERN));
                insertTableData(table, startDate, endDate, stopDate, duration, title, note, location);

            } else {
                PdfPTable table = createNewTableWithHeader(startDate);
                insertTableData(table, startDate, endDate, stopDate, duration, title, note, location);
                dateMap.put(DateUtil.formatDateToString(startDate, DateUtil.SHORT_TIMELESS_DATE_PATTERN), table);
            }
        }
    }

    public static PdfPTable createNewTableWithHeader(Date startDate) {
        PdfPTable table = new PdfPTable(3);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        // add header
        table.addCell(createDateHeader(DateUtil.formatDateToString(startDate, DateUtil.SHORT_TIMELESS_DATE_PATTERN)));
        table.addCell(createBlackLine());
        return table;
    }

    public static void insertTableData(PdfPTable table, Date startDate, DtEnd endDate, Date stopDate, Duration duration, Property title, Property note, Location location) {
        table.addCell(createStartCell(getTimeString(startDate)));
        table.addCell(createStopCell(getTimeString(stopDate)));
        table.addCell(createDurationCell(getDuration(startDate, endDate.getDate(), duration)));

        table.addCell(createTaskCell(title.getValue()));

        if (note != null) {
            table.addCell(new PdfPCell(new Phrase(new Chunk(note.getValue(), subFont))));
        }

        if (location != null) {
            table.addCell(createLocationCell(location.getValue()));
        }
        table.addCell(createEmptyCell());
    }

    public static String getTimeString(Date date) {
        return DateUtil.formatDateToString(date, DateUtil.TIME_ONLY_PATTERN);
    }

    public static String getDuration(Date date, Date endDate, Duration duration) {
        String dur = "Total:  ";
        if (duration == null) {
            long durationInMilliS = endDate.getTime() - date.getTime();
            dur = dur + getDuration(durationInMilliS);

        } else {
            dur = dur + duration.getValue();
        }
        return dur;
    }

    public static String getDuration(long milliSec) {
        Integer mins = Integer.valueOf("" + milliSec) / 1000 / 60;
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

    public static PdfPCell createLocationCell(String dateString) {
        Chunk location = new Chunk("Lokation: " + dateString, normalBoldFont);
        PdfPCell cell = new PdfPCell(new Phrase(location));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(1);
        cell.setPaddingRight(5);
        cell.setPaddingTop(20);
        cell.setColspan(3);
        return cell;
    }

    public static PdfPCell createTaskCell(String dateString) {
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

    public static PdfPCell createEmptyCell() {
        Chunk blackLine = new Chunk(" ", normalFont);
        PdfPCell cell = new PdfPCell(new Phrase(blackLine));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setColspan(3);
        return cell;
    }

    public static PdfPCell createDurationCell(String dateString) {
        Chunk duration = new Chunk("Varighed: " + dateString, normalFont);
        PdfPCell cell = new PdfPCell(new Phrase(duration));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setPadding(10);
        return cell;
    }

    public static PdfPCell createStopCell(String dateString) {
        Chunk stop = new Chunk("Stop: " + dateString, normalFont);
        PdfPCell cell = new PdfPCell(new Phrase(stop));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setPadding(10);
        return cell;
    }

    public static PdfPCell createStartCell(String dateString) {
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

    public static PdfPCell createBlackLine() {
        Chunk blackLine = new Chunk(" ", tinyFont);
        blackLine.setLineHeight(new Float(0.001));
        blackLine.setBackground(BaseColor.BLACK, new Float(0.001), new Float(0.001), 420, new Float(0.001));
        PdfPCell cell = new PdfPCell(new Phrase(blackLine));
        cell.setBorderColor(BaseColor.WHITE);
        cell.setColspan(3);
        cell.setPadding(1);
        return cell;
    }

    public static PdfPCell createDateHeader(String dateString) {
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


}
