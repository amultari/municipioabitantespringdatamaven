package it.prova.municipioabitantespringdatamaven.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.municipioabitantespringdatamaven.model.Abitante;
import it.prova.municipioabitantespringdatamaven.model.Municipio;
import it.prova.municipioabitantespringdatamaven.repository.AbitanteRepository;

@Service
public class AbitanteServiceImpl implements AbitanteService {

	@Autowired
	private AbitanteRepository abitanteRepository;

	// questo mi serve per il findByExample2 che risulta 'a mano'
	// o comunque in tutti quei casi in cui ho bisogno di costruire custom query nel
	// service
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional(readOnly = true)
	public List<Abitante> listAllAbitanti() {
		return (List<Abitante>) abitanteRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Abitante caricaSingoloAbitante(Long id) {
		return abitanteRepository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Abitante abitanteInstance) {
		abitanteRepository.save(abitanteInstance);
	}

	@Transactional
	public void inserisciNuovo(Abitante abitanteInstance) {
		abitanteRepository.save(abitanteInstance);
	}

	@Transactional
	public void rimuovi(Long idAbitante) {
		abitanteRepository.deleteById(idAbitante);
	}

	@Transactional(readOnly = true)
	public List<Abitante> findByExample(Abitante exampleInput) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING); // Match string
																										// containing
																										// pattern
		// .withIgnoreCase();
		return (List<Abitante>) abitanteRepository.findAll(Example.of(exampleInput, matcher));
	}

	// nel caso si volesse fare una query particolare nel service...
	@Transactional(readOnly = true)
	public List<Abitante> findByExample2(Abitante example) {

		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select a from Abitante a where a.id = a.id ");

		if (StringUtils.isNotEmpty(example.getNome())) {
			whereClauses.add(" a.nome  like :nome ");
			paramaterMap.put("nome", "%" + example.getNome() + "%");
		}
		if (StringUtils.isNotEmpty(example.getCognome())) {
			whereClauses.add(" a.cognome like :cognome ");
			paramaterMap.put("cognome", "%" + example.getCognome() + "%");
		}
		if (StringUtils.isNotEmpty(example.getResidenza())) {
			whereClauses.add(" a.residenza like :residenza ");
			paramaterMap.put("residenza", "%" + example.getResidenza() + "%");
		}
		if (example.getEta() != null && example.getEta() > 0) {
			whereClauses.add(" a.eta >= :eta ");
			paramaterMap.put("eta", example.getEta());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Abitante> typedQuery = entityManager.createQuery(queryBuilder.toString(), Abitante.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();

	}

	@Transactional(readOnly = true)
	public List<Abitante> findByNome(String nameInput) {
		return abitanteRepository.findByNome(nameInput);
	}

	// SE NON USO @TRANSACTIONAL DIVENTA UN METODO COMUNQUE DI SOLA LETTURA
	@Override
	public List<Abitante> cercaAbitantiConEtaMaggioreDi(int etaInput) {
		return abitanteRepository.findByEtaGreaterThan(etaInput);
	}

	@Override
	public List<Abitante> cercaAbitantiByEtaOrdinaPerNomeDesc(int eta) {
		return abitanteRepository.findByEtaOrderByNomeDesc(eta);
	}

	@Override
	public List<Abitante> cercaPerNomeCheIniziaCon(String tokenIniziale) {
		return abitanteRepository.findByNomeStartsWith(tokenIniziale);
	}

	@Override
	public List<Abitante> cercaPerNomeCheIniziaConFattoSenzaJpql(String tokenIniziale) {
		return abitanteRepository.findByNomeCheIniziaPer(tokenIniziale);
	}

	@Override
	public List<Abitante> cercaAbitantiPerNomeAndEta(String nomeInput, int etaInput) {
		return abitanteRepository.findByNomeAndEta(nomeInput, etaInput);
	}

	@Override
	public List<Abitante> cercaPerCognomeEager(String cognomeInput) {
		return abitanteRepository.findByCognome(cognomeInput);
	}

	@Override
	public List<Abitante> cercaIPrimiTreAbitantiConEtaInferioreA(int etaInput) {
		return abitanteRepository.readTop3ByEtaLessThan(etaInput);
	}

	@Override
	public List<Abitante> cercaPerMunicipio(Municipio municipioInput) {
		return abitanteRepository.searchByMunicipio(municipioInput);
	}

}
