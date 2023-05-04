package it.prova.municipioabitantespringdatamaven.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import it.prova.municipioabitantespringdatamaven.model.Abitante;
import it.prova.municipioabitantespringdatamaven.model.Municipio;

public interface AbitanteRepository extends CrudRepository<Abitante, Long>, QueryByExampleExecutor<Abitante> {

	// la query viene costruita in automatico!!!
	List<Abitante> findByNome(String name);

	// Anche questa!!!
	List<Abitante> findByEtaGreaterThan(int etaInput);

	// Combinazione di campi!!! (Come i dynamic finders)
	List<Abitante> findByNomeAndEta(String nome, int eta);

	// Order by!!!!
	List<Abitante> findByEtaOrderByNomeDesc(int eta);

	// si può usare anche read o query o search al posto di find e si possono usare
	// anche
	// first e top per limitare i risultati
	// es. voglio i primi 3 abitanti con età inferiore al parametro
	List<Abitante> readTop3ByEtaLessThan(int etaInput);

	List<Abitante> searchByMunicipio(Municipio municipioInput);

	// se voglio usare il like %
	List<Abitante> findByNomeStartsWith(String token);

	// posso scrivere lo stesso metodo scrivendomi la query a mano
	@Query("from Abitante p where p.nome like ?1%")
	List<Abitante> findByNomeCheIniziaPer(String token);

	// se voglio un caricamento EAGER per esempio by cognome
	// anche se i caricamenti EAGER conviene scriverli in JPQL
	@EntityGraph(attributePaths = { "municipio" })
	List<Abitante> findByCognome(String cognome);

}
