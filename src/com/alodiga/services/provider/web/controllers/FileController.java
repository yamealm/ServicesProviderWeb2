package com.alodiga.services.provider.web.controllers;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.GenericSignatureFormatError;

import org.zkoss.io.Files;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class FileController extends GenericForwardComposer {

//    public void onUpload$btnUpload(org.zkoss.zk.ui.event.UploadEvent event) throws Throwable, IOException, IOException, IOException {
//        org.zkoss.util.media.Media media = event.getMedia();
//
//        if (media != null) {
//            File csv = new File("/tmp/" + media.getName());
//            Files.copy(csv, (InputStream) new ByteArrayInputStream(media.getStringData().getBytes()));
//        }
//    }
    
    public void onUpload$btnUpload(org.zkoss.zk.ui.event.UploadEvent event) throws IOException {
        org.zkoss.util.media.Media media = event.getMedia();

        if (media != null) {
            File csv = new File("/tmp/" + media.getName());
            if (media.isBinary()){
                Files.copy(csv, media.getStreamData());
            } else {
                BufferedWriter writer = new BufferedWriter(new FileWriter(csv));
                Files.copy(writer, media.getReaderData());
            }
        }
    }

}
