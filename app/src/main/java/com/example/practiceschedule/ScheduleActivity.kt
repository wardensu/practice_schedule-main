package com.example.practiceschedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

/**
 * Экран 2 — список пар выбранного дня.
 *
 * Получает из Intent название группы (String) и индекс дня (Int), строит
 * карточки пар и по нажатию на карточку открывает [LessonActivity],
 * передавая ей сведения о выбранной паре.
 *
 * Разметка: activity_schedule.xml (LinearLayout) + item_lesson.xml (RelativeLayout).
 */
class ScheduleActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ScheduleActivity"

        // Ключи дополнительных данных вынесены в константы: опечатку в
        // строковом литерале компилятор не заметит, и ошибка проявилась бы
        // только во время выполнения.
        const val EXTRA_GROUP = "com.example.practiceschedule.EXTRA_GROUP"
        const val EXTRA_DAY_INDEX = "com.example.practiceschedule.EXTRA_DAY_INDEX"
    }

    private lateinit var group: String
    private var dayIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        Log.d(TAG, "onCreate: экран создан, подключена разметка activity_schedule")

        // Приём данных. Для String значение по умолчанию подставляется
        // elvis-оператором, для Int — вторым аргументом getIntExtra().
        group = intent.getStringExtra(EXTRA_GROUP) ?: getString(R.string.value_unknown)
        dayIndex = intent.getIntExtra(EXTRA_DAY_INDEX, 0)
        Log.i(TAG, "Получены данные из Intent: группа=$group, индекс дня=$dayIndex")

        // Шапка экрана синяя — значки строки состояния оставляем светлыми
        applySystemBarsInsets(
            root = findViewById(R.id.scheduleRoot),
            topInsetView = findViewById(R.id.header),
            lightStatusBar = false
        )

        showHeader()
        showLessons()

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            Log.i(TAG, "Нажата кнопка «Назад»: вызывается finish(), возврат на MainActivity")
            finish()
        }

        // Системная кнопка «Назад» обрабатывается через OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.i(TAG, "Системная кнопка «Назад»: экран закрывается")
                finish()
            }
        })
    }

    /** Заполняет шапку экрана: день недели, группа, количество пар. */
    private fun showHeader() {
        val daysAccusative = resources.getStringArray(R.array.days_of_week_accusative)
        val dayName = daysAccusative.getOrElse(dayIndex) { getString(R.string.value_unknown) }
        val lessonCount = Schedule.forDay(dayIndex).size

        findViewById<TextView>(R.id.tvDayTitle).text = getString(R.string.schedule_title, dayName)
        findViewById<TextView>(R.id.tvGroup).text = getString(R.string.schedule_group, group)
        findViewById<TextView>(R.id.tvCount).text = getString(R.string.schedule_count, lessonCount)
    }

    /**
     * Строит список карточек. Разметка одной карточки описана в
     * item_lesson.xml и создаётся через LayoutInflater.
     */
    private fun showLessons() {
        val lessons = Schedule.forDay(dayIndex)
        val llLessons = findViewById<LinearLayout>(R.id.llLessons)
        val tvDayOff = findViewById<TextView>(R.id.tvDayOff)
        val tvHint = findViewById<TextView>(R.id.tvHint)

        if (lessons.isEmpty()) {
            // Выходной: список и подсказку убираем, вместо них показываем
            // сообщение, которое занимает всё свободное место экрана
            Log.w(TAG, "Для дня с индексом $dayIndex занятий нет — показан выходной")
            findViewById<View>(R.id.svLessons).visibility = View.GONE
            tvHint.visibility = View.GONE
            tvDayOff.visibility = View.VISIBLE
            return
        }

        val inflater = LayoutInflater.from(this)
        lessons.forEach { lesson ->
            val card = inflater.inflate(R.layout.item_lesson, llLessons, false)

            card.findViewById<ImageView>(R.id.ivType).setImageResource(lesson.type.iconRes)
            card.findViewById<TextView>(R.id.tvItemSubject).text = lesson.subject
            card.findViewById<TextView>(R.id.tvItemTeacher).text = getString(
                R.string.item_type_teacher,
                getString(lesson.type.titleRes),
                lesson.teacher
            )
            card.findViewById<TextView>(R.id.tvItemRoom).text = lesson.room
            card.findViewById<TextView>(R.id.tvItemTime).text = lesson.time

            card.setOnClickListener { openLesson(lesson) }
            llLessons.addView(card)
        }

        Log.d(TAG, "Список построен: добавлено карточек — ${lessons.size}")
    }

    /** Открывает карточку пары, передавая её поля через putExtra(). */
    private fun openLesson(lesson: Lesson) {
        Log.i(TAG, "Выбрана пара «${lesson.subject}» (${lesson.time}), открывается LessonActivity")

        val intent = Intent(this, LessonActivity::class.java)
        intent.putExtra(LessonActivity.EXTRA_SUBJECT, lesson.subject)
        intent.putExtra(LessonActivity.EXTRA_TYPE, lesson.type.name)
        intent.putExtra(LessonActivity.EXTRA_TEACHER, lesson.teacher)
        intent.putExtra(LessonActivity.EXTRA_ROOM, lesson.room)
        intent.putExtra(LessonActivity.EXTRA_TIME, lesson.time)
        intent.putExtra(LessonActivity.EXTRA_GROUP, group)
        startActivity(intent)
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
