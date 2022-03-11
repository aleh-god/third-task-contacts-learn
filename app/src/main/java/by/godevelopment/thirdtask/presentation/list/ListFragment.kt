package by.godevelopment.thirdtask.presentation.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.godevelopment.thirdtask.R
import by.godevelopment.thirdtask.appComponent
import by.godevelopment.thirdtask.common.TAG
import by.godevelopment.thirdtask.databinding.FragmentListBinding
import by.godevelopment.thirdtask.di.factory.ViewModelFactory
import by.godevelopment.thirdtask.presentation.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    @Inject
    lateinit var viewModelFactor: ViewModelFactory
    lateinit var viewModel: ListViewModel
    lateinit var adapter: ListAdapter

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding !!

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactor)[ListViewModel::class.java]
        setupListRv()
        setupToolbar()
        setupButtons()
        setupEvent()
        return binding.root
    }

    private fun setupListRv() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateUI.collect { state ->
                Log.i(TAG, "ListFragment setupUI: size = ${state.contacts.size}")
                context?.let {
                    val colorMain = AppCompatResources.getColorStateList(it, R.color.anakiwa).defaultColor
                    val colorMark = AppCompatResources.getColorStateList(it, R.color.salmon).defaultColor
                    adapter = ListAdapter(colorMain, colorMark)
                    adapter.listItems = state.contacts
                    binding.rvView.adapter = adapter
                }
            }
        }
    }

    private fun setupButtons() {
        binding.btnSave.setOnClickListener {
            saveContact()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            inflateMenu(R.menu.toolbar_list_fragment)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_back -> {
                        parentFragmentManager.popBackStack()
                        true
                    }
                    R.id.action_save -> {
                        saveContact()
                        true
                    }
                    else -> false
                }
            }
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

    private fun saveContact() {
        adapter.listItems.firstOrNull {
            it.isSelected
        }?.let {
            viewModel.saveContactModel(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}