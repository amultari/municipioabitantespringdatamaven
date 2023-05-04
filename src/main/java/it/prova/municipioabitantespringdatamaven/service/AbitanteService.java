package it.prova.municipioabitantespringdatamaven.service;

import java.util.List;

import it.prova.municipioabitantespringdatamaven.model.Abitante;
import it.prova.municipioabitantespringdatamaven.model.Municipio;

public interface AbitanteService {
	
	public List<Abitante> listAllAbitanti();

	public Abitante caricaSingoloAbitante(Long id);

	public void aggiorna(Abitante abitanteInstance);

	public void inserisciNuovo(Abitante abitanteInstance);

	public void rimuovi(Long idAbitante);

	public List<Abitante> findByExample(Abitante example);
	
	public List<Abitante> findByExample2(Abitante example);
	
	public List<Abitante> findByNome(String nameInput);
	
	public List<Abitante> cercaAbitantiConEtaMaggioreDi(int etaInput);
	
	public List<Abitante> cercaAbitantiPerNomeAndEta(String nomeInput, int etaInput);
	
	public List<Abitante> cercaAbitantiByEtaOrdinaPerNomeDesc(int eta);
	
	public List<Abitante> cercaPerNomeCheIniziaCon(String tokenIniziale);
	
	public List<Abitante> cercaPerNomeCheIniziaConFattoSenzaJpql(String tokenIniziale);
	
	public List<Abitante> cercaPerCognomeEager(String cognomeInput);
	
	public List<Abitante> cercaPerMunicipio(Municipio municipioInput);
	
	public List<Abitante> cercaIPrimiTreAbitantiConEtaInferioreA(int etaInput);
	
	
}
