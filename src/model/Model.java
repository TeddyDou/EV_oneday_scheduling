package model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
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

    public Model(){
        customers = new ArrayList();
        chargers = new ArrayList();
    }

    public void processData() throws Exception{
        System.out.println("Input file is "+ inputFile.getName());

        readInputSheet();
        scheduling();

        for(EVCharger c : chargers){
            System.out.println(c.toString());
        }
        System.out.println("Finish calculation");
    }

    private void scheduling() {
        helper = new ChargingHelper();
        maxHeap = new EVCustomerMaxHeap(300);
        int priority = 3;
        for (; priority > 0; priority--){
            for (EVCustomer ev : customers){
                double rate = helper.getRateByPriority(ev.getTypeID(), priority);
                if (rate != 0){
                    ev.setChargingTimeAndRatio(rate);
                    if (ev.getBarWindowRatio() <= 1.0)
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
//        Time naiveTime = charger.getNaiveEarliestTime(EV.getStartTime());
//        if(!charger.positioning(EV, naiveTime)){
        if(!charger.positioning(EV, EV.startTime)){
            idFilter.add(new Integer(charger.ID));
            selectEVcharger(EV, priority, idFilter);
        }
        else
            customers.remove(EV);

    }

    private void selectEVcharger(EVCustomer EV, int priority, Set<Integer> idFilter){

        //select next preferred charger
        int chargerType = helper.getChargerByPriority(EV.getTypeID(), priority);
        int maxDuration = 0;
        EVCharger preferredCharger = null;
        for (EVCharger charger : chargers){
            if(charger.chargerType == chargerType && charger.getAvailableDuration() > maxDuration && !idFilter
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
        m.setInputFile(new File("data3.1.xlsx"));
        try {
            m.processData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
