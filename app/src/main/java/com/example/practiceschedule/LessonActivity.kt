package com.example.practiceschedule

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

/**
 * Экран 3 — карточка дисциплины: вид занятия, преподаватель, аудитория,
 * время и группа. Все сведения приходят из [ScheduleActivity] через extras.
 *
 * Разметка: activity_lesson.xml (RelativeLayout).
 */
class LessonActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LessonActivity"

        const val EXTRA_SUBJECT = "com.example.practiceschedule.EXTRA_SUBJECT"
        const val EXTRA_TYPE = "com.example.practiceschedule.EXTRA_TYPE"
        const val EXTRA_TEACHER = "com.example.practiceschedule.EXTRA_TEACHER"
        const val EXTRA_ROOM = "com.example.practiceschedule.EXTRA_ROOM"
        const val EXTRA_TIME = "com.example.practiceschedule.EXTRA_TIME"
        const val EXTRA_GROUP = "com.example.practiceschedule.EXTRA_GROUP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        Log.d(TAG, "onCreate: экран создан, подключена разметка activity_lesson")

        val unknown = getString(R.string.value_unknown)
        val subject = intent.getStringExtra(EXTRA_SUBJECT) ?: unknown
        val teacher = intent.getStringExtra(EXTRA_TEACHER) ?: unknown
        val room = intent.getStringExtra(EXTRA_ROOM) ?: unknown
        val time = intent.getStringExtra(EXTRA_TIME) ?: unknown
        val group = intent.getStringExtra(EXTRA_GROUP) ?: unknown

        // Вид занятия передан именем элемента перечисления. Если ключа нет
        // или значение не распознано, подставляется LECTURE, а в журнал
        // пишется ошибка — приложение при этом не падает.
        val type = parseLessonType(intent.getStringExtra(EXTRA_TYPE))

        if (!intent.hasExtra(EXTRA_SUBJECT)) {
            Log.e(TAG, "В Intent нет ключа EXTRA_SUBJECT: ${getString(R.string.lesson_not_found)}")
        } else {
            Log.i(TAG, "Получены данные: $subject, $teacher, $room, $time, группа $group")
        }

        // Фон экрана светлый — значки строки состояния делаем тёмными
        applySystemBarsInsets(root = findViewById(R.id.lessonRoot), lightStatusBar = true)

        findViewById<ImageView>(R.id.ivLessonType).setImageResource(type.iconRes)
        findViewById<TextView>(R.id.tvSubject).text = subject
        findViewById<TextView>(R.id.tvType).text = getString(type.titleRes)
        findViewById<TextView>(R.id.tvTeacher).text = teacher
        findViewById<TextView>(R.id.tvRoom).text = room
        findViewById<TextView>(R.id.tvTime).text = time
        findViewById<TextView>(R.id.tvLessonGroup).text = group

        findViewById<Button>(R.id.btnBackLesson).setOnClickListener {
            Log.i(TAG, "Нажата кнопка «Назад»: вызывается finish(), возврат на ScheduleActivity")
            finish()   // закрывает текущий экран и возвращает предыдущий
        }

        // Системная кнопка «Назад» обрабатывается через OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.i(TAG, "Системная кнопка «Назад»: экран закрывается")
                finish()
            }
        })
    }

    /** Преобразует имя элемента перечисления в [LessonType] с защитой от ошибок. */
    private fun parseLessonType(rawType: String?): LessonType = try {
        LessonType.valueOf(rawType ?: LessonType.LECTURE.name)
    } catch (e: IllegalArgumentException) {
        Log.e(TAG, "Неизвестный вид занятия: $rawType — подставлено значение по умолчанию", e)
        LessonType.LECTURE
    }

    // ---- Методы жизненного цикла ----

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: экран стал видимым")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: экран на переднем плане")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: экран потерял фокус")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: экран больше не виден")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: экран уничтожен")
    }
}
