package net.engineeringdigest.journalApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ConfigJournalAppEntity {
    @Id
    @Column(name = "config_key")
    private String key;
    @Column(name = "config_value")
    private String value;
}
