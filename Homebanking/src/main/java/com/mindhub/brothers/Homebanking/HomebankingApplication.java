package com.mindhub.brothers.Homebanking;

import com.mindhub.brothers.Homebanking.models.Account;
import com.mindhub.brothers.Homebanking.models.Client;
import com.mindhub.brothers.Homebanking.models.Transaction;
import com.mindhub.brothers.Homebanking.models.TransactionType;
import com.mindhub.brothers.Homebanking.repositories.AccountRepository;
import com.mindhub.brothers.Homebanking.repositories.ClientRepository;
import com.mindhub.brothers.Homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository repository , AccountRepository repository2, TransactionRepository repository3){
		return (args) -> {
			Client client1 = new Client("Melba","Morel","melba@mindhub.com");
			Client client2 = new Client("Luisa", "Mendoza", "luisa@gmail.com");
			repository.save(client1);
			repository.save(client2);

			Account account1 = new Account(5000, "VIN001", LocalDateTime.now(),client1);
			Account account2 = new Account(7500, "VIN002", LocalDateTime.now().plusDays(1),client1);
			repository2.save(account1);
			repository2.save(account2);

			Account account3 = new Account(4500, "VIN001",LocalDateTime.now(),client2);
			Account account4 = new Account(8200, "VIN002", LocalDateTime.now().plusDays(2), client2);
			repository2.save(account3);
			repository2.save(account4);


			Transaction transaction1 = new Transaction(1000, "debit of Melba",LocalDateTime.now(),account1, TransactionType.DEBIT);
			Transaction transaction2 = new Transaction(50568, "credit of Melba", LocalDateTime.now().plusDays(2).plusHours(5), account1, TransactionType.CREDIT);
			Transaction transaction3 = new Transaction(3550, "debit of Melba",LocalDateTime.now().plusDays(10).plusHours(4),account2, TransactionType.DEBIT);
			Transaction transaction4 = new Transaction(8690, "credit of Melba", LocalDateTime.now(), account2, TransactionType.CREDIT);
			repository3.save(transaction1);
			repository3.save(transaction2);
			repository3.save(transaction3);
			repository3.save(transaction4);

			Transaction transaction5 = new Transaction(15030, "debit of Luisa", LocalDateTime.now().plusDays(6).plusHours(8), account3,TransactionType.DEBIT);
			Transaction transaction6 = new Transaction(2500, "credit of Luisa", LocalDateTime.now(), account4, TransactionType.CREDIT);
			Transaction transaction7 = new Transaction(1569, "credit of Luisa", LocalDateTime.now().plusDays(2).plusHours(6), account3, TransactionType.CREDIT);
			Transaction transaction8 = new Transaction(69015, "debit of Luisa", LocalDateTime.now(), account4, TransactionType.DEBIT);
			repository3.save(transaction5);
			repository3.save(transaction6);
			repository3.save(transaction7);
			repository3.save(transaction8);
		};
}}
