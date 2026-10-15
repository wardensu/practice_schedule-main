package com.example.practiceschedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Экран 1 — стартовый. Пользователь вводит название группы и выбирает день
 * недели, после чего явным Intent открывается экран со списком пар.
 *
 * Разметка: activity_main.xml (LinearLayout).
 */
class MainActivity : AppCompatActivity() {

    companion object {
        /** Метка для фильтрации сообщений этого экрана в Logcat. */
        private const val TAG = "MainActivity"
    }

    private lateinit var etGroup: EditText
    private lateinit var spDay: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)          // подключаем разметку экрана
        Log.d(TAG, "onCreate: экран создан, подключена разметка activity_main")

        // Шапка экрана синяя, поэтому значки строки состояния оставляем светлыми
        applySystemBarsInsets(
            root = findViewById(R.id.mainRoot),
            topInsetView = findViewById(R.id.header),
            lightStatusBar = false
        )

        etGroup = findViewById(R.id.etGroup)
        spDay = findViewById(R.id.spDay)

        // Список дней недели берётся из ресурса res/values/strings.xml,
        // поэтому в коде нет ни одного названия дня.
        spDay.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.days_of_week,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        findViewById<TextView>(R.id.tvSummary).text =
            getString(R.string.main_summary, Schedule.totalLessons, Schedule.subjectCount)

        findViewById<Button>(R.id.btnShow).setOnClickListener { openSchedule() }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            Log.i(TAG, "Нажата кнопка «Очистить»: поля возвращены в исходное состояние")
            etGroup.text.clear()
            etGroup.error = null
            spDay.setSelection(0)
            Toast.makeText(this, R.string.toast_cleared, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnAbout).setOnClickListener {
            Log.i(TAG, "Нажата кнопка «О приложении»: переход MainActivity → AboutActivity")
            // Явный Intent без дополнительных данных
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    /**
     * Проверяет введённые данные и открывает [ScheduleActivity].
     * Если группа не указана, переход отменяется, а в журнал пишется
     * предупреждение уровня Warning.
     */
    private fun openSchedule() {
        val group = etGroup.text.toString().trim()

        if (group.isEmpty()) {
            Log.w(TAG, "Поле «Учебная группа» пустое — переход на ScheduleActivity отменён")
            // Ставим фокус на проблемное поле: тогда подсказка об ошибке
            // показывается сразу, а не после первого касания
            etGroup.requestFocus()
            etGroup.error = getString(R.string.error_empty_group)
            Toast.makeText(this, R.string.error_empty_group, Toast.LENGTH_SHORT).show()
            return
        }

        val dayIndex = spDay.selectedItemPosition
        val dayName = spDay.selectedItem.toString()
        Log.i(TAG, "Нажата кнопка «Показать пары»: группа=$group, день=$dayName (индекс $dayIndex)")

        // Явный Intent: класс открываемого экрана указан точно
        val intent = Intent(this, ScheduleActivity::class.java)
        intent.putExtra(ScheduleActivity.EXTRA_GROUP, group)        // String
        intent.putExtra(ScheduleActivity.EXTRA_DAY_INDEX, dayIndex) // Int
        startActivity(intent)

        Log.d(TAG, "startActivity выполнен: переход MainActivity → ScheduleActivity")
    }

    // ---- Методы жизненного цикла: каждый сопровождается записью в журнал ----

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: экран стал видимым")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: экран на переднем плане, доступен для ввода")
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
