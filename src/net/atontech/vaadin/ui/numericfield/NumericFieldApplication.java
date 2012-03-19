package net.atontech.vaadin.ui.numericfield;

import net.atontech.vaadin.ui.numericfield.widgetset.shared.NumericFieldType;

import com.vaadin.Application;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.ui.*;

@SuppressWarnings("serial")
public class NumericFieldApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("NumericField Application");
		
		final Form form = new Form(new FormLayout());

		final NumericField nf1 = new NumericField("Integer",NumericFieldType.INTEGER);
		nf1.setMaxLength(10);
		nf1.setAllowNegative(false);
		nf1.addListener(new FieldEvents.BlurListener() {
			@Override
			public void blur(BlurEvent event) {
				getMainWindow().showNotification((String) ((NumericField)event.getSource()).getValue());				
			}
		});
		form.addField("nf1", nf1);
		
		final NumericField nf2 = new NumericField("Double",NumericFieldType.DOUBLE);
		nf2.setMaxLength(5);
		nf2.setScale(2);
		nf2.addValidator(getLocale());
//		nf2.addListener(new FieldEvents.BlurListener() {
//			@Override
//			public void blur(BlurEvent event) {
//				getMainWindow().showNotification((String) ((NumericField)event.getSource()).getValue());				
//			}
//		});
		form.addField("nf2", nf2);
		
		mainWindow.addComponent(form);

		mainWindow.addComponent(new Button("Apply", form, "commit"));
		
		setMainWindow(mainWindow);
	}

}
