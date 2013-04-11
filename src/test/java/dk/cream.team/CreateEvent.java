package dk.cream.team;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAcroForm;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Location;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Date: 04/04/13
 */
public class CreateEvent extends PdfBoxCalendar {

    /**
     * The resulting PDF.
     */
    public static final String RESULT = "cal.pdf";
    public static final String RESULT2 = "calo.pdf";

    public static Map<String, TimeDuration> timeDurationMap = new TreeMap<String, TimeDuration>();
    public static Map<Integer, VEvent> dateEventMap = new HashMap<Integer, VEvent>();

    private int red = 194;
    private int green = 194;
    private int blue = 194;

    /**
     * A table event.
     */
    public PdfPTableEvent tableBackground;
    /**
     * A cell event.
     */
    public PdfPCellEvent cellBackground;
    /**
     * A cell event.
     */
    public PdfPCellEvent darkRoundRectangle;
    /**
     * A cell event.
     */
    public PdfPCellEvent lightRoundRectangle;

    /**
     * Inner class with a table event that draws a background with rounded corners.
     */
    class TableBackground implements PdfPTableEvent {

        int red = 249;
        int green = 249;
        int blue = 249;

        TableBackground(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public TableBackground() {
        }

        public void tableLayout(PdfPTable table, float[][] width, float[] height, int headerRows, int rowStart, PdfContentByte[] canvas) {
            PdfContentByte background = canvas[PdfPTable.BASECANVAS];
            background.saveState();
            // gray fill
//            background.setRGBColorFill(84, 84, 84);

            background.setRGBColorFill(red, green, blue);
//            background.setRGBColorFillF(207, 205, 205);
            background.roundRectangle(
                    width[0][0] - 2,
                    height[height.length - 1] - 6,
                    width[0][1] - width[0][0] + 3,
                    height[0] - height[height.length - 1] + 4,
                    4);
            background.fill();
            background.restoreState();
        }

    }

    /**
     * Inner class with a cell event that draws a background with rounded corners.
     */
    class CellBackground implements PdfPCellEvent {

        int color = 0x25;

        int red = 0;
        int green = 0;
        int blue = 0;

        CellBackground(int color) {
            this.color = color;
        }

        CellBackground(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public CellBackground() {
        }

        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.BACKGROUNDCANVAS];

//            canvas.saveState();

//            PdfWriter writer = canvas.getPdfWriter();


//            PdfShading axial = PdfShading.simpleAxial(writer,
//                    rect.getHeight() / 2, // x
//                    rect.getWidth() / 2, // y
//                    rect.getHeight(), // w
//                    rect.getWidth(), // h
//                    new BaseColor(0x00, 0x00, 0x00, color),
//                    new BaseColor(0xF7, 0x8A, 0x6B));

//                    new BaseColor(241, 241, 241),
//                    baseColor != null ? baseColor : new BaseColor(241, 241, 241),
//                    new BaseColor(94, 94, 94));
//                    new BaseColor(94, 94, 94));

//            PdfShading radial = PdfShading.simpleRadial(writer, 200, 700, 50, 300, 700, 100,
//                    new BaseColor(0xFF, 0xF7, 0x94),
//                    new BaseColor(0xF7, 0x8A, 0x6B),
//                    false, false);
//            canvas.paintShading(radial);

//            PdfShadingPattern shading = new PdfShadingPattern(axial);
//            canvas.setShadingFill(shading);
//            canvas.setColorFill(new ShadingColor(shading));

            canvas.roundRectangle(
                    rect.getLeft() + 1f, // x
                    rect.getBottom() - 2f, // y
                    rect.getWidth() - 4, // w
                    rect.getHeight() - 3, 4); // h

            if (red == 0 && green == 0 && blue == 0) {
                canvas.setCMYKColorFill(0x00, 0x00, 0x00, color);

            } else {
                canvas.setRGBColorFill(red, green, blue);

            }
//            canvas.paintShading(shading);

            canvas.fill();
//            canvas.restoreState();
        }
    }

    /**
     * Inner class with a cell event that draws a border with rounded corners.
     */
    class RoundRectangle implements PdfPCellEvent {
        /**
         * the border color described as CMYK values.
         */
        protected int[] color;

        /**
         * Constructs the event using a certain color.
         */
        public RoundRectangle(int[] color) {
            this.color = color;
        }

        public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                    rect.getLeft() + 2f,
                    rect.getBottom() + 2f,
                    rect.getWidth() + 20,
                    rect.getHeight() - 6, 4);
//            cb.setLineWidth(0.5f);
            cb.setLineDash(0.5f);
            cb.setCMYKColorStrokeF(color[0], color[1], color[2], color[3]);
            cb.stroke();
        }
    }

    public static String[] times = {
            "09:00",
            "09:15",
            "09:30",
            "09:45",

            "10:00",
            "10:15",
            "10:30",
            "10:45",

            "11:00",
            "11:15",
            "11:30",
            "11:45",

            "12:00",
            "12:15",
            "12:30",
            "12:45",

            "13:00",
            "13:15",
            "13:30",
            "13:45",

            "14:00",
            "14:15",
            "14:30",
            "14:45",

            "15:00",
            "15:15",
            "15:30",
            "15:45",

            "16:00",
            "16:15",
            "16:30",
            "16:45",

            "17:00",
            "17:15",
            "17:30",
            "17:45"
    };

    public static String[] exportTimes = {
            "09:00",
            "09:15",
            "09:30",
            "09:45",

            "10:00",
            "10:15",
            "10:30",
            "10:45",

            "11:00",
            "11:15",
            "11:30",
            "11:45",

            "12:00",
            "12:15",
            "12:30",
            "12:45",

            "13:00",
            "13:15",
            "13:30",
            "13:45",

            "14:00",
            "14:15",
            "14:30",
            "14:45",

            "15:00",
            "15:15",
            "15:30",
            "15:45",

            "16:00",
            "16:15",
            "16:30",
            "16:45",

            "17:00",
            "17:15",
            "17:30",
            "17:45"
    };

    HashMap<String, TextField> cache = new HashMap<String, TextField>();

    private static TextField createTextArea(String fieldName, PdfWriter writer) {
        TextField text = new TextField(writer, rectangle, fieldName);
        text.setBackgroundColor(new GrayColor(0.95f));
        text.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
        text.setBorderColor(BaseColor.BLACK);
        text.setBorderWidth(15);
        text.setFontSize(8);
        text.setOptions(TextField.MULTILINE | TextField.REQUIRED);
        return text;

    }

    static Rectangle rectangle = new Rectangle(36, 806, 126, 780);
    static Rectangle rectangle2 = new Rectangle(1, 20, 46, 30);
    static Rectangle rectangle3 = new Rectangle(300, 806, 360, 788);

    private static TextField createChoicesField(String fieldName, PdfWriter writer) throws IOException, DocumentException {
        TextField startTime = new TextField(writer, rectangle2, fieldName);
        startTime.setChoices(times);
        startTime.setChoiceSelection(2);
        startTime.setOptions(TextField.EDIT);
        startTime.setChoiceExports(exportTimes);
        startTime.setFontSize(8);
        startTime.setBorderColor(gray100);
        startTime.setBorderWidth(2f);
        startTime.setBorderStyle(PdfBorderDictionary.STYLE_INSET);
        startTime.setBackgroundColor(BaseColor.PINK);
//        ArrayList<Integer> selections = new ArrayList<Integer>();
//        selections.add(0);
//        selections.add(2);
//        startTime.setChoiceSelections(selections);
//        writer.addAnnotation(startTime.getComboField());
        return startTime;

    }

    static int llx = 36;
    static int lly = 806;
    static int urx = 126;
    static int ury = 780;

    private static TextField createDateField(String fieldName, PdfWriter writer) throws IOException, DocumentException {
        TextField timeField = new TextField(writer, new Rectangle(llx*2, lly+30, urx, ury), fieldName);
        timeField.setBorderColor(new GrayColor(0.2f));
        timeField.setText("10:30");
        PdfFormField datefield = timeField.getTextField();
        datefield.setAdditionalActions(
                PdfName.V, PdfAction.javaScript(
                "AFTime_Format(0);", writer));
        writer.addAnnotation(datefield);
        return timeField;
    }

    /**
     * Main method creating the PDF.
     *
     * @param args no arguments needed
     * @throws java.io.IOException
     * @throws com.itextpdf.text.DocumentException
     *
     */
    public static void mains(String[] args) throws IOException, DocumentException, ParserException {
        FontFactory.registerDirectory("/Users/Jo/Downloads/SFM_3.0.2_Magic/system/fonts");
        FontFactory.registerDirectory("/Library/Fonts");
//        Locale locale = new Locale(LANGUAGE);
        new CreateCalendarEvent().createCalendar(RESULT);
//        new CustomCalendar().createCalendar(RESULT2, locale, YEAR);
    }

    private static String javascript = "";

    public static void main(String[] args) throws IOException, DocumentException, ParserException {
        FontFactory.registerDirectory("/Users/Jo/Downloads/SFM_3.0.2_Magic/system/fonts");
        FontFactory.registerDirectory("/Library/Fonts");

        // step 1
        Document document = new Document(PageSize.A4);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("fields.pdf"));
        // step 3
        document.open();

        writer.addJavaScript(Utilities.readFileToString(javascript));

        PdfAcroForm acroForm = writer.getAcroForm();
        acroForm.setNeedAppearances(true);

//        TextField task = createTextArea("task", writer);
//        task.setText("IIIhhhaaaa");

//        acroForm.addHiddenField("hidden", "ghost");

//        writer.addAnnotation(task.getTextField());


        TextField list = createChoicesField("list", writer);
        writer.addAnnotation(list.getListField());

        acroForm.addMultiLineTextField("task2", "buhu", BaseFont.createFont(), 12, 50, 50, 200, 100);
        PdfFormField select = acroForm.addSelectList("select", times, "07:00", BaseFont.createFont(), 8, 20, 10, 60, 20);
        select.setFieldFlags(TextField.EDIT | TextField.COMB);
        select.setBorderStyle(new PdfBorderDictionary(1, PdfBorderDictionary.STYLE_DASHED));

        acroForm.drawButton(PdfFormField.createPushButton(writer), "Takk", BaseFont.createFont(), 8, llx, lly, urx, ury);

        PushbuttonField button = new PushbuttonField(writer, rectangle3, "testButton");
        PdfFormField field = button.getField();
        field.setAction(
                PdfAction.javaScript("this.showButtonState()", writer));
        writer.addAnnotation(field);



//        createDateField("startTime", writer);
//        startTime.setText("09:00");


        document.close();


    }

    /**
     * Initializes the fonts and collections.
     *
     * @throws com.itextpdf.text.DocumentException
     *
     * @throws java.io.IOException
     */
    public CreateEvent() throws DocumentException, IOException {
        super();
        tableBackground = new TableBackground();
        cellBackground = new CellBackground();
        darkRoundRectangle = new RoundRectangle(new int[]{0x00, 0x00, 0x00, 0x48});
        lightRoundRectangle = new RoundRectangle(new int[]{0x00, 0x00, 0x00, 0x25});
    }

    /**
     * Creates a PDF document.
     *
     * @param filename the path to the new PDF document
     * @param locale   Locale in case you want to create
     *                 a Calendar in another language
     * @param year     the year for which you want to make a calendar
     * @throws com.itextpdf.text.DocumentException
     *
     * @throws java.io.IOException
     */
    public void createPdf(String filename, Locale locale, int year) throws IOException, DocumentException {
        // step 1
        Document document = new Document(PageSize.A4.rotate());
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        PdfPTable table;
        Calendar calendar;
        PdfContentByte canvas = writer.getDirectContent();
        // Loop over the months
        for (int month = 0; month < 12; month++) {
            calendar = new GregorianCalendar(year, month, 1);
            // draw the background
//            drawImageAndText(canvas, calendar);
            // create a table with 7 columns
            table = new PdfPTable(7);
            table.setTableEvent(tableBackground);
            table.setTotalWidth(654);
            // add the name of the month
//            table.getDefaultCell().setBorderColor(BaseColor.GRAY);
//            table.getDefaultCell().setBorderWidth(1f);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setCellEvent(cellBackground);
//            table.getDefaultCell().setCellEvent(lightRoundRectangle);
            table.addCell(getDateHeaderCell(calendar, locale, writer));
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int day = 1;
//            int position = 2;
            // add empty cells
//            while (position != calendar.get(Calendar.DAY_OF_WEEK)) {
//                position = (position % 7) + 1;
//                table.addCell("");
//            }
            // add cells for each day
            while (day <= daysInMonth) {
                calendar = new GregorianCalendar(year, month, day++);
                table.addCell(getDayCell(calendar, locale));
            }
            // complete the table
            table.completeRow();
            // write the table to an absolute position
            table.writeSelectedRows(0, -1, 169, table.getTotalHeight() + 20, canvas);
            document.newPage();
        }
        // step 5
        document.close();
    }

    public void createCalendar(String filename) throws IOException, DocumentException, ParserException {
        Document document = new Document(PageSize.A4.rotate());

        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));

        document.open();

        writer.getDirectContent();

        IText.addHeader(document);

        IText.newLine(document);
        IText.newLine(document);
        IText.newLine(document);

//        createCalendarWithDateMap(document, writer);
        createCalendarFromEventMap(document);
        document.close();
    }

    private void createCalendarWithDateMap(Document document, PdfWriter writer) throws IOException, ParserException, DocumentException {
        populateDateTableMap(writer);

        java.util.List<Date> dateList = new ArrayList<Date>();

        for (String dateFormat : IText.dateMap.keySet()) {
            dateList.add(DateUtil.formatStringToDate(dateFormat));
        }

        Collections.sort(dateList);

        for (Date date : dateList) {
            PdfPTable table = (PdfPTable) IText.dateMap.get(DateUtil.formatDateToString(date, DateUtil.SHORT_TIMELESS_DATE_PATTERN));

            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(table);
            document.add(paragraph);

            IText.newLine(document);
            IText.newLine(document);
        }
    }

    private String currentDay = "";

    private void createCalendarFromEventMap(Document document) throws IOException, ParserException, DocumentException {
        populateDateEventMap();

        java.util.List<Integer> intDays = new ArrayList<Integer>(dateEventMap.keySet());

        Collections.sort(intDays);

        java.util.List<PdfPCell> cells = new ArrayList<PdfPCell>();
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        for (Integer intDay : intDays) {
            VEvent event = dateEventMap.get(intDay);
            Date startDate = event.getStartDate().getDate();
            Date endDate = event.getEndDate().getDate();

            Property title = event.getSummary();
            Property note = event.getDescription();
            Location location = event.getLocation();

            if (!isSameDay(intDay)) {
                String day = String.valueOf(intDay).substring(0, 6);

                if (currentDay.isEmpty()) {
                    currentDay = day;
                    gregorianCalendar.setTime(startDate);
                }

                if (timeDuration != null) {
                    Paragraph paragraph = new Paragraph();
                    paragraph.setAlignment(Element.ALIGN_CENTER);
                    PdfPTable table = createTableWithHeaderAndFooter(gregorianCalendar);

                    for (PdfPCell cell : cells) {
                        table.addCell(cell);
                    }
                    paragraph.add(table);
                    document.add(paragraph);

                    IText.newLine(document);
                    IText.newLine(document);

                    timeDuration = null;
                    currentDay = day;
                    cells = new ArrayList<PdfPCell>();
                    gregorianCalendar.setTime(startDate);
                }
//                handleDuration(startDate, endDate);
            } else {

            }
            handleDuration(startDate, endDate);
            cells.addAll(insertTableData(title, note, location));
        }
    }

    private boolean isSameDay(Integer intDay) {
        String day = String.valueOf(intDay).substring(0, 6);
        return currentDay.equals(day);
    }

    public void populateDateTableMap(PdfWriter writer) throws IOException, ParserException, DocumentException {
        String calendarFileName = "Group-nonAllDay.ics";
        ComponentList events = getComponentList(calendarFileName);

        for (Object component : events) {
            VEvent event = (VEvent) component;
            DtStart startDateTime = event.getStartDate();
            Date startDate = startDateTime.getDate();

            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(startDate);

            DtEnd endDate = event.getEndDate();
            Date stopDate = endDate.getDate();

//            Property title = event.getSummary();
//            Property note = event.getDescription();
//            Location location = event.getLocation();
            // if the date already exists, get it's table and add the new cells
            String dateString = DateUtil.formatDateToString(startDate, DateUtil.SHORT_TIMELESS_DATE_PATTERN);
            if (IText.dateMap.containsKey(dateString)) {
                PdfPTable table = (PdfPTable) IText.dateMap.get(dateString);
                timeDuration = timeDurationMap.get(dateString);
                handleDuration(startDate, stopDate);
//                insertTableData(table, title, note, location);

            } else {
                timeDuration = null;
                handleDuration(startDate, stopDate);

                PdfPTable table = createTableWithHeaderAndFooter(gregorianCalendar);
//                insertTableData(table, title, note, location);
                IText.dateMap.put(dateString, table);
                timeDurationMap.put(dateString, timeDuration);
            }
        }
    }

    private void populateDateEventMap() throws IOException, DocumentException, ParserException {
        String calendarFileName = "Group-nonAllDay.ics";
        ComponentList events = getComponentList(calendarFileName);

        for (Object component : events) {
            VEvent event = (VEvent) component;
            Date startDate = event.getStartDate().getDate();
            dateEventMap.put(DateUtil.formatDateAndTimeToIntegerValue(startDate), event);
        }
    }

    private ComponentList getComponentList(String calendarFileName) throws IOException, ParserException {
        FileInputStream fin = new FileInputStream(calendarFileName);
        CalendarBuilder builder = new CalendarBuilder();
        net.fortuna.ical4j.model.Calendar calendar = builder.build(fin);
        return calendar.getComponents(IText.Event);
    }

    private java.util.List<PdfPCell> insertTableData(Property title, Property note, Location location) throws IOException, DocumentException {
        java.util.List<PdfPCell> cells = new ArrayList<PdfPCell>(3);

        cells.add(getLocationCell(location));
//        table.addCell(getLocationCell(location));

        cells.add(getTaskCell(title.getValue(), note));

        cells.add(getTimeCell());

        return cells;
    }

    private PdfPCell getFooterCell() throws IOException, BadElementException {
        PdfPCell cell = getPdfPCell();
        cell.setColspan(5);
        cell.setPadding(5);
//        cell.setCellEvent(new CellBackground(0x40));
        cell.setCellEvent(new CellBackground(194, 194, 194));
//        cell.setCellEvent(new RoundRectangle(new int[]{0x00, 0x00, 0x00, 0x25}));
        Font smallGrayDroidSansFallback1 = smallGrayDroidSansFallback;
        smallGrayDroidSansFallback1.setColor(new BaseColor(248, 248, 248));
        smallGrayDroidSansFallback1.setSize(8);
        Chunk mikki = new Chunk("Mikkel T. Mathiasen", smallGrayDroidSansFallback1);
        mikki.setCharacterSpacing(1.5f);
        Paragraph paragraph = new Paragraph(mikki);
        paragraph.setAlignment(Element.ALIGN_CENTER);

//        paragraph.add(separator());
        paragraph.add(separator());

        // clock image
        Image clock = getClock();
        clock.scalePercent(70);
        paragraph.add(new Phrase(new Chunk(clock, 5, -4)));

        // total time duration
        Chunk totalDuration = new Chunk(String.format(" IAlt: %s", timeDuration.getTotalDuration()));
        paragraph.add(Chunk.createTabspace(3f));
        totalDuration.setCharacterSpacing(1.3f);
        paragraph.add(new Phrase(totalDuration));

        cell.addElement(paragraph);
        return cell;
    }

    private Phrase getLocation(Location location) throws IOException, BadElementException {
        Phrase locationP = new Phrase();
        if (location != null) {
            locationP.add(new Chunk(location.getValue(), smallGrayBoldDroidSansFallback));
        }
        return locationP;
    }

    private Image getLocationImage() throws BadElementException, IOException {
        Image image = Image.getInstance("location.png");
        image.setAlignment(Image.LEFT);
//        image.scaleAbsolute(15f, 12f);
        image.scalePercent(18);
        image.setAbsolutePosition(0, 0);
        return image;
    }

    private TimeDuration timeDuration;

    private PdfPCell getTimeCell() {
        PdfPCell cell = getPdfPCell();
        String timeString = String.format("%s - %s", timeDuration.getStartTime(), timeDuration.getEndTime());
        Chunk time = new Chunk(timeString, smallGrayBoldDroidSansFallback);
        Paragraph timeParagraph = new Paragraph(time);
        timeParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        cell.addElement(timeParagraph);

        Chunk totalDuration = new Chunk(String.format("Total:   %s", timeDuration.getDuration()), smallGrayBoldDroidSansFallback);
        Paragraph durationParagraph = new Paragraph(totalDuration);
        durationParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        cell.addElement(durationParagraph);
        return cell;
    }

    private void handleDuration(Date startDate, Date stopDate) {
        long milliSec = stopDate.getTime() - startDate.getTime();

        if (timeDuration == null) {
            timeDuration = new TimeDuration(milliSec);
        } else {
            timeDuration.addSubDuration(milliSec);
        }
        timeDuration.addStartTime(IText.getTimeString(startDate));
        timeDuration.addEndTime(IText.getTimeString(stopDate));
    }

    private PdfPCell getTaskCell(String titleValue, Property note) {
        PdfPCell cell = getPdfPCell();
        cell.setColspan(3);

        Phrase text = TextFormattingUtil.parseText(titleValue);
//        text.setLeading(1.5f);
        text.setLeading(text.getFont().getCalculatedSize() + 1);
        Paragraph taskParagraph = new Paragraph(text);
        taskParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        if (note != null) {
            taskParagraph.add(new Chunk(note.getValue()));
        }
        cell.addElement(taskParagraph);
        return cell;
    }

    private PdfPCell getLocationCell(Location location) throws IOException, DocumentException {
        PdfPCell cell = getPdfPCell();
        Paragraph locationParagraph = new Paragraph(new Chunk(getLocationImage(), 0, 0));
        locationParagraph.setAlignment(Element.ALIGN_LEFT);

        if (location != null) {
            locationParagraph.add(Chunk.createTabspace(3f));
            locationParagraph.add(getLocation(location));
        }
        cell.addElement(locationParagraph);
        return cell;
    }

    private PdfTemplate getPdfTemplate(Location location, PdfContentByte directContent) throws DocumentException, IOException {
        PdfTemplate template = directContent.createTemplate(200, 100);
        template.addImage(getLocationImage());

        template.beginText();
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        template.setFontAndSize(bf, 12);
        template.setTextMatrix(100, 100);
        template.showText(location.getValue());
        template.endText();
        return template;
    }

    private PdfPCell getPdfPCell() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setCellEvent(cellBackground);

        //padding
//        cell.setPaddingBottom(1f);
//        cell.setPaddingTop(1f);
//        cell.setPaddingLeft(3f);
//        cell.setPaddingRight(3f);
        cell.setPadding(3f);

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private PdfPTable createTableWithHeaderAndFooter(Calendar calendar) throws IOException, DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setTableEvent(tableBackground);
        table.getDefaultCell().setCellEvent(cellBackground);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.setKeepTogether(true);
//        table = getHeaderCell(table, calendar, new Locale("da"), writer);

        // add width & header width
        int[] headerWidths = {12, 1, 70, 8, 9};
        table.setWidths(headerWidths);
        table.setWidthPercentage(100);

        // set default alignments
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        //padding
        table.getDefaultCell().setPaddingBottom(1);
        table.getDefaultCell().setPaddingTop(1);
        table.getDefaultCell().setPaddingLeft(2);
        table.getDefaultCell().setPaddingRight(2);

        // space
        table.setSpacingBefore(0);
        table.setSpacingAfter(0);

        // add cells
//        table.addCell(getHeaderCell(calendar, new Locale("da"), writer));
        table.addCell(getHeaderCell(calendar, new Locale("da")));
        table.addCell(getFooterCell());
//        table.addCell(getDateHeaderCell(table, calendar, new Locale("da"), writer));
        table.setHeaderRows(2);
        table.setFooterRows(1);


        return table;
    }

    private PdfPCell getHeaderCell(Calendar calendar, Locale locale, PdfWriter writer) throws IOException, BadElementException {
        PdfPCell cell = getPdfPCell();
        cell.setColspan(5);
        cell.setCellEvent(new CellBackground(0x40));
//        cell.setUseDescender(true);
        cell.setPaddingLeft(34);
        cell.setPaddingBottom(6);
        cell.setVerticalAlignment(Element.ALIGN_RIGHT);


//        cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
//        cell.setBorderColor(BaseColor.ORANGE);
//        cell.setGrayFill(0.9f);

        StringBuilder builder = new StringBuilder();
        builder.append(String.format(locale, DateUtil.formatCalendarMonth(calendar))).append("\n");
        builder.append("  ").append(String.format(locale, "%1$te", calendar));

        // month
//        Chunk month = new Chunk(String.format(locale, DateUtil.formatCalendarMonth(calendar)), smallDarkNormal);
        Chunk monthAndDate = new Chunk(builder.toString(), size11GrayBoldDroidSansFallback);
        monthAndDate.setSkew(10, 10);
//        monthAndDate.setTextRise(-2);

//        cb.setLineDash(3, 3, 0);
//        cb.setRGBColorStrokeF(0f, 255f, 0f);
//        cb.circle(150f, 500f, 100f);
//        cb.stroke();


//        monthAndDate.setBackground(new BaseColor(84, 84, 84), 5, 5, 5, 5);
//        Paragraph monthP = new Paragraph(month);
//        monthP.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(monthP);

//        Chunk headerImage = new Chunk(getTransparentHeaderImage(), -16f, -36f);
//        Paragraph headerP = new Paragraph(headerImage);
//        headerP.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(headerP);

//        Chunk week = new Chunk(String.format(locale, DateUtil.formatCalendarWeek(calendar)), smallDarkNormal);
//        week.setTextRise(8);

//        Chunk date = new Chunk(String.format(locale, "%1$te", calendar), smallDarkNormal);
//        day.setTextRise(-4);
//        Paragraph p = new Paragraph(day);
        Paragraph monthAndDateP = new Paragraph(monthAndDate);

        monthAndDateP.add(separator());

//        Chunk newLine = new Chunk("\n", IText.tinyFont);
//        newLine.setLineHeight(2);
//        p.add(newLine);
//        p.add(date);
//        p.add(week);
//        p.add(new Chunk("\n", IText.tinyFont));
        monthAndDateP.setAlignment(Element.ALIGN_LEFT);

        monthAndDateP.add(separator());
        monthAndDateP.add(separator());
        cell.setVerticalAlignment(Element.ALIGN_LEFT);
//        cell.addElement(monthAndDateP);
        // and the number of the day
//        p.add(new Chunk(StringUtils.capitalize(String.format(locale, "%1$ta", calendar)), hugeBoldDarkGray));

        Chunk day = new Chunk(String.format(locale, DateUtil.formatCalendarDay(calendar)), size22BoldDarkGrayDroidSansFallback);
        day.setCharacterSpacing(3f);
//        Paragraph dayP = new Paragraph(day);
        monthAndDateP.add(day);
        monthAndDateP.add(separator());
        monthAndDateP.add(separator());

        Chunk week = new Chunk(String.format(locale, DateUtil.formatCalendarWeek(calendar)), smallGrayBoldDroidSansFallback);
        week.setTextRise(14);

//        monthAndDateP.add(new Chunk("                    "));

        monthAndDateP.add(week);
//        monthAndDateP.add(separator());
//        dayP.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(dayP);


//        dayP.add(separator());
        Chunk ClockImage = new Chunk(getClock(), 30, -6);
//        ClockImage.setTextRise(20);
        monthAndDateP.add(new Phrase(ClockImage));
        monthAndDateP.add(separator());
//        Paragraph imageP = new Paragraph(ClockImage);
        cell.addElement(monthAndDateP);
        monthAndDateP.add(separator());
//        Chunk day = new Chunk(String.format(locale, DateUtil.formatCalendarDay(calendar)), bigBoldGrayer);
//        day.setTextRise(-4);
//        Paragraph p = new Paragraph(day);


//        p.add(new Chunk(getClock(), 55, -7));
//        imageP.add(separator());
        return cell;
    }

    private PdfPCell getHeaderCell(Calendar calendar, Locale locale) throws IOException, DocumentException {
        PdfPCell cell = getPdfPCell();
        cell.setColspan(5);
        cell.setPaddingTop(3);

        cell.setCellEvent(new CellBackground(red, green, blue));

        // nested header table
        PdfPTable headerTable = createHeaderTable(red, green, blue);
        headerTable.getDefaultCell().setCellEvent(new CellBackground(red, green, blue));

        // add cells

        // month
        headerTable.addCell(getMonthCell(calendar, locale));

        // date
        PdfPCell dateCell = getActualDatePCell(calendar, locale);
        headerTable.addCell(dateCell);

        // day
        PdfPCell dayCell = getCurrentDateCell(calendar, locale);
        headerTable.addCell(dayCell);

        // week
        PdfPCell weekCell = getWeekCell();
        headerTable.addCell(weekCell);

        // week number
        PdfPCell weekNoCell = getWeekNumberCell(calendar, locale);
        headerTable.addCell(weekNoCell);

        // clock image
        PdfPCell clockImageCell = getClockImageCell();
        headerTable.addCell(clockImageCell);

        // start- and end time of the day
        PdfPCell totalTimeframeOfTheDayCell = getTotalTimeFrameOfTheDayCell();
        headerTable.addCell(totalTimeframeOfTheDayCell);

        // month & date
//        Chunk month = new Chunk(String.format(locale, DateUtil.formatCalendarMonth(calendar)), smallDarkNormal);
//        month.setTextRise(4);
//        month.setBackground(BaseColor.YELLOW);
//        Paragraph monthP = new Paragraph(month);
//        monthP.setAlignment(Element.ALIGN_LEFT);
//        monthP.add(separator());
//        monthP.setIndentationLeft(0);
//        monthP.setIndentationRight(50);
//        pdfPCell.addElement(monthP);
//        pdfPCell.setFixedHeight(36);

//        Chunk headerImage = new Chunk(getTransparentHeaderImage(), -78f, -20f);
//        Paragraph headerP = new Paragraph(headerImage);
//        headerP.setAlignment(Element.ALIGN_CENTER);
//        pdfPCell.addElement(headerP);

//        monthP.setAlignment(Element.ALIGN_BASELINE);
//        monthP.add(headerImage);
//        cell.addElement(monthP);

//        PdfPCell middleCell = getPdfPCell();
//        middleCell.setColspan(4);
//        middleCell.setCellEvent(new CellBackground(0x54));
//        Paragraph middleP = new Paragraph(new Chunk("MONDAY"));
//        cell.addElement(middleP);
//
//        Chunk imageChunk = new Chunk(getClock(), 55, -7);
//        Paragraph imageP = new Paragraph(imageChunk);
//        cell.addElement(imageP);

//        headerTable.addCell(cell);

        cell.addElement(headerTable);

        return cell;
    }

    private PdfPCell getTotalTimeFrameOfTheDayCell() {
        PdfPCell totalTimeFrameOfTheDayCell = new PdfPCell();
        totalTimeFrameOfTheDayCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        totalTimeFrameOfTheDayCell.setBorder(Rectangle.NO_BORDER);

        Paragraph startTimeParagraph = new Paragraph(new Chunk(timeDuration.getStartTimeOfTheDay(), smallGrayBoldDroidSansFallback));
        startTimeParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        totalTimeFrameOfTheDayCell.addElement(startTimeParagraph);

        Paragraph endTimeParagraph = new Paragraph(new Chunk(timeDuration.getEndOfDayTime(), smallGrayBoldDroidSansFallback));
        endTimeParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        totalTimeFrameOfTheDayCell.addElement(endTimeParagraph);
        totalTimeFrameOfTheDayCell.setBorder(Rectangle.NO_BORDER);
        return totalTimeFrameOfTheDayCell;
    }

    private PdfPCell getClockImageCell() throws BadElementException, IOException {
        Paragraph clockParagraph = new Paragraph(new Chunk(getClock(), 0, -30));
        clockParagraph.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell clockImageCell = new PdfPCell(clockParagraph);
        clockImageCell.setPaddingTop(3);
        clockImageCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        clockImageCell.setBorder(Rectangle.NO_BORDER);
        clockParagraph.add(Chunk.createTabspace(5f));
        return clockImageCell;
    }

    private PdfPCell getWeekNumberCell(Calendar calendar, Locale locale) {
        Paragraph weekNoParagraph = new Paragraph(new Chunk(String.format(locale, DateUtil.formatCalendarWeekNo(calendar)), size16BoldDarkGrayCopperplate));
//        weekNoParagraph.setAlignment(Element.ALIGN_LEFT);
        PdfPCell weekNoCell = new PdfPCell(weekNoParagraph);
        weekNoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        weekNoCell.setVerticalAlignment(Element.ALIGN_TOP);
        weekNoCell.setBorder(Rectangle.NO_BORDER);
        return weekNoCell;
    }

    private PdfPCell getWeekCell() {
        Font smallGrayBoldDroidSansFallback1 = smallGrayBoldDroidSansFallback;
        smallGrayBoldDroidSansFallback1.setColor(gray100);
        Chunk week = new Chunk("Uge ", smallGrayBoldDroidSansFallback1);
        week.setTextRise(1);
        Paragraph weekParagraph = new Paragraph(week);
        weekParagraph.setAlignment(Element.ALIGN_LEFT);
        PdfPCell weekCell = new PdfPCell(weekParagraph);
        weekCell.setPaddingTop(6);
        weekCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        weekCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        weekCell.setRotation(90);
        weekCell.setBorder(Rectangle.NO_BORDER);
        return weekCell;
    }

    private PdfPCell getCurrentDateCell(Calendar calendar, Locale locale) {
        Font dayFont = FontFactory.getFont("comic sans ms bold", 26, gray80);
        Chunk day = new Chunk(String.format(locale, DateUtil.formatCalendarDay(calendar)), dayFont);
        day.setCharacterSpacing(3f);
        PdfPCell dayCell = new PdfPCell(new Phrase(day));
        dayCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        dayCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        dayCell.setBorder(Rectangle.NO_BORDER);
        return dayCell;
    }

    private PdfPCell getActualDatePCell(Calendar calendar, Locale locale) {
        String dateString = DateUtil.formatCalendarToString(calendar, DateUtil.DATE_PATTERN);
        Font size24BoldDarkGrayCopperplate1 = size24BoldDarkGrayCopperplate;
        size24BoldDarkGrayCopperplate1.setSize(26);
        PdfPCell dateCell = new PdfPCell(new Phrase(new Chunk(String.format(locale, dateString), size24BoldDarkGrayCopperplate1)));
        dateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        dateCell.setBorder(Rectangle.NO_BORDER);
        return dateCell;
    }

    private PdfPCell getMonthCell(Calendar calendar, Locale locale) {
        Paragraph monthParagraph = new Paragraph(new Chunk(String.format(locale, DateUtil.formatCalendarMonth(calendar)), size11GrayBoldDroidSansFallback));
        monthParagraph.setAlignment(Element.ALIGN_LEFT);
        PdfPCell monthCell = new PdfPCell(monthParagraph);
        monthCell.setPaddingTop(9);
        monthCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        monthCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        monthCell.setRotation(90);
        monthCell.setCellEvent(new CellBackground(red, green, blue));
        monthCell.setBorder(Rectangle.NO_BORDER);
        return monthCell;
    }

    private PdfPTable createHeaderTable(int red, int green, int blue) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(7);
        headerTable.setTableEvent(new TableBackground(red, green, blue));
        headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        headerTable.setKeepTogether(true);

        // add width & header width
        int[] headerWidths = {5, 5, 67, 2, 7, 7, 7};
        headerTable.setWidths(headerWidths);
        headerTable.setWidthPercentage(100);

        // set default alignments
        headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        headerTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        //padding
        headerTable.getDefaultCell().setPadding(1);

        // space
        headerTable.setSpacingBefore(0);
        headerTable.setSpacingAfter(0);

        return headerTable;
    }

    private Chunk separator() {
        return new Chunk(new VerticalPositionMark());
    }


    /**
     * Creates a PdfPCell with the name of the month
     *
     * @param calendar a date
     * @param locale   a locale
     * @param writer
     * @return a PdfPCell with rowspan 7, containing the name of the month
     */
    public PdfPCell getDateHeaderCell(PdfPTable table, Calendar calendar, Locale locale, PdfWriter writer) throws DocumentException, IOException {
        PdfPCell cell = getPdfPCell();
        cell.setColspan(7);
        cell.setCellEvent(new CellBackground(0x48));
//        cell.setUseDescender(true);
//        cell.setFixedHeight(50);
//        cell.setUseAscender(true);
//        cell.setPadding(6);

        Paragraph monthP = new Paragraph();

//        Chunk headerImage = new Chunk(getTransparentHeaderImage(cell.getWidth(), cell.getHeight()), -15, -34);
//        Phrase phrase = new Phrase(headerImage);

//        monthP.add(phrase);

        // month & date
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(locale, DateUtil.formatCalendarMonth(calendar))).append("\n");
        builder.append("\t").append("  ").append(String.format(locale, "%1$te", calendar));
        // month
//        Chunk month = new Chunk(String.format(locale, DateUtil.formatCalendarMonth(calendar)), smallDarkNormal);
        Chunk monthAndDate = new Chunk(builder.toString(), smallBaldDark);
        monthP.setAlignment(Element.ALIGN_LEFT);
        monthAndDate.setTextRise(-4);
//        PdfContentByte cb = new PdfContentByte() canvas[PdfPTable.BACKGROUNDCANVAS];
//        PdfContentByte cb = writer.getDirectContent();
//        Rectangle rect = new Rectangle(cell.getLeft(), cell.getTop());
//        cb.roundRectangle(
//                rect.getLeft() + 1f, // x
//                rect.getBottom() - 2f, // y
//                rect.getWidth() - 4, // w
//                rect.getHeight() - 3, 4); // h
//        cb.setCMYKColorFill(0x00, 0x00, 0x00, color);
//        cb.setColorFill(new BaseColor(56, 56, 56));
//            cb.setShadingFill(new PdfShadingPattern(PdfShading.simpleAxial()));
//        cb.fill();
        monthP.add(new Phrase(monthAndDate));

        PdfContentByte over = writer.getDirectContent();
//        over.saveState();
//        float sinus = (float)Math.sin(Math.PI / 60);
//        float cosinus = (float)Math.cos(Math.PI / 60);
//        BaseFont bf = BaseFont.createFont();
//        over.beginText();
//        over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE);
//        over.setLineWidth(1.5f);
//        over.setRGBColorStroke(0xFF, 0x00, 0x00);
//        over.setRGBColorFill(0xFF, 0xFF, 0xFF);
//        over.setFontAndSize(bf, 10);
//        over.setTextMatrix(cosinus, sinus, -sinus, cosinus, 50, 324);
//        over.showText(builder.toString());
//        over.endText();
//        over.restoreState();
//        over.fill();

        ColumnText ct = new ColumnText(over);
        Phrase myText = new Phrase(new Chunk(builder.toString(), smallBaldDark));
//        ct.setSimpleColumn(myText, 20, 5, 700, 200, 19, Element.ALIGN_LEFT);
        PdfDocument pdfDocument = over.getPdfDocument();
        ct.setSimpleColumn(myText, pdfDocument.getPageSize().getLeft(), pdfDocument.getPageSize().getTop(), pdfDocument.getPageSize().getWidth(), pdfDocument.getPageSize().getHeight(), 19, Element.ALIGN_LEFT);
        ct.go();


//        Paragraph headerP = new Paragraph(headerImage);
//        headerP.setAlignment(Element.ALIGN_BOTTOM);

//        cell.addElement(headerP);

        Chunk week = new Chunk(String.format(locale, DateUtil.formatCalendarWeek(calendar)), smallBold);
        week.setTextRise(2);

        monthP.add(new Chunk(new VerticalPositionMark()));

        monthP.add(week);
        monthP.add(separator());

        Chunk day = new Chunk(String.format(locale, DateUtil.formatCalendarDay(calendar)), bigBoldGrayer);
        day.setTextRise(-4);
        monthP.add(day);

        monthP.add(separator());


        monthP.add(new Chunk(getClock(), 55, -7));
        monthP.add(separator());

        cell.addElement(monthP);
        return cell;
    }

    public PdfPCell getLeftHeaderCell(Calendar calendar, Locale locale) throws BadElementException, IOException {
        PdfPCell cell = getPdfPCell();
        cell.setCellEvent(new CellBackground(0x48));
//        cell.setUseDescender(true);
        cell.setPaddingLeft(12);
        cell.setUseAscender(true);

        // month
        Chunk month = new Chunk(String.format(locale, DateUtil.formatCalendarMonth(calendar)), smallDarkNormal);
        month.setTextRise(14);
//        Paragraph monthP = new Paragraph(month);
//        monthP.setAlignment(Element.ALIGN_CENTER);
//        cell.addElement(monthP);

        Chunk headerImage = new Chunk(getTransparentHeaderImage(cell.getWidth(), cell.getHeight()), -16f, -36f);
        Paragraph headerP = new Paragraph(headerImage);
        headerP.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(headerP);

//        Chunk week = new Chunk(String.format(locale, DateUtil.formatCalendarWeek(calendar)), smallDarkNormal);
//        week.setTextRise(8);

        Chunk date = new Chunk(String.format(locale, "%1$te", calendar), smallDarkNormal);
//        day.setTextRise(-4);
//        Paragraph p = new Paragraph(day);
        Paragraph p = new Paragraph();
        p.add(month);
        Chunk newLine = new Chunk("\n", IText.tinyFont);
        newLine.setLineHeight(2);
        p.add(newLine);
        p.add(date);
//        p.add(week);
//        p.add(new Chunk("\n", IText.tinyFont));
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        return cell;
    }

    public PdfPCell getMiddleHeaderCell(Calendar calendar, Locale locale) throws BadElementException, IOException {
        PdfPCell cell = getPdfPCell();
        cell.setColspan(6);
        cell.setCellEvent(new CellBackground(0x48));
//        cell.setUseDescender(true);

//        cell.setPadding(6);

//        Chunk week = new Chunk(String.format(locale, DateUtil.formatCalendarWeek(calendar)), smallBold);
//        week.setTextRise(5);

//        Chunk headerImage = new Chunk(getTransparentHeaderImage(), -8f, -35f);
//        Paragraph headerP = new Paragraph(headerImage);
//        headerP.setAlignment(Element.ALIGN_BOTTOM);

//        cell.addElement(headerP);
//        p.setAlignment(Element.ALIGN_CENTER);
//        p.add(week);
//        p.add(new Chunk(new VerticalPositionMark()));

        Chunk date = new Chunk(String.format(locale, "%1$te", calendar), bigBoldGrayer);
        date.setTextRise(8);
        // a paragraph with the day
        Paragraph p = new Paragraph(date);
        // a separator
        p.add(separator());
        // and the number of the day
//        p.add(new Chunk(StringUtils.capitalize(String.format(locale, "%1$ta", calendar)), hugeBoldDarkGray));
        p.add(new Chunk(String.format(locale, DateUtil.formatCalendarDay(calendar)), bigBoldGrayer));
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);


        p.add(separator());
        Chunk imageChunk = new Chunk(getClock(), 55, -7);
        Paragraph imageP = new Paragraph(imageChunk);
        cell.addElement(imageP);
//        Chunk day = new Chunk(String.format(locale, DateUtil.formatCalendarDay(calendar)), bigBoldGrayer);
//        day.setTextRise(-4);
//        Paragraph p = new Paragraph(day);


//        p.add(new Chunk(getClock(), 55, -7));
        imageP.add(separator());

//        cell.addElement(p);
        return cell;
    }

    private Image getTransparentHeaderImage(float width, float height) throws BadElementException, IOException {
        Image image = Image.getInstance("transparentDateImage.png");
        image.scaleAbsolute(146, 56);
//        image.setAbsolutePosition(20, 40);
//        image.scalePercent(146);
        image.setAlignment(Image.UNDERLYING);
//        image.scaleAbsolute(196f, 114f);
//        image.scaleAbsolute(442f, 342f);
//        image.scaleToFit(112, 22);
//        image.scaleToFit(width, height);
        return image;
    }

    private Image getClock() throws BadElementException, IOException {
        Image image = Image.getInstance("clock.tiff");
        image.setAlignment(Image.MIDDLE);
        image.scaleAbsolute(26f, 26f);
//        image.scalePercent(60);
        return image;
    }

    private Image getHeaderImage() throws BadElementException, IOException {
        Image image = Image.getInstance("dateImage.png");
        image.scaleAbsolute(94f, 56f);
        image.setScaleToFitLineWhenOverflow(true);
        return image;
    }

    public PdfPCell getClockCell() throws BadElementException, IOException {
        PdfPCell cell = getPdfPCell();
        Chunk imageChunk = new Chunk(getClock(), 55, -7);
        Paragraph imageP = new Paragraph(imageChunk);
        cell.addElement(imageP);
        return cell;
    }

    public PdfPCell getEmptyCell() throws BadElementException, IOException {
        return getPdfPCell();
    }

//        EEE, WW, MMM dd

//        SimpleDateFormat sdf;
//        Calendar cal;
//        Date date;
//        int week;
//        String sample = "12/25/1979";
//        sdf = new SimpleDateFormat("MM/dd/yyyy");
//        date = sdf.parse(sample);
//        cal = Calendar.getInstance();
//        cal.setTime(date);
//        week = cal.get(Calendar.WEEK_IN_MONTH);

    /**
     * Creates a PdfPCell for a specific day
     *
     * @param calendar a date
     * @param locale   a locale
     * @return a PdfPCell
     */
    public PdfPCell getDayCell(Calendar calendar, Locale locale) {
        PdfPCell cell = new PdfPCell();
        // set the event to draw the background
        cell.setCellEvent(cellBackground);
        // set the event to draw a special border
//        if (isSpecialDay(calendar)) {
//            cell.setCellEvent(darkRoundRectangle);
//        }
        cell.setPadding(3);
        cell.setBorder(PdfPCell.NO_BORDER);
        // set the content in the language of the locale
        Chunk chunk = new Chunk(String.format(locale, "%1$ta", calendar), small);
        chunk.setTextRise(8);
        // a paragraph with the day
        Paragraph p = new Paragraph(chunk);
        // a separator
        p.add(separator());
        // and the number of the day
        p.add(new Chunk(String.format(locale, "%1$te", calendar), normal));
        cell.addElement(p);
        return cell;
    }

    /**
     * Returns true if the date was found in a list with special days (holidays).
     *
     * @param calendar a date
     * @return true for holidays
     */
    public boolean isSpecialDay(Calendar calendar) {
        return specialDays.containsKey(String.format("%1$tm%1$td", calendar));
    }

}
