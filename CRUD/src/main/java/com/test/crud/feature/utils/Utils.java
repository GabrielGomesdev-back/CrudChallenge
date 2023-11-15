package com.test.crud.feature.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.crud.data.entity.IncidentEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@Service
@Transactional
@Validated
public class Utils {

    // Useful Methods for dates

    public Calendar getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    public Calendar StringToCalendar(String date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        cal.setTime(sdf.parse(date));
        return cal;
    }

    public HashMap<String, Object> FormatDate(IncidentEntity object, HashMap<String, Object> jsonIncident) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date createdAt = object.getCreatedAt().getTime();
        Date updatedAt = object.getUpdatedAt().getTime();
        Date closedAt  = null;
        if(object.getClosedAt() != null) {
            closedAt = object.getUpdatedAt().getTime();
        }

        jsonIncident.put("createdAt", dateFormat.format(createdAt));
        jsonIncident.put("updatedAt", dateFormat.format(updatedAt));
        if(closedAt != null) {
            jsonIncident.put("closedAt", dateFormat.format(closedAt));
        }
        return jsonIncident;
    }

    public void fillInDates(Object object) {
        try {
            Method getCreatedAt = object.getClass().getMethod("getCreatedAt");
            Method setCreatedAt = object.getClass().getMethod("setCreatedAt", Calendar.class);
            Method setUpdatedAt = object.getClass().getMethod("setUpdatedAt", Calendar.class);
            if (getCreatedAt.invoke(object) == null) {
                setCreatedAt.invoke(object, getCurrentDate());
            }
            setUpdatedAt.invoke(object, getCurrentDate());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Useful methods for cast Entities for HashMaps

    public HashMap<String, Object> ObjectToMap(IncidentEntity object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> map = mapper.convertValue(object, HashMap.class);
        return map;
    }

    // Useful methods for messages and returns

    public HashMap<String, Object> SuccesMessage() {
        HashMap<String, Object> dto = new HashMap<>();
        dto.put("status", "succes");
        dto.put("isOk", true);
        dto.put("message", "Operation carried out successfully");
        return dto;
    }

    public HashMap<String, Object> ErrorMessage() {
        HashMap<String, Object> dto = new HashMap<>();
        dto.put("status", "error");
        dto.put("isOk", false);
        dto.put("message", "Operation cannot carried out");
        return dto;
    }
}
