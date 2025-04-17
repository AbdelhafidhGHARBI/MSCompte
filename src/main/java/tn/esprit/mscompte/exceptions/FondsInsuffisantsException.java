package tn.esprit.mscompte.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public class FondsInsuffisantsException extends RuntimeException {
    private final Map<String, Object> errorDetails;

    public FondsInsuffisantsException(String message) {
        super(message);
        this.errorDetails = Map.of(
                "error", "Fonds insuffisants",
                "message", message,
                "timestamp", LocalDateTime.now()
        );
    }

    public Map<String, Object> getErrorDetails() {
        return errorDetails;
    }
}
