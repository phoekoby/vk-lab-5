package org.example.mapper;

import generated.tables.records.OrganizationRecord;
import org.example.entity.OrganizationDTO;

public class OrganizationMapper implements Mapper<OrganizationRecord, OrganizationDTO> {
    @Override
    public OrganizationRecord toRecord(OrganizationDTO organizationDTO) {
        final OrganizationRecord organizationRecord = new OrganizationRecord();
        organizationRecord.setId(organizationDTO.getId());
        organizationRecord.setInn(organizationDTO.getINN());
        organizationRecord.setCheckingAccount(organizationDTO.getCheckingAccount());
        return organizationRecord;
    }

    @Override
    public OrganizationDTO toEntity(OrganizationRecord organizationRecord) {
        return new OrganizationDTO(organizationRecord.getId(), organizationRecord.getInn(), organizationRecord.getCheckingAccount());
    }
}
