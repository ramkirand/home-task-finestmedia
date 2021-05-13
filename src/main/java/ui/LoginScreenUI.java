package ui;
/*
 D Rama Kiron
 */
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("Login")
@PageTitle("Log in")
@StyleSheet("frontend://styles/shared-styles.css")
public class LoginScreenUI extends FlexLayout {

	private static final String JOHN = "john";
	private static final String LOGIN_SCREEN = "login-screen";
	private static final String LOGIN_INFORMATION = "login-information";
	private static final String THIS_DASH_BOARD_UI_TOOL_CAN_BE_USED_TO_MONITOR_THE_ENERGY_COMPTION_FOR_SPECIFIC_DATE_RANGE = "This dash board UI tool can be used to monitor the energy consumption with respect to price for a specific.";
	private static final String ENERGY_CONSUMPTION_DASHBOARD = "Energy Consumption Dashboard";
	private static final long serialVersionUID = 1L;

	public LoginScreenUI() {
		buildUI();
	}

	private void buildUI() {
		setSizeFull();
		setClassName(LOGIN_SCREEN);
		LoginForm loginForm = new LoginForm();
		loginForm.addLoginListener(this::login);
		loginForm.addForgotPasswordListener(event -> Notification.show("Hint: same as username"));

		// layout to center login form when there is sufficient screen space
		FlexLayout centeringLayout = new FlexLayout();
		centeringLayout.setSizeFull();
		centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		centeringLayout.setAlignItems(Alignment.CENTER);
		centeringLayout.add(loginForm);

		// information text about logging in
		Component loginInformation = buildLoginInformation();

		add(loginInformation);
		add(centeringLayout);
	}

	private Component buildLoginInformation() {
		VerticalLayout loginInformation = new VerticalLayout();
		loginInformation.setAlignItems(Alignment.CENTER);
		loginInformation.setClassName(LOGIN_INFORMATION);
		H1 loginInfoHeader = new H1(ENERGY_CONSUMPTION_DASHBOARD);
		Span loginInfoText = new Span(
				THIS_DASH_BOARD_UI_TOOL_CAN_BE_USED_TO_MONITOR_THE_ENERGY_COMPTION_FOR_SPECIFIC_DATE_RANGE);
		loginInformation.add(loginInfoHeader);
		loginInformation.add(loginInfoText);
		return loginInformation;
	}

	private void login(LoginForm.LoginEvent event) {
		if (event.getUsername().equals(JOHN) && event.getPassword().equals(JOHN)) {
			getUI().ifPresent(ui -> ui.navigate("Main"));
		} else {
			event.getSource().setError(true);
		}
	}
}
