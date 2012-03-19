package net.atontech.vaadin.ui.numericfield;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import net.atontech.vaadin.ui.numericfield.widgetset.shared.NumericFieldType;

import com.vaadin.data.Property;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.TextField;


/**
 * Server side NumericField component.<br><br>
 * <b>Regional Definations:</b><br>
 * To define the regional definations, create system.properties on the root of source folder. Use the example system.properties.example in this package.<br><br>
 * Integer example: <br>
 * <pre>
 * 	final NumericField nf1 = new NumericField();
 * 	nf1.setCaption("Numeric");
 * 	nf1.setNumberType(NumericField.TYPE_INTEGER);
 * 	nf1.setAllowNegative(false);
 * 	mainWindow.addComponent(nf1);
 * </pre>
 * Double example: <br>
 * <pre>
 * 	final NumericField nf2 = new NumericField();
 * 	nf2.setCaption("Numeric 2");
 * 	nf2.setNumberType(NumericField.TYPE_DOUBLE);
 * 	//nf2.setAllowNegative(true);  // Is not necessary, cuz it's default
 * 	nf2.setScale(2);               // Number of decimals
 * 	mainWindow.addComponent(nf2);
 * </pre>
 * @author Luis Fernando Murara - Aton Tech
 *
 */
@SuppressWarnings("serial")
@com.vaadin.ui.ClientWidget(net.atontech.vaadin.ui.numericfield.widgetset.client.ui.VNumericField.class)
public class NumericField extends TextField {
	
	/**
	 * @deprecated use {@link NumericFieldType.INTEGER}
	 */
	@Deprecated
	public static final int TYPE_INTEGER = 1;
	/**
	 * @deprecated use {@link NumericFieldType.DOUBLE}
	 */
	@Deprecated
	public static final int TYPE_DOUBLE = 2;
	
	NumericFieldType numberType;
	boolean allowNegative = true;
	String decimalSeparator;
	int scale = 0;
	
	/**
	 * Constructs a empty NumericField
	 */
	public NumericField() {
		super();
	}
	
	/**
	 * Constructs a empty NumericField with given caption. 
	 * @param caption the caption String for the editor. 
	 */
	public NumericField(String caption) {
		super(caption);
	}

	public NumericField(String caption, NumericFieldType numberType) {
		setCaption(caption);
		setNumberType(numberType);
	}

	public NumericField(Property dataSource) {
		super(dataSource);
	}
	
	/**
	 * Constructs a new NumericField that's bound to the specified Property and has the given caption String.
	 * @param caption the caption String for the editor.
	 * @param dataSource the Property to be edited with this editor.
	 */
	public NumericField(String caption, Property dataSource) {
		super(caption,dataSource);
	}
	
	@Override
	public void attach() {
		addStyleName("v-numericfield");
	
		super.attach();

		DecimalFormatSymbols dfs = new DecimalFormatSymbols(getLocale());
		decimalSeparator = String.valueOf(dfs.getDecimalSeparator());
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("numberType", getNumberType().getValue());
		target.addAttribute("allowNegative", isAllowNegative());
		target.addAttribute("decimalSeparator", getDecimalSeparator());
		target.addAttribute("scale", getScale());
	}
	
	public NumericFieldType getNumberType() {
		return numberType;
	}

	/**
	 * Sets the number type of NumericField. This type can set with:
	 * <pre>
	 *	NumericFieldType.INTEGER - For integer values
	 *	NumericFieldType.DOUBLE - For double values
	 * </pre>
	 * @param numberType
	 */
	public void setNumberType(NumericFieldType numberType) {
		this.numberType = numberType;
		requestRepaint();
	}
	
	/**
	 * @deprecated use setNumberType(NumericFieldType numberType)
	 * @param numberType int old numeric type representantion
	 */
	@Deprecated
	public void setNumberType(int numberType) {
		setNumberType(NumericFieldType.getNumericFeildType(numberType));
		requestRepaint();
	}

	public boolean isAllowNegative() {
		return allowNegative;
	}

	/**
	 * Sets field to allow or deny negative values.
	 * @param allowNegative true allow negative value, false allow only >= 0 value.
	 */
	public void setAllowNegative(boolean allowNegative) {
		this.allowNegative = allowNegative;
		requestRepaint();
	}

	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	/**
	 * Sets the decimal separator manually. This method overrides system.properties defination.
	 * @param decimalSeparator the String to separete decimals.
	 */
	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
		requestRepaint();
	}

	public int getScale() {
		return scale;
	}

	/**
	 * Sets the quantity of decimals.<br>
	 * Example:
	 * <pre>
	 * 	12.9001 - 4 decimals
	 * 	1290.98 - 2 decimals
	 * </pre>
	 * @param scale the int value for decimals count.
	 */
	public void setScale(int scale) {
		if ((numberType == NumericFieldType.DOUBLE) && (scale <= 0)) {
			numberType = NumericFieldType.INTEGER;
		}
		this.scale = scale;
		requestRepaint();
	}
	
	/**
	 * Add preconfigured validator to field. A IntegerValidator will be used for
	 * Integer field, and a RegexpValidator with scale, decimal separator for Doubles.
	 * <br><br>
	 * A messagem like "Invalid Number" will be show if the validator fails. The languages
	 * available in this widget are english (default), portuguese, german, french, italian, spanish and japanese (thanks Google :P )
	 * <br><br>  
	 * Ex:
	 * <pre>
	 * 	field.addValidator(getLocale());
	 * </pre>
	 * @param locale the user or application locale
	 */
	public void addValidator(Locale locale) {
		String decimalSeparator = getDecimalSeparator();
		if (decimalSeparator == null) {
			DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
			decimalSeparator = String.valueOf(dfs.getDecimalSeparator());
		}
		
		if (numberType == NumericFieldType.INTEGER)
			addValidator(new IntegerValidator(Messages.get(locale)));
		if (numberType == NumericFieldType.DOUBLE) {
			String regexp = "^\\d{1,}\\"+decimalSeparator+"\\d{"+scale+"}$";
			if (allowNegative) regexp = "^\\-?\\d{1,}\\"+decimalSeparator+"?\\d{"+scale+"}?$";
			System.out.println(regexp);
			addValidator(new RegexpValidator(regexp,Messages.get(locale)));
		}
	}

}
