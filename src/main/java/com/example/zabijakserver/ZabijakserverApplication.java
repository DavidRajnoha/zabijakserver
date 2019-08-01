package com.example.zabijakserver;

import com.example.zabijakserver.Entities.Player;
import com.example.zabijakserver.Repositories.GameRepository;
import com.example.zabijakserver.Repositories.KillLogRepository;
import com.example.zabijakserver.Repositories.PlayerRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class ZabijakserverApplication {

	private static final Logger log = LoggerFactory.getLogger(ZabijakserverApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ZabijakserverApplication.class, args);
	}

	@Autowired
	private KillLogRepository killLogRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Bean
	public CommandLineRunner demo(PlayerService service){
		return (args) -> {

			Long tokenOne = service.createGame("New Game 1").getToken();
			service.addPlayer(tokenOne, "Miro");
			service.addPlayer(tokenOne, "Martin");
			service.addPlayer(tokenOne, "Filip");
			service.addPlayer(tokenOne, "Dominik");

			service.assignTargets(tokenOne);




			Long tokenTwo = service.createGame("New Game 2").getToken();
			Long petrToken = service.addPlayer(tokenTwo, "Petr").getToken();
			service.addPlayer(tokenTwo, "Stefan");
			service.addPlayer(tokenTwo, "Milan");
			service.addPlayer(tokenTwo, "Jeroným");
			service.assignTargets(tokenTwo);




			playerRepository.findAll().forEach(player -> {
				log.info(player.toString());
			});

			log.info("-------------------------------");
			service.killTarget(petrToken);
			service.killTarget(petrToken);
			service.killTarget(petrToken);
			log.info("-------------------------------");


			gameRepository.findAll().forEach(game -> {
				log.info(game.toString());
			});

			log.info("-------------------------------");


			playerRepository.findByGame_Id(2L).forEach(player -> {
				log.info(player.toString());
			});

			log.info("-------------------------------");



			killLogRepository.findAll().forEach(killLog -> {
				log.info(killLog.toString());
			});

			log.info("-------------------------------");


			killLogRepository.findByGame_Id(1L).forEach(killLog -> {
				log.info(killLog.toString());
			});

		};
	}
}
