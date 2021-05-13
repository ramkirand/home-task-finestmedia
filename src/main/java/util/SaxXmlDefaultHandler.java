package util;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.AccountTimeSeries;
import model.ConsumptionHistory;
import model.EnergyReport;
import model.HourConsumption;

public class SaxXmlDefaultHandler extends DefaultHandler {
	private static final String HOUR_CONSUMPTION = "HourConsumption";
	private static final String ACCOUNTING_POINT = "AccountingPoint";
	private static final String MEASUREMENT_UNIT = "MeasurementUnit";
	private static final String DOCUMENT_DATE_TIME = "DocumentDateTime";
	private static EnergyReport energyReport = null;
	private static AccountTimeSeries accountTimeSeries = null;
	boolean documentIdentification = false;
	boolean documentDate = false;
	boolean measurementUnit = false;
	boolean accountingPoint = false;
	boolean hourConsumption = false;
	HourConsumption hc;
	String ts = null;
	List<HourConsumption> hourConsumptions = new ArrayList<HourConsumption>();

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("DocumentIdentification")) {
			documentIdentification = true;

		} else if (qName.equals(DOCUMENT_DATE_TIME)) {
			documentDate = true;

		} else if (qName.equals(MEASUREMENT_UNIT)) {
			measurementUnit = true;

		} else if (qName.equals(ACCOUNTING_POINT)) {
			accountingPoint = true;

		} else if (qName.equals(HOUR_CONSUMPTION)) {
			ts = attributes.getValue("ts");
			hourConsumption = true;

		}

	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (energyReport == null)
			energyReport = new EnergyReport();
		if (accountTimeSeries == null)
			accountTimeSeries = new AccountTimeSeries();

		if (documentIdentification) {
			String documentId = new String(ch, start, length);
			energyReport.setDocumentIdentification(documentId);
			documentIdentification = false;
		} else if (documentDate) {
			String document = new String(ch, start, length);
			energyReport.setDocumentDateTime(document);

			documentDate = false;
		} else if (measurementUnit || accountingPoint || hourConsumption) {
			if (measurementUnit) {
				String measureUnit = new String(ch, start, length);
				accountTimeSeries.setMeasurementUnit(measureUnit);
				measurementUnit = false;
			} else if (accountingPoint) {
				String accountingPt = new String(ch, start, length);
				accountTimeSeries.setAccountingPoint(accountingPt);
				accountingPoint = false;
			}
			energyReport.setAccountTimeSeries(accountTimeSeries);

			if (hourConsumption) {
				hc = new HourConsumption();
				hc.setTs(ts);
				hc.setContent(new String(ch, start, length));
				hourConsumptions.add(hc);
				ConsumptionHistory chistory = new ConsumptionHistory();
				chistory.setHourConsumption(hourConsumptions);
				energyReport.getAccountTimeSeries().setConsumptionHistory(chistory);
				hourConsumption = false;
				ts = null;

			}

		}

	}

	@Override
	public void endDocument() throws SAXException {
		hourConsumptions.add(hc);
	}

	public static EnergyReport getEnergyReport() {
		return energyReport;
	}
}
