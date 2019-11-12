package ru.bolgar.kiko.rental.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.bolgar.kiko.rental.api.data.ViewSlot;
import ru.bolgar.kiko.rental.api.data.ViewSlotState;

import java.util.Date;

/**
 * Order for view slot
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewOrder {
    /**
     * identifier
     */
    private String id;

    /**
     * flat identifier
     */
    private String flatId;

    /**
     * slot time
     */
    private Date time;

    /**
     * tenant who made order
     */
    private String tenantId;

    /**
     * current state of order
     */
    @EqualsAndHashCode.Exclude
    private ViewSlotState state;

    public ViewSlot asViewSlot() {
        ViewSlot slot = new ViewSlot();
        slot.setId(id);
        slot.setFlatId(flatId);
        slot.setTime(time);
        slot.setState(state);
        return slot;
    }
}
