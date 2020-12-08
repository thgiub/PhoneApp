package ru.kamaz.itis.phoneapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.kamaz.itis.phoneapp.R
import ru.kamaz.itis.phoneapp.ui.fragments.LeftBluetoothSettingFragment
import ru.kamaz.itis.phoneapp.ui.fragments.RightBluetoothSettingFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openDefaultFragments()
    }

    private fun openDefaultFragments() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.bluetoothSettingLeft, LeftBluetoothSettingFragment())
            .commit()
    }
}