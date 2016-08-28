package ocularium.views;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.change_vision.jude.api.inf.model.IConstraint;

import ocularium.ConstraintFormatter;

public class OclTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7278451997120759726L;

	private static final List<IConstraint> EMPTY_DATA = new ArrayList<IConstraint>();

	/*
	 * 
	 */
	List<IConstraint> data = EMPTY_DATA;

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IConstraint c = data.get(rowIndex);
		ConstraintFormatter cf = ConstraintFormatter.getConstraintFormatter();
		cf.setConstraint(c);
		switch (columnIndex) {
		case 0:
			return cf.getContext();
		case 1:
			return cf.getSpecification();
		}
		return "##";
	}

	public void setData(List<IConstraint> cs) {
		data = cs;
		fireTableStructureChanged();

	}

	public void resetData() {
		data = EMPTY_DATA;

	}
}