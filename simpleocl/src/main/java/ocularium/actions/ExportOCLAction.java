/*
 * Copyright (c) 2016, Ocularium. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies 
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE 
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 * Please contact Ocularium at http://ocularium.github.io/ocularium if you need additional 
 * information or have any questions.
 */
package ocularium.actions;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

import ocularium.internal.OculariumFacade;

/**
 * OSGi action to export constraints.
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class ExportOCLAction implements IPluginActionDelegate {

	/**
	 * 
	 */
	public Object run(IWindow window) throws UnExpectedException {
		try {
			AstahAPI api = AstahAPI.getAstahAPI();
			ProjectAccessor projectAccessor = api.getProjectAccessor();
			IModel project = projectAccessor.getProject();
			OculariumFacade f = new OculariumFacade(project);
			JFileChooser chooser = new JFileChooser();
			Component aComponent = AstahAPI.getAstahAPI().getViewManager().getMainFrame();
			String projectDirectory = OculariumFacade.getOclProjectPath(projectAccessor);
			chooser.setCurrentDirectory(new File(projectDirectory));
			int returnVal = chooser.showSaveDialog(aComponent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Writer actual = new FileWriter(chooser.getSelectedFile());
				f.exportOCL(actual);
				actual.close();

				JOptionPane.showMessageDialog(window.getParent(),
						"Export OCL Completed! See project folder for generated file.");
			}
		} catch (ProjectNotFoundException e) {
			String message = "Project is not opened.Please open the project or create new project.";
			JOptionPane.showMessageDialog(window.getParent(), message, "Warning", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(window.getParent(), "Unexpected error has occurred.", "Alert",
					JOptionPane.ERROR_MESSAGE);
			throw new UnExpectedException();
		}
		return null;
	}

}
