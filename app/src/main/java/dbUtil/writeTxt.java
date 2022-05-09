package dbUtil;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class writeTxt {

    private static ArrayList<String> wordsTxt = new ArrayList();
    private static String path = "D:/words.txt";
    private static  File filename = new File(path);

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void writeTxtFile(String newWord) throws IOException {

        //get current time stamp
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime datetime = ZonedDateTime.now(zid);


        //if there doesn't exist such file, then create a new one
        if (!filename.exists()) {
            filename.createNewFile();
            System.err.println(filename + "CREATED!");
        }

        String filein = "Input Word: " + newWord + "\r\r\r\r" + datetime + "\n";
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(filename, "rw");
            raf.writeBytes(filein);
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        wordsTxt.add(filein);
        System.out.println(wordsTxt);
    }



}
