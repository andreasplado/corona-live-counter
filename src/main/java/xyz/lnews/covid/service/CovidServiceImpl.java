package xyz.lnews.covid.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import xyz.lnews.covid.model.LocStats;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class CovidServiceImpl {

    private static String CSV_READER_PATH = "corona-virus-dataset.csv";
    //private static String CSV_READER_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static String CSV_READER_URL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vQuDj0R6K85sdtI8I-Tc7RCx8CnIxKUQue0TCUdrFOKDw9G3JRtGhl64laDd3apApEvIJTdPFJ9fEUL/pubhtml?gid=0&single=true";

    private List<LocStats> allStats = new ArrayList<>();
    public List<LocStats> getAllStats() {
        return allStats;
    }

    @PostConstruct
    public void processCovidDatas() throws IOException,FileNotFoundException {

        List<LocStats> newStats = new ArrayList<>();
        URL covidUrl = new URL(CSV_READER_URL);
        HttpsURLConnection covidUrlConn = (HttpsURLConnection)covidUrl.openConnection();

        /*InputStream inputStream = covidUrlConn.getInputStream();
        File file = new File("covid-19.csv");
        OutputStream outputStream = new FileOutputStream(file);
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();
        outputStream.close();*/

        BufferedReader covidUrlReader = new BufferedReader(
                new InputStreamReader(covidUrlConn.getInputStream()));

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(covidUrlReader);

        int latestCases;
        int prevDayCases;

        for (CSVRecord record : records) {
            LocStats locationStat = new LocStats();
            System.out.println(record);
            locationStat.setNation(record.get(1));
            locationStat.setConfirmedCases(record.get(2));
            locationStat.setDeath(record.get(3));
            locationStat.setRecover(record.get(4));

            newStats.add(locationStat);
        }
        this.allStats = newStats;
        System.out.println(allStats);
    }

}
