package model;

import java.util.Optional;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AccountTimeSeries {
	private String AccountingPoint;

	private ConsumptionHistory ConsumptionHistory;

	private String MeasurementUnit;
}
