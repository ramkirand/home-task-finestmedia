package model;

import org.springframework.stereotype.Component;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Component
public class EnergyReport {

  private String documentIdentification;

  private String documentDateTime;

  private model.AccountTimeSeries accountTimeSeries;
  private String price;

}
