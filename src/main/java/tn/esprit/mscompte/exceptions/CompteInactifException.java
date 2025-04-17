package tn.esprit.mscompte.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public class CompteInactifException extends RuntimeException {
    private final Map<String, Object> errorDetails;

    public CompteInactifException(String message) {
        super(message);
        this.errorDetails = Map.of(
                "error", "Compte inactif",
                "message", message,
                "timestamp", LocalDateTime.now()
        );
    }

    public Map<String, Object> getErrorDetails() {
        return errorDetails;
    }
}
