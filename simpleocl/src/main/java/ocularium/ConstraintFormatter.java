package ocularium;

import com.change_vision.jude.api.inf.model.IAttribute;
import com.change_vision.jude.api.inf.model.IClass;
import com.change_vision.jude.api.inf.model.IConstraint;
import com.change_vision.jude.api.inf.model.IElement;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.model.IOperation;
import com.change_vision.jude.api.inf.model.IParameter;

public class ConstraintFormatter {

	private static final ConstraintFormatter DEFAULT_FORMATTER = new ConstraintFormatter();

	public static ConstraintFormatter getConstraintFormatter() {
		return DEFAULT_FORMATTER;
	}

	private IConstraint iConstraint;

	@Override
	public String toString() {
		assert iConstraint != null;
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("context ");
			sb.append(getContext());
			sb.append("\n");
			sb.append(getSpecification());
			sb.append("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

	public void setConstraint(IConstraint iConstraint) {
		this.iConstraint = iConstraint;

	}

	/**
	 * 
	 * @param iClass
	 * @return
	 */
	private static String getFullName(IClass iClass) {
		assert iClass != null;

		StringBuffer sb = new StringBuffer();
		IElement owner = iClass.getOwner();
		while (owner != null && owner instanceof INamedElement && owner.getOwner() != null) {
			sb.insert(0, ((INamedElement) owner).getName() + "::");
			owner = owner.getOwner();
		}
		sb.append(iClass.getName());
		return sb.toString();
	}

	public String getContext() {
		assert iConstraint != null;

		StringBuffer sb = new StringBuffer();

		IElement[] ces = iConstraint.getConstrainedElement();
		assert ces.length == 1;
		IElement e = ces[0];
		if (e instanceof IAttribute) {
			IElement owner = e.getOwner();
			if (owner instanceof IClass) {
				IClass cl = (IClass) owner;
				sb.append(getFullName(cl));
			} else {
				sb.append(owner.toString());
			}
			// output.write(owner.toString());
			sb.append("::");
			sb.append(e.toString());
		} else if (e instanceof IOperation) {
			IOperation op = (IOperation) e;

			IElement owner = op.getOwner();
			if (owner instanceof IClass) {
				IClass cl = (IClass) owner;
				sb.append(getFullName(cl));
			} else {
				sb.append(owner.toString());
			}
			// output.write(owner.toString());
			sb.append("::");
			sb.append(op.toString());
			sb.append("(");
			IParameter[] ps = op.getParameters();

			boolean firstParam = true;

			for (IParameter iParameter : ps) {
				if (!firstParam) {
					sb.append(", ");

				}
				String paramName = iParameter.getName().toString();
				String paramType = iParameter.getType().toString();
				sb.append(paramName == null ? "" : paramName);
				sb.append(paramType == null ? "" : ": " + paramType);

			}
			sb.append(")");

			String returnType = op.getReturnType().toString();

			sb.append(returnType == null ? "" : ": " + returnType);
		} else {
			if (e instanceof IClass) {
				IClass cl = (IClass) e;
				sb.append(getFullName(cl));
			} else {
				sb.append(e.toString());
			}
		}
		return sb.toString();
	}

	public String getSpecification() {
		assert iConstraint != null;

		StringBuffer sb = new StringBuffer();
		String spec = iConstraint.getSpecification();
		if (spec.startsWith("BODYCONDITION:")) {
			spec = spec.substring("BODYCONDITION:".length());
		} else if (spec.startsWith("POSTCONDITION:")) {
			spec = spec.substring("POSTCONDITION:".length());
		}

		sb.append(spec);
		return sb.toString();
	}

}
