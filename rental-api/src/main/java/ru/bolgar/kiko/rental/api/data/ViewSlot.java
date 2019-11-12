package ru.bolgar.kiko.rental.api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 20-minute viewing slot
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewSlot implements Serializable {
    public static final int SIZE = 20;

    /**
     * view slot order identifier, may be <code>null</code>
     */
    private String id;

    /**
     * flat identifier
     */
    private String flatId;

    /**
     * time of view slot
     */
    private Date time;

    /**
     * state of view slot
     */
    private ViewSlotState state;
}
