package ca.mohawk.kolar.api;

/**
 * All valid get request types within this app
 * Enum with toStrings provides simple way to utilize Strings as enums
 */
public enum GetRequestType {

    // Request for all mounts from mount database
    AllMounts {
        public String toString() {
            return "AllMounts";
        }
    },

    // Request for a creature image from detail view
    CreatureImage {
        public String toString() {
            return "CreatureImage";
        }
    },

    // Request for additional mount details from detail view
    MountDetail {
        public String toString() {
            return "MountDetail";
        }
    }
}
