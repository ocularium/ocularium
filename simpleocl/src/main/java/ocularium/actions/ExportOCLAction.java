package ocularium.actions;


import java.io.FileWriter;
import java.io.Writer;

import javax.swing.JOptionPane;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

import ocularium.OculariumFacade;

/**
 * 
 * @author marco.mangan@pucrs.br
 *
 */
public class ExportOCLAction implements IPluginActionDelegate {

	public Object run(IWindow window) throws UnExpectedException {
	    try {
	        AstahAPI api = AstahAPI.getAstahAPI();
	        ProjectAccessor projectAccessor = api.getProjectAccessor();
	        IModel project = projectAccessor.getProject();
			OculariumFacade f = new OculariumFacade(project);			

			Writer actual = new FileWriter(OculariumFacade.getOclProjectPath(projectAccessor));
			f.exportOCL(actual);
			projectAccessor.close();	
			actual.close();

	        JOptionPane.showMessageDialog(window.getParent(),"Export OCL Action");
	    } catch (ProjectNotFoundException e) {
	        String message = "Project is not opened.Please open the project or create new project.";
			JOptionPane.showMessageDialog(window.getParent(), message, "Warning", JOptionPane.WARNING_MESSAGE); 
	    } catch (Exception e) {
	    	JOptionPane.showMessageDialog(window.getParent(), "Unexpected error has occurred.", "Alert", JOptionPane.ERROR_MESSAGE); 
	        throw new UnExpectedException();
	    }
	    return null;
	}


}
