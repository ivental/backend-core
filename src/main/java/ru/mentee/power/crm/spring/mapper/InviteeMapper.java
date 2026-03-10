package ru.mentee.power.crm.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.mentee.power.crm.spring.dto.CreateInviteeRequest;
import ru.mentee.power.crm.spring.dto.InviteeResponse;
import ru.mentee.power.crm.spring.model.Invitee;

@Mapper(componentModel = "spring")
public interface InviteeMapper {
  InviteeResponse toResponse(Invitee invitee);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "status", ignore = true)
  Invitee toEntity(CreateInviteeRequest request);
}
