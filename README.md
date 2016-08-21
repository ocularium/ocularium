# ocularium
A simple tool for OCL constraints visualisation using Astah/JUDE.

# Examples

In order to test our proposed tool, we need a valid model and constraints. 
The first example is from Warmer & Kleppe book: Royal & Loyal.

## Royal & Loyal from Warmer & Kleppe

There are two editions of the book with small differences, we adopted the second edition model.
The first edition is available through a local library. We got the second edition model from the Web.

More than one document use Royal & Loyal example model, among them:
- The Object Constraint Language First Edition by Jos Warmer and Anneke Kleppe
- The Object Constraint Language Second Edition by Jos Warmer and Anneke Kleppe
- Eclipse OCL Documentation by Christian Damus, Adolfo Sánchez-Barbudo Herrera, Axel Uhl, Edward Willink et alli
- Model Driven Engineering Languages and Systems edited by Dorina C. Petriu, Nicolas Rouquette, Oystein Haugen

Modeling Royal & Loyal, second edition, was a nice task to help us understand about the Astah tool.
The default Astah template is a Java template. In order to model, we need primitive types that are platform independent.

Object Management Group provide XMI files with primitive types and other Unified Modeling Language classifiers.
The files are model revision dependent, we adopted the revision 2.5 (current as August 2016).

We need to install an Astah plugin in order to import a XMI model. Astah has a built-in XML import and export capability with a tool specific schema.

# Similar Tools

A number of modeling and programming tools support constraints, among them:
- No Magic, MagicDraw, CameoOCL
- Eclipse, Papyrus
- Enterprise Architect
- Poseidon
- ArgoUML, Dresden OCL
