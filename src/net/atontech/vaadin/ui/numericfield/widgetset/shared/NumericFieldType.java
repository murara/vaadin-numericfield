package net.atontech.vaadin.ui.numericfield.widgetset.shared;

public enum NumericFieldType {

	INTEGER (1),
	DOUBLE (2);

	private final int value;   

	NumericFieldType(int value) {
		this.value = value;
	}

	public int getValue() { 
		return value; 
	}

	public static NumericFieldType getNumericFeildType(int value) {
		switch (value) {
		case 1: return NumericFieldType.INTEGER;
		case 2: return NumericFieldType.DOUBLE;
		default: return NumericFieldType.INTEGER;
		}
	}


}
