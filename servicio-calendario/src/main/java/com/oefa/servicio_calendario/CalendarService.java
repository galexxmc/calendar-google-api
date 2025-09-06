package com.oefa.servicio_calendario;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.ConferenceData;
import com.google.api.services.calendar.model.CreateConferenceRequest;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId; // Importa esta clase
import java.time.Instant;
import java.time.ZonedDateTime; // Importa esta clase

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Importaciones de fecha y hora
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Service
public class CalendarService {

    @Value("${google.oauth.clientId}")
    private String clientId;
    @Value("${google.oauth.clientSecret}")
    private String clientSecret;
    @Value("${google.oauth.redirectUri}")
    private String redirectUri;

    private static final String APPLICATION_NAME = "Servicio Calendario OEFA";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);
    private final NetHttpTransport HTTP_TRANSPORT;
    private GoogleAuthorizationCodeFlow flow;
    private final UserDAO userDAO;

    @Autowired
    public CalendarService(UserDAO userDAO) throws GeneralSecurityException, IOException {
        this.userDAO = userDAO;
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    }

    @PostConstruct
    public void init() throws IOException {
        GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        clientSecrets.setInstalled(details);

        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .setApprovalPrompt("force")
                .build();
    }

    public String getAuthorizationUrl(String userId) throws IOException {
        return flow.newAuthorizationUrl().setRedirectUri(redirectUri).setState(userId).build();
    }

    public void storeCredentials(String code, String userId) throws IOException {
        TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
        userDAO.saveRefreshToken(userId, response.getRefreshToken());
        System.out.println("Credenciales de usuario guardadas en la base de datos.");
    }

    private Credential getCredential(String userId) throws IOException {
        String refreshToken = userDAO.getRefreshToken(userId);
        if (refreshToken != null) {
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setRefreshToken(refreshToken);
            return flow.createAndStoreCredential(tokenResponse, userId);
        }
        return null;
    }

    public Map<String, String> createEvent(Map<String, Object> eventData, String userId) throws IOException {
        Credential userCredential = getCredential(userId);
        if (userCredential == null) {
            System.err.println("Error interno: la credencial no pudo ser cargada.");
            Map<String, String> error = new HashMap<>();
            error.put("error", "usuario no autenticado.");
            return error;
        }

        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, userCredential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        userCredential.refreshToken();

        Event event = new Event()
                .setSummary((String) eventData.get("summary"))
                .setDescription("Evento generado por el servicio de calendario OEFA");

        event.setConferenceData(new ConferenceData().setCreateRequest(new CreateConferenceRequest().setRequestId("randomIdHere")));

        ZonedDateTime startZoned = ZonedDateTime.of(
                LocalDateTime.parse((String) eventData.get("startTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                ZoneId.of("America/Lima")
        );
        ZonedDateTime endZoned = ZonedDateTime.of(
                LocalDateTime.parse((String) eventData.get("endTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                ZoneId.of("America/Lima")
        );

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(startZoned.toInstant().toEpochMilli()))
                .setTimeZone(startZoned.getZone().getId());
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(endZoned.toInstant().toEpochMilli()))
                .setTimeZone(endZoned.getZone().getId());
        event.setEnd(end);

        List<String> emails = (List<String>) eventData.get("emails");
        if (emails != null && !emails.isEmpty()) {
            List<EventAttendee> attendees = new java.util.ArrayList<>();
            for (String email : emails) {
                attendees.add(new EventAttendee().setEmail(email));
            }
            event.setAttendees(attendees);
        }

        String calendarId = "primary";
        event = service.events().insert(calendarId, event).setConferenceDataVersion(1).execute();

        String meetLink = null;
        if (event.getConferenceData() != null) {
            for (EntryPoint entryPoint : event.getConferenceData().getEntryPoints()) {
                if ("video".equals(entryPoint.getEntryPointType())) {
                    meetLink = entryPoint.getUri();
                    break;
                }
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("htmlLink", event.getHtmlLink());
        response.put("meetLink", meetLink != null ? meetLink : "No se pudo generar el enlace de Meet.");

        return response;
    }
}