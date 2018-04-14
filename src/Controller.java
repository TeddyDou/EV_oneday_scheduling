/**
 * Created by Ted on 3/20/2018.
 */
import model.EVCharger;
import model.Model;
import view.View;

import java.util.List;

public class Controller {

    private Model model;
    private View view;

    public Controller(Model m, View v) {
        model = m;
        view = v;
        initView();
    }

    public void initView() {
        view.init(view.getBrowseButton(), view.getLoadButton());
    }

    public void initController() {
        view.getBrowseButton().addActionListener(e -> selectFile());
        view.getLoadButton().addActionListener(e -> loadFile());
    }

    private void initPostprocessingController() {
        view.getExcelButton().addActionListener(e -> outputExcel());
        view.getGanttButton().addActionListener(e -> outputGantt());
    }

    private void outputGantt() {
        view.generateGantt(model.getAssignedNumber());
    }

    private void outputExcel() {
        model.generateExcel();
    }

    private void loadFile() {
        if(view.isFileValid()){
            model.setInputFile(view.getInputFile());
            try {
                view.showProcessing();
                List<EVCharger> chargers = model.processData();
                view.showPostprocessing(chargers);
                initPostprocessingController();
            } catch (Exception e) {
                view.fileNoInputMsg();
            }
        }
    }

    private void selectFile() {
        view.selectFile();
    }

//    private void saveFirstname() {
//        model1.setFirstname(view.getFirstnameTextfield().getText());
//        JOptionPane.showMessageDialog(null, "Firstname saved : " + model1.getFirstname(), "Info", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    private void saveLastname() {
//        model1.setLastname(view.getLastnameTextfield().getText());
//        JOptionPane.showMessageDialog(null, "Lastname saved : " + model1.getLastname(), "Info", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    private void sayHello() {
//        JOptionPane.showMessageDialog(null, "Hello " + model1.getFirstname() + " " + model1.getLastname(), "Info", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    private void sayBye() {
//        System.exit(0);
//    }

}
