package com.example.practiceschedule

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Вид занятия. Название хранится ссылкой на строковый ресурс, значок —
 * ссылкой на drawable, поэтому ни текст, ни картинка не «зашиты» в коде.
 */
enum class LessonType(
    @param:StringRes val titleRes: Int,
    @param:DrawableRes val iconRes: Int
) {
    LECTURE(R.string.lesson_type_lecture, R.drawable.ic_lecture),
    PRACTICE(R.string.lesson_type_practice, R.drawable.ic_practice),
    LAB(R.string.lesson_type_lab, R.drawable.ic_lab)
}

/**
 * Одна пара в расписании.
 *
 * Содержимое полей — учебные данные, а не элементы интерфейса, поэтому они
 * задаются в коде списком (хранение в базе данных практической работой
 * не требуется).
 */
data class Lesson(
    val subject: String,
    val type: LessonType,
    val teacher: String,
    val room: String,
    val time: String
)

/**
 * Расписание учебной недели.
 *
 * Ключ словаря — индекс дня недели, он совпадает с позицией элемента в
 * строковом массиве `R.array.days_of_week` (0 — понедельник, 5 — суббота).
 */
object Schedule {

    private val lessonsByDay: Map<Int, List<Lesson>> = mapOf(
        // Понедельник
        0 to listOf(
            Lesson(
                subject = "Разработка мобильных приложений",
                type = LessonType.LECTURE,
                teacher = "Асқар Э.",
                room = "ГУК, ауд. 415",
                time = "08:00–09:20"
            ),
            Lesson(
                subject = "Разработка мобильных приложений",
                type = LessonType.LAB,
                teacher = "Асқар Э.",
                room = "ГУК, ауд. 318",
                time = "09:30–10:50"
            ),
            Lesson(
                subject = "Базы данных",
                type = LessonType.LECTURE,
                teacher = "Сейтжанов Б. Т.",
                room = "ГУК, ауд. 220",
                time = "11:00–12:20"
            )
        ),
        // Вторник
        1 to listOf(
            Lesson(
                subject = "Алгоритмы и структуры данных",
                type = LessonType.LECTURE,
                teacher = "Нурланова А. К.",
                room = "ГМК, ауд. 112",
                time = "09:30–10:50"
            ),
            Lesson(
                subject = "Алгоритмы и структуры данных",
                type = LessonType.PRACTICE,
                teacher = "Нурланова А. К.",
                room = "ГМК, ауд. 114",
                time = "11:00–12:20"
            ),
            Lesson(
                subject = "Английский язык",
                type = LessonType.PRACTICE,
                teacher = "Ибраева Г. С.",
                room = "ГУК, ауд. 207",
                time = "13:00–14:20"
            )
        ),
        // Среда
        2 to listOf(
            Lesson(
                subject = "Базы данных",
                type = LessonType.LAB,
                teacher = "Сейтжанов Б. Т.",
                room = "ГУК, ауд. 221",
                time = "08:00–09:20"
            ),
            Lesson(
                subject = "Операционные системы",
                type = LessonType.LECTURE,
                teacher = "Данияров Р. М.",
                room = "ГУК, ауд. 310",
                time = "09:30–10:50"
            ),
            Lesson(
                subject = "Операционные системы",
                type = LessonType.LAB,
                teacher = "Данияров Р. М.",
                room = "ГУК, ауд. 312",
                time = "11:00–12:20"
            ),
            Lesson(
                subject = "Философия",
                type = LessonType.LECTURE,
                teacher = "Калиева Ж. Н.",
                room = "ГУК, ауд. 105",
                time = "13:00–14:20"
            )
        ),
        // Четверг
        3 to listOf(
            Lesson(
                subject = "Компьютерные сети",
                type = LessonType.LECTURE,
                teacher = "Жумабеков Т. А.",
                room = "ГМК, ауд. 203",
                time = "09:30–10:50"
            ),
            Lesson(
                subject = "Компьютерные сети",
                type = LessonType.LAB,
                teacher = "Жумабеков Т. А.",
                room = "ГМК, ауд. 205",
                time = "11:00–12:20"
            ),
            Lesson(
                subject = "Разработка мобильных приложений",
                type = LessonType.PRACTICE,
                teacher = "Асқар Э.",
                room = "ГУК, ауд. 318",
                time = "13:00–14:20"
            )
        ),
        // Пятница
        4 to listOf(
            Lesson(
                subject = "Проектирование программного обеспечения",
                type = LessonType.LECTURE,
                teacher = "Оспанова Д. Б.",
                room = "ГУК, ауд. 402",
                time = "08:00–09:20"
            ),
            Lesson(
                subject = "Проектирование программного обеспечения",
                type = LessonType.PRACTICE,
                teacher = "Оспанова Д. Б.",
                room = "ГУК, ауд. 404",
                time = "09:30–10:50"
            ),
            Lesson(
                subject = "Физическая культура",
                type = LessonType.PRACTICE,
                teacher = "Серіков Н. Қ.",
                room = "Спортивный комплекс",
                time = "11:00–12:20"
            )
        )
        // Суббота (индекс 5) в словаре отсутствует — занятий нет
    )

    /**
     * Возвращает пары указанного дня. Если день в расписании не описан
     * (суббота), возвращается пустой список, а не null.
     */
    fun forDay(dayIndex: Int): List<Lesson> = lessonsByDay[dayIndex].orEmpty()

    /** Общее число пар за неделю. */
    val totalLessons: Int
        get() = lessonsByDay.values.sumOf { it.size }

    /** Число различных дисциплин за неделю. */
    val subjectCount: Int
        get() = lessonsByDay.values.flatten().distinctBy { it.subject }.size
}
