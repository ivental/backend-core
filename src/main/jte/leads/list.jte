@import ru.mentee.power.crm.model.Lead
@import ru.mentee.power.crm.model.LeadStatus
@import java.util.List
@param List<Lead> leads
@param LeadStatus currentFilter

<div class="mb-4 flex gap-2">
    <a href="/leads"
       class="px-4 py-2 rounded ${currentFilter == null ? "bg-blue-500 text-white" : "bg-gray-200"}">
        Все
    </a>
    <a href="/leads?status=NEW"
       class="px-4 py-2 rounded ${currentFilter == LeadStatus.NEW ? "bg-blue-500 text-white" : "bg-gray-200"}">
        NEW
    </a>
    <a href="/leads?status=CONTACTED"
       class="px-4 py-2 rounded ${currentFilter == LeadStatus.CONTACTED ? "bg-blue-500 text-white" : "bg-gray-200"}">
        CONTACTED
    </a>
    <a href="/leads?status=QUALIFIED"
       class="px-4 py-2 rounded ${currentFilter == LeadStatus.QUALIFIED ? "bg-blue-500 text-white" : "bg-gray-200"}">
        QUALIFIED
    </a>
</div>

@if(currentFilter != null)
    <p class="text-sm text-gray-600 mb-2">Показаны лиды со статусом: ${currentFilter}</p>
@endif

<!-- Таблица лидов -->
@if(!leads.isEmpty())
    <table class="min-w-full bg-white border">
        <thead>
        <tr class="bg-gray-100">
            <th class="px-4 py-2 border">Email</th>
            <th class="px-4 py-2 border">Телефон</th>
            <th class="px-4 py-2 border">Компания</th>
            <th class="px-4 py-2 border">Статус</th>
        </tr>
        </thead>
        <tbody>
        @for(int i = 0; i < leads.size(); i++)
            <tr class="hover:bg-gray-50">
                <td class="px-4 py-2 border">${leads.get(i).email()}</td>
                <td class="px-4 py-2 border">${leads.get(i).phone()}</td>
                <td class="px-4 py-2 border">${leads.get(i).company()}</td>
                <td class="px-4 py-2 border">${leads.get(i).status()}</td>
            </tr>
        @endfor
        </tbody>
    </table>
@else
    <p class="text-gray-500">Лидов не найдено</p>
@endif