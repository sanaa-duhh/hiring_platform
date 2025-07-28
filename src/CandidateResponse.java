package src;

import Enums.Gender;
public record CandidateResponse(
        Integer applicationId,
        Integer candidateId,
        String name,
        Gender gender,
        String resumeLinkPath,
        int experience
) {
}
