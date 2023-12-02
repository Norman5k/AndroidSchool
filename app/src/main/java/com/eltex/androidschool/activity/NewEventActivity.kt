package com.eltex.androidschool.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.ActivityNewEventBinding
import com.eltex.androidschool.utils.toast

class NewEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewEventBinding.inflate(layoutInflater)
        val editContent = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (editContent?.isNotBlank() == true) {
            binding.content.setText(editContent)
        }
        setContentView(binding.root)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.save -> {
                    val content = binding.content.text?.toString().orEmpty()

                    if (content.isNotBlank()) {
                        val id = intent.getStringExtra(Intent.EXTRA_UID)
                        if (id != null) {
                            setResult(
                                RESULT_OK, Intent()
                                    .putExtra(Intent.EXTRA_TEXT, content)
                                    .putExtra(Intent.EXTRA_UID, id)
                            )
                        } else {

                            setResult(
                                RESULT_OK, Intent()
                                    .putExtra(Intent.EXTRA_TEXT, content)
                            )
                        }
                        finish()
                    } else {
                        toast(R.string.event_empty_error)
                    }
                    true
                }

                else -> false
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}