package com.company.enroller.persistence;

import java.util.Collection;

import javax.transaction.Transaction;

import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		return connector.getSession().createCriteria(Participant.class).list();
	}

	public Participant findByLogin(String login) {
		Participant participant = (Participant) connector.getSession().get(Participant.class, login);
		return participant;
	}

	public void add(Participant participant) {
		org.hibernate.Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
	}

	public void delete(Participant participant) {
		org.hibernate.Transaction transaction = this.connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();		
	}
}
