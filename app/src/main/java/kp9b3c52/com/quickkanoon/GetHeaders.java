package kp9b3c52.com.quickkanoon;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ADS on 9/10/2017.
 */

public class GetHeaders {
    InputStream inputStream;
    public GetHeaders(InputStream inputStream){
        this.inputStream = inputStream;
    }
    public ArrayList<String> read(){
        ArrayList<String> headers = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                csvLine = csvLine.trim();
                if(csvLine.startsWith("/o/")) {
                    headers.add(csvLine.substring(3));
                    Log.i("FUCK", csvLine);
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return headers;
    }

    public ArrayList<String> getText(int n)
    {
        int ctr = 0 ;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayList<String> res = new ArrayList<>();
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                csvLine = csvLine.trim();
                if (csvLine.startsWith("/n/"))
                    res.add(csvLine);
                else if (csvLine.startsWith("/s/")) {
                    if (ctr == n) {
                        res.add(csvLine);
                    }
                        ctr++;
                        if (ctr > n+1)
                            break;

                } else {
                    if (ctr == n+1)
                        res.add(csvLine);
                    else if (ctr > n+1)
                        break;
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return res;
    }

}
