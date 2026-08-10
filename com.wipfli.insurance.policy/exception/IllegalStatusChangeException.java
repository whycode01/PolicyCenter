package exception;

public class IllegalStatusChangeException extends PolicyBusinessException {

    public IllegalStatusChangeException(String policyNumber, String message) {
        super(policyNumber, message);
    }
}