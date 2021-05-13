package consumption.energy.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import model.ConsumptionData;
import repository.EnergyReportRepository;
import service.ConsumptionService;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
public class ConsumerServiceTest {

	@Mock
	private EnergyReportRepository energyReportRepositoryMock;

	@MockBean
	ConsumptionService consumptionService;

	@Test
	public void shouldfindByMeasurmentPrice() {
		List<ConsumptionData> consumptionDatas = new ArrayList<ConsumptionData>();
		ConsumptionData consumptionData = buildConsumptionData();
		when(energyReportRepositoryMock.findByMeasurmentPrice("measurmentPrice")).thenReturn(consumptionDatas);

		List<ConsumptionData> ans = consumptionService.findByMeasurmentPrice(consumptionData.getDocumentDateTime(),
				consumptionData.getMeasurmentPrice());
		assertTrue(ans.size() == consumptionDatas.size());

	}

	@Test
	public void shouldfindAllData() {
		List<ConsumptionData> consumptionDatas = new ArrayList<ConsumptionData>();
		when(energyReportRepositoryMock.findAll()).thenReturn(consumptionDatas);
		List<ConsumptionData> ans = consumptionService.findAllData();
		assertTrue(ans.size() == consumptionDatas.size());

	}

	private ConsumptionData buildConsumptionData() {
		ConsumptionData consumptionData = new ConsumptionData();
		consumptionData.setAccountingPoint("12345");
		consumptionData.setDocumentDateTime("2021-04-25T01:00:00+03:00");

		consumptionData.setDocumentIdentification("1620918872");

		consumptionData.setMeasurementUnit("KWH");
		consumptionData.setMeasurmentPrice("0.12");
		return consumptionData;
	}
}
