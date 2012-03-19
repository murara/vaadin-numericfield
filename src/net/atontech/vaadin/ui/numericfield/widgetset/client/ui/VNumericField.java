package net.atontech.vaadin.ui.numericfield.widgetset.client.ui;

import net.atontech.vaadin.ui.numericfield.widgetset.shared.NumericFieldType;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VTextField;

/**
 * This is a NumericField client-side class responsable to validate the input data.
 * @author Luis Fernando Murara - Aton Tech
 * @since 03/13/2012
 *
 */
public class VNumericField extends VTextField {

	protected NumericFieldType numberType;
	protected boolean allowNegative;
	protected String decimalSeparator;
	protected int scale;
	private String lastValue = "";

	public VNumericField() {
		super();
		addKeyUpHandler(keyUpHandler);
		addKeyPressHandler(keyPressHandler);
		addBlurHandler(blurHandler);
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		setNumberType(NumericFieldType.getNumericFeildType(uidl.getIntAttribute("numberType")));
		setAllowNegative(uidl.getBooleanAttribute("allowNegative"));
		setDecimalSeparator(uidl.getStringAttribute("decimalSeparator"));
		setScale(uidl.getIntAttribute("scale"));
		super.updateFromUIDL(uidl, client);
	}
	
	private KeyUpHandler keyUpHandler = new KeyUpHandler() {
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (numberType == NumericFieldType.INTEGER) return;

			if (getValue().isEmpty()) return;
			if (event.getRelativeElement().getClassName().indexOf("v-numericfield-error") >= 0) return;

			String regex = "^\\-?\\d{0,}\\"+decimalSeparator+"?\\d{0,"+scale+"}?$";
			if (!getValue().matches(regex)) {
				setValue(lastValue);
				return;
			}

//			if (getMaxLength() > 0) {
//				int maxInt = getMaxLength() - scale - 1;
//
//				String maskInt = "^\\-?\\d{,"+maxInt+"}\\"+decimalSeparator+"?$";
//				if (getValue().matches(maskInt)) {
//					setValue(lastValue);
//					return;
//				} else {
//					if (!getValue().matches(regex)) {
//						setValue(lastValue);
//						return;
//					}
//				}
//			}

			lastValue = getValue();
		}
	};
	
	private KeyPressHandler keyPressHandler = new KeyPressHandler() {
		
		@Override
		public void onKeyPress(KeyPressEvent event) {
			if ((numberType == NumericFieldType.INTEGER) && (event.getUnicodeCharCode() == ".".codePointAt(0)))
				event.preventDefault();

			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DELETE
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_END
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_HOME
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_LEFT
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_PAGEDOWN
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_PAGEUP
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_RIGHT
					|| event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB
					|| event.isAnyModifierKeyDown())
				return;

			if ((allowNegative) && (event.getUnicodeCharCode() == "-".codePointAt(0)))
				return;

			if ((numberType == NumericFieldType.DOUBLE) && (event.getUnicodeCharCode() == decimalSeparator.codePointAt(0)))
				return;

			if (!Character.isDigit(event.getCharCode()))
				event.preventDefault();
		}
	};
	
	private BlurHandler blurHandler = new BlurHandler() {
		
		@Override
		public void onBlur(BlurEvent event) {
			getElement().removeClassName("v-numericfield-error");
			if (getValue().isEmpty()) return;

			if (numberType == NumericFieldType.INTEGER) {
				String regex = "^\\d{1,}$";
				if (allowNegative) regex = "^\\-?\\d{1,}$";
				if (!getValue().matches(regex)) {
					getElement().addClassName("v-numericfield-error");
					event.preventDefault();
					return;
				}
			}

			if (numberType == NumericFieldType.DOUBLE) {
				boolean alterado = false;
				if (getValue().indexOf(decimalSeparator) == -1) {
					setValue(getValue() + decimalSeparator);
					alterado = true;
				}

				// Remove excesso de decimais
				if (getValue().substring(getValue().indexOf(decimalSeparator)).length() > scale) {
					setValue(getValue().substring(0, getValue().indexOf(decimalSeparator)+scale+1));
					alterado = true;
				}

				// Preenche com zeros a direita
				while (getValue().substring(getValue().indexOf(decimalSeparator)).length() <= scale) {
					setValue(getValue().concat("0"));
					alterado = true;
				}

				// Preenche com zeros a esquerda
				if (getValue().substring(0, 1).equals(decimalSeparator)) {
					setValue("0".concat(getValue()));
					alterado = true;
				}

				String regexp = "^\\d{1,}\\"+decimalSeparator+"\\d{"+scale+"}$";
				if (allowNegative) regexp = "^\\-?\\d{1,}\\"+decimalSeparator+"?\\d{"+scale+"}?$";
				
				if (!getValue().matches(regexp)) {
					getElement().addClassName("v-numericfield-error");
				}
				if (alterado) {
					valueChange(true);
					return;
				}
			}
		}
	};
	
	public NumericFieldType getNumberType() {
		return numberType;
	}

	public void setNumberType(NumericFieldType numberType) {
		this.numberType = numberType;
	}

	public boolean isAllowNegative() {
		return allowNegative;
	}

	public void setAllowNegative(boolean allowNegative) {
		this.allowNegative = allowNegative;
	}

	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	public void setDecimalSeparator(String decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

}
