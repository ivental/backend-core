@param ru.mentee.power.crm.spring.model.Lead lead
@param org.springframework.validation.BindingResult errors

@template.layout.main(content = @`
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Добавить нового лида</h1>

        <form action="/jpa-leads" method="post" class="space-y-4">
            <!-- Email -->
            <div>
                <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email *</label>
                <input
                        type="email"
                        id="email"
                        name="email"
                        value="${lead.getEmail()}"
                        class="w-full px-3 py-2 border ${errors != null && errors.hasFieldErrors("email") ? "border-red-500" : "border-gray-300"} rounded-md"
                />
                @if(errors != null && errors.hasFieldErrors("email"))
                    <p class="text-red-600 text-sm mt-1">
                        ${errors.getFieldError("email").getDefaultMessage()}
                    </p>
                @endif
            </div>
            <!-- Phone -->
            <div>
                <label for="phone" class="block text-sm font-medium text-gray-700 mb-1">Телефон *</label>
                <input
                        type="tel"
                        id="phone"
                        name="phone"
                        value="${lead.getPhone()}"
                        class="w-full px-3 py-2 border ${errors != null && errors.hasFieldErrors("phone") ? "border-red-500" : "border-gray-300"} rounded-md"
                />
                @if(errors != null && errors.hasFieldErrors("phone"))
                    <p class="text-red-600 text-sm mt-1">
                        ${errors.getFieldError("phone").getDefaultMessage()}
                    </p>
                @endif
            </div>
            <!-- Company -->
            <div>
                <label for="company" class="block text-sm font-medium text-gray-700 mb-1">Компания *</label>
                <input
                        type="text"
                        id="company"
                        name="company"
                        value="${lead.getCompany()}"
                        class="w-full px-3 py-2 border ${errors != null && errors.hasFieldErrors("company") ? "border-red-500" : "border-gray-300"} rounded-md"
                />
                @if(errors != null && errors.hasFieldErrors("company"))
                    <p class="text-red-600 text-sm mt-1">
                        ${errors.getFieldError("company").getDefaultMessage()}
                    </p>
                @endif
            </div>
            <!-- Status -->
            <div>
                <label for="status" class="block text-sm font-medium text-gray-700 mb-1">Статус *</label>
                <select
                        id="status"
                        name="status"
                        class="w-full px-3 py-2 border ${errors != null && errors.hasFieldErrors("status") ? "border-red-500" : "border-gray-300"} rounded-md"
                >
                    <option value="NEW">Новый</option>
                    <option value="CONTACTED">Связались</option>
                    <option value="QUALIFIED">Квалифицирован</option>
                    <option value="LOST">Потерян</option>
                </select>
                @if(errors != null && errors.hasFieldErrors("status"))
                    <p class="text-red-600 text-sm mt-1">
                        ${errors.getFieldError("status").getDefaultMessage()}
                    </p>
                @endif
            </div>

            <!-- Submit Button -->
            <button
                    type="submit"
                    class="w-full bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500">
                Создать лида
            </button>

            <!-- Cancel Link -->
            <a
                    href="/jpa-leads"
                    class="block text-center text-sm text-gray-600 hover:text-gray-900"
            >
                Отмена
            </a>
        </form>
    </div>
`))