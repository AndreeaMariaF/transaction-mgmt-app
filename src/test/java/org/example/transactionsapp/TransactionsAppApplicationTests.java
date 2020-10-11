package org.example.transactionsapp;

import org.example.transactionsapp.dto.TransactionDTO;
import org.example.transactionsapp.model.*;
import org.example.transactionsapp.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.*;
import java.time.*;
import java.util.*;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionsAppApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@LocalServerPort
	private int randomServerPort;

	@Test
	public void createAccountTest() throws Exception {
		HttpEntity<Account> request = new HttpEntity<>(getAccount());
		ResponseEntity<Account> response = testRestTemplate.postForEntity(getUri("/account/create"), request, Account.class);

		assertNotNull(accountRepository.findByOwnerName(getAccount().getOwnerName()));
		assertEquals(201, response.getStatusCodeValue());
	}

	@Test
	public void addTransactionTest() throws Exception {
		HttpEntity<TransactionDTO> requestTransactions = new HttpEntity<TransactionDTO>(buildTransactionRequestBody());
		ResponseEntity<TransactionDTO> response = testRestTemplate.postForEntity(getUri("/transactions/add"), requestTransactions, TransactionDTO.class);

		Account account = accountRepository.findByAccountNumber(buildTransactionRequestBody().getAccountNumber()).get();

		assertFalse(transactionRepository.findByAccount(account).isEmpty());
		assertEquals(201, response.getStatusCodeValue());
	}

	@Test
	public void deleteAccountTest() throws Exception {
		String accountNumber = accountRepository.findById(1L).get().getAccountNumber();
		testRestTemplate.delete(getUri("/account/delete/1"));
		List<Transaction> transactions = transactionRepository.findByAccountNumber(accountNumber);

		assertTrue(accountRepository.findById(1L).isEmpty());
		assertTrue(transactions.isEmpty());
	}

	@Test
	public void getTransactionsLastHours() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("accountNumber", buildTransactionRequestBody().getAccountNumber());
		HttpEntity<Transaction> requestTransactions = new HttpEntity<>(headers);

		Account account = accountRepository.findByAccountNumber(buildTransactionRequestBody().getAccountNumber()).get();
		Transaction transaction = transactionRepository.findByAccount(account).get(0);

		ResponseEntity<List<Transaction>> responseBeforeUpdate = testRestTemplate.exchange(getUri("/transactions/hours/3"), HttpMethod.GET, requestTransactions, new ParameterizedTypeReference<>() {});
		assertEquals(responseBeforeUpdate.getBody().size(),5);
		assertTrue(responseBeforeUpdate.getBody().stream().anyMatch(t -> t.getId() == transaction.getId()));

		transaction.setTransactionDate(Instant.now().minus(Duration.ofHours(4)));
		transactionRepository.save(transaction);

		ResponseEntity<List<Transaction>> responseAfterUpdate = testRestTemplate.exchange(getUri("/transactions/hours/3"), HttpMethod.GET, requestTransactions, new ParameterizedTypeReference<>() {});

		assertFalse(responseAfterUpdate.getBody().stream().anyMatch(t -> t.getId() == transaction.getId()));
		assertEquals(responseAfterUpdate.getBody().size(),4);
		assertEquals(200, responseBeforeUpdate.getStatusCodeValue());
	}

	@Test
	public void getTransactionsLastDays() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("accountNumber", buildTransactionRequestBody().getAccountNumber());
		HttpEntity<Transaction> requestTransactions = new HttpEntity<>(headers);

		Account account = accountRepository.findByAccountNumber(buildTransactionRequestBody().getAccountNumber()).get();
		Transaction transaction = transactionRepository.findByAccount(account).get(0);

		ResponseEntity<List<Transaction>> responseBeforeUpdate = testRestTemplate.exchange(getUri("/transactions/days/2"), HttpMethod.GET, requestTransactions, new ParameterizedTypeReference<>() {});
		assertEquals(responseBeforeUpdate.getBody().size(),5);
		assertTrue(responseBeforeUpdate.getBody().stream().anyMatch(t -> t.getId() == transaction.getId()));

		transaction.setTransactionDate(Instant.now().minus(Duration.ofDays(3)));
		transactionRepository.save(transaction);

		ResponseEntity<List<Transaction>> responseAfterUpdate = testRestTemplate.exchange(getUri("/transactions/days/2"), HttpMethod.GET, requestTransactions, new ParameterizedTypeReference<>() {});

		assertFalse(responseAfterUpdate.getBody().stream().anyMatch(t -> t.getId() == transaction.getId()));
		assertEquals(responseAfterUpdate.getBody().size(),4);
		assertEquals(200, responseBeforeUpdate.getStatusCodeValue());
	}

	private URI getUri(String mappedUri) throws URISyntaxException {
		final String baseUrl ="http://localhost:" + randomServerPort + mappedUri;
		return new URI(baseUrl);
	}

	private Account getAccount() {
		Account account = new Account();
		account.setOwnerName("gigelTest");
		account.setBalance(2000);
		return account;
	}

	private TransactionDTO buildTransactionRequestBody() {
		Optional<Account> account = accountRepository.findById(1L);

		TransactionDTO transaction = new TransactionDTO();
		transaction.setAccountNumber(account.get().getAccountNumber());
		transaction.setAmount(2000);
		transaction.setDescription("transactionTest");
		return transaction;
	}
}