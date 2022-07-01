import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import data_handling.prepress;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;


public class Main {
    public static void main(String[] args) throws IOException {

        try (PDDocument doc = new PDDocument()) {

            //laden der Druckdatei
            File file = new File("src/main/resources/test_data/color.pdf");
            PDDocument printfile = PDDocument.load(file);


            //Definiton des SRA3 Format als PDRectangle zum erstellen von SRA3 PDFs
            //und erstellen einer PDPage mit den SRA3 Maßen
            PDRectangle SRA3 = new PDRectangle();
            SRA3.setLowerLeftX(0);
            SRA3.setLowerLeftY(0);
            SRA3.setUpperRightX((float) 907.0867);
            SRA3.setUpperRightY((float) 1275.5906);


            //initialisieren einer prepress instanz um inhalte aus der quell pdf zu extrahieren
            prepress prepress = new prepress();

            PDPage myPage;

            //PDPage Element mit PDResources als Inhalt erstellt wird
            //zum Zwischenspeichern der Seiten der Druckdatei verwendet
            PDPage page;

            boolean uneven = false;

            if(printfile.getPages().getCount()%2==1){
                uneven = true;
            }

            for(int i=0; i < printfile.getPages().getCount()-1; i=i+2){
                myPage = new PDPage(SRA3);
                page = new PDPage();
                page.setResources(new PDResources());

                PDFormXObject xobject1 = prepress.importAsXObject(doc, printfile.getPages().get(i));
                PDFormXObject xobject2 = prepress.importAsXObject(doc, printfile.getPages().get(i+1));
                page.getResources().add(xobject1);
                page.getResources().add(xobject2);


                try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {

                    AffineTransform transform = new AffineTransform();
                    transform.quadrantRotate(45);

                    transform.translate(28.5302,-842-32.54335);
                    cont.drawXObject(xobject1, transform);

                    transform.translate(595 + 28.5302, 0);
                    cont.drawXObject(xobject2, transform);


                }

                doc.addPage(myPage);

            }

            if(uneven){
                myPage = new PDPage(SRA3);
                page = new PDPage();
                page.setResources(new PDResources());

                PDFormXObject xobject1 = prepress.importAsXObject(doc, printfile.getPages().get(printfile.getNumberOfPages()-1));
                page.getResources().add(xobject1);

                try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {

                    AffineTransform transform = new AffineTransform();
                    transform.quadrantRotate(45);

                    transform.translate(28.5302,-842-32.54335);
                    cont.drawXObject(xobject1, transform);

                }

                doc.addPage(myPage);
            }












            doc.save("src/main/resources/wwii.pdf");
        }
    }

}