@param String companyName
@param String newEmail
@param String message

@template.layout.main(content = @`
    <div class="max-w-md mx-auto mt-8">
        <h1 class="text-2xl font-bold mb-6">Массовое обновление email</h1>

        <!-- Сообщение об успехе/ошибке -->
        @if(message != null && !message.isEmpty())
            <div class="mb-4 p-4 ${message.contains("Обновлено") ? "bg-green-100 text-green-700" : "bg-yellow-100 text-yellow-700"} rounded-lg">
                ${message}
            </div>
        @endif

        <!-- Форма -->
        <form action="/jpa-leads/update-email" method="post" class="space-y-4 bg-white p-6 rounded-lg shadow">
            <!-- Название компании -->
            <div>
                <label for="companyName" class="block text-sm font-medium text-gray-700 mb-1">
                    Название компании <span class="text-red-500">*</span>
                </label>
                <input
                        type="text"
                        id="companyName"
                        name="companyName"
                        value="${companyName}"
                        required
                        placeholder="Например: ООО Ромашка"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                />
                <p class="text-xs text-gray-500 mt-1">
                    Будут обновлены все лиды с этой компанией
                </p>
            </div>

            <!-- Новый email -->
            <div>
                <label for="newEmail" class="block text-sm font-medium text-gray-700 mb-1">
                    Новый email <span class="text-red-500">*</span>
                </label>
                <input
                        type="email"
                        id="newEmail"
                        name="newEmail"
                        value="${newEmail}"
                        required
                        placeholder="new@company.com"
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-2 focus:ring-blue-500"
                />
            </div>

            <!-- Кнопки -->
            <div class="flex gap-3 pt-4">
                <button
                        type="submit"
                        class="flex-1 bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 focus:ring-2 focus:ring-blue-500 transition-colors">
                    Обновить email
                </button>

                <a
                        href="/jpa-leads"
                        class="flex-1 bg-gray-200 text-gray-700 px-4 py-2 rounded-md hover:bg-gray-300 text-center transition-colors">
                    Отмена
                </a>
            </div>

            <!-- Предупреждение -->
            <div class="mt-4 p-3 bg-yellow-50 border border-yellow-200 rounded-md">
                <p class="text-sm text-yellow-800">
                    <span class="font-bold">⚠️ Внимание:</span> Это массовое обновление.
                    Email изменится у всех лидов указанной компании.
                </p>
            </div>
        </form>

        <!-- Ссылка назад к списку -->
        <div class="mt-4 text-center">
            <a href="/jpa-leads" class="text-sm text-gray-600 hover:text-gray-900 hover:underline">
                ← Вернуться к списку лидов
            </a>
        </div>
    </div>
`)