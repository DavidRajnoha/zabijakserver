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

			service.createGame("New Game 1", "MiroMiro", "Martin", "Dominik", "Stefan");
			service.assignTargets(1L);




			service.createGame("New Game 2", "MiroMiro", "Martin", "Dominik", "Stefan");
			service.assignTargets(2L);




			playerRepository.findAll().forEach(player -> {
				log.info(player.toString());
			});

			log.info("-------------------------------");


			service.killTarget(1L, 1);
			service.killTarget(1L, 1);
			service.killTarget(1L, 1);


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
