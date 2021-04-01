package ca.mohawk.kolar.models;

import ca.mohawk.kolar.models.MountResult;

/**
 * Result Model associated with reauest for all mounts
 */
public class AllMountRequestResult {
    public transient String _links;
    public MountResult[] mounts;
}
