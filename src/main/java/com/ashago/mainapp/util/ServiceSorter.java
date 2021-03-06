package com.ashago.mainapp.util;

public class ServiceSorter {

    private final Sort priority;

    public Sort getPriority() {
        return priority;
    }

    private ServiceSorter(Sort priority) {
        this.priority = priority;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Sort priority;

        public ServiceSorter build() {
            return new ServiceSorter(priority);
        }

        public Builder priority(Sort sort) {
            this.priority = sort;
            return this;
        }
    }

    public enum Sort {
        ASC, DESC
    }
}
