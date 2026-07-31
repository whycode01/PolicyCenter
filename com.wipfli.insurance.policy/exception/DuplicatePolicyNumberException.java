package exception;

public class DuplicatePolicyNumberException extends PolicyBusinessException {
    public DuplicatePolicyNumberException(String policyNumber, String message) {
        super(policyNumber, message);
    }
}