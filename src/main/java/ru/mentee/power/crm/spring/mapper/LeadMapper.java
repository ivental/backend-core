package ru.mentee.power.crm.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.mentee.power.crm.spring.dto.generated.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.generated.LeadResponse;
import ru.mentee.power.crm.spring.dto.generated.UpdateLeadRequest;
import ru.mentee.power.crm.spring.model.Lead;

@Mapper(componentModel = "spring")
public interface LeadMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "company", ignore = true) // Нужно будет установить отдельно
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "status", constant = "NEW")
  Lead toEntity(CreateLeadRequest request);

  @Mapping(source = "company.id", target = "companyId")
  @Mapping(source = "status", target = "status")
  LeadResponse toGeneratedResponse(Lead lead);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  void updateEntityFromGenerated(UpdateLeadRequest request, @MappingTarget Lead lead);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "status", constant = "NEW")
  Lead toEntityFromGenerated(CreateLeadRequest request);
}
