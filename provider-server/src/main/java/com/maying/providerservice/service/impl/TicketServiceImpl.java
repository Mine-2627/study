package com.maying.providerservice.service.impl;

import com.maying.providerservice.service.TicketService;
import org.springframework.stereotype.Component;

/**
 * description
 */
@Component
public class TicketServiceImpl implements TicketService {

    @Override
    public String getTicket() {
        return "获得票据";
    }
}
