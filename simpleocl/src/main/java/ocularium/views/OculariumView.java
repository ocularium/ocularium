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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

import ocularium.internal.OculariumFacade;

/**
 * Constraint table panel.
 * 
 * Ocularium main user interface is a Astah API extra tab view. This panel has a
 * single JTable component and the corresponding table model. There are two
 * listeners: a row listener and a project event listener,
 * 
 * The row listener is notified when the use selects a row in the table view.
 * Row listener acts to display the constrained model in Astah structure view.
 * 
 * Project event listener is notified when a change occurs on Astah model.
 * Currently, all table data and structure is refreshed when any change occurs
 * in the model. Note: The reason for this is to have the simplest
 * implementation possible. Further development should try and implement a
 * partial or incremental refresh in order to minimize changes in the user
 * interface, as column width and selected row.
 * 
 * @author marco.mangan@gmail.com
 * 
 * @see OclTableModel
 * 
 */
public class OculariumView extends JPanel implements IPluginExtraTabView {

	/**
	 * Listen from table selections.
	 * 
	 * Selected row element is show in Astah project view.
	 * 
	 * @author marco.mangan@gmail.com
	 *
	 */
	private class RowListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			int row = e.getFirstIndex();
			System.out.println("==> RowListener::valueChanged");
			System.out.println(row);
			System.out.println(e);

			IConstraint c = dm.getConstraintAtRow(row);
			System.out.println(c);
		}

	}

	/**
	 * Listen for notifications from Astah projects.
	 * 
	 * Project open and project close cause a complete redraw. Project close
	 * resets view to an empty list.
	 * 
	 * @author marco.mangan@gmail.com
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

		// addListener on activate?
		addProjectEventListener();
		table.getSelectionModel().addListSelectionListener(new RowListener());
		// table.setRowSelectionAllowed(true);
		ListSelectionModel listMod = table.getSelectionModel();
		listMod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// listMod.addListSelectionListener(this);

		// System.out.println("Adding listeners");
		 table.addMouseListener(new MouseAdapter() {
		 public void mouseClicked(MouseEvent e) {
		 if (e.getClickCount() == 2) {
		 JTable target = (JTable) e.getSource();
		 int row = target.getSelectedRow();
		 // int column = target.getSelectedColumn();
		 // do some action
		 if (row != -1) {
		 System.out.println("==> MouseAdapter::mouseClicked");
		 System.out.println(row);
			IConstraint c = dm.getConstraintAtRow(row);
			System.out.println(c);
		 }
		 }
		 }
		 });
	}

	/**
	 * 
	 */
	private void addProjectEventListener() {
		try {
			ProjectAccessor projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			projectAccessor.addProjectEventListener(new OculariumProjectListener());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addSelectionListener(ISelectionListener listener) {
		// TODO: System.out.printf("Teste%n");
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
		refresh();
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
		// TODO: could implement incremental refresh
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