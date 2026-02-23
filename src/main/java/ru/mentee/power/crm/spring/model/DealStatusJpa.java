package ru.mentee.power.crm.spring.model;



import java.util.Map;
import java.util.Set;

public enum DealStatusJpa {
    NEW,
    QUALIFIED,
    PROPOSAL_SENT,
    NEGOTIATION,
    WON,
    LOST;

    private static final Map<DealStatusJpa, Set<DealStatusJpa>> VALID_TRANSITIONS = Map.of(
            NEW, Set.of(QUALIFIED, LOST),
            QUALIFIED, Set.of(PROPOSAL_SENT, LOST),
            PROPOSAL_SENT, Set.of(NEGOTIATION, LOST),
            NEGOTIATION, Set.of(WON, LOST),
            WON, Set.of(),
            LOST, Set.of()
    );

    public boolean canTransitionTo(DealStatusJpa target) {
        if (target == null) {
            return false;
        }
        return VALID_TRANSITIONS.get(this).contains(target);
    }
}

