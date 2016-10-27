package com.hpe.caf.util.processidentifier;

import java.util.UUID;

/**
 * Provides a unique identifier representing the current process.
 */
public final class ProcessIdentifier {
    /**
     * A unique identifier for the current process.
     */
    private static final UUID processId;

    /**
     * Initializing an identifier for this process.
     */
    static {
        processId = UUID.randomUUID();
    }

    /**
     * Ensure this class can't be instantiated (it is intended to be static)
     */
    private ProcessIdentifier() {
    }

    /**
     * Returns a unique identifier for this process.
     * @return Unique identifier for this process.
     */
    public static UUID getProcessId(){
        return processId;
    }
}
