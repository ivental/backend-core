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