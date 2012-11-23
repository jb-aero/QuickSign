package me.DDoS.Quicksign.permission;

/**
 *
 * @author DDoS
 */
public enum Permission {

    MASTER("quicksign.*"),
    USE("quicksign.use"),
    ALLOW_ICS("quicksign.allowics"),
    FS("quicksign.fastsign"),
    RC("quicksign.reloadconfig"),
    FS_NO_INV("quicksign.fsnoinv"),
    WG_MEMBER("quicksign.wgmember"),
    WG_OWNER("quicksign.wgowner"),
    WG_CAN_BUILD("quicksign.wgcanbuild"),
    LOCKETTE_IS_OWNER("quicksign.locketteisowner"),
    LOCKETTE_IS_OWNER_FP("quicksign.locketteisownerfp"),
    FREE_USE("quicksign.freeuse"),
    IGNORE_BLACK_LIST("quicksign.ignoreblacklist");
    //
    private final String nodeString;

    private Permission(String name) {

        this.nodeString = name;

    }

    public String getNodeString() {

        return nodeString;

    }
}
