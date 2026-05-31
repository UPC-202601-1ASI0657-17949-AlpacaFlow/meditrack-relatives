package com.alpacaflow.meditrack.relatives.shared.infrastructure.messaging;

/**
 * JMS destination names shared with IAM (keep in sync manually across repos).
 */
public final class MessagingQueueNames {

    public static final String RELATIVE_REGISTRATION_REQUESTED = "meditrack.iam.relative-registration.requested";

    private MessagingQueueNames() {
    }
}
