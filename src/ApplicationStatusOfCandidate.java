package src;

public record ApplicationStatusOfCandidate(
        Integer applicationId,
        Integer jobId,
        String status,
        String jobTitle,
        String appliedAt
) {
}
