package com.leandrosps.demo_sell_ecom.application.services;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.leandrosps.demo_sell_ecom.domain.Client;
import com.leandrosps.demo_sell_ecom.infra.db.ClientRepository;
import com.leandrosps.demo_sell_ecom.infra.db.UserRepository;
import com.leandrosps.demo_sell_ecom.infra.errors.NotFoundEx;

@Service
public class ClientService {

	private ClientRepository clientRepository;
	private UserRepository userRepository;

	public ClientService(ClientRepository clientRepository, UserRepository userRepository) {
		this.clientRepository = clientRepository;
		this.userRepository = userRepository;
	}

	public record CreateInput(String name, String email, String password, String city, LocalDate birthday) {
	}

	public void create(CreateInput input) {
		this.userRepository.findByEmail(input.email())
				.orElseThrow(() -> new NotFoundEx("This client has not been assing as an user!"));

		Client client = Client.create(input.name(), input.email(), input.password(), input.city(), input.birthday());

		this.clientRepository.persiste(client);
	}
}
