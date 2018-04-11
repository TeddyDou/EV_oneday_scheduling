package src.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private String filePath;
    private File inputFile;
    private File outputFile;
    private Workbook workbook;
    private List<EVCustomer> customers;
    private List<EVCharger> chargers;

    public Model(){
        customers = new ArrayList();
        chargers = new ArrayList();
    }

    public void setWorkbook(File file) throws IOException, InvalidFormatException {
        this.workbook = new WorkbookFactory().create(file);
    }

    public void processData() throws Exception{
        System.out.println("Input file is "+ inputFile.getName());

        setWorkbook(inputFile);
        readInputSheet();

        System.out.println("Finish calculation");
    }

    private void readInputSheet() throws Exception{
        System.out.println("Looking for sheet with name 'Input'...");

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

//                List<String> EVMap
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
        int type = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(4)));

        EVCustomer customer = new EVCustomer(id, mile, type, startTime, endTime);
        this.customers.add(customer);
    }

    private void createCharger(Row row, DataFormatter dataFormatter) {
        int id = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(0)));
        int type = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(1)));

        EVCharger charger = new EVCharger(id, type);
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
        m.setInputFile(new File("test data1.xlsx"));
        try {
            m.processData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
