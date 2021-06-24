package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;
import com.company.enroller.model.Meeting;
import com.company.enroller.persistence.MeetingService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@Autowired
	ParticipantService participantService;

	// GET all meetings
	// localhost:8080/meetings
	@RequestMapping(value = "", method = RequestMethod.GET)

	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	// GET specific meeting
	// localhost:8080/meetings/2
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)

	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity("There is no meeting with this id", HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	// POST create new meeting
	// localhost:8080/meetings
	@RequestMapping(value = "", method = RequestMethod.POST)

	public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {
		if (meetingService.findById(meeting.getId()) != null) {
			return new ResponseEntity<String>("Unable to create - meeting already exists", HttpStatus.CONFLICT);
		}
		meetingService.create(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}

	// POST add participant to meeting
	// localhost:8080/meetings/4/participants/user5
	@RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.POST)

	public ResponseEntity<?> addMeetingParticipant(@PathVariable("id") long id, @PathVariable("login") String login) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity("There is no meeting with this id", HttpStatus.NOT_FOUND);
		}
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity("There is no participant with this login", HttpStatus.NOT_FOUND);
		}
		meeting.addParticipant(participant);
		meetingService.update(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	// GET all participants from meeting
	// localhost:8080/meetings/2/participants
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<String>("There is no meeting with this id", HttpStatus.NOT_FOUND);
		}
		Collection<Participant> participants = meeting.getParticipants();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	// DELETE specific meeting
	// localhost:8080/meetings/4
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)

	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity("Unable to delete meeting - does not exist", HttpStatus.NOT_FOUND);
		}
		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(HttpStatus.NO_CONTENT);
	}

	// PUT update specific meeting
	// localhost:8080/meetings
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)

	public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting meeting) {
		Meeting foundMeeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		foundMeeting.setTitle(meeting.getTitle());
		foundMeeting.setDescription(meeting.getDescription());
		foundMeeting.setDate(meeting.getDate());
		meetingService.update(foundMeeting);
		return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
	}

	// DELETE participant from meeting
	// localhost:8080/meetings/4/participants/user5
	@RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)

	public ResponseEntity<?> removeMeetingParticipant(@PathVariable("id") long id,
			@PathVariable("login") String login) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity("There is no meeting with this id", HttpStatus.NOT_FOUND);
		}
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity("There is no participant with this login", HttpStatus.NOT_FOUND);
		}
		meeting.removeParticipant(participant);
		meetingService.update(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
}
