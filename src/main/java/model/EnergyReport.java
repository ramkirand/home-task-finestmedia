package model;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Component
public class EnergyReport {

	private String DocumentIdentification;

	private String DocumentDateTime;

	private model.AccountTimeSeries AccountTimeSeries;
	private String price;

}
