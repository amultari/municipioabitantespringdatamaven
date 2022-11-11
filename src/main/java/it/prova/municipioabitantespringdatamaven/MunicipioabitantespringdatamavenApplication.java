package it.prova.municipioabitantespringdatamaven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.municipioabitantespringdatamaven.service.BatteriaDiTestService;

@SpringBootApplication
public class MunicipioabitantespringdatamavenApplication implements CommandLineRunner{
	
	@Autowired
	private BatteriaDiTestService batteriaDiTestService;	

	public static void main(String[] args) {
		SpringApplication.run(MunicipioabitantespringdatamavenApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {

		System.out.println("################ START   #################");
		System.out.println("################ eseguo i test  #################");

		batteriaDiTestService.testInserisciNuovoMunicipio();
		batteriaDiTestService.testRemoveConEccezioneVaInRollback();
		batteriaDiTestService.testInserisciAbitante();
		batteriaDiTestService.testCercaAbitantePerCognomeEager();
		batteriaDiTestService.testCercaAbitantiPerNomeCheIniziaCon();
		batteriaDiTestService.testCercaTuttiIMunicipiConAbitantiConEtaMaggioreDi();
		batteriaDiTestService.testCercaIPrimiTreConEtaInferioreA();

		System.out.println("################ FINE   #################");
	}

}
