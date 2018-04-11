package src;
/**
 * Created by Ted on 3/20/2018.
 */
import src.model.Model;
import src.view.View;

public class Controller {

    private Model model;
    private View view;

    public Controller(Model m, View v) {
        model = m;
        view = v;
        initView();
    }

    public void initView() {
        view.init();
    }

    public void initController() {
        view.getBrowseButton().addActionListener(e -> selectFile());
        view.getLoadButton().addActionListener(e -> loadFile());
//        view.getHello().addActionListener(e -> sayHello());
//        view.getBye().addActionListener(e -> sayBye());
    }

    private void loadFile() {
        if(view.isFileValid()){
            model.setInputFile(view.getInputFile());
            try {
                model.processData();
                view.showProcessing();
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
