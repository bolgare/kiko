package ru.bolgar.kiko.rental.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Flat
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flat implements Serializable {
    /**
     * identifier
     */
    private String id;

    /**
     * name of flat
     */
    private String name;

    /**
     * current tenant id
     */
    private String ownerId;
}
