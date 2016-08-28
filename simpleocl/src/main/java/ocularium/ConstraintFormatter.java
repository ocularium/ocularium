package ocularium;

import java.io.StringWriter;
import java.io.Writer;

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
		String s = "";
		Writer output = new StringWriter();

		try {
			output.write("context ");
			IElement[] ces = iConstraint.getConstrainedElement();
			assert ces.length == 1;
			IElement e = ces[0];
			if (e instanceof IAttribute) {
				IElement owner = e.getOwner();
				if (owner instanceof IClass) {
					IClass cl = (IClass) owner;
					output.write(getFullName(cl));
				} else {
					output.write(owner.toString());
				}
				// output.write(owner.toString());
				output.write("::");
				output.write(e.toString());
			} else if (e instanceof IOperation) {
				IOperation op = (IOperation) e;

				IElement owner = op.getOwner();
				if (owner instanceof IClass) {
					IClass cl = (IClass) owner;
					output.write(getFullName(cl));
				} else {
					output.write(owner.toString());
				}
				// output.write(owner.toString());
				output.write("::");
				output.write(op.toString());
				output.write("(");
				IParameter[] ps = op.getParameters();

				boolean firstParam = true;

				for (IParameter iParameter : ps) {
					if (!firstParam) {
						output.write(", ");

					}
					String paramName = iParameter.getName().toString();
					String paramType = iParameter.getType().toString();
					output.write(paramName == null ? "" : paramName);
					output.write(paramType == null ? "" : ": " + paramType);

				}

				output.write(")");

				String returnType = op.getReturnType().toString();

				output.write(returnType == null ? "" : ": " + returnType);
			} else {
				if (e instanceof IClass) {
					IClass cl = (IClass) e;
					output.write(getFullName(cl));
				} else {
					output.write(e.toString());
				}
			}

			output.write("\n");
			String spec = iConstraint.getSpecification();
			if (spec.startsWith("BODYCONDITION:")) {
				spec = spec.substring("BODYCONDITION:".length());
			} else if (spec.startsWith("POSTCONDITION:")) {
				spec = spec.substring("POSTCONDITION:".length());
			}

			output.write(spec);
			output.write("\n");
			s = output.toString();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;

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

}
