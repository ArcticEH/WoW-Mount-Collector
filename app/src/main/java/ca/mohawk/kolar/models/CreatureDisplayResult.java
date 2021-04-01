package ca.mohawk.kolar.models;

/**
 * Result model associated with a creature display request
 */
public class CreatureDisplayResult {
    public transient String _links;
    public CreatureDisplayMediaResult[] assets;
    public int id;
}
