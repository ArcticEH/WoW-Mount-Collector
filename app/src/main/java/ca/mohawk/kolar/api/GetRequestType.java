package ca.mohawk.kolar.api;

public enum GetRequestType {
    AllMounts {
        public String toString() {
            return "AllMounts";
        }
    },

    CreatureImage {
        public String toString() {
            return "CreatureImage";
        }
    },

    MountDetail {
        public String toString() {
            return "MountDetail";
        }
    }
}
