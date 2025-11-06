package com.openlodge.dto;

public class AmenityDTO {
    private Long id;
    private String name;

    // Constructors
    public AmenityDTO() {}
    
    public AmenityDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Builder pattern
    public static AmenityDTOBuilder builder() {
        return new AmenityDTOBuilder();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Builder class
    public static class AmenityDTOBuilder {
        private Long id;
        private String name;

        public AmenityDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AmenityDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AmenityDTO build() {
            return new AmenityDTO(id, name);
        }
    }
}
