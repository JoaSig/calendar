package dk.cream.team;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
 * Date: 04/04/13
 */
public class ITextTest {

    private static Map<String, Element> dateMap = new TreeMap<String, Element>();

    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
    private static Font HeaderFont = new Font(Font.FontFamily.UNDEFINED, 20);
    private static Font SubHeaderFont = new Font(Font.FontFamily.UNDEFINED, 18, Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static Font tinyFont = new Font(Font.FontFamily.UNDEFINED, 2);
    private static Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);
    private static Font normalBoldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

    final static String Event = "VEVENT";

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
    private static void addMetaData(Document document) {
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

    private static void addHeader(Document document) throws DocumentException {
        Chunk headerChunk = new Chunk("Log-bog", HeaderFont);
        headerChunk.setBackground(BaseColor.LIGHT_GRAY);
        headerChunk.setFont(catFont);
        headerChunk.setBackground(new BaseColor(208, 208, 208), 30, 13, 470, 15);
        Paragraph headerParagraph = new Paragraph(headerChunk);
        document.add(headerParagraph);
        newLine(document);
    }

    private static void newLine(Document document) throws DocumentException {
        document.add(new Paragraph(Chunk.NEWLINE));
    }

    private static void addBody(Document document) throws DocumentException, ParserException, IOException {
        populateTableMap();

        java.util.List<java.util.Date> dateList = new ArrayList<Date>();

        for (String dateFormat : dateMap.keySet()) {
            dateList.add(formatStringToDate(dateFormat));
        }

        Collections.sort(dateList);

        for (java.util.Date date : dateList) {
            document.add(dateMap.get(DateUtil.formatDateToString(date, DateUtil.SHORT_TIMELESS_DATE_PATTERN)));
            // Start a new page after every other event
            if (dateList.indexOf(date) % 2 != 0) {
                document.newPage();
            }
        }
    }

    private static void populateTableMap() throws IOException, ParserException, DocumentException {
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

    private static PdfPTable createNewTableWithHeader(Date startDate) {
        PdfPTable table;
        table = new PdfPTable(3);

        // add header
        table.addCell(createDateHeader(DateUtil.formatDateToString(startDate, DateUtil.SHORT_TIMELESS_DATE_PATTERN)));
        table.addCell(createBlackLine());
        return table;
    }

    private static void insertTableData(PdfPTable table, Date startDate, DtEnd endDate, Date stopDate, Duration duration, Property title, Property note, Location location) {
        table.addCell(createStartCell(getTimeString(startDate)));
        table.addCell(createStopCell(getTimeString(stopDate)));
        table.addCell(createDurationCell(getDuration(startDate, endDate, duration)));

        table.addCell(createTaskCell(title.getValue()));

        if (note != null) {
            table.addCell(new PdfPCell(new Phrase(new Chunk(note.getValue(), subFont))));
        }

        if (location != null) {
            table.addCell(createLocationCell(location.getValue()));
        }
        table.addCell(createEmptyCell());
    }

    private static String getTimeString(Date date) {
        return DateUtil.formatDateToString(date, DateUtil.TIME_ONLY_PATTERN);
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

    private static PdfPCell createLocationCell(String dateString) {
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


}
