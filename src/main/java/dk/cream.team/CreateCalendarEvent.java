package dk.cream.team;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAcroForm;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.TextField;
import net.fortuna.ical4j.data.ParserException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Date: 04/04/13
 */
public class CreateCalendarEvent {

    /**
     * The resulting PDF.
     */
    public static final String RESULT = "cal.pdf";


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

    public void createCalendar(String result) {
        //To change body of created methods use File | Settings | File Templates.
    }

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
        startTime.setBorderColor(BaseColor.GRAY);
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
        TextField timeField = new TextField(writer, new Rectangle(llx * 2, lly + 30, urx, ury), fieldName);
        timeField.setBorderColor(new GrayColor(0.2f));
        timeField.setText("10:30");
        PdfFormField datefield = timeField.getTextField();
        datefield.setAdditionalActions(
                PdfName.V, PdfAction.javaScript(
                "AFTime_Format(0);", writer));
        writer.addAnnotation(datefield);
        return timeField;
    }

    private static String javascript = "";

    /**
     * Main method creating the PDF.
     *
     * @param args no arguments needed
     * @throws java.io.IOException
     * @throws com.itextpdf.text.DocumentException
     *
     */
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
    public CreateCalendarEvent() throws DocumentException, IOException {
        super();
        tableBackground = new TableBackground();
        cellBackground = new CellBackground();
        darkRoundRectangle = new RoundRectangle(new int[]{0x00, 0x00, 0x00, 0x48});
        lightRoundRectangle = new RoundRectangle(new int[]{0x00, 0x00, 0x00, 0x25});
    }


}
