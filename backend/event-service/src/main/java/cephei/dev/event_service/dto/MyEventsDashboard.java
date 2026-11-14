package cephei.dev.event_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MyEventsDashboard{
    public Long totalEvents;
    public Long totalHostedEvents;

    public Long pendingInvitations;

    public List<EventPreviewReadDto> weeklyEvents;
    public EventPreviewReadDto nextEvent;

    public MyEventsDashboard(Long totalEvents, Long totalHostedEvents) {
        this.totalEvents = totalEvents;
        this.totalHostedEvents = totalHostedEvents;
    }
}
