package kp9b3c52.com.quickkanoon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public ArrayList<ArrayList<String>> read(){
        ArrayList<ArrayList<String>> arl = new ArrayList<ArrayList<String>>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                ArrayList resultList = new ArrayList();
                String[] row = csvLine.split(",");
                for(int i = 0 ; i<row.length;i++)
                resultList.add(row[i]);
                arl.add(resultList);
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
        return arl;
    }
}