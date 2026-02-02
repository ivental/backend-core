@param java.util.List<ru.mentee.power.crm.model.Lead> leads
@param ru.mentee.power.crm.model.LeadStatus currentFilter

<div class="mb-4 flex justify-between items-center">
    <div class="mb-4 flex gap-2">
        <a href="/leads"
           class="px-4 py-2 rounded ${currentFilter == null ? "bg-blue-500 text-white" : "bg-gray-200"}">
            Все
        </a>
        <a href="/leads?status=NEW"
           class="px-4 py-2 rounded ${currentFilter == ru.mentee.power.crm.model.LeadStatus.NEW ? "bg-blue-500 text-white" : "bg-gray-200"}">
            NEW
        </a>
        <a href="/leads?status=CONTACTED"
           class="px-4 py-2 rounded ${currentFilter == ru.mentee.power.crm.model.LeadStatus.CONTACTED ? "bg-blue-500 text-white" : "bg-gray-200"}">
            CONTACTED
        </a>
        <a href="/leads?status=QUALIFIED"
           class="px-4 py-2 rounded ${currentFilter == ru.mentee.power.crm.model.LeadStatus.QUALIFIED ? "bg-blue-500 text-white" : "bg-gray-200"}">
            QUALIFIED
        </a>
    </div>
    <a href="/leads/new"
       class="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600">
        + Добавить лида
    </a>
</div>
<div class="bg-white rounded-lg shadow overflow-hidden">
    <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
        <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Phone</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Company</th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
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
                                  "bg-green-100 text-green-800"}">
                                ${lead.status().toString()}
                            </span>
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
