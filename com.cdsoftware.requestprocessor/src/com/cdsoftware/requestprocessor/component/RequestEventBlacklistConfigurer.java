package com.cdsoftware.requestprocessor.component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.compiere.util.CLogger;
import org.compiere.util.Ini;
import org.compiere.util.Util;

public class RequestEventBlacklistConfigurer {

	private static final CLogger log = CLogger.getCLogger(RequestEventBlacklistConfigurer.class);

	private static final String BLACKLIST_FILE_NAME = "event.handlers.blacklist";
	private static final String REQUEST_EVENT_HANDLER = "org.adempiere.base.event.RequestEventHandler";
	private static final String REQUEST_SEND_EMAIL_TOPIC = "idempiere/requestSendEMail";
	private static final String REQUIRED_ENTRY = REQUEST_EVENT_HANDLER + "[" + REQUEST_SEND_EMAIL_TOPIC + "]";
	private static final String FULL_HANDLER_ENTRY = REQUEST_EVENT_HANDLER + "[*]";

	protected void activate() {
		ensureRequestSendEmailBlacklist();
	}

	private void ensureRequestSendEmailBlacklist() {
		String adempiereHome = Ini.getAdempiereHome();
		if (Util.isEmpty(adempiereHome, true)) {
			log.warning("Cannot configure request email blacklist: ADEMPIERE_HOME is empty");
			return;
		}

		Path blacklistPath = Path.of(adempiereHome, BLACKLIST_FILE_NAME);
		try {
			if (hasBlacklistEntry(blacklistPath, FULL_HANDLER_ENTRY)) {
				log.warning(FULL_HANDLER_ENTRY + " found in " + blacklistPath
					+ ". This disables the full core RequestEventHandler and can stop request update notifications.");
				return;
			}

			if (hasBlacklistEntry(blacklistPath, REQUIRED_ENTRY)) {
				log.info(REQUIRED_ENTRY + " already configured in " + blacklistPath);
				return;
			}

			appendBlacklistEntry(blacklistPath);
			log.warning(REQUIRED_ENTRY + " added to " + blacklistPath
				+ ". Restart iDempiere so EventManager reloads the blacklist.");
		} catch (IOException e) {
			log.warning("Cannot configure " + blacklistPath + ": " + e.getLocalizedMessage()
				+ ". Add manually: " + REQUIRED_ENTRY);
		}
	}

	private boolean hasBlacklistEntry(Path blacklistPath, String entry) throws IOException {
		if (!Files.exists(blacklistPath))
			return false;

		List<String> lines = Files.readAllLines(blacklistPath, StandardCharsets.UTF_8);
		for (String line : lines) {
			String normalizedLine = normalize(line);
			if (entry.equals(normalizedLine))
				return true;
		}
		return false;
	}

	private void appendBlacklistEntry(Path blacklistPath) throws IOException {
		String prefix = "";
		if (Files.exists(blacklistPath) && Files.size(blacklistPath) > 0)
			prefix = System.lineSeparator();

		String content = prefix
			+ "# Added by com.cdsoftware.requestprocessor to avoid duplicate Request Updated emails"
			+ System.lineSeparator()
			+ REQUIRED_ENTRY
			+ System.lineSeparator();

		Files.writeString(blacklistPath, content, StandardCharsets.UTF_8,
			StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}

	private String normalize(String line) {
		if (line == null)
			return "";
		String trimmed = line.trim();
		if (trimmed.startsWith("#"))
			return "";
		return trimmed.replace(" ", "");
	}
}
