package service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import annotation.TrackExecutionTime;
import lombok.extern.slf4j.Slf4j;
import model.ConsumptionData;
import repository.EnergyReportRepository;

@Slf4j
@Component
public class ConsumptionServiceImpl implements ConsumptionService {
	@Autowired
	private EnergyReportRepository energyReportRepository;

	@Override
	public List<ConsumptionData> findAllData() {
		return (List<ConsumptionData>) energyReportRepository.findAll();
	}

	@Override
	@TrackExecutionTime
	@Cacheable(value = "consumptionCache", key = "#measurmentPrice")
	public List<ConsumptionData> findByMeasurmentPrice(String startDate, String measurmentPrice) {

		List<ConsumptionData> consumptionDatas = energyReportRepository.findByMeasurmentPrice(measurmentPrice);
		log.info("consumptionDatas:" + consumptionDatas.size());
		List<ConsumptionData> filteredConsumptionDatas = new ArrayList<ConsumptionData>();
		for (ConsumptionData consumptionData : consumptionDatas) {
			String currentDate = consumptionData.getDocumentDateTime().substring(0, 4);
			if (currentDate.equals(startDate.substring(0, 4))) {
				filteredConsumptionDatas.add(consumptionData);
			}

		}
		log.info("filteredConsumptionDatas:" + filteredConsumptionDatas.size()
				+ ", Retrieving from Database for Input Date: " + startDate);
		return filteredConsumptionDatas;
	}
}
