package ru.mentee.power.crm.model;
import jakarta.validation.constraints.*;
import java.util.UUID;


public record Lead(UUID id,
                   @NotBlank(message = "Email обязателен")
                   @Email(message = "Некорректный формат email")
                   @Pattern(
                           regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                           message = "Email должен содержать точку в домене (например: example@gmail.com)"
                   )
                   String email,

                   @NotBlank(message = "Телефон обязателен")
                   @Size(max = 20, message = "Введите 11-ти значный номер")
                   String phone,

                   @NotBlank(message = "Компания обязательна")
                   String company,

                   @NotNull(message = "Статус обязателен")
                   LeadStatus status) {
}
