package com.example.gladson.socorramev2.helper;

import com.example.gladson.socorramev2.model.EmmergencyContact;

import java.util.List;

public interface IEmmergencyContactDAO {

    boolean save(EmmergencyContact emmergencyContact);

    boolean delete(EmmergencyContact emmergencyContact);

    List<EmmergencyContact> list();
}
