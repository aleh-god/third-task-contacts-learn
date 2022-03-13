package by.godevelopment.thirdtask.presentation.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.godevelopment.thirdtask.R
import by.godevelopment.thirdtask.appComponent
import by.godevelopment.thirdtask.common.CHANNEL_ID
import by.godevelopment.thirdtask.common.TAG
import by.godevelopment.thirdtask.data.entities.ContactEntity
import by.godevelopment.thirdtask.databinding.FragmentMainBinding
import by.godevelopment.thirdtask.di.factory.ViewModelFactory
import by.godevelopment.thirdtask.presentation.dialogFragments.ListDialogFragment
import by.godevelopment.thirdtask.presentation.list.ListFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModelFactor: ViewModelFactory
    lateinit var viewModel: MainViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding !!

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactor)[MainViewModel::class.java]
        setupUI()
        setupListeners()
        setupEvent()
        return binding.root
    }

    private fun setupUI() {
        showSharedPreferenceInfo()
    }

    private fun setupListeners() {
        binding.btnSelect.setOnClickListener {
            navigateToList()
        }

        binding.btnShow.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getArrayFlow().collect {
                    ListDialogFragment.show(parentFragmentManager, it)
                }
            }
        }
        setupListDialogFragmentListener()

        binding.btnSharedPreference.setOnClickListener {
            showSharedPreferenceInfo()
            Snackbar.make(binding.root, viewModel.getNumberFromSP(), Snackbar.LENGTH_LONG).show()
        }

        binding.btnNotification.setOnClickListener {
            viewModel.showNameAndSurnameByNumber()
            Snackbar.make(binding.root, getString(R.string.fragment_main_notif_message), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showSharedPreferenceInfo() {
        with(binding) {
            name.text = getString(R.string.fragment_view_text_main)
            name.visibility = View.VISIBLE
            surname.visibility = View.GONE
            email.visibility = View.GONE
            number.text = viewModel.getNumberFromSP()
            number.visibility = View.VISIBLE
        }
    }

    private fun setupListDialogFragmentListener() {
        ListDialogFragment.setupListener(parentFragmentManager, this) { result ->
            doProcessContactByIndex(result)
        }
    }

    private fun setupEvent() {
        lifecycleScope.launchWhenStarted {
            viewModel.eventUI.collect {
                Snackbar
                    .make(
                        binding.root,
                        it.message,
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    private fun doProcessContactByIndex(index: Int) {
        lifecycleScope.launchWhenStarted {
            viewModel.getContactByIndexFlow(index).collect { contact ->
                viewModel.saveContactByIndexInSharedPref(contact)
                showContactOnScreen(contact)
                Snackbar.make(binding.root, getString(R.string.fragment_main_text_save), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun showContactOnScreen(contact: ContactEntity) {
        with(binding) {
            name.text = contact.name
            name.visibility = View.VISIBLE

            surname.text = contact.surname
            surname.visibility = View.VISIBLE

            number.text = contact.taskPhoneNumber
            number.visibility = View.VISIBLE

            email.text = contact.email
            email.visibility = View.VISIBLE
        }
    }

    private fun navigateToList() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ListFragment>(R.id.fragment_container_view)
            addToBackStack(null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}