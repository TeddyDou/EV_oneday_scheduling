package model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Model {

    private String filePath;
    private File inputFile;
    private File outputFile;
    private Workbook workbook;
    private List<EVCustomer> customers;
    private List<EVCharger> chargers;
    private ChargingHelper helper;
    private EVCustomerMaxHeap maxHeap;

    static final int NISSAN = 1;
    static final int CHEV =2;
    static final int TESLA = 3;
    static final int LEVEL2 = 1;
    static final int CHADEMO = 2;
    static final int CCS = 3;
    static final int SUPERCHARGER =4;
    static final Time MIDNIGHT = new Time("24:00");
    static double ALPHA = 1.0;

    public Model(){
        customers = new ArrayList();
        chargers = new ArrayList();
    }

    public List<EVCharger> run() throws Exception {
        ALPHA = calculateAlpha();
        return processData();
    }

    private double calculateAlpha() {
        int max = 0;
        double preferredAlpha = 1;
        for (double d = 0.1; d <= 1; d += 0.1) {
            Model m = new Model();
            m.setInputFile(inputFile);
            setALPHA(d);
            try {
                m.processData();
                int n = m.getAssignedNumber();
                if (n > max){
                    max = n;
                    preferredAlpha = d;
                }
                System.out.println("Scheduled " + n + " EVs.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Max is " + max + " Alpha is " + preferredAlpha);
        return preferredAlpha;
    }

    public List<EVCharger> processData() throws Exception{
        System.out.println("Input file is "+ inputFile.getName());

        readInputSheet();
        scheduling();

//        for(EVCharger c : chargers){
//            System.out.println(c.toString());
//        }
        System.out.println("Finish calculation");
        return this.chargers;
    }

    public int getAssignedNumber(){
        int number = 0;
        for (EVCharger c : this.chargers) {
//            System.out.println("number " + c.getAssignedList().size());
            number += c.getAssignedList().size();
        }
        return number;
    }

    private void scheduling() {
        helper = new ChargingHelper();
        maxHeap = new EVCustomerMaxHeap(300);
        int priority = 3;
        for (; priority > 0; priority--){
            for (EVCustomer ev : customers){
                double rate = helper.getRateByPriority(ev.getTypeID(), priority);
                if (rate != 0){
                    ev.setRatioAndAbsAndScore(rate);
                    if (ev.getBarWindowRatio() <= 1.0)
//                    if (ev.getBarWindowRatio() >= 1.0)
                        maxHeap.insert(ev);
                }
            }
//            maxHeap.demo();
            // initialize max heap
            maxHeap.maxHeap();
            while(maxHeap.hasNext()){
                EVCustomer nextEV = maxHeap.remove();
                Set<Integer> idFilter = new HashSet<>();
                selectEVcharger(nextEV, priority, idFilter);
            }
            


        }


    }

    private void assignEVToCharger(EVCustomer EV, EVCharger charger, int priority, Set<Integer> idFilter) {

        //find earliest available time

//        if(!charger.positioning(EV, EV.startTime)){
        if(!charger.positioning(EV)){

            idFilter.add(new Integer(charger.ID));
            selectEVcharger(EV, priority, idFilter);
        }
        else
            customers.remove(EV);

    }

    private void selectEVcharger(EVCustomer EV, int priority, Set<Integer> idFilter){

        //select next preferred charger
        int chargerType = helper.getChargerByPriority(EV.getTypeID(), priority);
        int maxDuration = 24 * 60;
//        int maxDuration = 0;
        EVCharger preferredCharger = null;
        for (EVCharger charger : chargers){
            if(charger.chargerType == chargerType && charger.getAvailableDuration() <= maxDuration && !idFilter
                    .contains(charger.ID)){
                maxDuration = charger.getAvailableDuration();
                preferredCharger = charger;
            }
        }

        //try to assign
        if (preferredCharger != null){
            assignEVToCharger(EV, preferredCharger, priority, idFilter);
        }
//        else
//            System.out.println("here");

    }

    private void readInputSheet() throws Exception{
        workbook = new WorkbookFactory().create(inputFile);
        boolean sheetExists = false;
        DataFormatter dataFormatter = new DataFormatter();
        for(Sheet sheet : workbook){
            String sheetName = sheet.getSheetName();
            if(sheetName.equals("Input1")){
                sheetExists = true;
                for (Row row: sheet) {
                    if(!Character.isDigit(dataFormatter.formatCellValue(row.getCell(0)).charAt(0)))
                        continue;
                    //create customer or charging point by row
                    createCustomer(row, dataFormatter);
                }
            }
            else if(sheetName.equals("Input2")){
                for(Row row: sheet) {
                    if(!Character.isDigit(dataFormatter.formatCellValue(row.getCell(0)).charAt(0)))
                        continue;
                    //create charger points or charging point by row
                    createCharger(row, dataFormatter);
                }
            }
        }
        if (!sheetExists){
            throw new Exception();
        }
    }

    private void createCustomer(Row row, DataFormatter dataFormatter) {
        int id = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(0)));
        Time startTime = new Time(dataFormatter.formatCellValue(row.getCell(1)));
        Time endTime = new Time(dataFormatter.formatCellValue(row.getCell(2)));
        int mile = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(3)));
        int typeID = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(4)));

        EVCustomer customer = new EVCustomer(id, mile, typeID, startTime, endTime);
        this.customers.add(customer);
    }

    private void createCharger(Row row, DataFormatter dataFormatter) {
        int id = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(0)));
        int typeID = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(1)));

        EVCharger charger = new EVCharger(id, typeID);
        this.chargers.add(charger);
    }

    public static void setALPHA(double alpha) {
        Model.ALPHA = alpha;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public static void main(String[] args) {
        Model m = new Model();
//        m.setInputFile(new File("data3.1.xlsx"));
        m.setInputFile(new File("data_final_2.xlsx"));
        try {
            m.processData();
            System.out.println("Scheduled " + m.getAssignedNumber() + " EVs.");
            m.generateExcel();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //test alpha

//        int max = 0;
//        for(double d = 0.1; d <= 1.0; d+=0.1){
//            Model m = new Model();
//            m.setInputFile(new File("data_final_2.xlsx"));
//            m.setALPHA(d);
//            try {
//                m.processData();
//                int n = m.getAssignedNumber();
//                if (n > max)
//                    max = n;
//                System.out.println("Scheduled " + n + " EVs." + " Alpha is " + d);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println("Max is " + max);
//        }
    }

    public void generateExcel() {
        String FILE_NAME = "OutputExcel.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Schedule");
        Object[][] datatypes = {
                {"Datatype", "Type", "Size(in bytes)"},
                {"int", "Primitive", 2},
                {"float", "Primitive", 4},
                {"double", "Primitive", 8},
                {"char", "Primitive", 1},
                {"String", "Non-Primitive", "No fixed size"}
        };
        int rowNum = 0;
        System.out.println("Creating excel");

        Row row = sheet.createRow(rowNum);

        Cell cell1 = row.createCell(0);
        cell1.setCellValue("ID");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("Type");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("Start Time");
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("End Time");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue("Charger ID");

        for (EVCharger c : chargers) {
            for (EVCustomer ev : c.getAssignedList()){
                rowNum ++;
                writeToRow(sheet, ev, rowNum, c.getID());

            }
        }

        try {
            outputFile = new File(FILE_NAME);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

    private void writeToRow(XSSFSheet sheet, EVCustomer ev, int rowNum, int chargerID) {
        Row row = sheet.createRow(rowNum);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(ev.getEvID());
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(ev.getTypeID());
        Cell cell3 = row.createCell(2);
        cell3.setCellValue(ev.getAssignedStartTime().toString());
        Cell cell4 = row.createCell(3);
        cell4.setCellValue(ev.getAssignedEndTime().toString());
        Cell cell5 = row.createCell(4);
        cell5.setCellValue(chargerID);
    }

}
