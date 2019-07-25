package com.example.zabijakserver;

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
			service.addPlayer("Tom", 0L, 1L);
			service.addPlayer("David", 1L, 2L);
			service.addPlayer("Petr", 2L, 3L);
			service.addPlayer("Marta", 3L, 0L);



			log.info("-------------------------------");

			service.killTarget(1L);

			log.info("-------------------------------");

			killLogRepository.findByKillerId(1L).forEach(killLog -> {
				log.info(killLog.toString());
			});

			log.info("-------------------------------");

			service.createGame("New game" , new Player("Miro", 4L, 5L),
										 new Player("Michal", 5L, 6L),
					 					 new Player("Jakub", 6L, 4L)
			);

			service.killTarget(4L);
			service.killTarget(4L);

			service.createGame("New Game 2", "MiroMiro", "Martin", "Dominik", "Stefan");




			playerRepository.findAll().forEach(player -> {
				log.info(player.toString());
			});

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
