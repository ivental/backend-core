package ru.mentee.power.crm.spring.mapper;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.mentee.power.crm.spring.dto.CreateLeadRequest;
import ru.mentee.power.crm.spring.dto.LeadResponse;
import ru.mentee.power.crm.spring.dto.UpdateLeadRequest;
import ru.mentee.power.crm.spring.model.Lead;

@Mapper(componentModel = "spring")
public interface LeadMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  Lead toEntity(CreateLeadRequest request);

  @Mapping(source = "company.id", target = "companyId")
  LeadResponse toResponse(Lead entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "email", source = "request.email", qualifiedByName = "unwrapOptional")
  @Mapping(target = "phone", source = "request.phone", qualifiedByName = "unwrapOptional")
  void updateEntity(UpdateLeadRequest request, @MappingTarget Lead entity);

  @Named("unwrapOptional")
  default <T> T unwrapOptional(Optional<T> optional) {
    return optional.isPresent() ? optional.orElse(null) : null;
  }
}
