import model.Model;
import view.View;

/**
 * Created by Ted on 3/20/2018.
 */
public class App {

    public static void main(String[] args) {
        // Assemble all the pieces of the MVC
        Model m = new Model();
        View v = new View("EV scheduling system");
        Controller c = new Controller(m, v);
        c.initController();
    }

}
