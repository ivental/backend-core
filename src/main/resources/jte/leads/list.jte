@param java.util.List<ru.mentee.power.crm.model.Lead> leads
@param ru.mentee.power.crm.model.LeadStatus currentFilter
@param String search

@template.layout.main(content = @`
    <form method="get" action="/leads" class="mb-6 bg-white p-4 rounded shadow">
        <div class="flex gap-4">
            <input
                    type="text"
                    name="search"
                    placeholder="Поиск по email или компании..."
                    value="${search}"
                    class="px-3 py-2 border rounded w-full"
            />
            <select name="status" class="px-3 py-2 border rounded">
                <option value="">Все статусы</option>
                <option value="NEW">Новый</option>
                <option value="CONTACTED">Связались</option>
                <option value="QUALIFIED">Квалифицирован</option>
                <option value="LOST">Потерян</option>
            </select>
            <button type="submit" class="bg-blue-500 text-white px-4 py-2 rounded">Поиск</button>
        </div>
        <a href="/leads" class="text-sm text-gray-600 hover:underline">Очистить фильтры</a>
    </form>

    <div class="mb-4 flex justify-between items-center">
        <div class="mb-4 flex gap-2">
            <a href="/leads"
               class="px-4 py-2 rounded ${currentFilter == null ? "bg-blue-500 text-white" : "bg-gray-200"}">
                Все
            </a>
            <a href="/leads?status=NEW"
               class="px-4 py-2 rounded ${currentFilter == ru.mentee.power.crm.model.LeadStatus.NEW ? "bg-blue-500 text-white" : "bg-gray-200"}">
                Новый
            </a>
            <a href="/leads?status=CONTACTED"
               class="px-4 py-2 rounded ${currentFilter == ru.mentee.power.crm.model.LeadStatus.CONTACTED ? "bg-blue-500 text-white" : "bg-gray-200"}">
                Связались
            </a>
            <a href="/leads?status=QUALIFIED"
               class="px-4 py-2 rounded ${currentFilter == ru.mentee.power.crm.model.LeadStatus.QUALIFIED ? "bg-blue-500 text-white" : "bg-gray-200"}">
                Квалифицирован
            </a>
            <a href="/leads?status=LOST"
               class="px-4 py-2 rounded ${currentFilter == ru.mentee.power.crm.model.LeadStatus.LOST ? "bg-blue-500 text-white" : "bg-gray-200"}">
                Потерян
            </a>
        </div>

        <a href="/leads/new"
           class="bg-green-500 text-white px-4 py-2 rounded-lg hover:bg-green-600 transition-colors">
            + Добавить лида
        </a>
    </div>

    <div class="bg-white rounded-lg shadow overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
            <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Телефон</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Компания</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Статус</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"></th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"></th>

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

                    <td>
                        <form method="post" action="/leads/${lead.id().toString()}/delete" class="inline"
                              onclick="return confirm('Удалить лида ${lead.email()}?')">
                            <button type="submit" class="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700">
                                Удалить
                            </button>
                        </form>
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
