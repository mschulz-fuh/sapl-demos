package org.demo.domain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DemoData implements CommandLineRunner {

	public static final String ROLE_DOCTOR = "DOCTOR";
	public static final String ROLE_NURSE = "NURSE";
	public static final String ROLE_VISITOR = "VISITOR";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String NAME_DOMINIC = "Dominic";
	public static final String NAME_JULIA = "Julia";
	public static final String NAME_PETER = "Peter";
	public static final String NAME_ALINA = "Alina";
	public static final String NAME_THOMAS = "Thomas";
	public static final String NAME_BRIGITTE = "Brigitte";
	public static final String NAME_JANOSCH = "Janosch";
	public static final String NAME_JANINA = "Janina";
	public static final String NAME_LENNY = "Lenny";
	public static final String NAME_KARL = "Karl";
	public static final String NAME_HORST = "Horst";
	public static final String DEFAULT_RAW_PASSWORD = "password";

	private final PatientRepository patientRepository;
	private final RelationRepository relationRepository;

	@Override
	public void run(String... args) throws Exception {

		LOGGER.info("Loading demo dataset.");

		// Create patients
		patientRepository.save(new Patient(null, NAME_LENNY, "Duodenal ulcer with acute haemorrhage.", "123456",
				"DA63.Z/ME24.90", "+78(0)456-789", NAME_JULIA, NAME_THOMAS, "A.3.47"));
		patientRepository.save(new Patient(null, NAME_KARL, "Type 2 diabetes mellitus", "987654", "9B71.0Z/5A11",
				"+78(0)456-567", NAME_ALINA, NAME_JANINA, "C.2.23"));

		// Establish relations between users and patients
		relationRepository.save(new Relation(NAME_DOMINIC, patientRepository.findByName(NAME_LENNY).get().getId()));
		relationRepository.save(new Relation(NAME_JULIA, patientRepository.findByName(NAME_KARL).get().getId()));
		relationRepository.save(new Relation(NAME_ALINA, patientRepository.findByName(NAME_KARL).get().getId()));
		relationRepository.save(new Relation(NAME_JANOSCH, patientRepository.findByName(NAME_KARL).get().getId()));
	}

	public static void loadUsers(InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inmen,
			PasswordEncoder encoder) {
		inmen.withUser(NAME_DOMINIC).password(encoder.encode(DEFAULT_RAW_PASSWORD)).roles(ROLE_VISITOR);
		inmen.withUser(NAME_JULIA).password(encoder.encode(DEFAULT_RAW_PASSWORD)).roles(ROLE_DOCTOR);
		inmen.withUser(NAME_PETER).password(encoder.encode(DEFAULT_RAW_PASSWORD)).roles(ROLE_DOCTOR);
		inmen.withUser(NAME_ALINA).password(encoder.encode(DEFAULT_RAW_PASSWORD)).roles(ROLE_DOCTOR);
		inmen.withUser(NAME_THOMAS).password(encoder.encode(DEFAULT_RAW_PASSWORD)).roles(ROLE_NURSE);
		inmen.withUser(NAME_BRIGITTE).password(encoder.encode(DEFAULT_RAW_PASSWORD)).roles(ROLE_NURSE);
		inmen.withUser(NAME_JANOSCH).password(encoder.encode(DEFAULT_RAW_PASSWORD)).roles(ROLE_NURSE);
		inmen.withUser(NAME_JANINA).password(encoder.encode(DEFAULT_RAW_PASSWORD)).roles(ROLE_ADMIN);
	}

}
