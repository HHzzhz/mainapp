package com.ashago.mainapp.util;

public class ServiceFilter {

    private final String location;
    private final String category;

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    private ServiceFilter(String location, String category) {
        this.location = location;
        this.category = category;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String location;
        private String category;

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public ServiceFilter build() {
            return new ServiceFilter(this.location, this.category);
        }
    }
}
