package com.eltex.androidschool.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.eltex.androidschool.R
import com.eltex.androidschool.adapter.EventsAdapter
import com.eltex.androidschool.adapter.OffsetDecoration
import com.eltex.androidschool.databinding.ActivityMainBinding
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.InMemoryEventRepository
import com.eltex.androidschool.viewmodel.EventViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<EventViewModel> {
            viewModelFactory {
                initializer {
                    EventViewModel(InMemoryEventRepository())
                }
            }
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newEventContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val content = it.data?.getStringExtra(Intent.EXTRA_TEXT)

                if (content != null) {
                    viewModel.addEvent(content)
                }
            }

        val editEventContract =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val content = it.data?.getStringExtra(Intent.EXTRA_TEXT)
                val id = it.data?.getStringExtra(Intent.EXTRA_UID)

                if (content != null && id != null) {
                    viewModel.editById(id.toLong(), content)
                }
            }

        if (intent.action == Intent.ACTION_SEND) { // Обязательно проверьте соответствует ли action ожидаемому
            val text = intent.getStringExtra(Intent.EXTRA_TEXT)
            intent.removeExtra(Intent.EXTRA_TEXT) // Удаляем, чтобы при повороте экрана снова не открывалась активити
            val intent = Intent(this, NewEventActivity::class.java)
                .putExtra(Intent.EXTRA_TEXT, text)
            newEventContract.launch(intent)
        }

        binding.newEvent.setOnClickListener {
            val intent = Intent(this, NewEventActivity::class.java)
            newEventContract.launch(intent)
        }

        val eventsAdapter = EventsAdapter(
            object : EventsAdapter.EventListener {
                override fun onLikeClickListener(event: Event) {
                    viewModel.likeById(event.id)
                }

                override fun onShareClickListener(event: Event) {
                    val intent = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, event.content)
                        .setType("text/plain")

                    val chooser = Intent.createChooser(intent, null)
                    startActivity(chooser)
                }

                override fun onDeleteClickListener(event: Event) {
                    viewModel.deleteById(event.id)
                }

                override fun onEditClickListener(event: Event) {

                    val intent = Intent(this@MainActivity, NewEventActivity::class.java)
                        .putExtra(Intent.EXTRA_TEXT, event.content)
                        .putExtra(Intent.EXTRA_UID, event.id.toString())
                    editEventContract.launch(intent)

                }

                override fun onParticipateClickListener(event: Event) {
                    viewModel.participateById(event.id)
                }

            }
        )
        binding.list.adapter = eventsAdapter

        binding.list.addItemDecoration(
            OffsetDecoration(resources.getDimensionPixelSize(R.dimen.small_spacing))
        )

        viewModel.state.flowWithLifecycle(lifecycle)
            .onEach {
                eventsAdapter.submitList(it.events)
            }
            .launchIn(lifecycleScope)
    }


}