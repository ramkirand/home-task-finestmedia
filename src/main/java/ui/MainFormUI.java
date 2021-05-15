package ui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import constant.Constant;
import exception.ApiRequestException;
import lombok.extern.slf4j.Slf4j;
import model.ConsumptionData;
import service.ConsumptionService;
import service.DataLoaderService;

@Slf4j
@Route("Main")
@PageTitle("Main page")
@StyleSheet("frontend://styles/styles.css")
public class MainFormUI extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private Button submitbtn, reSetbtn, logoutbtn;
	private TextField inputTextField;
	private DatePicker inputDatePicker;
	private Label documentIdentificationLabel;
	private List<ConsumptionData> energyReport;
	private static final String DOCUMENT_IDENTIFICATION = "documentIdentification";
	private static final String MEASURMENT_PRICE = "measurmentPrice";
	private static final String MEASUREMENT_UNIT = "measurementUnit";
	private static final String ACCOUNTING_POINT = "accountingPoint";
	private static final String DOCUMENT_DATE_TIME = "documentDateTime";

	@Autowired
	private ConsumptionService ifinestService;

	@Autowired
	private DataLoaderService dataLoaderService;

	private Grid<ConsumptionData> grid = new Grid<>(ConsumptionData.class);

	public MainFormUI(ConsumptionService ifinestService, DataLoaderService dataLoaderService)
			throws ParserConfigurationException, SAXException, IOException {

		this.ifinestService = ifinestService;
		this.dataLoaderService = dataLoaderService;
		buildMainFormUI();
	}

	private void buildMainFormUI() throws ParserConfigurationException, SAXException, IOException {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.addClassName(Constant.FORM_MAIN_VIEW);
		mainLayout.setHorizontalComponentAlignment(Alignment.START);
		setSizeFull();
		try {
			if (dataLoaderService.loadSeedData(Constant.URL).equals(Constant.DATALOADED_SUCCESS)) {
				energyReport = ifinestService.findAllData();
				populateFormData(mainLayout, energyReport);
				add(mainLayout);
			}
		} catch (ApiRequestException ex) {
			log.info(ex.getMessage());
			createWarningDialogBox(null, false);
		}

	}

	private void populateFormData(VerticalLayout mainLayout, List<ConsumptionData> energyReport) {
		documentIdentificationLabel = new Label(Constant.CUSTOMER_ENERGY_CONSUMPTION_DATA);
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.add(documentIdentificationLabel);
		hLayout.setAlignItems(Alignment.CENTER);

		HorizontalLayout buttonPanel = createButtonPanel();
		mainLayout.add(buttonPanel);
		grid.setColumns(DOCUMENT_IDENTIFICATION, DOCUMENT_DATE_TIME, ACCOUNTING_POINT, MEASUREMENT_UNIT,
				MEASURMENT_PRICE);
		addClassName(Constant.GRID_LIST_VIEW);

		grid.setItems(energyReport);

		add(hLayout);
		add(grid);
	}

	private HorizontalLayout createButtonPanel() {
		submitbtn = createButton("Submit");
		submitbtn.setEnabled(false);
		inputTextField = new TextField();
		inputTextField.setPlaceholder(Constant.INPUT_TEXT_FIELD_PLACE_HOLDER);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setAlignItems(Alignment.CENTER);
		buttonLayout.setVerticalComponentAlignment(Alignment.CENTER, buttonLayout);
		buttonLayout.setBoxSizing(BoxSizing.BORDER_BOX);

		inputDatePicker = inputDateSelection(Constant.START_DATE);

		buttonLayout.add(inputDatePicker, inputTextField);

		HorizontalLayout buttonHLayout = new HorizontalLayout();

		reSetbtn = createButton(Constant.RESET);
		reSetbtn.addClickListener(event -> {
			reset();
		});

		logoutbtn = createButton(Constant.LOG_OUT);
		buttonHLayout.add(submitbtn, reSetbtn, logoutbtn);

		clickSubmitButton(inputDatePicker, inputTextField, submitbtn);

		clickLogoutButton(logoutbtn);
		buttonHLayout.add(submitbtn, reSetbtn, logoutbtn);

		VerticalLayout vLayout = new VerticalLayout();
		vLayout.add(buttonHLayout, buttonLayout);
		vLayout.setAlignItems(Alignment.END);
		add(vLayout);

		return buttonLayout;
	}

	private void clickLogoutButton(Button logoutbtn) {
		logoutbtn.addClickListener(e -> {
			getUI().get();
			UI.getCurrent().navigate(LoginScreenUI.class);

		});
	}

	private DatePicker inputDateSelection(String date) {
		DatePicker datePicker = new DatePicker();

		datePicker.setPlaceholder(Constant.INPUT_DATE_PLACE_HOLDER);
		Div value = new Div();
		value.setText(date);
		datePicker.addValueChangeListener(event -> {
			validateInputDate(date, value, event);
		});

		return datePicker;
	}

	private void validateInputDate(String date, Div value, ComponentValueChangeEvent<DatePicker, LocalDate> event) {
		LocalDate today = LocalDate.now();
		if (event.getValue() != null) {
			if (inputDatePicker.getValue().toString().compareTo(today.toString()) > 0) {
				createWarningDialogBox(inputDatePicker.getValue().toString(), true);
				submitbtn.setEnabled(false);
				return;
			}

		}
		if (event.getValue() == null) {
			value.setText(date + Constant.NOT_SELECTED);
		} else {
			submitbtn.setEnabled(true);
		}
	}

	private void reset() {
		inputTextField.setValue("");
		inputDatePicker.clear();
		addClassName(Constant.GRID_LIST_VIEW);
		grid.setItems(energyReport);
		submitbtn.setEnabled(false);

	}

	private Button createButton(String label) {
		Button logoutbtn = new Button(label);
		logoutbtn.addThemeName(Constant.BUTTON_THEME);
		return logoutbtn;
	}

	private void clickSubmitButton(DatePicker inputDatePicker, TextField inputTextField, Button submitbtn) {
		submitbtn.addClickListener(e -> {
			if (inputDatePicker.getValue() != null && inputTextField.getValue().isEmpty()) {
				createWarningDialogBox(inputTextField.getValue(), false);
			}
			if (Double.parseDouble(inputTextField.getValue()) < 0
					|| Double.parseDouble(inputTextField.getValue()) > Constant.MAX_PRICE_VALUE) {
				validateInputPrice(inputTextField);
				return;
			} else {
				addClassName(Constant.GRID_LIST_VIEW);
				if (inputDatePicker.getValue() != null && inputTextField.getValue() != null) {
					List<ConsumptionData> fetchByPrice = ifinestService
							.findByMeasurmentPrice(inputDatePicker.getValue().toString(), inputTextField.getValue());

					grid.setItems(fetchByPrice);
					submitbtn.setEnabled(false);
				}
			}

		});
	}

	private void validateInputPrice(TextField inputTextField) {
		createWarningDialogBox(inputTextField.getValue(), false);
		inputTextField.clear();
		return;
	}

	private Dialog createWarningDialogBox(String input, boolean flag) {
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		Button confirmButton = new Button("OK", event -> {
			dialog.close();
		});
		confirmButton.setWidthFull();

		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setAlignItems(Alignment.CENTER);
		verticalLayout.add(confirmButton);
		dialog.add(verticalLayout);
		dialog.open();
		if (input == null && !flag) {
			dialog.add(Constant.SERVICE_UNAVIALABLE);
			return dialog;
		}

		if (flag) {
			dialog.add(Constant.INVALID_DATE + input);

		} else {
			dialog.add(Constant.INVALID_PRICE_INPUT + input);
		}

		return dialog;
	}
}
