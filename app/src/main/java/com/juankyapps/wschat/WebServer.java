package com.juankyapps.wschat;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import fi.iki.elonen.NanoHTTPD;


public class WebServer extends NanoHTTPD {

    private AssetManager assetManager;

    public WebServer(int port, AssetManager assetManager) {
        super(port);
        this.assetManager = assetManager;
    }

    public WebServer(String hostname, int port) {
        super(hostname, port);
    }

    /**
     * Selects the right file path to give as response.
     * This allows angular routing.
     * @param uri Requested URL
     * @return the requested file path or 'index.html'.
     */
    private  String getFilePath(String uri) {
        try {
            boolean fileExists = Arrays.asList(this.assetManager.list("dist")).contains(uri.substring(1));
            if (fileExists) {
                return uri.substring(1);
            }
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return "index.html";
    }

    private static String getMimeType(String file) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(file);
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        if (type == null) {
            switch (ext) {
                case "js":
                    return "application/javascript";
                case "woff2":
                    return "font/woff2";
                case "woff":
                    return "font/woff";
                case "ttf":
                    return "font/ttf";
            }
        }
        return type;
    }

    private static boolean binaryResponse(String mimeType) {
        if (mimeType == null) {
            mimeType = "";
        }
        switch (mimeType) {
            case "text/html":
            case "application/javascript":
            case "":
                return false;
        }
        return true;
    }

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String filepath = getFilePath(session.getUri());
        String mimeType = getMimeType(filepath);
        String content;
        byte[] buffer;
        InputStream is;
        try {
            is = this.assetManager.open("dist/" + filepath);
            if (binaryResponse(mimeType)) {
                return newFixedLengthResponse(Response.Status.OK, mimeType, is, -1);
            }
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer);
            content = content.replace("old string", "new string");
        }catch(IOException e) {
            System.out.println("IOException (at serve): " + e.getMessage());
            mimeType = "text/html";
            content = "<html><body><h1>IOException</h1>\n<p>" + e.getMessage() + "</p>\n<p>Serving " + session.getUri() + " !</p></body></html>\n";
        }
        return newFixedLengthResponse(Response.Status.OK, mimeType, content);
    }
}