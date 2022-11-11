package it.prova.municipioabitantespringdatamaven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import it.prova.municipioabitantespringdatamaven.model.Municipio;

public interface MunicipioRepository extends CrudRepository<Municipio, Long>,QueryByExampleExecutor <Municipio> {

	List<Municipio> findAllByDescrizioneContaining(String term);
	
	Municipio findByCodice(String codice);
	
	//c'è anche la possibilità di andare nelle 'nested' properties...MAGNIFICO
	//lo faccio eager per provare
	@EntityGraph(attributePaths = { "abitanti" })
	List<Municipio> findAllDistinctByAbitanti_EtaGreaterThan(int eta);

}
