import view.SimulatorGUI;
import java.io.FileWriter;

/* to install JavaFX (adapted from https://openjfx.io/openjfx-docs/):
 *	- Open Module Settings
 * 		- Libraries, add
 *			- from Maven openjfx.javafx.fxml:19.0.2 (this includes all necessary other JavaFX libraries)
 *	- Run Configuration Edit
 *		- Modify Options/Add VM options
 *			- --module-path "lib" --add-modules javafx.controls,javafx.fxml
 */

public class Main {// Simulator using Java FX

	public static void main(String args[]) {
        try (java.io.FileWriter fw = new java.io.FileWriter("service_log.csv", false)) {
            fw.write(String.format(""
            ));
        } catch (java.io.IOException e) {
            System.err.println("CSV logging failed: " + e.getMessage());
        }

        SimulatorGUI.main(args);
	}
}
