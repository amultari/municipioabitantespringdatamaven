package it.prova.municipioabitantespringdatamaven.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.municipioabitantespringdatamaven.model.Abitante;
import it.prova.municipioabitantespringdatamaven.model.Municipio;

@Service
public class BatteriaDiTestService {

	@Autowired
	private MunicipioService municipioService;

	@Autowired
	private AbitanteService abitanteService;

	public void testInserisciNuovoMunicipio() {
		Long nowInMillisecondi = new Date().getTime();

		Municipio nuovoMunicipioIII = new Municipio("Municipio" + nowInMillisecondi, nowInMillisecondi.toString(),
				"Via dei " + nowInMillisecondi);
		if (nuovoMunicipioIII.getId() != null)
			throw new RuntimeException("testInserisciNuovoMunicipio...failed: transient object con id valorizzato");
		// salvo
		municipioService.inserisciNuovo(nuovoMunicipioIII);
		if (nuovoMunicipioIII.getId() == null || nuovoMunicipioIII.getId() < 1)
			throw new RuntimeException("testInserisciNuovoMunicipio...failed: inserimento fallito");

		System.out.println("testInserisciNuovoMunicipio........OK");
	}

	public void testRemoveConEccezioneVaInRollback() {
		Long nowInMillisecondi = new Date().getTime();
		// inserisco un paio di municipi
		Municipio nuovoMunicipio = new Municipio("Municipio" + nowInMillisecondi, nowInMillisecondi.toString(),
				"Via dei " + nowInMillisecondi);
		nowInMillisecondi++;
		Municipio nuovoMunicipio2 = new Municipio("Municipioasdfa" + nowInMillisecondi, nowInMillisecondi.toString(),
				"Via dei " + nowInMillisecondi);

		municipioService.inserisciNuovo(nuovoMunicipio);
		municipioService.inserisciNuovo(nuovoMunicipio2);

		if (nuovoMunicipio.getId() == null || nuovoMunicipio.getId() < 1 || nuovoMunicipio2.getId() == null
				|| nuovoMunicipio2.getId() < 1)
			throw new RuntimeException("testRemoveConEccezioneVaInRollback...failed: inserimento fallito");

		try {
			municipioService.removeConEccezione(nuovoMunicipio2);
			throw new RuntimeException("testRemoveConEccezioneVaInRollback...failed: eccezione non lanciata");
		} catch (Exception e) {
			// se passo di qui è tutto ok
		}

		if (nuovoMunicipio2 == null || nuovoMunicipio2.getId() == null)
			throw new RuntimeException(
					"testRemoveConEccezioneVaInRollback...failed: cancellazione avvenuta senza rollback");

		System.out.println("testRemoveConEccezioneVaInRollback........OK");
	}

	public void testInserisciAbitante() {
		Long nowInMillisecondi = new Date().getTime();
		Municipio nuovoMunicipio = new Municipio("Municipio" + nowInMillisecondi, nowInMillisecondi.toString(),
				"Via dei " + nowInMillisecondi);
		municipioService.inserisciNuovo(nuovoMunicipio);

		Abitante nuovoAbitante = new Abitante("Pluto", "Plutorum", 77, "Via " + nowInMillisecondi);
		nuovoAbitante.setMunicipio(nuovoMunicipio);
		// salvo abitante
		abitanteService.inserisciNuovo(nuovoAbitante);
		if (nuovoAbitante.getId() == null || nuovoAbitante.getId() < 1)
			throw new RuntimeException("testInserisciAbitante...failed: inserimento fallito");

		System.out.println("testInserisciAbitante........OK");
	}

	public void testCercaAbitantePerCognomeEager() {
		Long nowInMillisecondi = new Date().getTime();
		String cognomeToken = "Mariottoli";

		Municipio nuovoMunicipio = new Municipio("Municipio" + nowInMillisecondi, nowInMillisecondi.toString(),
				"Via dei " + nowInMillisecondi);
		municipioService.inserisciNuovo(nuovoMunicipio);

		// inserisco 5 abitanti usando i range degli stream
		IntStream.range(1, 6).forEach(i -> {
			abitanteService.inserisciNuovo(new Abitante("Pluto" + i, cognomeToken, 50 + i, "Via " + i, nuovoMunicipio));
		});

		// controllo che siano stati inseriti tutti e 5
		if (abitanteService.cercaPerCognomeEager(cognomeToken).size() != 5)
			throw new RuntimeException(
					"testCercaAbitantePerCognomeEager...failed: non tutti gli elementi sono stati trovati");

		System.out.println("testCercaAbitantePerCognomeEager........OK");
	}

	public void testCercaAbitantiPerNomeCheIniziaCon() {
		Long nowInMillisecondi = new Date().getTime();
		String nomeToken = "Abit";

		Municipio nuovoMunicipio = new Municipio("Municipio" + nowInMillisecondi, nowInMillisecondi.toString(),
				"Via dei " + nowInMillisecondi);
		municipioService.inserisciNuovo(nuovoMunicipio);

		// inserisco 4 abitanti che iniziano con il token + 4 no
		IntStream.range(1, 5).forEach(i -> {
			abitanteService
					.inserisciNuovo(new Abitante(nomeToken + i, "Rossi" + i, 50 + i, "Via " + i, nuovoMunicipio));
			abitanteService.inserisciNuovo(new Abitante("Anto" + i, "Bianchi" + i, 30 + i, "Via " + i, nuovoMunicipio));
		});

		// ora cerco tutti quelli che iniziano per Abit e il risultato deve essere 4
		if (!abitanteService.cercaPerNomeCheIniziaCon(nomeToken).stream()
				.allMatch(abitante -> abitante.getNome().startsWith(nomeToken)))
			throw new RuntimeException(
					"testCercaAbitantiPerNomeCheIniziaCon...failed: non iniziano tutti con: " + nomeToken);

		System.out.println("testCercaAbitantiPerNomeCheIniziaCon........OK");
	}

	public void testCercaTuttiIMunicipiConAbitantiConEtaMaggioreDi() {
		Long nowInMillisecondi = new Date().getTime();
		// devo usare un'eta esagerata per non andare ad includere i dati degli altri
		// test
		int etaToCheck = 400;

		// inserisco 4 municipi con due abitanti ciascuno con eta inferiore e 4 con due
		// abitanti con eta maggiore
		IntStream.range(1, 5).forEach(i -> {
			int etaToSet = i % 2 == 0 ? 300 : 500;
			Municipio nuovoMunicipio = new Municipio("Municipio" + i, nowInMillisecondi.toString(), "Via dei " + i);
			municipioService.inserisciNuovo(nuovoMunicipio);
			abitanteService
					.inserisciNuovo(new Abitante("Mario" + i, "Rossi" + i, etaToSet, "Via " + i, nuovoMunicipio));
			abitanteService
					.inserisciNuovo(new Abitante("Anto" + i, "Bianchi" + i, etaToSet, "Via " + i, nuovoMunicipio));
		});

		// devono essere 2 municipi
		List<Municipio> risultatiAttesi = municipioService.cercaTuttiIMunicipiConAbitantiConEtaMaggioreDi(etaToCheck);
		if (risultatiAttesi.size() != 2)
			throw new RuntimeException(
					"testCercaTuttiIMunicipiConAbitantiConEtaMaggioreDi...failed: non sono il numero previsto");

		// la riprova tramite gli stream: prima trasformo in lista di abitanti
		List<Abitante> listaAbitantiDaMunicipi = risultatiAttesi.stream()
				.flatMap(municipio -> municipio.getAbitanti().stream()).collect(Collectors.toList());
		// devono essere 4 abitanti
		if (listaAbitantiDaMunicipi.size() != 4)
			throw new RuntimeException(
					"testCercaTuttiIMunicipiConAbitantiConEtaMaggioreDi...failed: gli abitanti non sono il numero previsto");

		System.out.println("testCercaTuttiIMunicipiConAbitantiConEtaMaggioreDi........OK");
	}

	public void testCercaIPrimiTreConEtaInferioreA() {
		Long nowInMillisecondi = new Date().getTime();
		int etaToCheck = 30;

		// inserisco un municipio con 10 abitanti con eta inferiore a 30
		Municipio nuovoMunicipio = new Municipio("Municipio" + nowInMillisecondi, nowInMillisecondi.toString(),
				"Via dei " + nowInMillisecondi);
		municipioService.inserisciNuovo(nuovoMunicipio);
		IntStream.range(1, 11).forEach(i -> {
			abitanteService.inserisciNuovo(
					new Abitante("Matteo" + i, "Fiasconi" + i, etaToCheck - i, "Via " + i, nuovoMunicipio));
		});

		// se cerco by municipio devo ottenere esattamente 10 abitanti
		List<Abitante> listaAbitantiByMunicipio = abitanteService.cercaPerMunicipio(nuovoMunicipio);
		if (listaAbitantiByMunicipio.size() != 10)
			throw new RuntimeException(
					"testCercaIPrimiTreConEtaInferioreA...failed: gli abitanti non sono il numero previsto");

		// se provo il metodo che cerca i primi tre deve estrane solo tre
		if (abitanteService.cercaIPrimiTreAbitantiConEtaInferioreA(etaToCheck).size() != 3)
			throw new RuntimeException(
					"testCercaIPrimiTreConEtaInferioreA...failed: gli abitanti non sono il numero previsto");

		System.out.println("testCercaIPrimiTreConEtaInferioreA........OK");
	}

	public void testMunicipioFindByCodice() {
		Long nowInMillisecondi = new Date().getTime();
		final String CODICE_FISSO = "cod" + nowInMillisecondi;

		// inserisco il municipio
		Municipio nuovoMunicipio = new Municipio("Municipio" + nowInMillisecondi, CODICE_FISSO,
				"Via dei " + nowInMillisecondi);
		municipioService.inserisciNuovo(nuovoMunicipio);

		// ricarico
		Municipio municipioConCodiceFisso = municipioService.cercaPerCodice(CODICE_FISSO);
		if (municipioConCodiceFisso == null || !municipioConCodiceFisso.getId().equals(nuovoMunicipio.getId()))
			throw new RuntimeException("testMunicipioFindByCodice...failed: municipio con codice atteso non caricato");

		System.out.println("testMunicipioFindByCodice........OK");
	}

}
