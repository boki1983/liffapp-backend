package com.jpmorgan.lineproject.domain;

public enum BusinessUnit {
    TAIWAN_DIRECT("nvfsq8hh42m148iydkj9mphl", "VkjoOCScnG0IRmK9KPy9Cufz", 518000925);

    private final String clientId;
    private final String clientSecret;
    private final Integer mid;

    BusinessUnit(String clientId, String clientSecret, Integer mid) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.mid = mid;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Integer getMid() {
        return mid;
    }
}
