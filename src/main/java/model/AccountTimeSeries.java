package model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountTimeSeries {
  private String AccountingPoint;

  private ConsumptionHistory consumptionHistory;

  private String measurementUnit;
}
