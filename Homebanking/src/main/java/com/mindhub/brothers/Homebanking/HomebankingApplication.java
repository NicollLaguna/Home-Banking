package com.mindhub.brothers.Homebanking;

import com.mindhub.brothers.Homebanking.models.*;
import com.mindhub.brothers.Homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository , AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository,CardRepository cardRepository){
		return (args) -> {
			Client admin = new Client("Admin","Admin","admin@hopenedBank.com",passwordEncoder.encode("9999"));
			clientRepository.save(admin);
			Client client1 = new Client("Melba","Morel","melba@mindhub.com", passwordEncoder.encode("0000"));
			clientRepository.save(client1);

			Account account1 = new Account(5000, "VIN001", LocalDateTime.now());
			Account account2 = new Account(7500, "VIN002", LocalDateTime.now().plusDays(1));

			client1.addAccount(account1);
			client1.addAccount(account2);

			Transaction transaction1 = new Transaction(1000, "debit of Melba",LocalDateTime.now(), TransactionType.DEBIT);
			Transaction transaction2 = new Transaction(50568, "credit of Melba", LocalDateTime.now().plusDays(2).plusHours(5), TransactionType.CREDIT);
			Transaction transaction3 = new Transaction(3550, "debit of Melba",LocalDateTime.now().plusDays(10).plusHours(4), TransactionType.DEBIT);
			Transaction transaction4 = new Transaction(8690, "credit of Melba", LocalDateTime.now(), TransactionType.CREDIT);

			accountRepository.save(account1);
			accountRepository.save(account2);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);

			accountRepository.save(account1);
			accountRepository.save(account2);


			Client client2 = new Client("Luisa", "Mendoza", "luisa@gmail.com", passwordEncoder.encode("1503" ));
			clientRepository.save(client2);

			Account account3 = new Account(4500, "VIN003",LocalDateTime.now());
			Account account4 = new Account(8200, "VIN004", LocalDateTime.now().plusDays(2));

			client2.addAccount(account3);
			client2.addAccount(account4);

			Transaction transaction5 = new Transaction(15030, "debit of Luisa", LocalDateTime.now().plusDays(6).plusHours(8),TransactionType.DEBIT);
			Transaction transaction6 = new Transaction(2500, "credit of Luisa", LocalDateTime.now(), TransactionType.CREDIT);
			Transaction transaction7 = new Transaction(1569, "credit of Luisa", LocalDateTime.now().plusDays(2).plusHours(6), TransactionType.CREDIT);
			Transaction transaction8 = new Transaction(69015, "debit of Luisa", LocalDateTime.now(), TransactionType.DEBIT);

			accountRepository.save(account3);
			accountRepository.save(account4);

			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account4.addTransaction(transaction7);
			account4.addTransaction(transaction8);

			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);

			accountRepository.save(account3);
			accountRepository.save(account4);

			Loan loan1 = new Loan("Mortgage",500000, Set.of(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal",100000, Set.of(6,12,24));
			Loan loan3 = new Loan("Automotive",300000,Set.of(6,12,24,36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);


			ClientLoan clientLoan = new ClientLoan(400000,60,"Mortgage");
			ClientLoan clientLoan2 = new ClientLoan(50000,12,"Personal");

			client1.addClientLoan(clientLoan);
			loan1.addClientLoan(clientLoan);
			client1.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan2);

			clientLoanRepository.save(clientLoan);
			clientLoanRepository.save(clientLoan2);

			ClientLoan clientLoan3 = new ClientLoan(100000,24,"Personal");
			ClientLoan clientLoan4 = new ClientLoan(200000,36,"Automotive");

			client2.addClientLoan(clientLoan3);
			loan2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			loan3.addClientLoan(clientLoan4);

			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card = new Card(CardType.DEBIT,CardColor.GOLD,"1234-5556-7890-0102", LocalDate.now(),LocalDate.now().plusYears(5),client1.getFirstName()+' '+client1.getLastName(),990);
			client1.addCards(card);

			Card card2 = new Card(CardType.CREDIT,CardColor.TITANIUM,"0501-2530-3008-2003",LocalDate.now(),LocalDate.now().plusYears(5),client1.getFirstName()+' '+client1.getLastName(),750);
			client1.addCards(card2);

			Card card3 = new Card(CardType.CREDIT, CardColor.SILVER,"1503-2023-0110-3008", LocalDate.now(), LocalDate.now().plusYears(5),client2.getFirstName()+' '+client2.getLastName(),690);
			client2.addCards(card3);

			cardRepository.save(card);
			cardRepository.save(card2);
			cardRepository.save(card3);
		};
}}
