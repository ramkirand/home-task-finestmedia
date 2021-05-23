package validation;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class InputDateValidation {
  // TBD Need to implement start date and end date validation by user
  public InputDateValidation() {

  }

  public static boolean validate(LocalDate startDate, LocalDate endDate) {
    return false;

  }
}
