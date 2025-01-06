package com.tr.redenvelope.util;

import java.io.*;

public class IOUtils {

    public static String convertInputStream(InputStream in, String encoding) {
        if (in == null)
            throw new NullPointerException();
        Reader r = null;
        try {
            r = new InputStreamReader(new BufferedInputStream(in), encoding);
            r = new BufferedReader(r, 1024);
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = r.read()) != -1)
                sb.append((char) c);
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (r != null)
                    r.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static byte[] convertByteArray(InputStream in) {
        BufferedInputStream r = new BufferedInputStream(in);
        ByteArrayOutputStream w = new ByteArrayOutputStream();
        int c;
        try {
            while ((c = r.read()) != -1) {
                w.write(c);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return w.toByteArray();
    }

}
