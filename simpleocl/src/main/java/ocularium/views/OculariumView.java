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
package ocularium.views;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import ocularium.OculariumFacade;

/**
 * 
 * @author marco.mangan@gmail.com
 *
 */
public class OculariumView extends JPanel implements IPluginExtraTabView {

	/**
	 * Listen for notifications from Astah projects
	 * 
	 * @author marco
	 *
	 */
	private final class OculariumProjectListener implements ProjectEventListener {

		@Override
		public void projectChanged(ProjectEvent e) {
			refresh();
		}

		@Override
		public void projectClosed(ProjectEvent e) {
			dm.resetData();
		}

		@Override
		public void projectOpened(ProjectEvent e) {
			refresh();
		}
		

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6960786230313145651L;
	
	/**
	 * 
	 */
	private OclTableModel dm;

	/**
	 * 
	 * @return
	 */
	public OclTableModel getModel() {
		return dm;
	}
	
	/**
	 * 
	 */
	public OculariumView() {
		setLayout(new GridLayout(1, 0));

		dm = new OclTableModel();
		final JTable table = new JTable(dm);
		// table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(table);

		add(scrollPane);
		//

		addProjectEventListener();
	}

	/**
	 * 
	 */
	private void addProjectEventListener() {
		try {
			ProjectAccessor projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			projectAccessor.addProjectEventListener(new OculariumProjectListener());
		} catch (ClassNotFoundException e) {
			e.getMessage();
		}
	}

	@Override
	public void addSelectionListener(ISelectionListener listener) {
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public String getDescription() {
		return "Ocularium constraint viewer";
	}

	@Override
	public String getTitle() {
		return "Ocularium";
	}

	/**
	 * 
	 */
	public void activated() {
// TODO: may need to call refresh...
		//refresh();
	}

	/**
	 * 
	 */
	public void deactivated() {

	}
	
	
	/**
	 * Load all constraints into table model
	 */
	private void refresh() {
		AstahAPI api;
		try {
			api = AstahAPI.getAstahAPI();

			ProjectAccessor projectAccessor = api.getProjectAccessor();
			IModel project = projectAccessor.getProject();
			OculariumFacade f = new OculariumFacade(project);
			List<IConstraint> cs = f.getConstraints();
			dm.setData(cs);

		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (ProjectNotFoundException e1) {
			e1.printStackTrace();
		}
	}	
}