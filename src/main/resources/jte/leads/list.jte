@param java.util.List<ru.mentee.power.crm.model.Lead> leads
@param ru.mentee.power.crm.model.LeadStatus currentFilter

@template.layout.main(content = @`
    <div class="mb-4 flex justify-between items-center">
        <div class="mb-4 flex gap-2">
            <a href="/leads"
               class="px-4 py-2 rounded-lg ${currentFilter == null ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-700 hover:bg-gray-300"}">
                Все
            </a>
            <a href="/leads?status=NEW"
               class="px-4 py-2 rounded-lg ${currentFilter == ru.mentee.power.crm.model.LeadStatus.NEW ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-700 hover:bg-gray-300"}">
                Новый
            </a>
            <a href="/leads?status=CONTACTED"
               class="px-4 py-2 rounded-lg ${currentFilter == ru.mentee.power.crm.model.LeadStatus.CONTACTED ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-700 hover:bg-gray-300"}">
                Связались
            </a>
            <a href="/leads?status=QUALIFIED"
               class="px-4 py-2 rounded-lg ${currentFilter == ru.mentee.power.crm.model.LeadStatus.QUALIFIED ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-700 hover:bg-gray-300"}">
                Квалифицирован
            </a>
            <a href="/leads?status=LOST"
               class="px-4 py-2 rounded-lg ${currentFilter == ru.mentee.power.crm.model.LeadStatus.LOST ? "bg-blue-600 text-white" : "bg-gray-200 text-gray-700 hover:bg-gray-300"}">
                Утерян
            </a>
        </div>

        <a href="/leads/new"
           class="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition-colors">
            + Добавить лида
        </a>
    </div>
        + Добавить лида
        </a>

    <div class="bg-white rounded-lg shadow overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Телефон</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Компания</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Статус</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            @for(ru.mentee.power.crm.model.Lead lead : leads)
                <tr class="hover:bg-gray-50">
                    <td class="px-6 py-4 whitespace-nowrap">${lead.email()}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${lead.phone() != null ? lead.phone() : "-"}</td>
                    <td class="px-6 py-4 whitespace-nowrap">${lead.company()}</td>
                    <td class="px-6 py-4 whitespace-nowrap">
                            <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full
                                ${lead.status() == ru.mentee.power.crm.model.LeadStatus.NEW ? "bg-blue-100 text-blue-800" :
                                  lead.status() == ru.mentee.power.crm.model.LeadStatus.CONTACTED ? "bg-yellow-100 text-yellow-800" :
                                  lead.status() == ru.mentee.power.crm.model.LeadStatus.QUALIFIED ? "bg-purple-100 text-purple-800" :
                                  "bg-green-100 text-green-800"}">
                                ${lead.status().getRussianName()}
                            </span>
                    </td>
                    <td class="px-4 py-2">
                        <a href="/leads/${lead.id().toString()}/edit"
                           class="text-blue-600 hover:underline font-medium">
                            Редактировать
                        </a>
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
    @if(leads.isEmpty())
        <div class="text-center py-8 text-gray-500">
            Лидов не найдено
        </div>
    @endif
`)
