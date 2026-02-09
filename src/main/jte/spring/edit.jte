@import ru.mentee.power.crm.model.Lead
@param Lead lead

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактирование лида</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 min-h-screen">
<div class="container mx-auto p-6">

    <!-- Заголовок -->
    <h1 class="text-2xl font-bold mb-6 text-gray-800">Редактирование лида</h1>

    <!-- Форма -->
    <form method="post" action="/leads/${lead.id}" class="bg-white p-6 rounded-lg shadow-md max-w-2xl">

        <!-- Скрытое поле ID (ВАЖНО!) -->
        <input type="hidden" name="id" value="${lead.id}">

        <!-- Поле: Email -->
        <div class="mb-4">
            <label for="email" class="block text-gray-700 text-sm font-medium mb-2">
                Email *
            </label>
            <input
                    type="email"
                    id="email"
                    name="email"
                    value="${lead.email}"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="example@mail.com"
            >
        </div>

        <!-- Поле: Телефон -->
        <div class="mb-4">
            <label for="phone" class="block text-gray-700 text-sm font-medium mb-2">
                Телефон
            </label>
            <input
                    type="tel"
                    id="phone"
                    name="phone"
                    value="${lead.phone}"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="+7 (999) 123-45-67"
            >
        </div>

        <!-- Поле: Компания -->
        <div class="mb-4">
            <label for="company" class="block text-gray-700 text-sm font-medium mb-2">
                Компания *
            </label>
            <input
                    type="text"
                    id="company"
                    name="company"
                    value="${lead.company}"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                    placeholder="Введите название компании"
            >
        </div>

        <!-- Поле: Статус  -->
        <div class="mb-6">
            <label for="status" class="block text-gray-700 text-sm font-medium mb-2">
                Статус *
            </label>
            <select
                    id="status"
                    name="status"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
                <option value="NEW" ${lead.status.name() == 'NEW' ? 'selected' : ''}>Новый</option>
                <option value="CONTACTED" ${lead.status.name() == 'CONTACTED' ? 'selected' : ''}>Связались</option>
                <option value="QUALIFIED" ${lead.status.name() == 'QUALIFIED' ? 'selected' : ''}>Квалифицирован</option>
                <option value="LOST" ${lead.status.name() == 'LOST' ? 'selected' : ''}>Утерян</option>
            </select>
        </div>

        <div class="flex items-center justify-between space-x-4">

            <button
                    type="submit"
                    class="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-6 rounded-md transition duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
            >
                Сохранить изменения
            </button>

            <a
                    href="/leads"
                    class="text-gray-600 hover:text-gray-800 font-medium py-2 px-4 rounded-md border border-gray-300 hover:border-gray-400 transition duration-200"
            >
                Отмена
            </a>
        </div>

    </form>

    <p class="mt-4 text-sm text-gray-500">
        * - обязательные для заполнения поля
    </p>

</div>
</body>
</html>