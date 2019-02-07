package com.example.gladson.socorramev2.helper;

import com.example.gladson.socorramev2.model.EmmergencyContact;

import java.util.List;

public interface IEmmergencyContactDAO {

    public boolean save(EmmergencyContact emmergencyContact);

    public boolean delete(EmmergencyContact emmergencyContact);

    public List<EmmergencyContact> list();
}
