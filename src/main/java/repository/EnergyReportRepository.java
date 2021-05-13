package repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import model.ConsumptionData;
import model.EnergyReport;

@Repository
public interface EnergyReportRepository extends CrudRepository<ConsumptionData, Integer> {

	void save(EnergyReport energyReport);

	
	List<ConsumptionData> findByMeasurmentPrice( String measurmentPrice);
}
