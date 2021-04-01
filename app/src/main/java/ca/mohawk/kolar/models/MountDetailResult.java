package ca.mohawk.kolar.models;

import ca.mohawk.kolar.models.CreatureDisplayResult;

public class MountDetailResult {
    public transient String _links;
    public int id;
    public String name;
    public CreatureDisplayResult[] creature_displays;
    public String description;
    public transient String source;
    public transient String faction;
    public transient String should_exclude_if_uncollected;
    public transient String requirements;
}
