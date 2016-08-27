package ocularium;

/*
 * Please change this class's package to your genearated Plug-in's package.
 * Plug-in's package namespace => com.example
 *   com.change_vision.astah.extension.plugin => X
 *   com.example                              => O
 *   com.example.internal                     => O
 *   learning                                 => X
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;

/**
 * 
 * @author marco.mangan@pucrs.br
 *
 */
public class OculariumView extends JPanel implements IPluginExtraTabView {

	private final class OculariumProjectListener implements ProjectEventListener {
		@Override
		public void projectChanged(ProjectEvent e) {
		}

		@Override
		public void projectClosed(ProjectEvent e) {
		}

		@Override
		public void projectOpened(ProjectEvent e) {
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6960786230313145651L;

	public OculariumView() {
        setLayout(new GridLayout(1,0));
        
        String[] columnNames = {"Context",
                                "Expression"};
 
        Object[][] data = {
        {"Kathy", "Smith",
         },
        {"John", "Doe",
         },
        {"Sue", "Black",
         },
        {"Jane", "White",
         },
        {"Joe", "Brown",
         }
        };
 
        final JTable table = new JTable(data, columnNames);
        //table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
 
        JScrollPane scrollPane = new JScrollPane(table);
 
        add(scrollPane);		
		//
		
		addProjectEventListener();
	}

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

	public void activated() {

	}

	public void deactivated() {

	}
}