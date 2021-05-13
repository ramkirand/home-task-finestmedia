package ui;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

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
import model.ConsumptionData;
import service.ConsumptionService;
import service.DataLoaderService;

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
		dataLoaderService.loadSeedData(Constant.URL);
		energyReport = ifinestService.findAllData();

		populateFormData(mainLayout, energyReport);

		add(mainLayout);
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

		inputTextField.addValueChangeListener(e -> {
			if (inputTextField.getValue() != null && !inputTextField.getValue().isEmpty()) {
				submitbtn.setEnabled(true);
			}
		});

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
			if (event.getValue() == null) {
				value.setText(date + Constant.NOT_SELECTED);
			} else {
				submitbtn.setEnabled(true);
			}
		});

		return datePicker;
	}

	private void reset() {
		inputTextField.setValue("");
		inputDatePicker.clear();
		addClassName(Constant.GRID_LIST_VIEW);
		grid.setItems(energyReport);
		submitbtn.setEnabled(true);

	}

	private Button createButton(String label) {
		Button logoutbtn = new Button(label);
		logoutbtn.addThemeName(Constant.BUTTON_THEME);
		return logoutbtn;
	}

	private void clickSubmitButton(DatePicker inputDatePicker, TextField inputTextField, Button submitbtn) {
		submitbtn.addClickListener(e -> {
			if (inputDatePicker.getValue() != null) {
				submitbtn.setVisible(true);
			}
			if (Double.parseDouble(inputTextField.getValue()) < 0
					|| Double.parseDouble(inputTextField.getValue()) > Constant.MAX_PRICE_VALUE) {
				createWarningDialogBox(inputTextField.getValue());
				inputTextField.clear();
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

	private Dialog createWarningDialogBox(String input) {
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(false);
		dialog.setCloseOnOutsideClick(false);
		dialog.add(Constant.INVALID_PRICE_INPUT + input);

		Button confirmButton = new Button("OK", event -> {
			dialog.close();
		});
		confirmButton.setWidthFull();

		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setAlignItems(Alignment.CENTER);
		verticalLayout.add(confirmButton);
		dialog.add(verticalLayout);
		dialog.open();
		return dialog;
	}
}
