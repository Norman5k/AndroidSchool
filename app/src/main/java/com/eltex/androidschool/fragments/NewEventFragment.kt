package com.eltex.androidschool.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.api.MediaApi
import com.eltex.androidschool.databinding.FragmentNewEventBinding
import com.eltex.androidschool.model.AttachmentType
import com.eltex.androidschool.model.FileModel
import com.eltex.androidschool.model.Status
import com.eltex.androidschool.repository.NetworkEventRepository
import com.eltex.androidschool.utils.getText
import com.eltex.androidschool.utils.toast
import com.eltex.androidschool.viewmodel.NewEventViewModel
import com.eltex.androidschool.viewmodel.ToolbarViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class NewEventFragment : Fragment() {

    companion object {
        const val ARG_ID = "ARG_ID"
        const val ARG_CONTENT = "ARG_CONTENT"
        const val EVENT_UPDATED = "EVENT_UPDATED"
    }

    private val toolbarViewModel by activityViewModels<ToolbarViewModel>()

    override fun onStart() {
        super.onStart()
        toolbarViewModel.showSave(true)
    }

    override fun onStop() {
        super.onStop()
        toolbarViewModel.showSave(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewEventBinding.inflate(inflater, container, false)

        val editContent = arguments?.getString(ARG_CONTENT)

        if (editContent?.isNotBlank() == true) {
            binding.content.setText(editContent)
        }

        val id = arguments?.getLong(ARG_ID) ?: 0L

        val viewModel by viewModels<NewEventViewModel> {
            viewModelFactory {
                initializer {
                    NewEventViewModel(
                        repository = NetworkEventRepository(
                            EventsApi.INSTANCE,
                            MediaApi.INSTANCE,
                            requireContext().contentResolver
                        ),
                        id = id
                    )
                }
            }
        }

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                viewModel.saveFile(FileModel(it, AttachmentType.IMAGE))
            }
        }

        val imageUri = createFileUri()
        val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {success->
            if (success) {
                viewModel.saveFile(FileModel(imageUri, AttachmentType.IMAGE))
            }
        }

        binding.pickFile.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.takePhoto.setOnClickListener {
            takePhoto.launch(imageUri)
        }

        binding.remove.setOnClickListener {
            viewModel.saveFile(null)
        }

        viewModel.state.onEach { state ->
            if (state.result != null) {
                requireActivity().supportFragmentManager.setFragmentResult(
                    EVENT_UPDATED,
                    bundleOf()
                )

                findNavController().navigateUp()
            }

            val file = state.file

            when (file?.attachmentType) {
                AttachmentType.IMAGE -> {
                    binding.preview.isVisible = true
                    binding.photoPreview.setImageURI(file.uri)
                }

                AttachmentType.VIDEO,
                AttachmentType.AUDIO,
                null -> binding.preview.isGone = true
            }

            (state.status as? Status.Error)?.let {
                Toast.makeText(
                    requireContext(),
                    it.reason.getText(requireContext()),
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.handleError()
            }
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        toolbarViewModel.saveClicked
            .filter { it }
            .onEach {
                val content = binding.content.text?.toString().orEmpty()

                if (content.isNotBlank()) {
                    viewModel.save(content)

                } else {
                    requireContext().toast(R.string.event_empty_error)
                }

                toolbarViewModel.saveClicked(false)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        return binding.root
    }

    private fun createFileUri(): Uri {
        val directory = requireContext().cacheDir.resolve("file_picker").apply {
            mkdirs()
        }

        val file = directory.resolve("image.png")

        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
    }
}