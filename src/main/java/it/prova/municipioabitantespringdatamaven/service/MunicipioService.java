package it.prova.municipioabitantespringdatamaven.service;

import java.util.List;

import it.prova.municipioabitantespringdatamaven.model.Municipio;

public interface MunicipioService {
	
	public List<Municipio> listAllMunicipi() ;

	public Municipio caricaSingoloMunicipio(Long id);

	public void aggiorna(Municipio municipioInstance);

	public void inserisciNuovo(Municipio municipioInstance);

	public void rimuovi(Long idMunicipio);

	public List<Municipio> findByExample(Municipio example);
	
	public List<Municipio> cercaByDescrizioneILike(String term);
	
	public void removeConEccezione(Municipio municipioInstance);
	
	public List<Municipio> cercaTuttiIMunicipiConAbitantiConEtaMaggioreDi(int etaInput);
	
	public Municipio cercaPerCodice(String codice);

}
