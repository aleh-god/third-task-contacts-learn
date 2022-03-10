package by.godevelopment.thirdtask.presentation.main

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.godevelopment.thirdtask.appComponent
import by.godevelopment.thirdtask.databinding.FragmentMainBinding
import by.godevelopment.thirdtask.di.factory.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
//import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

//@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

//    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var viewModelFactor: ViewModelFactory
    lateinit var viewModel: MainViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding !!

    // TODO = "inject"
    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        // TODO = "Set"
        viewModel = ViewModelProvider(this, viewModelFactor)[MainViewModel::class.java]
        setupUI()
        setupEvent()
        return binding.root
    }

    private fun setupUI() {
        lifecycleScope.launchWhenStarted {
            viewModel.stateUI.collect { state ->
                val myStringArray = state.contacts.map {
                    it.taskPhoneNumber
                }.toTypedArray()
                val adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, myStringArray)
                    binding.list.adapter = adapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}