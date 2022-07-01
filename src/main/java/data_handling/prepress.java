package data_handling;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class prepress {

    public prepress(){
    }


    public static PDFormXObject importAsXObject(PDDocument target, PDPage page) throws IOException {
        final InputStream src = page.getContents();
        if (src != null) {
            final PDFormXObject xobject = new PDFormXObject(target);

            OutputStream os = xobject.getStream().createOutputStream();
            InputStream is = src;
            try {
                IOUtils.copy(is, os);
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }

            xobject.setResources(page.getResources());
            xobject.setBBox(page.getCropBox());

            return xobject;
        }
        return null;
    }


}
