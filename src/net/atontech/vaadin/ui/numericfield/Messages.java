package net.atontech.vaadin.ui.numericfield;

import java.util.Locale;

/**
 * Ugly i18n.
 * @author Murara
 *
 */
public class Messages {
	
	public static String get(Locale locale) {
		if (locale.getLanguage().equals(new Locale("pt").getLanguage())) return "Valor inválido!";
		if (locale.getLanguage().equals(new Locale("es").getLanguage())) return "Valor no válido!";
		if (locale.getLanguage().equals(new Locale("de").getLanguage())) return "Ungültiger Wert!";
		if (locale.getLanguage().equals(new Locale("fr").getLanguage())) return "Valeur non valide!";
		if (locale.getLanguage().equals(new Locale("it").getLanguage())) return "Valore non valido!";
		if (locale.getLanguage().equals(new Locale("ja").getLanguage())) return "値が無効です！";
		return "Invalid value!";
	}
	
}
